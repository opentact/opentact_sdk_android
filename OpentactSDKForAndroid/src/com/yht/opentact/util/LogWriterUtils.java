package com.yht.opentact.util;

import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;

import android.util.Log;

public class LogWriterUtils extends LogWriter{

	@Override
	public void write(LogEntry entry) {
		Log.d("opentact log", entry.getMsg());
		printLog(entry.getMsg());
	}

	public void printLog(String logMsg){
		
	}
}