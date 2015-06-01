package com.yht.opentact.sip;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;

public class SipAccount extends Account {

	public AccountConfig accountConfig;

	public SipAccount() {
		super();
	}

	public SipAccount(AccountConfig cfg) {
		super();
		accountConfig = cfg;
	}

	/**
	 * PJSIP_SC_TRYING = 100, PJSIP_SC_RINGING = 180,
	 * PJSIP_SC_CALL_BEING_FORWARDED = 181, PJSIP_SC_QUEUED = 182,
	 * PJSIP_SC_PROGRESS = 183, PJSIP_SC_OK = 200, PJSIP_SC_MULTIPLE_CHOICES =
	 * 300, PJSIP_SC_MOVED_PERMANENTLY = 301, PJSIP_SC_MOVED_TEMPORARILY = 302,
	 * PJSIP_SC_USE_PROXY = 305, PJSIP_SC_ALTERNATIVE_SERVICE = 380,
	 * PJSIP_SC_BAD_REQUEST = 400, PJSIP_SC_UNAUTHORIZED = 401, Page 34 JSIP
	 * Developer’s Guide PJSIP_SC_PAYMENT_REQUIRED = 402, PJSIP_SC_FORBIDDEN =
	 * 403, PJSIP_SC_NOT_FOUND = 404, PJSIP_SC_METHOD_NOT_ALLOWED = 405,
	 * PJSIP_SC_NOT_ACCEPTABLE = 406, PJSIP_SC_PROXY_AUTHENTICATION_REQUIRED =
	 * 407, PJSIP_SC_REQUEST_TIMEOUT = 408, PJSIP_SC_GONE = 410,
	 * PJSIP_SC_REQUEST_ENTITY_TOO_LARGE = 413, PJSIP_SC_REQUEST_URI_TOO_LONG =
	 * 414, PJSIP_SC_UNSUPPORTED_MEDIA_TYPE = 415,
	 * PJSIP_SC_UNSUPPORTED_URI_SCHEME = 416, PJSIP_SC_BAD_EXTENSION = 420,
	 * PJSIP_SC_EXTENSION_REQUIRED = 421, PJSIP_SC_INTERVAL_TOO_BRIEF = 423,
	 * PJSIP_SC_TEMPORARILY_UNAVAILABLE = 480, PJSIP_SC_CALL_TSX_DOES_NOT_EXIST
	 * = 481, PJSIP_SC_LOOP_DETECTED = 482, PJSIP_SC_TOO_MANY_HOPS = 483,
	 * PJSIP_SC_ADDRESS_INCOMPLETE = 484, PJSIP_AC_AMBIGUOUS = 485,
	 * PJSIP_SC_BUSY_HERE = 486, PJSIP_SC_REQUEST_TERMINATED = 487,
	 * PJSIP_SC_NOT_ACCEPTABLE_HERE = 488, PJSIP_SC_REQUEST_PENDING = 491,
	 * PJSIP_SC_UNDECIPHERABLE = 493, PJSIP_SC_INTERNAL_SERVER_ERROR = 500,
	 * PJSIP_SC_NOT_IMPLEMENTED = 501, PJSIP_SC_BAD_GATEWAY = 502,
	 * PJSIP_SC_SERVICE_UNAVAILABLE = 503, PJSIP_SC_SERVER_TIMEOUT = 504,
	 * PJSIP_SC_VERSION_NOT_SUPPORTED = 505, PJSIP_SC_MESSAGE_TOO_LARGE = 513,
	 * PJSIP_SC_BUSY_EVERYWHERE = 600, PJSIP_SC_DECLINE = 603,
	 * PJSIP_SC_DOES_NOT_EXIST_ANYWHERE = 604, PJSIP_SC_NOT_ACCEPTABLE_ANYWHERE
	 * = 606, PJSIP_SC_TSX_TIMEOUT = 701, PJSIP_SC_TSX_RESOLVE_ERROR = 702,
	 * PJSIP_SC_TSX_TRANSPORT_ERROR = 703,
	 */
	@Override
	public void onRegState(OnRegStateParam prm) {
		try {
			AccountInfo ai = getInfo();
			SipService.onSipCallback.onSipRegStateListener(ai.getRegIsActive());
			System.out.println(ai.getRegIsActive() ? "*** Register: code=" : "*** Unregister: code=" + prm.getCode().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onIncomingCall(OnIncomingCallParam prm) {
		SipCall call = new SipCall(this, prm.getCallId());
		if (SipService.getInstance().currentCall != null) {
			call.delete();
			return;
		} else {
			SipService.getInstance().currentCall = call;
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
			String sipNumber = str.substring(str.indexOf("sip:") + 4, str.indexOf('@'));
			SipService.onSipCallback.onIncomingCallListener(sipNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
