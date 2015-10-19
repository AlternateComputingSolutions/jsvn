package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Commandable;

import javax.swing.SwingUtilities;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is used to execute a JSVN command in a separate thread.  It will notify any registered listeners of any
 * events that may be of interest.
 */
public class JSVNCommandExecutor implements Runnable {
	private Commandable _command;
	private Map _args;
	private Collection _jsvnEventListeners;

	/**
	 * constructor
	 * @param command JSVN command to execute
	 * @param args arguments to initialize the command with
	 */
	public JSVNCommandExecutor(Commandable command, Map args) {
		_command = command;
		_args = args;
		_jsvnEventListeners = new HashSet();
	}

	/**
	 * adds the given listener to the set of listeners wanting to be notified of any command interesting events
	 * @param listener JSVN event listener
	 */
	public void addJSVNEventListener(JSVNEventListener listener) {
		_jsvnEventListeners.add(listener);
	}

	/**
	 * notifies all registered listeners that a given JSVN user interface event has occurred
	 * @param event
	 */
	private void notifyListerers(final JSVNUIEvent event) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (Iterator iterator = _jsvnEventListeners.iterator(); iterator.hasNext();) {
					JSVNEventListener jsvnEventListener = (JSVNEventListener) iterator.next();
                    // Shouldn't have any nulls, bit I just had some thrown so a check is always handy.
					if ( jsvnEventListener != null ) {
						jsvnEventListener.processJSVNEvent(event);
					}
				}
			}
		});
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see     Thread#run()
	 */
	public void run() {
		try {
			// executor status is executing
			notifyListerers(new JSVNStatusEvent(JSVNStatusEvent.EXECUTING));
			_command.init(_args);
			_command.execute();
			String result = _command.getResult();
			JSVNCommandEvent commandEvent = new JSVNCommandEvent();
			commandEvent.setCommand(_command);
			commandEvent.setResult(result);
			// command has successfully completed
			notifyListerers(commandEvent);
		} catch (CommandException e) {
			JSVNCommandEvent commandEvent = new JSVNCommandEvent();
			commandEvent.setError(e.getMessage());
			// there was an error
			notifyListerers(commandEvent);
		} finally {
			// executor status is ready
			notifyListerers(new JSVNStatusEvent(JSVNStatusEvent.READY));
		}
	}
}
