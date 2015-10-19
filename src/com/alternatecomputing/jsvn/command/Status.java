package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	status (stat, st): Print the status of working copy files and directories.
 *	usage: status [PATH [PATH ... ]]
 *
 *	  With no args, print only locally modified items (no network access).
 *	  With -u, add working revision and server out-of-date information.
 *	  With -v, print full revision information on every item.
 *
 *	The first five columns in the output are each one character wide:
 *	    First column: Says if item was added, deleted, or otherwise changed
 *	      ' ' no modifications
 *	      'A' Added
 *	      'D' Deleted
 *	      'M' Modified
 *	      'C' Conflicted
 *	      '?' item is not under revision control
 *	      '!' item is missing and was removed via a non-svn command
 *	      '~' versioned as a directory, but is a file, or vice versa
 *	    Second column: Modifications of a file's or directory's properties
 *	      ' ' no modifications
 *	      'M' Modified
 *	      'C' Conflicted
 *	    Third column: Whether the working copy directory is locked
 *	      ' ' not locked
 *	      'L' locked
 *	    Fourth column: Scheduled commit will contain addition-with-history
 *	      ' ' no history scheduled with commit
 *	      '+' history scheduled with commit
 *	    Fifth column: Whether the item is switched relative to its parent
 *	      ' ' normal
 *	      'S' switched
 *	    The out-of-date information appears in the eighth column
 *	      '*' a newer revision exists on the server
 *	      ' ' the working copy is up to date
 *
 *	Remaining fields are variable width and delimited by spaces:
 *
 *	The working revision is the next field if -u or -v is given, followed
 *	by both the last committed revision and last committed author if -v is
 *	given.  The working copy path is always the final field, so it can
 *	include spaces.
 *
 *	  Example output:
 *	    svn status wc
 *	     M     wc/bar.c
 *	    A  +   wc/qax.c
 *
 *	    svn status -u wc
 *	     M           965    wc/bar.c
 *	           *     965    wc/foo.c
 *	    A  +         965    wc/qax.c
 *	    Head revision:   981
 *
 *	    svn status --show-updates --verbose wc
 *	     M           965       938     kfogel   wc/bar.c
 *	           *     965       922    sussman   wc/foo.c
 *	    A  +         965       687        joe   wc/qax.c
 *	                 965       687        joe   wc/zig.c
 *	    Head revision:   981
 *
 *	Valid options:
 *	  -u [--show-updates]      : display update information
 *	  -v [--verbose]           : print extra information
 *	  -N [--non-recursive]     : operate on single directory only
 *	  -q [--quiet]             : print as little as possible
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 *	  --no-auth-cache          : do not cache authentication tokens
 *	  --non-interactive        : do no interactive prompting
 *	  --no-ignore              : disregard default and svn:ignore property ignores
 */
public class Status extends Command implements WorkingCopyModifiable {
	private static final String COMMAND = "svn status {0} {1} {2} {3} {4} " + Command.NON_INTERACTIVE_MODIFIER;
	public static final String SHOW_UPDATES = "SHOW_UPDATES";
	public static final String TARGETS = "TARGETS";
	public static final String VERBOSE = "VERBOSE";
	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String QUIET = "QUIET";

	public void init(Map args) throws CommandException {
		super.init(args);

		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
		}

		Boolean verbose = (Boolean) args.get(VERBOSE);
		String verboseOption;
		if (Boolean.TRUE == verbose) {
			verboseOption = "-v";
		} else {
			verboseOption = "";
		}

		Boolean nonRecursive = (Boolean) args.get(NONRECURSIVE);
		String nonRecursiveOption;
		if (Boolean.TRUE == nonRecursive) {
			nonRecursiveOption = "-N";
		} else {
			nonRecursiveOption = "";
		}

		Boolean quiet = (Boolean) args.get(QUIET);
		String quietOption;
		if (Boolean.TRUE == quiet) {
			quietOption = "-q";
		} else {
			quietOption = "";
		}

		Boolean showUpdates = (Boolean) args.get(SHOW_UPDATES);
		String showUpdatesOption;
		if (Boolean.TRUE == showUpdates) {
			showUpdatesOption = "-u";
		} else {
			showUpdatesOption = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{showUpdatesOption, verboseOption, nonRecursiveOption, quietOption, targets}));
	}

}
