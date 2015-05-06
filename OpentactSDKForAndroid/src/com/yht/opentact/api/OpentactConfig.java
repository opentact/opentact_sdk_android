package com.yht.opentact.api;

import com.yht.opentact.im.IMConfig;
import com.yht.opentact.sip.SipConfig;

public class OpentactConfig {
	private IMConfig imConfig = new IMConfig();
    private SipConfig sipConfig = new SipConfig();
    private boolean enableSip = true;
    private boolean enableIm = true;

    public OpentactConfig(){

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
}
