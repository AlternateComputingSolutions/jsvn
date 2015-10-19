package com.alternatecomputing.jsvn.command;

import java.util.Map;
import java.text.MessageFormat;

/**
 *	resolve: Remove 'conflicted' state on working copy files or directories.
 *	usage: resolve TARGET1 [TARGET2 [TARGET3 ... ]]
 *
 *	    Note:  this routine does not semantically resolve conflict markers;
 *	    it merely removes conflict-related artifact files and allows TARGET
 *	    to be committed again.
 *
 *	Valid options:
 *	  --targets arg            : pass contents of file "ARG" as additional args
 *	  -R [--recursive]         : descend recursively
 *    -q [--quiet]             : print as little as possible
 */
public class Resolve extends Command {
	private static final String COMMAND = "svn resolve {0}";
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
