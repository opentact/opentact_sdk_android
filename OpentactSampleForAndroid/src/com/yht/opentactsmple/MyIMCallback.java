package com.yht.opentactsmple;

import com.yht.opentact.im.callback.ACTION;
import com.yht.opentact.im.callback.IMCallback;

public class MyIMCallback extends IMCallback{

	public MyIMCallback(){
		super();
	}

	public MyIMCallback(ACTION action) {
		super(action);
	}
	
	@Override
	public void onActionCallback(ACTION action, boolean isSuccess, Throwable throwable) {
		System.out.println("onActionCallback " + action.toString());
	}
	
}
