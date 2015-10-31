package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

import java.util.Map;


/**
 * This class is the basis for all implemented subversion commands.  It is responsible for authenticating with the
 * subversion server (if necessary) and executing a configured command.
 */
public class Command implements Commandable {
	public static final String NON_INTERACTIVE_MODIFIER = " --non-interactive";
	private static final String MESSAGE_UNINITIALIZED = "Cannot execute an uninitialized command.";

	private String _command;
	private String _result;
	private Map _args;

	public void init(Map args) throws CommandException {
		_args = args;
	}

	public void execute() throws CommandException {
		if (_command == null) {
			throw new CommandException(MESSAGE_UNINITIALIZED);
		}

		// run the command
		CommandRunner exec = new CommandRunner();
		exec.runCommand(System.getProperty(Constants.USER_HOME), _command);
		if (exec.getError().length() > 0) {
			throw new CommandException(exec.getError());
		}

		// save the result
		_result = exec.getOutput();

	}

	/**
	 * Helper to get the executable from config
	 * @return
	 */
	protected String getExecutablePath() {
		return ConfigurationManager.getInstance().getConfig().getExecutablePath();
	}

	/**
	 * returns the configured subversion command
	 * @return subversion command
	 */
	public String getCommand() {
		return _command;
	}

	/**
	 * Sets the subversion command.
	 * @param command subversion command
	 */
	protected void setCommand(String command) {
		_command = command;
	}

	/**
	 * returns the output of a successfully executed  subversion command
	 * @return command output
	 */
	public String getResult() {
		return _result;
	}

	/**
	 *
	 * @return
	 */
	public Map getArgs() {
		return _args;
	}

}
