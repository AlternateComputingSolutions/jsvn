package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *  update (up): Bring changes from the repository into the working copy.
 *  usage: update [PATH [PATH ... ]]
 *
 *    If no revision given, bring working copy up-to-date with HEAD rev.
 *    Else synchronize working copy to revision given by -r.
 *
 *    For each updated item a line will start with a character reporting the
 *    action taken.  These characters have the following meaning:
 *
 *      A  Added
 *      D  Deleted
 *      U  Updated
 *      C  Conflict
 *      G  Merged
 *
 *    A character in the first column signifies an update to the actual file,
 *    while updates to the file's props are shown in the second column.
 *
 *  Valid options:
 *    -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                               {DATE}      date instead of revision number
 *                               "HEAD"      latest in repository
 *                               "BASE"      base revision of item's working copy
 *                               "COMMITTED" revision of item's last commit
 *                               "PREV"      revision before item's last commit
 *    -N [--non-recursive]     : operate on single directory only
 *    -q [--quiet]             : print as little as possible
 *    --username arg           : specify a username ARG
 *    --password arg           : specify a password ARG
 *    --no-auth-cache          : do not cache authentication tokens
 *    --non-interactive        : do no interactive prompting
 */
public class Update extends Command implements WorkingCopyModifiable {
	private static final String COMMAND = "svn update {0} {1} {2} " + Command.NON_INTERACTIVE_MODIFIER;   // revision, non-recursive, targets
	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String REVISION = "REVISION";
	public static final String TARGETS = "TARGETS";

	public void init(Map args) throws CommandException {
		super.init(args);

		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
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
		setCommand(MessageFormat.format(COMMAND, new String[]{revision, nonRecursiveOption, targets}));
	}
}
