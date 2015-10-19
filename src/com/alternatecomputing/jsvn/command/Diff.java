package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *  diff (di): display the differences between two paths.
 *  usage: 1. diff [-r N[:M]] [TARGET [TARGET ... ]]
 *         2. diff URL1[@N] URL2[@M]
 *
 *    1. Each TARGET can be either a working copy path or URL.  If no
 *       TARGET is specified, a value of '.' is assumed.
 *
 *       If TARGET is a URL, then revs N and M must be given via -r.
 *
 *       If TARGET is a working copy path, then -r switch means:
 *         -r N:M  : server compares TARGET@N and TARGET@M,
 *         -r N    : client compares TARGET@N against working copy
 *         (no -r) : client compares base and working copies of TARGET
 *
 *    2. If the alternate syntax is used, the server compares URL1 and URL2
 *       at revisions N and M respectively.  If either N or M are ommitted,
 *       a value of HEAD is assumed.
 *
 *    Valid options:
 *    -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                               {DATE}      date instead of revision number
 *                               "HEAD"      latest in repository
 *                               "BASE"      base revision of item's working copy
 *                               "COMMITTED" revision of item's last commit
 *                               "PREV"      revision before item's last commit
 *    -x [--extensions] arg    : pass "ARG" as bundled options to GNU diff
 *    -N [--non-recursive]     : operate on single directory only
 *    --username arg           : specify a username ARG
 *    --password arg           : specify a password ARG
 *    --no-auth-cache          : do not cache authentication tokens
 *    --non-interactive        : do no interactive prompting

 */
public class Diff extends Command {
	private static final String COMMAND = "svn diff {0} {1} {2} {3} " + Command.NON_INTERACTIVE_MODIFIER;   // revision, extensions, non-recursive, targets
	public static final String EXTENSIONS = "EXTENSIONS";
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

		String extensions = (String) args.get(EXTENSIONS);
		if (extensions != null && !"".equals(extensions.trim())) {
			extensions = "--diff-cmd diff -x " + extensions;
		} else {
			extensions = "";
		}

		Boolean nonRecursive = (Boolean) args.get(NONRECURSIVE);
		String nonRecursiveOption;
		if (Boolean.TRUE == nonRecursive) {
			nonRecursiveOption = "-N";
		} else {
			nonRecursiveOption = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{revision, extensions, nonRecursiveOption, targets}));
	}
}
