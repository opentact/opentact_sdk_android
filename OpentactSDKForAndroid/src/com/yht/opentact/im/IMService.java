package com.yht.opentact.im;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.yht.opentact.api.OpentactConfig;
import com.yht.opentact.im.callback.IMCallback;

/**
 * IM服务类,封装了IM的业务逻辑. 这是一个单例类
 * 
 * @author weichao.yht
 *
 */
public class IMService {

	/**
	 * IM服务类实例
	 */
	private static IMService instance = new IMService();

	/**
	 * 常量--opentact
	 */
	public static final String OPENTACT = "opentact";
	/**
	 * 字符常量--左斜线
	 */
	public static final String SPRIT = "/";

	/**
	 * 连接mqtt服务器
	 */
	private MqttAndroidClient mqttAndroidClient = null;
	/**
	 * mqtt连接操作
	 */
	private MqttConnectOptions mqttConnectOptions = null;
	/**
	 * IM配置
	 */
	private IMConfig imCfg;
	/**
	 * IM消息监听
	 */
	private IMCallback imCallback;

	private IMService() {

	}

	/**
	 * 获取IM服务实例
	 * 
	 * @return
	 */
	public static IMService getInstance() {
		return instance;
	}

	/**
	 * 启动IM服务
	 * 
	 * @param cfg
	 */
	public void imStart(IMConfig cfg) {
		this.imCfg = cfg;
		String uri = "tcp://" + IMConstants.HOST + ":" + IMConstants.PORT;
		mqttAndroidClient = new MqttAndroidClient(OpentactConfig.getInstance().getAppContext(), uri, OpentactConfig.getInstance().getSsid());
	}

	/**
	 * 断开IM服务器连接
	 * 
	 * @param callback
	 *            --监听
	 * 
	 * @see IMCallback
	 */
	public void imDisconnect(IMCallback callback) {
		try {
			mqttAndroidClient.disconnect(OpentactConfig.getInstance().getAppContext(), callback);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建IM服务器连接
	 * 
	 * @param callback
	 *            --监听
	 * 
	 * @see IMCallback
	 */
	public void doConnect(IMCallback callback) {
		if (this.imCfg == null)
			imCfg = new IMConfig();
		try {
			mqttConnectOptions = new MqttConnectOptions();
			mqttConnectOptions.setUserName(OpentactConfig.getInstance().getSsid());
			mqttConnectOptions.setPassword(OpentactConfig.getInstance().getAuthtoken().toCharArray());
			mqttConnectOptions.setKeepAliveInterval(imCfg.getKeepAliveInterval());
			mqttConnectOptions.setCleanSession(imCfg.isCleanSession());
			mqttConnectOptions.setConnectionTimeout(imCfg.getConnectionTimeout());
			mqttAndroidClient.connect(mqttConnectOptions, null, callback);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发布方法,将消息发送至指定好友账户.
	 * 
	 * @param frient_ssid
	 *            --好友账户
	 * @param msg
	 *            --待发送的消息
	 * @param callback
	 *            --事件监听
	 * 
	 * @see IMCallback
	 */
	public void publish(String frient_ssid, String msg, IMCallback callback) {
		if (this.imCfg == null)
			imCfg = new IMConfig();
		try {
			mqttAndroidClient.publish(buildTopic(frient_ssid), msg.getBytes(), imCfg.getQos(), imCfg.isRetained(), null, callback);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 消息订阅,将本地账户订阅到IM服务器,以便其他账户能向此账户发送消息.
	 * 
	 * @param callback
	 *            --事件监听
	 * 
	 * @see IMCallback
	 */
	public void subscribe(IMCallback callback) {
		if (this.imCfg == null)
			imCfg = new IMConfig();
		if (this.imCallback == null)
			this.imCallback = callback;
		mqttAndroidClient.setCallback(callback);
		try {
			mqttAndroidClient.subscribe(buildTopic(OpentactConfig.getInstance().getSsid()), imCfg.getQos(), null, callback);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构建订阅的主题,即需要订阅的子账户.
	 * 
	 * @param ssid
	 *            --需订阅的子账户.
	 * @return 组合后的主题字符串格式.
	 */
	private String buildTopic(String ssid) {
		StringBuffer topic = new StringBuffer();
		topic.append(OPENTACT).append(SPRIT).append(OpentactConfig.getInstance().getSid()).append(SPRIT).append(ssid);
		return topic.toString().trim();
	}
}
