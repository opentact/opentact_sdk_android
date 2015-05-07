package com.yht.opentactsmple;

import com.yht.opentact.im.callback.ACTION;
import com.yht.opentact.im.callback.IMCallback;

public class MyIMCallback extends IMCallback {

	public MyIMCallback() {
		super();
	}

	/**
	 * 
	 * @param action
	 *            {@link ACTION} IM Action Enum.
	 */
	public MyIMCallback(ACTION action) {
		super(action);
	}

	/**
	 * When a action is already comleted, this callback will be called.
	 * @param action
	 *            {@link ACTION} IM Action Enum. What action is already completed.
	 * @param isSuccess
	 *            if the action is success, return true, or else return false.
	 * @param throwable
	 *            the Throwable
	 */
	@Override
	public void onActionCallback(ACTION action, boolean isSuccess, Throwable throwable) {
		// TODO something you want to do
		System.out.println("onActionCallback " + action.toString());
	}

	/**
	 * 	When a message arrived, this callback will be called.
	 * 	@param ssid
	 * 				the sender's ssid
	 * 	@param msg
	 * 				arrive message
	 * 	@param Qos
	 * 				what Qos at this transport
	 */
	@Override
	public void messageArrivedCallback(String ssid, String msg, int Qos) {
		// TODO something you want to do

	}

	/**
	 * When the current IM connect lost, this callback will be called.
	 * @param isLost
	 * 		if connect lost, return true.
	 * @param throwable
	 */
	@Override
	public void connectLostCallback(boolean isLost, Throwable throwable) {
		// TODO something you want to do

	}

}
