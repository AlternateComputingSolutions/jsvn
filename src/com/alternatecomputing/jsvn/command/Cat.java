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
	private static final String COMMAND = "svn cat {0} {1}";   // revision, targets
	public static final String REVISION = "REVISION";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		_mustAuthenticate = false;

		String targets = (String) args.get(TARGETS);
		if (targets == null) {
			throw new CommandException("Missing targets");
		}

		String revision = (String) args.get(REVISION);
		if (revision != null) {
			revision = "-r " + revision;
		} else {
			revision = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{revision, targets}));
	}
}
