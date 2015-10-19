package com.alternatecomputing.jsvn.gui;

/**
 * JSVN user interface event abstracting a general change in status
 */
public class JSVNStatusEvent implements JSVNUIEvent {
	public static final int READY = 0;
	public static final int EXECUTING = 1;
	public static final int REFRESHING = 2;
	private int _status;

	/**
	 * constructor
	 * @param status status
	 */
	public JSVNStatusEvent(int status) {
		_status = status;
	}

	/**
	 * returns the new status
	 * @return status
	 */
	public int getStatus() {
		return _status;
	}

}
