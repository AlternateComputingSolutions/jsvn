package com.alternatecomputing.jsvn.command;

import java.util.Map;
import java.text.MessageFormat;

/**
 * cleanup: Recursively clean up the working copy, removing locks, resuming
 * unfinished operations, etc.
 * usage: cleanup [PATH [PATH ... ]]
 */
public class Cleanup extends Command implements WorkingCopyModifiable {
	private static final String COMMAND = "{0} cleanup {1}";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		super.init(args);

		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, getExecutablePath(), targets));
	}
}
