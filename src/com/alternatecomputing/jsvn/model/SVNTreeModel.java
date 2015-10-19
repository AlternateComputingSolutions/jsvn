package com.alternatecomputing.jsvn.model;

import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Status;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * models a subversion working copy
 */
public class SVNTreeModel {
	private SVNTreeNodeData _root;

	/**
	 * builds a complete model of the svn working copy by executing an "svn status -v" command and parsing it's output
	 * @param workingCopy
	 * @param online
	 */
	public SVNTreeModel(String workingCopy, boolean online) {
		// if working copy is null (not set), return an empty node
		if ((workingCopy == null) || !(new File(workingCopy).exists())) {
			_root =  new SVNTreeNodeData();
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
				e.printStackTrace();
				_root =  new SVNTreeNodeData();
			} catch (IOException e) {
				e.printStackTrace();
				_root =  new SVNTreeNodeData();
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
