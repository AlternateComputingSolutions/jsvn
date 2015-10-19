package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	proplist (plist, pl): List all properties attached to files or directories.
 *	usage: proplist [TARGETS]
 *
 *	Valid options:
 *	  -v [--verbose]           : print extra information
 *	  -R [--recursive]         : descend recursively
 */
public class PropList extends Command {

	public void init(Map args) throws CommandException {
		super.init(args);

	}
}
