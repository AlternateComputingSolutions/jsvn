package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

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
public class Add extends Command implements WorkingCopyModifiable {
	private static final String COMMAND = "{0} add {1}";
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
