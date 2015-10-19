package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	delete (del, remove, rm): Remove files and directories from version control.
 *	usage: svn delete [TARGET | URL]
 *
 *	  * If run on a working-copy TARGET, item is scheduled for deletion
 *	    upon the next commit. Files, and directories that have not been
 *	    committed, are immediately removed from the working copy. The
 *	    command will not remove TARGETs that are, or contain, unversioned
 *	    or modified items; use the --force option to override this
 *	    behaviour.
 *	  * If run on an URL, item is deleted from the repository via an
 *	    immediate commit.
 *
 *	Valid options:
 *	  --force                  : force operation to run
 *	  -m [--message] arg       : specify commit message "ARG"
 *	  -F [--file] arg          : read data from file ARG
 *	  --targets arg            : pass contents of file "ARG" as additional args
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 *	  --message-encoding arg   : take log message in charset encoding ARG
 */
public class Delete extends Command {
	private static final String COMMAND = "svn delete --message {0} {1}";
	public static final String COMMIT_MESSAGE = "COMMIT_MESSAGE";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		_mustAuthenticate = true;

		String targets = (String) args.get(TARGETS);
		if (targets == null) {
			throw new CommandException("Missing targets");
		}

		String commitMessage = (String) args.get(COMMIT_MESSAGE);
		if (commitMessage == null) {
			throw new CommandException("Missing commit message");
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{"'" + commitMessage + "'", targets}));
	}
}
