package com.yht.opentactsmple;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yht.opentact.api.OpentactConfig;
import com.yht.opentact.api.OpentactManager;
import com.yht.opentact.im.callback.ACTION;
import com.yht.opentact.im.callback.IMCallback;
import com.yht.opentact.util.HttpService;
import com.yht.opentact.util.HttpService.OnMessageResponseListener;

public class MainActivity extends Activity implements Handler.Callback, OnItemClickListener, OnClickListener {

	public static final String TAG = MainActivity.class.getSimpleName();
	/***
	 * these SID,SSID,AUTHTOKEN are test data, please change to your data.
	 */
	public static final String SID = "553d9e6d1073e9455be0b30e";
	public static final String SSID = "553d9f231073e9465b4ac7da";
	public static final String AUTHTOKEN = "cb1f04160faa4ccbb8b368aebbd2a873";

	public static final int DO_GET_FRIENDS = 1;
	public static final int DO_GET_SIPACCOUNT = 2;
	public static final int CALLING_TO_PHONENUMBER = 3;
	public static final int CALLING_TO_SIPACCOUNT = 4;
	public static final int HAS_INCOMING_CALL = 5;
	public static final String JSON_KEY_FRIENDS = "friends";
	public static final String JSON_KEY_SID = "sid";

	private OpentactManager opentactManager;
	private SipCallback onSipCallback;
	private MyIMCallback myIMCallback;

	private ArrayList<String> arr = new ArrayList<String>();
	private ListView mListView = null;
	private ArrayAdapter<String> adapter;
	private Button btnMakecall;
	private Button btnSendIMMessage;
	private Button btnAnswerCall;
	private Button btnHangupCall;

