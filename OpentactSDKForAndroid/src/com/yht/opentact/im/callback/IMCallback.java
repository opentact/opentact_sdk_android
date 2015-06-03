package com.yht.opentact.im.callback;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * IM事件监听
 * 
 * @author weichao.yht
 *
 */
public class IMCallback implements MqttCallback, IMqttActionListener {

	/**
	 * IM ACTION
	 */
	public ACTION action;

	public IMCallback() {
		this.action = ACTION.DEFAULT;
	}

	public IMCallback(ACTION action) {
		this.action = action;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		connectLostCallback(true, arg0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		messageArrivedCallback(arg0.substring(arg0.lastIndexOf('/') + 1), arg1.toString(), arg1.getQos());
	}

	@Override
	public void onFailure(IMqttToken arg0, Throwable arg1) {
		onActionCallback(action, false, arg1);
	}

	@Override
	public void onSuccess(IMqttToken arg0) {
		onActionCallback(action, true, null);
	}

	/**
	 * 获取MQTT操作返回.
	 * 
	 * @param action
	 *            --IM action
	 * @param isSuccess
	 *            --操作是否成功
	 * @param throwable
	 *            --抛出的异常
	 * 
	 * @see ACTION
	 */
	public void onActionCallback(ACTION action, boolean isSuccess, Throwable throwable) {

	}

	/**
	 * 接收到消息的事件监听
	 * 
	 * @param ssid
	 *            --目标账户
	 * @param msg
	 *            --接收到的消息,字符串类型
	 * @param Qos
	 *            --Qos格式
	 */
	public void messageArrivedCallback(String ssid, String msg, int Qos) {

	}

	/**
	 * 连接失效的事件监听
	 * 
	 * @param isLost
	 *            --是否失效,如果为true则失效,反之亦然.
	 * @param throwable
	 *            --抛出的异常.
	 */
	public void connectLostCallback(boolean isLost, Throwable throwable) {

	}

	/**
	 * 获取ACTION
	 * 
	 * @return
	 * @see ACTION
	 */
	public ACTION getAction() {
		return action;
	}

	/**
	 * 配置action
	 * 
	 * @param action
	 * @see ACTION
	 */
	public void setAction(ACTION action) {
		this.action = action;
	}

}
