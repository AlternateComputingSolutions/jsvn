package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	checkout (co): Check out a working copy from a repository.
 *	usage: svn checkout REPOS_URL [DESTINATION]
 *	  Note: If DESTINATION is omitted, the basename of the REPOS_URL will
 *	  be used as the destination.
 *
 *	Valid options:
 *	  -r [--revision] arg      : specify revision number ARG (or X:Y range)
 *	  -D [--date] arg          : specify a date ARG (instead of a revision)
 *	  -q [--quiet]             : print as little as possible
 *	  -N [--nonrecursive]      : operate on single directory only
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 *	  --xml-file arg           : read/write xml to specified file ARG
 */
public class Checkout extends Command {
	private static final String COMMAND = "svn checkout -r {0} {1} {2} {3}";
	public static final String DESTINATION = "DESTINATION";
	public static final String REVISION = "REVISION";
	public static final String REPOS_URL = "REPOS_URL";
	public static final String NONRECURSIVE = "NONRECURSIVE";

	public void init(Map args) throws CommandException {
		_mustAuthenticate = true;

		String url = (String) args.get(REPOS_URL);
		if (url == null) {
			throw new CommandException("Missing url");
		}

		String destination = (String) args.get(DESTINATION);
		if (destination == null) {
			throw new CommandException("Missing destination");
		}

		String revision = (String) args.get(REVISION);
		if (revision == null) {
			revision = "HEAD";
		}

		Boolean nonRecursive = (Boolean) args.get(NONRECURSIVE);
		String nonRecursiveOption;
		if (Boolean.TRUE == nonRecursive) {
			nonRecursiveOption = "-N";
		} else {
			nonRecursiveOption = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{revision, url, destination, nonRecursiveOption}));
	}
}
