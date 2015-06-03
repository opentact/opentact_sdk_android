package com.yht.opentact.sip;

/**
 * sip常量类
 * 
 * @author weichao.yht
 *
 */
public class SipConstants {

	/**
	 * stun服务器地址
	 */
	public static final String STUN_SERVER_URI = "108.165.2.111:3478";
	/**
	 * 传输端口
	 */
	public static final int TRANSPORT_PORT = 5060;
	/**
	 * turn服务器地址
	 */
	public static final String TURN_SERVER = "108.165.2.111:3478";
	/**
	 * turn服务器密码
	 */
	public static final String TURN_PASSWORD = "opentact@123456";
	/**
	 * TURN服务器用户名
	 */
	public static final String TURN_USERNAME = "opentact";
	/**
	 * SIP服务器地址
	 */
	public static final String SIP_SERVER_URI = "108.165.2.111:5060";
	/**
	 * SIP服务器realm类型
	 */
	public static final String SIP_SERVER_REALM = "*";
	/**
	 * sip服务器主题
	 */
	public static final String SIP_SERVER_SCHEME = "digest";
	/**
	 * sip服务器数据格式
	 */
	public static final int SIP_SERVER_DATATYEP = 0;
	/**
	 * error msg
	 */
	public static final String ON_SIP_CALLBACK_MSG_INCOMINGCALL_ERROR = "there has a incoming call error";

	/**
	 * 编码ID列表
	 * 
	 * @author weichao.yht
	 *
	 */
	public class CODEC_ID {
		public final static String SPEEX_16000 = "speex/16000/1";
		public final static String SPEEX_8000 = "speex/8000/1";
		public final static String SPEEX_32000 = "speex/32000/1";
		public final static String GSM_8000 = "GSM/8000/1";
		public final static String PCMU_8000 = "PCMU/8000/1";
		public final static String PCMA_8000 = "PCMA/8000/1";
		public final static String G722_16000 = "G722/16000/1";
		public final static String G729_8000 = "G729/8000/1";
		public final static String ILBC_8000 = "iLBC/8000/1";

	}

	/**
	 * 通话状态代码
	 * 
	 * NULL Before INVITE is sent or received
	 * 
	 * CALLING After INVITE is sent
	 * 
	 * INCOMING After INVITE is received.
	 * 
	 * EARLY After response with To tag.
	 * 
	 * CONNECTING After 2xx is sent/received.
	 * 
	 * CONFIRMED After ACK is sent/received.
	 * 
	 * DISCONNECTED Session is terminated.
	 * 
	 * @author weichao.yht
	 *
	 */
	public enum CALL_STATE {
		NULL, CALLING, INCOMING, EARLY, CONNECTING, CONFIRMED, DISCONNECTED
	}

	/**
	 * 通过sip用户名组合sip请求url
	 * 
	 * @param username
	 *            sip用户名
	 * @return
	 */
	public static String buildSipIdUri(String username) {
		StringBuffer idUri = new StringBuffer();
		idUri.append("sip:").append(username).append("@").append(SIP_SERVER_URI);
		return idUri.toString().trim();
	}

}
