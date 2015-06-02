package com.yht.opentact.api;


import android.content.Context;

import com.yht.opentact.im.IMService;
import com.yht.opentact.im.callback.IMCallback;
import com.yht.opentact.sip.SipService;
import com.yht.opentact.sip.SipService.OnCallingListener;
import com.yht.opentact.sip.callback.OnHungupCallListener;

public class OpentactManager {

    private static OpentactManager instance = new OpentactManager();	
    private SipService sipService;
    private IMService imService;
    public static Context appContext;
    public boolean isCreated = false;

    public void init(Context ctx){
    	if(isCreated){
    		return;
    	}
    	if(appContext == null){
    		appContext = ctx;
    	}
    	if(OpentactConfig.getInstance().isEnableIm()){
    		this.imService = IMService.getInstance();
    	}
    	if(OpentactConfig.getInstance().isEnableSip()){
    		this.sipService = SipService.getInstance();
    	}
    	isCreated = true;
    }
    
    public static OpentactManager getInstance(){
    	return instance;
    }

    private OpentactManager(){
    	
    }
    
    public void stopOpentact(){
    	stopSipService();
    }
    
    public void stopSipService(){
    	sipService.sipStop();
    }

    public void answerCall(){
    	this.sipService.answer();
    }
    
    public void hangupCall(){
    	this.sipService.hangup();
    }
    
    public void setSipCodecPriority(String codecID,short priority,boolean isDefault){
    	this.sipService.setCodecPriority(codecID, priority, isDefault);
    }
    
    public String[] getCodecList(){
    	return this.sipService.getCodecList();
    }
    
    public void setImService(IMService imService) {
        this.imService = imService;
    }
    
    public void makeCallToSsid(String friend_ssid){
    	this.sipService.makeCallToSid(friend_ssid);
    }
    
    public void makeCallToTermination(String number){
    	this.sipService.makeCallToTermination(number);
    }
    
    public void addSipAccount(String username,String password){
    	this.sipService.addAccount(username, password);
    }
    
    public void clearCurrentSipCall(){
    	this.sipService.setCurrentCall(null);
    }
    
    public void imDoConnect(IMCallback callback){
    	this.imService.doConnect(callback);
    }
    
    public void imPublish(String friend_ssid,String msg,IMCallback callback){
    	this.imService.publish(friend_ssid, msg, callback);
    }
    
    public void imSubscribe(IMCallback callback){
    	this.imService.subscribe(callback);
    }
    
    public boolean getSipAccountOnlineStatus(){
    	return this.sipService.getAccountStatus();
    }
    
    public String getSipAccountOnlineStatusString(){
    	return this.sipService.getAccountStatusString();
    }
    
    public void setOnHungupCallListener(OnHungupCallListener l){
    	this.sipService.setOnHungupCallListener(l);
    }
    
    public void setOnCallingListener(OnCallingListener l){
    	this.sipService.setOnCallingListener(l);
    }
}
