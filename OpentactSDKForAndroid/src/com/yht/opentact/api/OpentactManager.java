package com.yht.opentact.api;

import android.content.Context;

import com.yht.opentact.im.IMService;
import com.yht.opentact.im.callback.IMCallback;
import com.yht.opentact.sip.SipService;

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
    	this.sipService = SipService.getInstance();
    	this.imService = IMService.getInstance();
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

    public SipService getSipService() {
        return sipService;
    }

    public void setSipService(SipService sipService) {
        this.sipService = sipService;
    }

    public IMService getImService() {
        return imService;
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
    
    public void imDoConnect(IMCallback callback){
    	this.imService.doConnect(callback);
    }
    
    public void imPublish(String friend_ssid,String msg,IMCallback callback){
    	this.imService.publish(friend_ssid, msg, callback);
    }
    
    public void imSubscribe(IMCallback callback){
    	this.imService.subscribe(callback);
    }
    
//    private String getMetaValue(Context ctx,String key){
//    	String value = null;
//    	try {
//			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
//			if(ai != null){
//				value = ai.metaData.getString(key);
//			}
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//    	return value;
//    }
    
}