	private final Handler hand = new Handler(this);
	private String[] friends_sid;
	private String sipAccount;
	private String sipAccountPwd;
	private NotifyCall notifyCall;
	private IntentFilter intentFilter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		notifyCall = new NotifyCall();
		intentFilter  = new IntentFilter("com.yht.opentactsmple.notifycall");
		setContentView(R.layout.layout_main);
		initListview();
		initLayout();
		initOpentact();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		opentactManager.stopSipService();
		if (opentactManager != null) {
			opentactManager = null;
		}
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(notifyCall, intentFilter);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(notifyCall);
	}
	
	
	/**
	 * initialize opentact
	 */
	private void initOpentact() {
		// opentact configuration
		OpentactConfig cfg = new OpentactConfig();
		cfg.getSipConfig().setIceEnable(true);
		cfg.getSipConfig().setStunEnable(true);
		cfg.getSipConfig().setTurnEnable(true);
		onSipCallback = new SipCallback(getApplicationContext());
		myIMCallback = new MyIMCallback();

		// start the opentact
		opentactManager = OpentactManager.startWork(getApplicationContext(), SID, AUTHTOKEN, SSID, cfg, onSipCallback);

		// IM connection
		myIMCallback.setAction(ACTION.CONNECT);
		opentactManager.imDoConnect(new IMCallback() {
			@Override
			public void onActionCallback(ACTION action, boolean isSuccess, Throwable throwable) {
				System.out.println("onActionCallback " + action.toString());
				if (isSuccess) {
					myIMCallback.setAction(ACTION.SUBSCRIBE);
					opentactManager.imSubscribe(myIMCallback);
				}
			}
		});

		HttpService httpService = new HttpService();
		httpService.getSipAccount(SID, AUTHTOKEN, SSID, new OnMessageResponseListener() {

			@Override
			public void messageResponse(JSONObject jsonMsg) {
				Message msg = Message.obtain(hand, DO_GET_SIPACCOUNT, jsonMsg);
				msg.sendToTarget();
			}

			@Override
			public void errorCode(int errCode) {
				Log.e(TAG, "there is a error on getSipAccount and errCode = " + errCode);
			}
		});
	}

	private void initListview() {
		mListView = (ListView) findViewById(R.id.list_friends);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arr);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		Handler handler = new Handler();
		handler.postDelayed(changeList, 200);
	}

	private void initLayout() {
		btnMakecall = (Button) findViewById(R.id.btn_call);
		btnMakecall.setOnClickListener(this);
		btnSendIMMessage = (Button) findViewById(R.id.btn_im);
		btnSendIMMessage.setOnClickListener(this);
		btnAnswerCall = (Button) findViewById(R.id.btn_answer);
		btnAnswerCall.setOnClickListener(this);
		btnHangupCall = (Button) findViewById(R.id.btn_hangup);
		btnHangupCall.setOnClickListener(this);
	}

	Runnable changeList = new Runnable() {

		@Override
		public void run() {
			HttpService httpService = new HttpService();
			httpService.getFriendsList(SID, AUTHTOKEN, SSID, new OnMessageResponseListener() {

				@Override
				public void messageResponse(JSONObject jsonMsg) {
					Message msg = Message.obtain(hand, DO_GET_FRIENDS, jsonMsg);
					msg.sendToTarget();
				}

				@Override
				public void errorCode(int errCode) {

				}
			});
		}
	};

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case DO_GET_FRIENDS:
			try {
				JSONObject json = (JSONObject) msg.obj;
				JSONArray jsonFriends = json.getJSONArray(JSON_KEY_FRIENDS);
				friends_sid = new String[jsonFriends.length()];
				for (int i = 0; i < jsonFriends.length(); i++) {
					arr.add(i, jsonFriends.getJSONObject(i).toString());
					friends_sid[i] = jsonFriends.getJSONObject(i).getString("sid");
				}
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case DO_GET_SIPACCOUNT:
			try {
				JSONObject json = (JSONObject) msg.obj;
				sipAccount = json.getString("sip_number");
				sipAccountPwd = json.getString("sip_password");
				opentactManager.addSipAccount(sipAccount, sipAccountPwd);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case CALLING_TO_PHONENUMBER:
			String callTarget = (String) msg.obj;
			Toast.makeText(this, callTarget, 20).show();
			break;

		case CALLING_TO_SIPACCOUNT:
			try {
				JSONObject json = (JSONObject) msg.obj;
				String sipNumber = json.getString("sip_number");
				opentactManager.makeCallToSid(sipNumber);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
			
		case HAS_INCOMING_CALL:
			btnAnswerCall.setVisibility(View.VISIBLE);
			btnHangupCall.setVisibility(View.VISIBLE);
			break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Toast.makeText(this, friends_sid[position], Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_call:
			int itemPosition = mListView.getCheckedItemPosition();
			if (itemPosition >= 0) {
				Toast.makeText(this, "checkedItemPosition = " + friends_sid[mListView.getCheckedItemPosition()], Toast.LENGTH_SHORT).show();
				buildMakeCallDialog();
			} else {
				Toast.makeText(this, "please check the listview first", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.btn_im:
			Random random = new Random(100);
			if (mListView.getCheckedItemPosition() >= 0) {
				myIMCallback.setAction(ACTION.PUBLISH);
				opentactManager
						.imPublish(friends_sid[mListView.getCheckedItemPosition()], "test message from android and NO." + random.nextInt(), myIMCallback);
			}
			break;

		case R.id.btn_answer:
			opentactManager.answerCall();
			break;

		case R.id.btn_hangup:
			opentactManager.hangupCall();
			break;
		}

	}

	private void buildMakeCallDialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View makeCallDialog = factory.inflate(R.layout.dialog_make_call, null);
		final EditText callTarget = (EditText) makeCallDialog.findViewById(R.id.make_call_dialog_et);
		callTarget.setText(friends_sid[mListView.getCheckedItemPosition()]);
		build.setTitle("Make Call");
		build.setView(makeCallDialog);
		build.setPositiveButton("Call", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				RadioButton rbCallByNum = (RadioButton) makeCallDialog.findViewById(R.id.rb_call_by_number);
				RadioButton rbCallBySsid = (RadioButton) makeCallDialog.findViewById(R.id.rb_call_by_ssid);
				if (callTarget.getText().length() > 0) {
					if (rbCallByNum.isChecked()) {
						opentactManager.makeCallToTermination(callTarget.getText().toString().trim());
						Message msg = Message.obtain(hand, CALLING_TO_PHONENUMBER, callTarget.getText().toString().trim());
						msg.sendToTarget();
					} else if (rbCallBySsid.isChecked()) {
						HttpService httpService = new HttpService();
						httpService.getSipAccount(SID, AUTHTOKEN, callTarget.getText().toString().trim(), new OnMessageResponseListener() {

							@Override
							public void messageResponse(JSONObject jsonMsg) {
								Message msg = Message.obtain(hand, CALLING_TO_SIPACCOUNT, jsonMsg);
								msg.sendToTarget();
							}

							@Override
							public void errorCode(int errCode) {

							}
						});
					}
				}
			}
		});

		build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		build.create().show();
	}
	
	public class NotifyCall extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = Message.obtain(hand, HAS_INCOMING_CALL, intent.getBooleanExtra("isIncomingCall", false));
			msg.sendToTarget();
		}
		
	}

}
