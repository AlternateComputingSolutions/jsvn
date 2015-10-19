package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	propdel (pdel): Remove property PROPNAME on files and directories.
 *	usage: propdel PROPNAME [TARGETS]
 *
 *	Valid options:
 *	  -q [--quiet]             : print as little as possible
 *	  -R [--recursive]         : descend recursively
 */
public class PropDel extends Command {

	public void init(Map args) throws CommandException {
	}
}
