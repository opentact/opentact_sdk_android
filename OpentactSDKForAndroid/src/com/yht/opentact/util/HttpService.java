package com.yht.opentact.util;

import org.apache.http.Header;
import org.json.JSONObject;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

@SuppressWarnings("deprecation")
public class HttpService {
	public static final String TAG = HttpService.class.getSimpleName();

	public static final String OPENTACT_HTTPS_SERVER_URI = "108.165.2.111";
	public static final String HTTPS = "https://";
	public static final String OPENTACT_HTTPS_SERVER_VERSION = "v1";
	public static final String RESPONSE_DATA_TYPE_JSON = "json";
	public static final String RESPONSE_DATA_TYPE_XML = "xml";
	public static final String REQUEST_ACTION_ACCOUNTS = "accounts";
	public static final String REQUEST_ACTION_FRIENDS = "friends";
	public static final String REQUEST_ACTION_SIP = "sip";
	public static final String REQUEST_ACTION_NUMBERS = "numbers";
	public static final String SYMBOLS_POINT = ".";
	public static final String SYMBOLS_COLON = ":";
	public static final String SYMBOLS_SOLIDUS = "/";
	public static final String OPENTACT_SIP_ACCOUNT_URI_ACTION = "/sip.json";
	public static final String SIP_ACCOUNT_KEY_STATUS = "status";
	public static final String SIP_ACCOUNT_STATUS_ONLINE = "online";
	public static final String REQUEST_PARAMS_USERNAME = "name";
	public static final String REQUEST_PARAMS_FRIEND_SID = "friend_sid";
	public static final int HTTP_PORT = 5060;
	public static final int HTTPS_PORT = 443;

	public AsyncHttpClient client;
	public SyncHttpClient syncClient;
	public OnMessageResponseListener onMessageResponseListener;
	
	public interface OnMessageResponseListener {
		abstract void messageResponse(JSONObject jsonMsg);
		abstract void errorCode(int errCode);
	}

	public HttpService() {
		client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
	}
	
	public HttpService(boolean isAsync){
		if(isAsync){
			client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
		}
		else{
			syncClient = new SyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
		}
	}

	public void createSubAccount(String sid, String authToken, String username, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_ACCOUNTS)
				.append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		RequestParams rp = new RequestParams();
		rp.put(REQUEST_PARAMS_USERNAME, username);
		client.setBasicAuth(sid, authToken);
		client.post(uri.toString(), rp, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onMessageResponseListener.messageResponse(response);
				Log.d(TAG, "jsonResponse = " + response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				onMessageResponseListener.errorCode(statusCode);
				Log.d(TAG, "https error code = " + statusCode);
			}
		});
	}

	public void getSubAccountList(String sid, String authToken, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_ACCOUNTS)
				.append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		client.setBasicAuth(sid, authToken);
		client.get(uri.toString(), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onMessageResponseListener.messageResponse(response);
				Log.d(TAG, "jsonResponse = " + response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				onMessageResponseListener.errorCode(statusCode);
				Log.d(TAG, "https error code = " + statusCode);
			}
		});
	}

	public void makeFriend(String sid, String authToken, String ssid, String friendSid, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_FRIENDS)
				.append(SYMBOLS_SOLIDUS).append(ssid).append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		RequestParams rp = new RequestParams();
		rp.put(REQUEST_PARAMS_FRIEND_SID, friendSid);
		client.setBasicAuth(sid, authToken);
		client.post(uri.toString(), rp, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onMessageResponseListener.messageResponse(response);
				Log.d(TAG, "jsonResponse = " + response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				onMessageResponseListener.errorCode(statusCode);
				Log.d(TAG, "https error code = " + statusCode);
			}

		});
	}

	public void getFriendsList(String sid, String authToken, String ssid, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_FRIENDS)
				.append(SYMBOLS_SOLIDUS).append(ssid).append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		client.setBasicAuth(sid, authToken);
		client.get(uri.toString(), new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onMessageResponseListener.messageResponse(response);
				Log.d(TAG, "jsonResponse = " + response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				onMessageResponseListener.errorCode(statusCode);
				Log.d(TAG, "https error code = " + statusCode);
			}

		});
	}

	public void getSipAccount(String sid, String authToken, String ssid, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_ACCOUNTS)
				.append(SYMBOLS_SOLIDUS).append(ssid).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_SIP).append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		client.setBasicAuth(sid, authToken);
		client.get(uri.toString(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onMessageResponseListener.messageResponse(response);
				Log.d(TAG, "jsonResponse = " + response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				onMessageResponseListener.errorCode(statusCode);
				Log.d(TAG, "https error code = " + statusCode);
			}
		});
	}

	public void checkAccountStatus(String sid, String authToken, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_ACCOUNTS)
				.append(SYMBOLS_SOLIDUS).append(sid).append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		client.setBasicAuth(sid, authToken);
		client.get(uri.toString(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onMessageResponseListener.messageResponse(response);
				Log.d(TAG, "jsonResponse = " + response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				onMessageResponseListener.errorCode(statusCode);
				Log.d(TAG, "https error code = " + statusCode);
			}
		});
	}

	public void getSipAccountBySipNumber(String sid, String authToken, String number, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_NUMBERS)
				.append(SYMBOLS_SOLIDUS).append(number).append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		client.setBasicAuth(sid, authToken);
		client.get(uri.toString(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d(TAG, "jsonResponse = " + response.toString());
				onMessageResponseListener.messageResponse(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				Log.d(TAG, "https error code = " + statusCode);
				onMessageResponseListener.errorCode(statusCode);
			}
		});
	}
	
	public void getSipAccountBySipNumberWithSync(String sid, String authToken, String number, OnMessageResponseListener l) {
		onMessageResponseListener = l;
		StringBuffer uri = new StringBuffer();
		uri.append(HTTPS).append(OPENTACT_HTTPS_SERVER_URI).append(SYMBOLS_SOLIDUS).append(OPENTACT_HTTPS_SERVER_VERSION).append(SYMBOLS_SOLIDUS).append(REQUEST_ACTION_NUMBERS)
				.append(SYMBOLS_SOLIDUS).append(number).append(SYMBOLS_POINT).append(RESPONSE_DATA_TYPE_JSON);
		Log.d(TAG, "request uri = " + uri.toString());
		syncClient.setBasicAuth(sid, authToken);
		syncClient.get(uri.toString(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d(TAG, "jsonResponse = " + response.toString());
				onMessageResponseListener.messageResponse(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				Log.d(TAG, "https error code = " + statusCode);
				onMessageResponseListener.errorCode(statusCode);
			}
		});
	}


}
