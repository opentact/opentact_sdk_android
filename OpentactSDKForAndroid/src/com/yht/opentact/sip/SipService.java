package com.yht.opentact.sip;

import org.json.JSONObject;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.CodecInfoVector;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;
import org.pjsip.pjsua2.pj_log_decoration;
import org.pjsip.pjsua2.pj_turn_tp_type;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsip_transport_type_e;
import org.pjsip.pjsua2.pjsua_state;

import com.yht.opentact.api.OpentactManager;
import com.yht.opentact.sip.callback.OnSipCallback;
import com.yht.opentact.util.HttpService;
import com.yht.opentact.util.HttpService.OnMessageResponseListener;
import com.yht.opentact.util.LogWriterUtils;

import android.util.Log;

public class SipService {

	static {
		System.loadLibrary(NativeLibManager.PJSUA2_LIB_NAME);
	}

	public static final String TAG = SipService.class.getSimpleName();

	private static SipService instance = new SipService();
	public static SipCall currentCall;
	private SipEndpoint ep = new SipEndpoint();
	private SipAccount acc;
	private EpConfig ep_cfg = new EpConfig();
	private TransportConfig tcfg = new TransportConfig();
	private AccountConfig acc_cfg = new AccountConfig();
	private LogWriterUtils logWriterUtil;
	private SipConfig sipConfig = new SipConfig();
	private SipCall sipCall;
	

	private boolean created = false;

	public static OnSipCallback onSipCallback;

	private SipService() {

	}

	public boolean isCreated() {
		return created;
	}

	
	public static SipService getInstance() {
		return instance;
	}


