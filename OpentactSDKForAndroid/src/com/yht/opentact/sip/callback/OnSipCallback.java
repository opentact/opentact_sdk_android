package com.yht.opentact.sip.callback;

public interface OnSipCallback {
	
	abstract void onIncomingCallListener(String msg);
	abstract void onCallStateListener(String msg);
	abstract void onSipRegStateListener(boolean isActive);
}
