package com.alternatecomputing.jsvn.command;

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

	public void init(Map args) throws CommandException {
	}
}
