package com.yht.opentactsmple;

import android.util.Log;

import com.yht.opentact.sip.callback.OnSipCallback;


public class SipCallback implements OnSipCallback {

	@Override
	public void onIncomingCallListener(String msg) {
		System.out.println("sample onSipCallback onincomingCallListener " + msg);
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
