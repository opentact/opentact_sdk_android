package com.yht.opentact.api;

import android.content.Context;

import com.yht.opentact.im.IMService;
import com.yht.opentact.im.callback.IMCallback;
import com.yht.opentact.sip.NativeLibManager;
import com.yht.opentact.sip.SipService;
import com.yht.opentact.sip.callback.OnSipCallback;

public class OpentactManager {

    private static OpentactManager instance;	
    private SipService sipService;
    private IMService imService;
    public static Context APP_CONTEXT;
    public static String ACCOUNT_SID;
    public static String ACCOUNT_SSID;
    public static String ACCOUNT_AUTHTOKEN;


    public static OpentactManager startWork(Context ctx,String sid,String authToken,String ssid,OpentactConfig opentactConfig,OnSipCallback sipCallback) {

        if(sid == null || authToken == null)
            return null;
        
        APP_CONTEXT = ctx;
        ACCOUNT_SID = sid;
        ACCOUNT_AUTHTOKEN = authToken;
        ACCOUNT_SSID = ssid;

        //连网验证sid 及 token 是否有效

        OpentactConfig cfgDefault = opentactConfig;
        if(cfgDefault == null){
            cfgDefault = new OpentactConfig();
        }

        SipService sipService1 = SipService.getInstance();
        //initialize sip
        if(cfgDefault.isEnableSip()){
            if(!sipService1.sipStart(opentactConfig.getSipConfig(),sipCallback)){
                return null;
            }

        }

        //initialize im
        IMService imService = IMService.getInstance();
        if(cfgDefault.isEnableIm()){
            imService.imStart(opentactConfig.getImConfig());
        }

        instance = new OpentactManager(sipService1,imService);
        return instance;    
    }

    public static OpentactManager getInstance(){
    	return instance;
    }

    private OpentactManager(SipService ss,IMService is){
        this.sipService = ss;
        this.imService = is;
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
    
    public void makeCallToSSid(String friend_ssid){
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
    
}
