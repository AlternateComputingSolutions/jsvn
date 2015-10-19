package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	merge: apply the differences between two paths to a working copy path.
 *	usage:  svn merge PATH1[@N] PATH2[@M] [WCPATH]
 *	    or  svn merge -rN:M PATH [WCPATH]
 *
 *	  * PATH1 and PATH2 are either working-copy paths or URLs, specified at
 *	    revisions N and M.  These are the two sources to be compared.
 *	    N and M default to HEAD if omitted.
 *
 *	  * WCPATH is the working-copy path that will receive the changes.
 *	    If omitted, a default value of '.' is assumed.
 *
 *
 *	Valid options:
 *	  -r [--revision] arg      : specify revision number ARG (or X:Y range)
 *	  -D [--date] arg          : specify a date ARG (instead of a revision)
 *	  -N [--nonrecursive]      : operate on single directory only
 *	  --force                  : force operation to run
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 */
public class Merge extends Command {
	protected boolean _mustAuthenticate = true;

	public void init(Map args) throws CommandException {
	}
}
