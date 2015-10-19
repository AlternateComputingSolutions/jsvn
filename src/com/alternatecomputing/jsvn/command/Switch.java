package com.alternatecomputing.jsvn.command;

import java.util.Map;
import java.text.MessageFormat;

/**
 * switch (sw): Update working copy to mirror a new URL
 * usage: switch URL [PATH]
 *
 *   Note:  this is the way to move a working copy to a new branch.
 *
 * Valid options:
 *   -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                              {DATE}      date instead of revision number
 *                              "HEAD"      latest in repository
 *                              "BASE"      base revision of item's working copy
 *                              "COMMITTED" revision of item's last commit
 *                              "PREV"      revision before item's last commit
 *   -N [--non-recursive]     : operate on single directory only
 *   -q [--quiet]             : print as little as possible
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 *   --no-auth-cache          : do not cache authentication tokens
 *   --non-interactive        : do no interactive prompting
 */
public class Switch extends Command {
	private static final String COMMAND = "svn switch {0} {1} {2} {3}";   // revision, non-recursive, url, path
	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String REVISION = "REVISION";
	public static final String URL = "URL";
	public static final String PATH = "PATH";

	public void init(Map args) throws CommandException {
		_mustAuthenticate = true;

		String url = (String) args.get(URL);
		if (url == null) {
			throw new CommandException("Missing URL");
		}

		String path = (String) args.get(PATH);
		if (path == null) {
			throw new CommandException("Missing path");
		}

		String revision = (String) args.get(REVISION);
		if (revision != null) {
			revision = "-r " + revision;
		} else {
			revision = "";
		}

		Boolean nonRecursive = (Boolean) args.get(NONRECURSIVE);
		String nonRecursiveOption;
		if (Boolean.TRUE == nonRecursive) {
			nonRecursiveOption = "-N";
		} else {
			nonRecursiveOption = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{revision, nonRecursiveOption, url, path}));
	}
}
