package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	switch (sw): Update working copy to mirror a new URL
 *	usage: switch REPOS_URL [TARGET]
 *
 *	   Note:  this is the way to move a working copy to a new branch.
 *
 *	Valid options:
 *	  -r [--revision] arg      : specify revision number ARG (or X:Y range)
 *	  -D [--date] arg          : specify a date ARG (instead of a revision)
 *	  -N [--nonrecursive]      : operate on single directory only
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 */
public class Switch extends Command {
	protected boolean _mustAuthenticate = true;

	public void init(Map args) throws CommandException {
	}
}
