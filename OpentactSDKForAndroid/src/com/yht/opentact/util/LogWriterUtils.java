package com.yht.opentact.util;

import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;

import com.yht.opentact.debug.OLog;

public class LogWriterUtils extends LogWriter{

	@Override
	public void write(LogEntry entry) {
		OLog.d("opentact log", entry.getMsg());
		printLog(entry.getMsg());
	}

	public void printLog(String logMsg){
		
	}
}