package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 * checkout (co): Check out a working copy from a repository.
 * usage: checkout URL [URL [URL ... ]] [PATH]
 *   Note: If PATH is omitted, the basename of the URL will be used as
 *   the destination. If multiple URLs are given each will be checked
 *   out into a sub-directory of PATH, with the name of the sub-directory
 *   being the basename of the URL.
 *
 * Valid options:
 *   -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                              {DATE}      date instead of revision number
 *                              "HEAD"      latest in repository
 *                              "BASE"      base revision of item's working copy
 *                              "COMMITTED" revision of item's last commit
 *                              "PREV"      revision before item's last commit
 *   -q [--quiet]             : print as little as possible
 *   -N [--non-recursive]     : operate on single directory only
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 *   --no-auth-cache          : do not cache authentication tokens
 *   --non-interactive        : do no interactive prompting
 */
public class Checkout extends Command {
	private static final String COMMAND = "svn checkout {0} {1} {2} {3} {4} {5} " + Command.NON_INTERACTIVE_MODIFIER;
	public static final String DESTINATION = "DESTINATION";
	public static final String REVISION = "REVISION";
	public static final String REPOS_URL = "REPOS_URL";
	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";

	public void init(Map args) throws CommandException {
		String url = (String) args.get(REPOS_URL);
		if (url == null || "".equals(url.trim())) {
			throw new CommandException("Missing url");
		}

		String destination = (String) args.get(DESTINATION);
		if (destination == null || "".equals(destination.trim())) {
			throw new CommandException("Missing destination");
		}

		String username = (String) args.get(USERNAME);
		if (username == null || "".equals(username.trim())) {
			username = "";
		} else {
			username = "--username " + username;
		}

		String password = (String) args.get(PASSWORD);
		if (password == null || "".equals(password.trim())) {
			password = "";
		} else {
			password = "--password " + password;
		}

		String revision = (String) args.get(REVISION);
		if (revision != null && !"".equals(revision.trim())) {
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
		setCommand(MessageFormat.format(COMMAND, new String[]{revision, url, destination, nonRecursiveOption, username, password}));
	}
}
