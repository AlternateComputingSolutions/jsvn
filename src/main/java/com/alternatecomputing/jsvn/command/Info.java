package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

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
	private static final String COMMAND = "{0} info {1}";
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
