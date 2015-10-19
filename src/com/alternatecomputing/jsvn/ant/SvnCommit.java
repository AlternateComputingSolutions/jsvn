package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Commit;

public class SvnCommit extends AbstractSvnTask {

	private String commitMessage ="";
	private String target;

	Command buildCommand() throws CommandException {
			
	Map args = new HashMap();
	args.put(Commit.COMMIT_MESSAGE, commitMessage);		
	args.put(Commit.TARGETS, target);
		
	Command command = new Commit();	
	command.init(args);	
			
	return command;			
	}

	public void setCommitMessage(String string) {
		commitMessage = string;
	}

	public void setTarget(File file) {
		try {
			target = file.getCanonicalPath();
		} catch (IOException e) {
			throw new BuildException(e);			
		}
	}

}
