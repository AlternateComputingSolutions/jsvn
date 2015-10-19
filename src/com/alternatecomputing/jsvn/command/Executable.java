package com.alternatecomputing.jsvn.command;

import java.util.Map;

public interface Executable {

	/**
	 * executes a given command with the given arguments
	 * @param command subversion command to run
	 * @param args arguments to configure the behavior of the given command
	 * @throws CommandException
	 */
	void executeCommand(Command command, Map args) throws CommandException;
}
