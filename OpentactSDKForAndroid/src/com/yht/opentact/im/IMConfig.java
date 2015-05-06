package com.yht.opentact.im;


public class IMConfig {
	
	    private int keepAliveInterval = 20;
	    private boolean isCleanSession = false;
	    private int connectionTimeout = 10;
	    private int qos = 1;
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
