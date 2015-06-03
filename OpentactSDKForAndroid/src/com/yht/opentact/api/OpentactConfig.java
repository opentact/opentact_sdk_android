package com.yht.opentact.api;

import android.content.Context;

import com.yht.opentact.im.IMConfig;
import com.yht.opentact.sip.SipConfig;

/**
 * OpentactSDK全局配置信息.这是一个单例类.
 * 
 * @author weichao.yht
 *
 */
public class OpentactConfig {
	/**
	 * OpentactConfig实例
	 */
	private static OpentactConfig instance = null;
	/**
	 * IMConfig实例
	 */
	private IMConfig imConfig = new IMConfig();
	/**
	 * SipConfig实例
	 */
	private SipConfig sipConfig = new SipConfig();
	/**
	 * 是否开启sip服务
	 */
	private boolean enableSip = true;
	/**
	 * 是否开启IM服务
	 */
	private boolean enableIm = true;
	/**
	 * 开发者账户sid
	 */
	private String sid = null;
	/**
	 * 开发者账户authtoken
	 */
	private String authtoken = null;
	/**
	 * 开发者账户ssid
	 */
	private String ssid = null;
	/**
	 * android系统上下文
	 */
	private Context appContext = null;

	private OpentactConfig() {

	}

	/**
	 * 获取OpentactConfig实例
	 * 
	 * @return opentacConfig实例
	 * @see OpentactConfig#instance
	 */
	public static OpentactConfig getInstance() {
		if (instance == null) {
			instance = new OpentactConfig();
		}
		return instance;
	}

	/**
	 * sip服务是否开启
	 * 
	 * @return 如果返回true,则sip服务开启.反之亦然
	 */
	public boolean isEnableSip() {
		return enableSip;
	}

	/**
	 * 设置是否开启sip服务
	 * 
	 * @param enableSip
	 *            --如果为true,则开启sip服务,反之亦然.
	 */
	public void setEnableSip(boolean enableSip) {
		this.enableSip = enableSip;
	}

	/**
	 * IM服务是否开启
	 * 
	 * @return 如果返回true,则IM服务开启.反之亦然
	 */
	public boolean isEnableIm() {
		return enableIm;
	}

	/**
	 * 设置是否开启IM服务
	 * 
	 * @param enableIm
	 *            --如果为true,则开启IM服务,反之亦然.
	 */
	public void setEnableIm(boolean enableIm) {
		this.enableIm = enableIm;
	}

	/**
	 * 获取IMConfig实例
	 * 
	 * @return 返回IMconfig实例
	 * 
	 * @see IMConfig
	 */
	public IMConfig getImConfig() {
		return imConfig;
	}

	/**
	 * 设置IMConfig实例
	 * 
	 * @param imConfig
	 *            --IMConfig实例
	 */
	public void setImConfig(IMConfig imConfig) {
		this.imConfig = imConfig;
	}

	/**
	 * 获取SipConfig实例
	 * 
	 * @return 返回SipConfig实例
	 * 
	 * @see SipConfig
	 */
	public SipConfig getSipConfig() {
		return sipConfig;
	}

	/**
	 * 设置SipConfig实例
	 * 
	 * @param sipConfig
	 */
	public void setSipConfig(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	/**
	 * 获取开发者账户sid
	 * 
	 * @return sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * 设置开发者账户sid
	 * 
	 * @param --sid
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * 设置开发者账户验证authtoken
	 * 
	 * @return authtoken
	 */
	public String getAuthtoken() {
		return authtoken;
	}

	/**
	 * 设置开发者账户authtoken
	 * 
	 * @param --authtoken
	 */
	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}

	/**
	 * 获取设置的sid子账户
	 * 
	 * @return ssid
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * 设置一个sid子账户
	 * 
	 * @param --ssid
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	/**
	 * 获取设置的android系统上下文
	 * 
	 * @return android系统上下文
	 */
	public Context getAppContext() {
		return appContext;
	}

	/**
	 * 设置android系统上下文
	 * 
	 * @param --appContext
	 */
	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

}
