package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 * log: Show the log messages for a set of revision(s) and/or file(s).
 * usage: log [URL] [PATH [PATH ... ]]
 *   Print the log messages for local PATHs, or for PATHs under
 *   URL, if URL is given.  If URL is given by itself, then print log
 *   messages for everything under it.  With -v, also print all affected
 *   paths with each log message.  With -q, don't print the log message
 *   body itself (note that this is compatible with -v).
 *
 *   Each log message is printed just once, even if more than one of the
 *   affected paths for that revision were explicitly requested.  Logs
 *   cross copy history by default; use --strict to disable this.
 *   For example:
 *
 *     svn log
 *     svn log foo.c
 *     svn log http://www.example.com/repo/project/foo.c
 *     svn log http://www.example.com/repo/project foo.c bar.c
 *
 * Valid options:
 *   -r [--revision] arg      : revision X or X:Y range.  X or Y can be one of:
 *                              {DATE}      date instead of revision number
 *                              "HEAD"      latest in repository
 *                              "BASE"      base revision of item's working copy
 *                              "COMMITTED" revision of item's last commit
 *                              "PREV"      revision before item's last commit
 *   -q [--quiet]             : print as little as possible
 *   -v [--verbose]           : print extra information
 *   --targets arg            : pass contents of file "ARG" as additional args
 *   --username arg           : specify a username ARG
 *   --password arg           : specify a password ARG
 *   --no-auth-cache          : do not cache authentication tokens
 *   --non-interactive        : do no interactive prompting
 *   --strict                 : use strict semantics
 *   --incremental            : give output suitable for concatenation
 *   --xml                    : output in xml
 */
public class Log extends Command {
	private static final String COMMAND = "{0} log {1} {2} {3} {4} " + Command.NON_INTERACTIVE_MODIFIER;    // revision, verbose, quiet, targets
	public static final String QUIET = "QUIET";
	public static final String REVISION = "REVISION";
	public static final String TARGETS = "TARGETS";
	public static final String VERBOSE = "VERBOSE";

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

		Boolean verbose = (Boolean) args.get(VERBOSE);
		String verboseOption;
		if (Boolean.TRUE == verbose) {
			verboseOption = "-v";
		} else {
			verboseOption = "";
		}

		Boolean quiet = (Boolean) args.get(QUIET);
		String quietOption;
		if (Boolean.TRUE == quiet) {
			quietOption = "-q";
		} else {
			quietOption = "";
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, getExecutablePath(), revision, verboseOption, quietOption, targets));
	}
}
