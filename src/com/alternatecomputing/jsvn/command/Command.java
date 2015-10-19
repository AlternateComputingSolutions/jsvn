package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

import java.util.Map;

/**
 * This class is the basis for all implemented subversion commands.  It is responsible for authenticating with the
 * subversion server (if necessary) and executing a configured command.
 */
public abstract class Command {
	private static final String MESSAGE_UNINITIALIZED = "Cannot execute an uninitialized command.";
	private String _command;
	private String _result;
	protected boolean _mustAuthenticate = false;

	abstract public void init(Map args) throws CommandException;

	public void execute() throws CommandException {
		if (_command == null) {
			throw new CommandException(MESSAGE_UNINITIALIZED);
		}


		// run the command
		CommandRunner exec = new CommandRunner();
		// do we need to append credentials to authenticate with the server?  If so, pass them in put do not set them
		// _command since we don't want credentials being displayed in the command history
		if (_mustAuthenticate) {
			exec.runCommand(_command + " " + setAuthenticationCredentials());
		} else {
			exec.runCommand(_command);
		}
		if (exec.getError().length() > 0) {
			throw new CommandException(exec.getError());
		}

		// save the result
		_result = exec.getOuput();
	}

	/**
	 * returns the configured subversion command
	 * @return subversion command
	 */
	public String getCommand() {
		return _command;
	}

	/**
	 * Sets the subversion command.  Any required authentication credentials should not be added to the command line.
	 * Instead, the attribute _mustAuthenticate should be set to true.
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
	 * Returns a string to be appended to the subversion command to allow authentication with the server.  This string
	 * will also tell the svn client not to cache authentication credentials and to not prompt for anything.
	 * @return authentication credential parameters
	 */
	public String setAuthenticationCredentials() throws CommandException {
		String username = ConfigurationManager.getInstance().getUsername();
		if (username == null) {
			throw new CommandException("Must specify a username");
		}
		String password = ConfigurationManager.getInstance().getPassword();
		if (password == null) {
			throw new CommandException("Must specify a password");
		}
		return "--username " + username + " --password " + password + " --no-auth-cache --non-interactive";

	}

}
