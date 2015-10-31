package com.alternatecomputing.jsvn.command;

import java.util.Map;
import java.text.MessageFormat;

/**
 * move (mv, rename, ren): Move/rename something in working copy or repository.
 * usage: move SRC DST
 *
 *   NOTE:  this command is equivalent to a 'copy' and 'delete'.
 *
 *   SRC and DST can both be working copy (WC) paths or URLs:
 *     WC  -> WC:   move and schedule for addition (with history)
 *     URL -> URL:  complete server-side rename.
 *
 * Valid options:
 *   -m [--message] arg       : specify commit message "ARG"
 *   -F [--file] arg          : read data from file ARG
 *   -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                              {DATE}      date instead of revision number
 *                              "HEAD"      latest in repository
 *                              "BASE"      base revision of item's working copy
 *                              "COMMITTED" revision of item's last commit
 *                              "PREV"      revision before item's last commit
 *   -q [--quiet]             : print as little as possible
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 *   --no-auth-cache          : do not cache authentication tokens
 *   --non-interactive        : do no interactive prompting
 *   --force                  : force operation to run
 *   --encoding arg           : treat value as being in charset encoding ARG
 */
public class Move extends Command implements WorkingCopyModifiable {
	private static final String COMMAND = "{0} move {1} {2} {3} " + Command.NON_INTERACTIVE_MODIFIER;	// revision, source, destination
	public static final String SOURCE = "SOURCE";
	public static final String DESTINATION = "DESTINATION";
	public static final String REVISION = "REVISION";

	public void init(Map args) throws CommandException {
		super.init(args);

		String source = (String) args.get(SOURCE);
		if (source == null || "".equals(source.trim())) {
			throw new CommandException("Missing source");
		}

		String destination = (String) args.get(DESTINATION);
		if (destination == null || "".equals(destination.trim())) {
			throw new CommandException("Missing destination");
		}

		String revision = (String) args.get(REVISION);
		if ((revision != null) && (!"".equals(revision.trim()))) {
			revision = "-r " + revision;
		} else {
			revision = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, getExecutablePath(), revision, source, destination));

	}
}
