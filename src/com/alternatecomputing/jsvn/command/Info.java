package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	info: Display info about a resource.
 *	usage: svn info [PATH1 [PATH2] ...]
 *
 *	    Print information about PATHs.
 *
 *	Valid options:
 *	  --targets arg            : pass contents of file "ARG" as additional args
 *	  -R [--recursive]         : descend recursively
 */
public class Info extends Command {
	private static final String COMMAND = "svn info {0}";
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
