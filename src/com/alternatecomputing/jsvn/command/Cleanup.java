package com.alternatecomputing.jsvn.command;

import java.util.Map;
import java.text.MessageFormat;

/**
 * cleanup: Recursively clean up the working copy, removing locks, resuming
 * unfinished operations, etc.
 * usage: cleanup [PATH [PATH ... ]]
 */
public class Cleanup extends Command {
	private static final String COMMAND = "svn cleanup {0}";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{targets}));
	}
}
