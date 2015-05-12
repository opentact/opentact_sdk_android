package com.yht.opentact.sip;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;


public class SipAccount extends Account{

	public AccountConfig accountConfig;

    public SipAccount(){
        super();
    }

    public SipAccount(AccountConfig cfg){
        super();
        accountConfig = cfg;
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
    	try {
			AccountInfo ai = getInfo();
			SipService.onSipCallback.onSipRegStateListener(ai.getRegIsActive());
			System.out.println(ai.getRegIsActive()? "*** Register: code=" : "*** Unregister: code="+prm.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
    	SipCall call = new SipCall(this,prm.getCallId());
    	if(SipService.currentCall != null){
    		call.delete();
    		return;
    	}
    	else{
    		SipService.currentCall = call;
    	}
		CallOpParam cop = new CallOpParam();
    	cop.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
    	try {
			call.answer(cop);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	try {
			CallInfo ci = call.getInfo();
			String str = ci.getRemoteUri();
			String sipNumber = str.substring(str.indexOf("sip:")+4,str.indexOf('@'));
			SipService.onSipCallback.onIncomingCallListener(sipNumber);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
