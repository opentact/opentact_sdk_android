package com.yht.opentactsmple;

import org.pjsip.pjsua2.LogEntry;

import android.util.Log;

import com.yht.opentact.util.LogWriterUtils;

public class LogUtils extends LogWriterUtils {

	@Override
	public void printLog(String logMsg) {
		Log.d("Opentact Log", logMsg);
	}

}
