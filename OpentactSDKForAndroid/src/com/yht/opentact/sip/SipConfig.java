package com.yht.opentact.sip;

/**
 * sip配置类
 * @author weichao.yht
 *
 */
public class SipConfig {

	/**
	 * 是否启用turn功能,默认开启
	 */
	private boolean turnEnable = true;
	/**
	 * 是否启用ice功能,默认开启
	 */
    private boolean iceEnable = true;
    /**
     * 是否开启stun,默认开启
     */
    private boolean stunEnable = true;
    /**
     * 是否开启debug模式,默认开启
     */
    private boolean debugModel = true;
    /**
     * 是否开启单一工作线程模式,默认开启
     */
    private boolean oneWorkThread = true;
    /**
     * 是否其中主线程模式,默认关闭.
     */
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
