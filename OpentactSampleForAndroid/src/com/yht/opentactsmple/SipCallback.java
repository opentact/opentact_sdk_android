package com.yht.opentactsmple;


import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.yht.opentact.sip.callback.OnSipCallback;


public class SipCallback implements OnSipCallback {
	
	private Context context;
	
	public SipCallback(Context ctx){
		this.context = ctx;
	}

	@Override
	public void onIncomingCallListener(String sipNumber) {
		System.out.println("sample onSipCallback onincomingCallListener " + sipNumber);
		Intent intent = new Intent();
		intent.putExtra("isIncomingCall", true);
		intent.setAction("com.yht.opentactsmple.notifycall");
		this.context.sendBroadcast(intent);
	}
	
	@Override
	public void onCallStateListener(String msg) {
		System.out.println("sample onSipCallback onCallStateListener " + msg);
	}

	@Override
	public void onSipRegStateListener(boolean isActive) {
		if(isActive){
			System.out.println("sipAccount create success" );
		}
	}
}
