package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.Commandable;

/**
 * JSVN user interface event abtracting a command's execution
 */
public class JSVNCommandEvent implements JSVNUIEvent {
	private Commandable _command;
	private String _error;
	private String _result;

	/**
	 * returns the command related to this event
	 * @return command
	 */
	public Commandable getCommand() {
		return _command;
	}

	/**
	 * sets the command that this event is associated with
	 * @param command originating command
	 */
	public void setCommand(Commandable command) {
		_command = command;
	}

	/**
	 * returns an error message associated with the command
	 * @return error message
	 */
	public String getError() {
		return _error;
	}

	/**
	 * sets the error message associated with the command
	 * @param error error
	 */
	public void setError(String error) {
		_error = error;
	}

	/**
	 * gets the resulting output of the command
	 * @return command output
	 */
	public String getResult() {
		return _result;
	}

	/**
	 * sets the command output
	 * @param result command output
	 */
	public void setResult(String result) {
		_result = result;
	}
}
