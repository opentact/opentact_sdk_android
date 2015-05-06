package com.yht.opentact.im.callback;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class IMCallback implements MqttCallback,IMqttActionListener{
	
	public ACTION action;
	
	public IMCallback(){
		this.action = ACTION.DEFAULT;
	}
	
	public IMCallback(ACTION action){
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
		messageArrivedCallback(arg0.substring(arg0.lastIndexOf('/')+1), arg1.toString(), arg1.getQos());
	}

	@Override
	public void onFailure(IMqttToken arg0, Throwable arg1) {
		onActionCallback(action, false, arg1);
	}

	@Override
	public void onSuccess(IMqttToken arg0) {
		onActionCallback(action, true, null);
	}

	public void onActionCallback(ACTION action,boolean isSuccess,Throwable throwable){
		
	}
	
	
	public void messageArrivedCallback(String ssid,String msg,int Qos){
		
	}
	
	public void connectLostCallback(boolean isLost,Throwable throwable){
		
	}

	public ACTION getAction() {
		return action;
	}

	public void setAction(ACTION action) {
		this.action = action;
	}
	
}
