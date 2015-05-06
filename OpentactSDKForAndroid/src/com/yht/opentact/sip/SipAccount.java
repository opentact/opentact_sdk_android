package com.yht.opentact.sip;

import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yht.opentact.api.OpentactManager;
import com.yht.opentact.util.HttpService;
import com.yht.opentact.util.HttpService.OnMessageResponseListener;


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
    	SipService.onSipCallback.onIncomingCallListener("have a call Incoming");
    	SipCall call = new SipCall(this,prm.getCallId());
		CallOpParam cop = new CallOpParam();
    	cop.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
    	try {
			call.answer(cop);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	cop.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
    	try {
			call.answer(cop);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
