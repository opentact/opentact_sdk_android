package com.yht.opentact.sip;

public class SipConstants {

	public static final String STUN_SERVER_URI = "108.165.2.111:3478";
    public static final int TRANSPORT_PORT = 5060;
    public static final String TURN_SERVER = "108.165.2.111:3478";
    public static final String TURN_PASSWORD = "opentact@123456";
    public static final String TURN_USERNAME = "opentact";
    public static final String SIP_SERVER_URI = "108.165.2.111:5060";
    public static final String SIP_SERVER_REALM = "*";
    public static final String SIP_SERVER_SCHEME = "digest";
    public static final int SIP_SERVER_DATATYEP = 0;
    
    public static final String ON_SIP_CALLBACK_MSG_INCOMINGCALL_ERROR = "there has a incoming call error";
    
    public class CODEC_ID{
    	public final static String SPEEX_16000 = "speex/16000/1"; 
    	public final static String SPEEX_8000 = "speex/8000/1";
    	public final static String SPEEX_32000 = "speex/32000/1";
    	public final static String GSM_8000 = "GSM/8000/1";
    	public final static String PCMU_8000 ="PCMU/8000/1";
    	public final static String PCMA_8000 = "PCMA/8000/1";
    	public final static String G722_16000 = "G722/16000/1";
    	public final static String G729_8000 = "G729/8000/1";
    	public final static String ILBC_8000 = "iLBC/8000/1";
    	
    }

    public static String buildSipIdUri(String username){
        StringBuffer idUri = new StringBuffer();
        idUri.append("sip:").append(username).append("@").append(SIP_SERVER_URI);
        return idUri.toString().trim();
    }
    
}
