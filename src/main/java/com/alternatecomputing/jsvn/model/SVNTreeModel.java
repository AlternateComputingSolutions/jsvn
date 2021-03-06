package com.alternatecomputing.jsvn.model;

import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Status;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * models a subversion working copy
 */
public class SVNTreeModel {
	private SVNTreeNodeData _root;

	/**
	 * Builds a complete model of the svn working copy by executing an status verbose command and parsing it's output.
	 * If workingCopy is null or doesn't exist, then a SVNTreemodel is returned with a root with value null.
	 * @param workingCopy
	 * @param online
	 */
	public SVNTreeModel(String workingCopy, boolean online) {
		// if working copy is null (not set), return an empty node
		if (workingCopy == null) {
			_root =  null;
		} else {
			// build the status command to run
			Status status = new Status();
			Map args = new HashMap();
			args.put(Status.VERBOSE, Boolean.TRUE);
			args.put(Status.TARGETS, workingCopy);
			if (online) {
				args.put(Status.SHOW_UPDATES, Boolean.TRUE);
			}
			try {
				// run the command
				status.init(args);
				status.execute();
				String result = status.getResult();
				_root =  Parser.parse(result);
			} catch (CommandException e) {
				// this may occur if the working copy is a non-versioned directory
				_root =  null;
			} catch (IOException e) {
				// this may be thrown by the parser if there are problems looking at physical files in the working copy
				e.printStackTrace();
				_root =  null;
			}
		}
	}

	/**
	 * gets the model's root node
	 * @return root node of the model
	 */
	public SVNTreeNodeData getRoot() {
		return _root;
	}

}
