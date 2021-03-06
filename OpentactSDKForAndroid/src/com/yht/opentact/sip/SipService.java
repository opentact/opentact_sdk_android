package com.yht.opentact.sip;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
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

import com.yht.opentact.cloud.HttpConfig;
import com.yht.opentact.debug.OLog;
import com.yht.opentact.exception.SameThreadException;
import com.yht.opentact.sip.callback.OnHungupCallListener;
import com.yht.opentact.sip.callback.OnSipCallback;
import com.yht.opentact.util.LogWriterUtils;

import android.util.Log;

/**
 * sip服务类,用于封装sip业务逻辑.这是一个单例类
 * 
 * @author weichao.yht
 *
 */
public class SipService {

	// static {
	// System.loadLibrary(NativeLibManager.PJSUA2_LIB_NAME);
	// }

	public static final String TAG = SipService.class.getSimpleName();

	/**
	 * sip服务实例
	 */
	private static SipService instance = new SipService();
	/**
	 * 当前通话
	 */
	public SipCall currentCall = null;
	/**
	 * sip核心
	 */
	private SipEndpoint ep = null;
	/**
	 * sip账户
	 */
	private SipAccount acc = null;
	/**
	 * endpoint config
	 */
	private EpConfig ep_cfg = null;
	/**
	 * transport config
	 */
	private TransportConfig tcfg = null;
	/**
	 * account config
	 */
	private AccountConfig acc_cfg = null;
	/**
	 * 日志处理单元
	 */
	private LogWriterUtils logWriterUtil;
	/**
	 * sip config
	 */
	private SipConfig sipConfig = null;
	/**
	 * sip 通话
	 */
	private SipCall sipCall = null;
	/**
	 * sip线程启动标识
	 */
	private boolean hasSipStack = false;
	/**
	 * sip线程故障标识
	 */
	private boolean sipStackIsCorrupted = false;
	/**
	 * sip初始化标识
	 */
	private boolean created = false;

	/**
	 * 设置一个监听器,监听来电状态
	 */
	public static OnIncomingCallListener onIncomingCallListener;
	/**
	 * 设置一个监听器,监听接通电话的状态
	 */
	public static OnAnswerCallListener onAnswerCallListener;
	/**
	 * 设置一个监听器,监听呼叫中的状态
	 */
	public static OnCallingListener onCallingListener;
	/**
	 * 设置一个监听器,监听sip账户注册的状态
	 */
	public static OnAccountRegisterStateListener onAccountRegisterStateListener;
	/**
	 * 设置一个监听器,监听挂断通话的状态
	 */
	public static OnHungupCallListener onHungupCallListener;
	/**
	 * 设置一个同意sip监听器.用于监听sip过程的各种状态
	 */
	public static OnSipCallback onSipCallback;

	private SipService() {

	}

	/**
	 * 加载pjsip动态库,若已加载,则返回true.<br>
	 * 加载成功,返回true.若加载动态库失败,则抛出UnsatisfiedLinkError异常.
	 * 
	 * @return
	 */
	public boolean tryToLoadStack() {
		if (hasSipStack) {
			return true;
		}

		if (!sipStackIsCorrupted) {
			try {
				System.loadLibrary(NativeLibManager.PJSUA2_LIB_NAME);
				hasSipStack = true;
				return true;
			} catch (UnsatisfiedLinkError e) {
				// If it fails we probably are running on a special hardware
				OLog.e(TAG, "We have a problem with the current stack.... NOT YET Implemented", e);
				hasSipStack = false;
				sipStackIsCorrupted = true;
				return false;
			} catch (Exception e) {
				Log.e(TAG, "We have a problem with the current stack....", e);
			}
		}
		return false;
	}

	public boolean isCreated() {
		return created;
	}

	/**
	 * 获取sip服务类实例
	 * 
	 * @return
	 */
	public static SipService getInstance() {
		return instance;
	}

