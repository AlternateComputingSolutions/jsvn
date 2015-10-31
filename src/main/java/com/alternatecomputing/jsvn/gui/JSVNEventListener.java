package com.alternatecomputing.jsvn.gui;

import java.util.EventListener;

/**
 * interface for classes implementing JSVN event processing
 */
public interface JSVNEventListener extends EventListener {

	/**
	 * handles a JSVN user interface event
	 * @param event UI event to handle
	 */
	public void processJSVNEvent(JSVNUIEvent event);
}