	public boolean sipStart(SipConfig cfg, OnSipCallback callback) {
		if (cfg != null) {
			sipConfig = cfg;
		}

		if (callback != null) {
			onSipCallback = callback;
		}

		// Ensure the stack is not already created or is being created
		if (!created) {
			Log.d(TAG, "Starting sip stack");

			// General config
			{
				try {
					ep.libCreate();
					ep_cfg.getUaConfig().setMaxCalls(4);
					ep_cfg.getMedConfig().setClockRate(16000);
					ep_cfg.getLogConfig().setLevel(5);

					// LOG CONFIG
					if (sipConfig.isDebugModel()) {
						LogConfig log_cfg = ep_cfg.getLogConfig();
						logWriterUtil = new LogWriterUtils();
						log_cfg.setWriter(logWriterUtil);
						log_cfg.setDecor(log_cfg.getDecor() & ~(pj_log_decoration.PJ_LOG_HAS_CR.swigValue() | pj_log_decoration.PJ_LOG_HAS_NEWLINE.swigValue()));
					}

					// STUN
					if (sipConfig.isStunEnable()) {
						System.out.println("stun Open");
						UaConfig ua_cfg = ep_cfg.getUaConfig();
						ua_cfg.setUserAgent("YHT Android" + ep.libVersion().getFull());
						StringVector stun_servers = new StringVector();
						stun_servers.add(SipConstants.STUN_SERVER_URI);
						ua_cfg.setStunServer(stun_servers);
					}

					ep.libInit(ep_cfg);

				} catch (Exception e) {
					Log.e(TAG, "init sip error");
					e.printStackTrace();
				}

			}
			// transport configure
			{
				try {
					tcfg.setPort(SipConstants.TRANSPORT_PORT);
					ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, tcfg);
					ep.libStart();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		created = true;
		setCodecPriority();
		return true;
	}
	
	private void setCodecPriority(){
		try {
			ep.codecSetPriority(SipConstants.CODEC_ID.G722_16000, (short) 0);
			ep.codecSetPriority(SipConstants.CODEC_ID.GSM_8000, (short) 0);
			ep.codecSetPriority(SipConstants.CODEC_ID.PCMA_8000, (short) 0);
			ep.codecSetPriority(SipConstants.CODEC_ID.PCMU_8000, (short) 0);
			ep.codecSetPriority(SipConstants.CODEC_ID.SPEEX_16000, (short) 0);
			ep.codecSetPriority(SipConstants.CODEC_ID.SPEEX_32000, (short) 0);
			ep.codecSetPriority(SipConstants.CODEC_ID.SPEEX_8000, (short) 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sipStop() {
		if (ep.libGetState() == pjsua_state.PJSUA_STATE_CREATED) {
			try {
				ep.libDestroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addAccount(String username, String password) {
		Log.d(TAG, "start registar sip account");
		acc = new SipAccount();
		acc_cfg.getCallConfig().setTimerMinSESec(100);
		acc_cfg.getCallConfig().setTimerSessExpiresSec(180);
		// acc_cfg.getCallConfig().setTimerUse(pjsua_sip_timer_use.PJSUA_SIP_TIMER_ALWAYS);
		// ICE
		if (sipConfig.isIceEnable()) {
			System.out.println("ice open");
			acc_cfg.getNatConfig().setIceEnabled(sipConfig.isIceEnable());
			// acc_cfg.getNatConfig().setIceAggressiveNomination(false);
		}
		// TURN
		if (sipConfig.isTurnEnable()) {
			System.out.println("turn open");
			acc_cfg.getNatConfig().setTurnEnabled(true);
			acc_cfg.getNatConfig().setTurnServer(SipConstants.TURN_SERVER);
			acc_cfg.getNatConfig().setTurnUserName(SipConstants.TURN_USERNAME);
			acc_cfg.getNatConfig().setTurnPassword(SipConstants.TURN_PASSWORD);
			acc_cfg.getNatConfig().setTurnPasswordType(0);
			acc_cfg.getNatConfig().setTurnConnType(pj_turn_tp_type.PJ_TURN_TP_TCP);
		}
		acc_cfg.setIdUri(SipConstants.buildSipIdUri(username));
		acc_cfg.getRegConfig().setRegistrarUri("sip:" + SipConstants.SIP_SERVER_URI);
		acc_cfg.getSipConfig().getAuthCreds()
				.add(new AuthCredInfo(SipConstants.SIP_SERVER_SCHEME, SipConstants.SIP_SERVER_REALM, username, SipConstants.SIP_SERVER_DATATYEP, password));
		try {
			acc.create(acc_cfg, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void makeCallToSid(String sid) {
		if(currentCall != null){
			return;
		}
		sipCall = new SipCall(acc);
		CallOpParam cop = new CallOpParam(true);
		try {
			StringBuffer dst_uri = new StringBuffer();
			dst_uri.append("sip:11111").append(sid).append("@").append(HttpService.OPENTACT_HTTPS_SERVER_URI).append(":").append("5060");
			sipCall.makeCall(dst_uri.toString(), cop);
		} catch (Exception e) {
			sipCall.delete();
			return;
		}
		currentCall = sipCall;
	}

	public void makeCallToTermination(String number) {
		if(currentCall != null){
			return;
		}
		Log.d(TAG, "makeCallToTermination" + number);
		sipCall = new SipCall(acc, -1);
		CallOpParam cop = new CallOpParam();
		CallSetting opt = cop.getOpt();
		opt.setAudioCount(1);
		opt.setVideoCount(0);
		try {
			StringBuffer dst_uri = new StringBuffer();
			dst_uri.append("sip:99999").append(number).append("@").append(HttpService.OPENTACT_HTTPS_SERVER_URI).append(":").append("5060");
			sipCall.makeCall(dst_uri.toString(), cop);
		} catch (Exception e) {
			e.printStackTrace();
			sipCall.delete();
			return;
		}
		currentCall = sipCall;

	}

	public void answer() {
		if(currentCall != null){
			CallOpParam prm = new CallOpParam();
			prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
			try {
				currentCall.answer(prm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			onSipCallback.onIncomingCallListener(SipConstants.ON_SIP_CALLBACK_MSG_INCOMINGCALL_ERROR);
		}
	}

	public void hangup() {
		if(currentCall != null){
			CallOpParam prm = new CallOpParam();
			prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
			try {
				currentCall.hangup(prm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removeAccount() {

	}

	public SipEndpoint getEp() {
		return ep;
	}

	public void setEp(SipEndpoint ep) {
		this.ep = ep;
	}

	
}
