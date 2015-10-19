package com.alternatecomputing.jsvn.command;

import java.text.MessageFormat;
import java.util.Map;

/**
 *	propset (pset, ps): Set property PROPNAME to PROPVAL on files or directories.
 *	usage: propset PROPNAME PROPVAL [TARGETS]
 *
 *	    Use -F (instead of PROPVAL) to get the value from a file.
 *
 *	    Note: svn recognizes the following special properties but will
 *	    store any arbitrary properties set:
 *	      svn:ignore     - A newline separated list of file patterns to ignore.
 *	      svn:keywords   - Keywords to be expanded.  Valid keywords are:
 *	        URL, HeadURL             - The URL for the head version of the object.
 *	        Author, LastChangedBy    - The last person to modify the file.
 *	        Date, LastChangedDate    - The date/time the object was last modified.
 *	        Rev, LastChangedRevision - The last revision the object changed.
 *	      svn:executable - If present, make the file executable. (Any
 *	        attempt to set this property on a directory will be ignored.)
 *	      svn:eol-style  - One of 'native', 'LF', 'CR', 'CRLF'.
 *	      svn:mimetype   - The mimetype of the file.  Used to determine
 *	        whether to merge the file, and how to serve it from Apache.
 *	        A mimetype beginning with 'text/' (or an absent mimetype) is
 *	        treated as text.  Anything else is treated as binary.
 *
 *	Valid options:
 *	  -F [--file] arg          : read data from file ARG
 *	  -q [--quiet]             : print as little as possible
 *	  --targets arg            : pass contents of file "ARG" as additional args
 *	  -R [--recursive]         : descend recursively
 */
public class PropSet extends Command {

	private static final String COMMAND = "svn propset {0} {1} {2} {3} {4}";

	public static final String NONRECURSIVE = "NONRECURSIVE";
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	public static final String PROPVALUE_IS_FILE = "PROPVALUE_IS_FILE";
	public static final String TARGETS = "TARGETS";
	public static final String QUIET = "QUIET";

	public void init(Map args) throws CommandException {

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

		String targets = (String) args.get(TARGETS);
		if (targets == null || "".equals(targets.trim())) {
			throw new CommandException("Missing target(s)");
		}

		String propertyNameOption = (String) args.get(PROPERTY_NAME);
		if (propertyNameOption == null || "".equals(propertyNameOption.trim())) {
			throw new CommandException("Missing property name");
		}

		String propertyValueOption = (String) args.get(PROPERTY_VALUE);
		if (propertyValueOption == null || "".equals(propertyValueOption.trim())) {
			throw new CommandException("Missing property value");
		}
		propertyValueOption = "\""+ propertyValueOption + "\"";

		Boolean propValIsFile = (Boolean) args.get(PROPVALUE_IS_FILE);
		if (Boolean.TRUE == propValIsFile) {
			 propertyValueOption = "-F " + propertyValueOption;
		}

		setCommand(MessageFormat.format(COMMAND,
			new String[]{quietOption, nonRecursiveOption, propertyNameOption, propertyValueOption, targets}));
	}
}