	/**
	 * 启动sip服务
	 * 
	 * @param cfg
	 *            加载sip配置
	 * @param callback
	 *            加载sip全局监听
	 * @return 启动成功则返回true,启动失败则返回false.
	 * @throws SameThreadException
	 *             抛出线程异常
	 */
	public boolean sipStart(SipConfig cfg, OnSipCallback callback) throws SameThreadException {
		if (!hasSipStack) {
			tryToLoadStack();
		}
		ep = new SipEndpoint();
		ep_cfg = new EpConfig();
		tcfg = new TransportConfig();
		acc_cfg = new AccountConfig();
		sipConfig = new SipConfig();

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

					if (sipConfig.isOneWorkThread() && !sipConfig.isMainThreadOnly()) {
						ep_cfg.getUaConfig().setThreadCnt(1);
					}

					if (sipConfig.isMainThreadOnly() && !sipConfig.isOneWorkThread()) {
						ep.libHandleEvents(5000);
						ep_cfg.getUaConfig().setThreadCnt(0);
						ep_cfg.getUaConfig().setMainThreadOnly(sipConfig.isMainThreadOnly());
					}

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
		setCodecsPriorityByDefault();
		return true;
	}

	/**
	 * 配置编码格式优先级.
	 */
	private void setCodecsPriorityByDefault() {
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

	/**
	 * 
	 * @param codecID
	 * @param priority
	 * @param isDefault
	 */
	public void setCodecPriority(String codecID, short priority, boolean isDefault) {
		try {
			if (isDefault) {
				ep.codecSetPriority(codecID, (short) 0);
			} else {
				ep.codecSetPriority(codecID, priority);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getCodecList() {
		try {
			int listSize = (int) ep.codecEnum().size();
			String[] codecList = new String[listSize];
			for (int i = 0; i < listSize; i++) {
				codecList[i] = ep.codecEnum().get(i).getCodecId();
			}
			return codecList;
		} catch (Exception e) {
			return null;
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
		if (currentCall != null) {
			return;
		}
		sipCall = new SipCall(acc);
		CallOpParam cop = new CallOpParam(true);
		try {
			StringBuffer dst_uri = new StringBuffer();
			dst_uri.append("sip:11111").append(sid).append("@").append(HttpConfig.OPENTACT_HTTPS_SERVER_URI).append(":").append("5060");
			sipCall.makeCall(dst_uri.toString(), cop);
		} catch (Exception e) {
			sipCall.delete();
			return;
		}
		currentCall = sipCall;
	}

	public void makeCallToTermination(String number) {
		if (currentCall != null) {
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
			dst_uri.append("sip:99999").append(number).append("@").append(HttpConfig.OPENTACT_HTTPS_SERVER_URI).append(":").append("5060");
			sipCall.makeCall(dst_uri.toString(), cop);
		} catch (Exception e) {
			e.printStackTrace();
			sipCall.delete();
			return;
		}
		currentCall = sipCall;

	}

	public void answer() {
		if (currentCall != null) {
			CallOpParam prm = new CallOpParam();
			prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
			try {
				currentCall.answer(prm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			onSipCallback.onIncomingCallListener(SipConstants.ON_SIP_CALLBACK_MSG_INCOMINGCALL_ERROR);
		}
	}

	public void hangup() {
		if (currentCall != null) {
			CallOpParam prm = new CallOpParam();
			prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
			try {
				currentCall.hangup(prm);
				currentCall = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removeAccount() {

	}

	public boolean getAccountStatus() {
		if (this.acc == null) {
			return false;
		}
		try {
			return acc.getInfo().getOnlineStatus();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getAccountStatusString() {
		if (this.acc == null) {
			return null;
		}
		try {
			return acc.getInfo().getOnlineStatusText();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setCurrentCall(SipCall currentCall) {
		this.currentCall = currentCall;
	}

	public SipEndpoint getEp() {
		return ep;
	}

	public void setEp(SipEndpoint ep) {
		this.ep = ep;
	}


	public void setOnIncomingCallListener(OnIncomingCallListener l) {
		onIncomingCallListener = l;
	}

	public void setOnAnswerCallListener(OnAnswerCallListener l) {
		onAnswerCallListener = l;
	}

	public void setOnHungupCallListener(OnHungupCallListener l) {
		onHungupCallListener = l;
	}

	public void setOnAccountRegisterStateListener(OnAccountRegisterStateListener l) {
		onAccountRegisterStateListener = l;
	}

	public void setOnCallingListener(OnCallingListener l) {
		onCallingListener = l;
	}

	public interface OnAccountRegisterStateListener {

		abstract void onAccountRegisterStateListener(boolean isActive);

	}

	public interface OnAnswerCallListener {

		abstract void onAnswerCallListener();

	}

	public interface OnCallingListener {

		abstract void onCallingListener();

	}

	public interface OnIncomingCallListener {

		abstract void onIncomingCallListener(String sid);

	}

}
