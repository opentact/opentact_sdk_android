package com.yht.opentact.api;

import android.content.Context;

import com.yht.opentact.im.IMService;
import com.yht.opentact.im.callback.IMCallback;
import com.yht.opentact.sip.SipService;
import com.yht.opentact.sip.SipService.OnCallingListener;
import com.yht.opentact.sip.callback.OnHungupCallListener;

/**
 * <li>Opentact管理类.实现Opentact业务逻辑的代理.开发者在使用OpentactSDK进行开发时,只需要关注此管理类所提供的方法即可.
 * <br> <li>这是一个单利类.
 * 
 * @author weichao.yht
 *
 */
public class OpentactManager {

	/**
	 * OpentactManager实例,用于实现单利模式
	 */
	private static OpentactManager instance = new OpentactManager();
	/**
	 * SipService类实例
	 */
	private SipService sipService;
	/**
	 * IMService类实例
	 */
	private IMService imService;
	/**
	 * android系统上下文
	 */
	public static Context appContext;
	/**
	 * 初始化标识
	 */
	public boolean isCreated = false;

	/**
	 * 初始化OpentactManager
	 * 
	 * @param ctx
	 *            --android系统上下文
	 */
	public void init(Context ctx) {
		if (isCreated) {
			return;
		}
		if (appContext == null) {
			appContext = ctx;
		}
		if (OpentactConfig.getInstance().isEnableIm()) {
			this.imService = IMService.getInstance();
		}
		if (OpentactConfig.getInstance().isEnableSip()) {
			this.sipService = SipService.getInstance();
		}
		isCreated = true;
	}

	/**
	 * 获得OpentactManager实例
	 * 
	 * @return
	 */
	public static OpentactManager getInstance() {
		return instance;
	}

	private OpentactManager() {

	}

	/**
	 * 停止OpentactSDK服务
	 */
	public void stopOpentact() {
		stopSipService();
	}

	/**
	 * 停止Sip服务
	 */
	public void stopSipService() {
		sipService.sipStop();
	}

	/**
	 * 应答来电
	 */
	public void answerCall() {
		this.sipService.answer();
	}

	/**
	 * 挂断当前电话
	 */
	public void hangupCall() {
		this.sipService.hangup();
	}

	/**
	 * 设置语音通话编码优先级
	 * 
	 * @param codecID
	 *            --编码ID
	 * @param priority
	 *            --优先级.设置为0时表示不启用此编码
	 * @param isDefault
	 *            --使用系统默认优先级.
	 */
	public void setSipCodecPriority(String codecID, short priority, boolean isDefault) {
		this.sipService.setCodecPriority(codecID, priority, isDefault);
	}

	/**
	 * 获取语音通话系统编码列表
	 * 
	 * @return 编码列表
	 */
	public String[] getCodecList() {
		return this.sipService.getCodecList();
	}

	/**
	 * 设置IMService实例
	 * 
	 * @param imService
	 */
	public void setImService(IMService imService) {
		this.imService = imService;
	}

	/**
	 * 开启一个点对点呼叫, 呼叫另一个与之建立朋友关系的子账户.
	 * 
	 * @param friend_ssid
	 *            --呼叫目标子账户
	 */
	public void makeCallToSsid(String friend_ssid) {
		this.sipService.makeCallToSid(friend_ssid);
	}

	/**
	 * 呼叫一个终端.
	 * 
	 * @param number
	 *            --终端电话号,如移动电话号码或固定电话号码.
	 */
	public void makeCallToTermination(String number) {
		this.sipService.makeCallToTermination(number);
	}

	/**
	 * 通过sip用户名和密码,向sip服务器进行注册.
	 * 
	 * @param username
	 *            --sip用户名
	 * @param password
	 *            --sip密码
	 */
	public void addSipAccount(String username, String password) {
		this.sipService.addAccount(username, password);
	}

	/**
	 * 清理当前通话.用于在挂断电话后对通话进行清理.
	 */
	public void clearCurrentSipCall() {
		this.sipService.setCurrentCall(null);
	}

	/**
	 * 连接IM服务器
	 * 
	 * @param callback
	 *            IM监听回调.连接状态通过此回调返回.
	 */
	public void imDoConnect(IMCallback callback) {
		this.imService.doConnect(callback);
	}

	/**
	 * 发布一个im账户到服务器.用于将消息发送到目的账户.
	 * 
	 * @param friend_ssid
	 *            --与之建立朋友关系的子账户
	 * @param msg
	 *            --封装的消息.
	 * @param callback
	 *            --IM监听回调.publish的结果将通过此回调返回.
	 */
	public void imPublish(String friend_ssid, String msg, IMCallback callback) {
		this.imService.publish(friend_ssid, msg, callback);
	}

	/**
	 * 订阅一个本地IM账户至服务器,用于接收发送至此账户的消息
	 * 
	 * @param callback
	 *            --IM监听回调,所有接收到的im消息都通过此回调返回.
	 */
	public void imSubscribe(IMCallback callback) {
		this.imService.subscribe(callback);
	}

	/**
	 * 获取sip账户在线状态.
	 * 
	 * @return 在线状态.true即为在线.反之亦然.
	 */
	public boolean getSipAccountOnlineStatus() {
		return this.sipService.getAccountStatus();
	}

	/**
	 * 获取sip账户在线状态说明
	 * 
	 * @return 在线状态的说明
	 */
	public String getSipAccountOnlineStatusString() {
		return this.sipService.getAccountStatusString();
	}

	/**
	 * 设置挂断电话监听.监听挂断电话过程.
	 * @param l	监听函数
	 */
	public void setOnHungupCallListener(OnHungupCallListener l) {
		this.sipService.setOnHungupCallListener(l);
	}

	/**
	 * 设置接听等待监听.监听电话等待接听的过程
	 * @param l	监听函数
	 */
	public void setOnCallingListener(OnCallingListener l) {
		this.sipService.setOnCallingListener(l);
	}
}
