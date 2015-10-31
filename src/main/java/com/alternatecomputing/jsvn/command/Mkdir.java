package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	mkdir: Create a new directory under revision control.
 *	usage: mkdir [NEW_DIR | REPOS_URL].
 *
 *	    Either create NEW_DIR in working copy scheduled for addition,
 *	    or create REPOS_URL via immediate commit.
 *
 *	Valid options:
 *	  -m [--message] arg       : specify commit message "ARG"
 *	  -F [--file] arg          : read data from file ARG
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 *	  --message-encoding arg   : take log message in charset encoding ARG
 */
public class Mkdir extends Command implements WorkingCopyModifiable {

	public void init(Map args) throws CommandException {
		super.init(args);

	}
}
