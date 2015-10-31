package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 * cat: Output the content of specified files or URLs.
 * usage: cat TARGET [TARGET [TARGET ... ]]
 *
 * Valid options:
 *   -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                              {DATE}      date instead of revision number
 *                              "HEAD"      latest in repository
 *                              "BASE"      base revision of item's working copy
 *                              "COMMITTED" revision of item's last commit
 *                              "PREV"      revision before item's last commit
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 */
public class Cat extends Command {
	private static final String COMMAND = "{0} cat {1} {2}";   // revision, targets
	public static final String REVISION = "REVISION";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		super.init(args);

		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
		}

		String revision = (String) args.get(REVISION);
		if (revision != null  && !"".equals(revision)) {
			revision = "-r " + revision;
		} else {
			revision = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, getExecutablePath(), revision, targets));
	}
}
