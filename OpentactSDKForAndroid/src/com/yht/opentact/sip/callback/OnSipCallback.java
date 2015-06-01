package com.yht.opentact.sip.callback;

import com.yht.opentact.sip.SipConstants.CALL_STATE;

public interface OnSipCallback {
	
	abstract void onIncomingCallListener(String sipNumber);
	abstract void onCallStateListener(CALL_STATE state);
	abstract void onSipRegStateListener(boolean isActive);
}
