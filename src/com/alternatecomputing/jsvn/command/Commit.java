package com.alternatecomputing.jsvn.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * commit (ci): Send changes from your working copy to the repository.
 * usage: commit [PATH [PATH ... ]]
 *
 *   Be sure to use one of -m or -F to send a log message.
 *
 * Valid options:
 *   -m [--message] arg       : specify commit message "ARG"
 *   -F [--file] arg          : read data from file ARG
 *   -q [--quiet]             : print as little as possible
 *   -N [--non-recursive]     : operate on single directory only
 *   --targets arg            : pass contents of file "ARG" as additional args
 *   --force                  : force operation to run
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 *   --no-auth-cache          : do not cache authentication tokens
 *   --non-interactive        : do no interactive prompting
 *   --encoding arg           : treat value as being in charset encoding ARG
 */
public class Commit extends Command {
	private static final String COMMAND = "svn commit --file {0} {1}";
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

		// write commit message to tmp file
		File tempFile;
		try {
			tempFile = File.createTempFile("jsvn", ".tmp", null);
			// Delete temp file when program exits.
			tempFile.deleteOnExit();

			// Write to temp file
			BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
			out.write(commitMessage);
			out.close();

			// build the command
			setCommand(MessageFormat.format(COMMAND, new String[]{tempFile.getCanonicalPath(), targets}));
		} catch (IOException e) {
			throw new CommandException("Error while building commit command.  Original exception: " + e.getMessage());
		}
	}
}
