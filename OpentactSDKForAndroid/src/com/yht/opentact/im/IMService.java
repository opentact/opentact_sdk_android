package com.yht.opentact.im;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.yht.opentact.api.OpentactManager;
import com.yht.opentact.im.callback.IMCallback;

import android.content.Context;

public class IMService {

	private static IMService instance = new IMService();
	
	public static final String OPENTACT = "opentact";
    public static final String SPRIT = "/";

    private MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttConnectOptions;
    private IMConfig imCfg;
    private IMCallback imCallback;

    private IMService(){
    	
    }
    
    public static IMService getInstance(){
    	return instance;
    }
    
    public void imStart(IMConfig cfg){
    	this.imCfg = cfg;
        String uri = "tcp://"+IMConstants.HOST+":"+IMConstants.PORT;
        mqttAndroidClient = new MqttAndroidClient(OpentactManager.APP_CONTEXT,uri,OpentactManager.ACCOUNT_SSID);
    }

    public void imDisconnect(IMCallback callback){
    	try {
			mqttAndroidClient.disconnect(OpentactManager.APP_CONTEXT, callback);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void doConnect(IMCallback callback){
        if(this.imCfg == null)
            imCfg = new IMConfig();
        try {
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName(OpentactManager.ACCOUNT_SSID);
            mqttConnectOptions.setPassword(OpentactManager.ACCOUNT_AUTHTOKEN.toCharArray());
            mqttConnectOptions.setKeepAliveInterval(imCfg.getKeepAliveInterval());
            mqttConnectOptions.setCleanSession(imCfg.isCleanSession());
            mqttConnectOptions.setConnectionTimeout(imCfg.getConnectionTimeout());
            mqttAndroidClient.connect(mqttConnectOptions,null,callback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String frient_ssid,String msg,IMCallback callback){
        if(this.imCfg == null)
            imCfg = new IMConfig();
        try {
            mqttAndroidClient.publish(buildTopic(frient_ssid),msg.getBytes(),imCfg.getQos(),imCfg.isRetained(),null,callback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(IMCallback callback){
        if(this.imCfg == null)
            imCfg = new IMConfig();
        if(this.imCallback == null)
        	this.imCallback = callback;
        mqttAndroidClient.setCallback(callback);
        try {
        	mqttAndroidClient.subscribe(buildTopic(OpentactManager.ACCOUNT_SSID), imCfg.getQos(), null, callback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private String buildTopic(String ssid){
        StringBuffer topic = new StringBuffer();
        topic.append(OPENTACT).append(SPRIT).append(OpentactManager.ACCOUNT_SID).append(SPRIT)
                .append(ssid);
        return topic.toString().trim();
    }
}
