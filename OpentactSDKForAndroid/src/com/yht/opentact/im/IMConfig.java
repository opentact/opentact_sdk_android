package com.yht.opentact.im;

/**
 * IM的基本配置信息
 * @author weichao.yht
 *
 */
public class IMConfig {
	
		/**
		 * 心跳检查间距,默认20秒
		 */
	    private int keepAliveInterval = 20;
	    /**
	     * 是否清空回话,默认为false
	     */
	    private boolean isCleanSession = false;
	    /**
	     * 连接超时设置,默认为10秒
	     */
	    private int connectionTimeout = 10;
	    /**
	     * qos类型,默认为1
	     */
	    private int qos = 1;
	    /**
	     * 是否保留数据,默认为false
	     */
	    private boolean isRetained = false;
	    


	    public int getKeepAliveInterval() {
	        return keepAliveInterval;
	    }

	    public void setKeepAliveInterval(int keepAliveInterval) {
	        this.keepAliveInterval = keepAliveInterval;
	    }

	    public boolean isCleanSession() {
	        return isCleanSession;
	    }

	    public void setCleanSession(boolean isCleanSession) {
	        this.isCleanSession = isCleanSession;
	    }

	    public int getConnectionTimeout() {
	        return connectionTimeout;
	    }

	    public void setConnectionTimeout(int connectionTimeout) {
	        this.connectionTimeout = connectionTimeout;
	    }

	    public int getQos() {
	        return qos;
	    }

	    public void setQos(int qos) {
	    	if ((qos < 0) || (qos > 2)) {
				this.qos = 1;
			}
	        else{
	            this.qos = qos;
	        }
	    }

	    public boolean isRetained() {
	        return isRetained;
	    }

	    public void setRetained(boolean isRetained) {
	        this.isRetained = isRetained;
	    }

}
