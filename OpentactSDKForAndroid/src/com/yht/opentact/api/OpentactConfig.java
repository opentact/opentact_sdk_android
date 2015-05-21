package com.yht.opentact.api;

import android.content.Context;

import com.yht.opentact.im.IMConfig;
import com.yht.opentact.sip.SipConfig;

public class OpentactConfig {
	private static OpentactConfig instance = null;
	private IMConfig imConfig = new IMConfig();
    private SipConfig sipConfig = new SipConfig();
    private boolean enableSip = true;
    private boolean enableIm = true;
    private String sid = null;
	private String authtoken = null;
	private String ssid = null;
	private Context appContext = null;
	private boolean debugMode = false;
    

    private OpentactConfig(){

    }
    
    public static OpentactConfig getInstance(){
    	if(instance == null){
    		instance = new OpentactConfig();
    	}
    	return instance;
    }

    public boolean isEnableSip() {
        return enableSip;
    }

    public void setEnableSip(boolean enableSip) {
        this.enableSip = enableSip;
    }

    public boolean isEnableIm() {
        return enableIm;
    }

    public void setEnableIm(boolean enableIm) {
        this.enableIm = enableIm;
    }

    public IMConfig getImConfig() {
        return imConfig;
    }

    public void setImConfig(IMConfig imConfig) {
        this.imConfig = imConfig;
    }

    public SipConfig getSipConfig() {
        return sipConfig;
    }

    public void setSipConfig(SipConfig sipConfig) {
        this.sipConfig = sipConfig;
    }

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getAuthtoken() {
		return authtoken;
	}

	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Context getAppContext() {
		return appContext;
	}

	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
    
	
    
}
