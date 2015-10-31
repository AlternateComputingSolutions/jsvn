package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *
 */
public interface Commandable {

	public void init(Map args) throws CommandException;

	public void execute() throws CommandException;

	/**
	 * returns the configured subversion command
	 * @return subversion command
	 */
	public String getCommand();

	/**
	 * returns the output of a successfully executed  subversion command
	 * @return command output
	 */
	public String getResult();

	/**
	 *
	 * @return
	 */
	public Map getArgs();
}
