package com.yht.opentact.sip;


public class SipConfig {

	private boolean turnEnable = true;
    private boolean iceEnable = true;
    private boolean stunEnable = true;
    private boolean debugModel = true;
    private boolean oneWorkThread = true;
    private boolean mainThreadOnly = false;

    public boolean isTurnEnable() {
        return turnEnable;
    }

    public void setTurnEnable(boolean turnEnable) {
        this.turnEnable = turnEnable;
    }

    public boolean isIceEnable() {
        return iceEnable;
    }

    public void setIceEnable(boolean iceEnable) {
        this.iceEnable = iceEnable;
    }

    public boolean isStunEnable() {
        return stunEnable;
    }

    public void setStunEnable(boolean stunEnable) {
        this.stunEnable = stunEnable;
    }

    public boolean isDebugModel() {
        return debugModel;
    }

    public void setDebugModel(boolean debugModel) {
        this.debugModel = debugModel;
    }

	public boolean isOneWorkThread() {
		return oneWorkThread;
	}

	public void setOneWorkThread(boolean oneWorkThread) {
		this.oneWorkThread = oneWorkThread;
	}

	public boolean isMainThreadOnly() {
		return mainThreadOnly;
	}

	public void setMainThreadOnly(boolean mainThreadOnly) {
		this.mainThreadOnly = mainThreadOnly;
	}
}
