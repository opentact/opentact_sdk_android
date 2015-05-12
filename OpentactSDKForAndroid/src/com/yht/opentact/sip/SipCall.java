package com.yht.opentact.sip;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsua_call_media_status;

public class SipCall extends Call{

	public SipCall(SipAccount acc) {
		super(acc);
	}
	
	public SipCall(SipAccount acc,int call_id){
		super(acc, call_id);
	}
	
	@Override
	public void onCallState(OnCallStateParam prm) {
		
		try {
			CallInfo ci = getInfo();
			if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
				this.delete();
			}
//			String str = ci.getRemoteUri();
//			String number = str.substring(str.indexOf("sip:")+4,str.indexOf('@'));
//
//			HttpService https = new HttpService(false);
//			https.getSipAccountBySipNumberWithSync(OpentactManager.ACCOUNT_SID, OpentactManager.ACCOUNT_AUTHTOKEN, number,new OnMessageResponseListener() {
//				
//				@Override
//				public void messageResponse(JSONObject jsonMsg) {
//					try {
//						SipService.onSipCallback.onCallStateListener(jsonMsg.getString("sid"));
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//				
//				@Override
//				public void errorCode(int errCode) {
//					
//				}
//			});
		} catch (Exception e) {
			return;
		}
	}
	
	@Override
	public void onCallMediaState(OnCallMediaStateParam prm) {
		super.onCallMediaState(prm);
		CallInfo ci;
		try {
			ci = getInfo();
		} catch (Exception e) {
			return;
		}
		
		CallMediaInfoVector cmiv = ci.getMedia();
		
		for (int i = 0; i < cmiv.size(); i++) {
			CallMediaInfo cmi = cmiv.get(i);
			if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
			    (cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
			     cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD))
			{
				// unfortunately, on Java too, the returned Media cannot be downcasted to AudioMedia 
				Media m = getMedia(i);
				AudioMedia am = AudioMedia.typecastFromMedia(m);
				
				// connect ports
				try {
					if(SipService.getInstance() != null){
						SipService.getInstance().getEp().audDevManager().getCaptureDevMedia().startTransmit(am);
						am.startTransmit(SipService.getInstance().getEp().audDevManager().getPlaybackDevMedia());
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
	}

}
