package com.alternatecomputing.jsvn.command;

import java.util.Map;
import java.text.MessageFormat;

/**
 * merge: apply the differences between two URLs to a working copy path.
 * usage: 1. merge URL1[@N] URL2[@M] [PATH]
 *        2. merge -r N:M TARGET [PATH]
 *
 *   1. In the first form, URL1 and URL2 are URLs specified at revisions
 *      N and M.  These are the two sources to be compared.  The revisons
 *      default to HEAD if omitted.
 *
 *   2. In the second form TARGET can be an URL, or it can be a working copy
 *      path in which case the corresponding URL is used.  This URL, at
 *      revisions N and M, defines the two sources to be compared.
 *
 *   PATH is the working copy path that will receive the changes.
 *   If omitted, a default value of '.' is assumed.
 *
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
 *   --force                  : force operation to run
 *   --dry-run                : try operation but make no changes
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 *   --no-auth-cache          : do not cache authentication tokens
 *   --non-interactive        : do no interactive prompting
 */
public class Merge extends Command implements WorkingCopyModifiable {
	private static final String COMMAND = "svn merge {0} {1} {2} {3} {4} " + Command.NON_INTERACTIVE_MODIFIER;   // non-recursive, dry-run, source1, source2, target
	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String DRY_RUN = "DRY_RUN";
	public static final String SOURCE1 = "SOURCE1";
	public static final String SOURCE2 = "SOURCE2";
	public static final String TARGET = "TARGET";

	public void init(Map args) throws CommandException {
		super.init(args);

		String source1 = (String) args.get(SOURCE1);
		if (source1 == null || "".equals(source1.trim())) {
			throw new CommandException("Missing first source");
		}

		String source2 = (String) args.get(SOURCE2);
		if (source2 == null || "".equals(source2.trim())) {
			throw new CommandException("Missing second source");
		}

		String target = (String) args.get(TARGET);
		if (target == null || "".equals(target.trim())) {
			throw new CommandException("Missing target");
		}

		Boolean nonRecursive = (Boolean) args.get(NONRECURSIVE);
		String nonRecursiveOption;
		if (Boolean.TRUE == nonRecursive) {
			nonRecursiveOption = "-N";
		} else {
			nonRecursiveOption = "";
		}

		Boolean dryRun = (Boolean) args.get(DRY_RUN);
		String dryRunOption;
		if (Boolean.TRUE == dryRun) {
			dryRunOption = "--dry-run";
		} else {
			dryRunOption = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{nonRecursiveOption, dryRunOption, source1, source2, target}));
	}
}
