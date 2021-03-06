package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	propget (pget, pg): Print value of property PROPNAME on files or directories.
 *	usage: propget PROPNAME [TARGETS]
 *
 *	Valid options:
 *	  -R [--recursive]         : descend recursively
 */
public class PropGet extends Command {
	private static final String COMMAND = "{0} propget {1} {2}";

	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		super.init(args);

		Boolean nonRecursive = (Boolean) args.get(NONRECURSIVE);
		String nonRecursiveOption;
		if (Boolean.TRUE == nonRecursive) {
			nonRecursiveOption = "-N";
		} else {
			nonRecursiveOption = "";
		}

		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
		}

		setCommand(MessageFormat.format(COMMAND, getExecutablePath(), targets, nonRecursiveOption));
	}
}
