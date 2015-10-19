package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	cleanup: Recursively clean up the working copy, removing locks, resuming
 *	unfinished operations, etc.
 *	usage: svn cleanup [TARGETS]
 */
public class Cleanup extends Command {

	public void init(Map args) throws CommandException {
	}
}
