package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 * add: Put files and directories under revision control, scheduling
 * them for addition to repository.  They will be added in next commit.
 * usage: add PATH [PATH [PATH ... ]]
 *
 * Valid options:
 *   --targets arg            : pass contents of file "ARG" as additional args
 *   -R [--recursive]         : descend recursively
 *   -q [--quiet]             : print as little as possible
 */
public class Add extends Command {
	private static final String COMMAND = "svn add {0}";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		String targets = (String) args.get(TARGETS);
		if (targets == null) {
			throw new CommandException("Missing targets");
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{targets}));
	}
}
