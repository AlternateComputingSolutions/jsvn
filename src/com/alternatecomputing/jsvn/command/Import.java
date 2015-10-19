package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	import: Commit an unversioned file or tree into the repository.
 *	usage: svn import REPOS_URL [PATH] [NEW_ENTRY_IN_REPOS]
 *
 *	    Recursively commit a copy of PATH to REPOS_URL.
 *	    If no 3rd arg, copy top-level contents of PATH into REPOS_URL
 *	    directly.  Otherwise, create NEW_ENTRY underneath REPOS_URL and
 *	    begin copy there.  (-r is only needed if importing to --xml-file)
 *
 *	Valid options:
 *	  -F [--file] arg          : read data from file ARG
 *	  -m [--message] arg       : specify commit message "ARG"
 *	  -q [--quiet]             : print as little as possible
 *	  -N [--nonrecursive]      : operate on single directory only
 *	  --username arg           : specify a username ARG
 *	  --password arg           : specify a password ARG
 *	  --xml-file arg           : read/write xml to specified file ARG
 *	  -r [--revision] arg      : specify revision number ARG (or X:Y range)
 *	  --message-encoding arg   : take log message in charset encoding ARG
 */
public class Import extends Command {
	private static final String COMMAND = "svn import --message {0} {1} {2} {3}";
	public static final String COMMIT_MESSAGE = "COMMIT_MESSAGE";
	public static final String NEW_ENTRY_IN_REPOS = "NEW_ENTRY_IN_REPOS";
	public static final String PATH = "PATH";
	public static final String REPOS_URL = "REPOS_URL";

	public void init(Map args) throws CommandException {
		String url = (String) args.get(REPOS_URL);
		if (url == null || "".equals(url.trim())) {
			throw new CommandException("Missing url");
		}

		String path = (String) args.get(PATH);
		if (path == null || "".equals(path.trim())) {
			throw new CommandException("Missing path");
		}

		String newEntry = (String) args.get(NEW_ENTRY_IN_REPOS);
		if (newEntry == null || "".equals(newEntry.trim())) {
			throw new CommandException("Missing new entry");
		}

		String commitMessage = (String) args.get(COMMIT_MESSAGE);
		if (commitMessage == null || "".equals(commitMessage.trim()) ) {
			throw new CommandException("Missing commit message");
		}

		// build the command
		setCommand(MessageFormat.format(COMMAND, new String[]{"'" + commitMessage + "'", url, path, newEntry}));
	}
}
