package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	revert: Restore pristine working copy file (undo all local edits).
 *	usage: revert TARGET1 [TARGET2 [TARGET3 ... ]]
 *
 *	    Note:  this routine does not require network access, and
 *	    resolves any conflicted states.
 *
 *	Valid options:
 *	  --targets arg            : pass contents of file "ARG" as additional args
 *	  -R [--recursive]         : descend recursively
 *    -q [--quiet]             : print as little as possible
 */
public class Revert extends Command {
	private static final String COMMAND = "svn revert {0}";
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
