package com.yht.opentact.sip;

import java.util.HashMap;
import java.util.Map;

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

import com.yht.opentact.sip.SipConstants.CALL_STATE;

public class SipCall extends Call {

	public static Map<pjsip_inv_state, CALL_STATE> callStateMap = new HashMap<pjsip_inv_state, SipConstants.CALL_STATE>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(pjsip_inv_state.PJSIP_INV_STATE_CALLING, CALL_STATE.CALLING);
			put(pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED, CALL_STATE.CONFIRMED);
			put(pjsip_inv_state.PJSIP_INV_STATE_CONNECTING, CALL_STATE.CONNECTING);
			put(pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED, CALL_STATE.DISCONNECTED);
			put(pjsip_inv_state.PJSIP_INV_STATE_EARLY, CALL_STATE.EARLY);
			put(pjsip_inv_state.PJSIP_INV_STATE_INCOMING, CALL_STATE.INCOMING);
			put(pjsip_inv_state.PJSIP_INV_STATE_NULL, CALL_STATE.NULL);
		}
	};

	public SipCall(SipAccount acc) {
		super(acc);
	}

	public SipCall(SipAccount acc, int call_id) {
		super(acc, call_id);
	}

	@Override
	public void onCallState(OnCallStateParam prm) {
		/**
		 * PJSIP_INV_STATE_NULL Before INVITE is sent or received
		 * 
		 * PJSIP_INV_STATE_CALLING After INVITE is sent
		 * 
		 * PJSIP_INV_STATE_INCOMING After INVITE is received.
		 * 
		 * PJSIP_INV_STATE_EARLY After response with To tag.
		 * 
		 * PJSIP_INV_STATE_CONNECTING After 2xx is sent/received.
		 * 
		 * PJSIP_INV_STATE_CONFIRMED After ACK is sent/received.
		 * 
		 * PJSIP_INV_STATE_DISCONNECTED Session is terminated.
		 */
		try {
			CallInfo ci = getInfo();
			SipService.onSipCallback.onCallStateListener(callStateMap.get(ci.getState()));
			if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
				this.delete();
			}
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
			if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO
					&& (cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE || cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
				// unfortunately, on Java too, the returned Media cannot be
				// downcasted to AudioMedia
				Media m = getMedia(i);
				AudioMedia am = AudioMedia.typecastFromMedia(m);

				// connect ports
				try {
					if (SipService.getInstance() != null) {
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
