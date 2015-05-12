package com.yht.opentact.sip;

import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.OnNatDetectionCompleteParam;

public class SipEndpoint extends Endpoint{

	public SipEndpoint(){
        super();
    }
	
	@Override
	public void onNatDetectionComplete(OnNatDetectionCompleteParam prm) {
		System.out.println(prm.getNatTypeName() + prm.getReason());
	}
}
