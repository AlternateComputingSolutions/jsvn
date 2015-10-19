package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Update;

public class SvnUpdate extends AbstractSvnTask {

	private String revision = "HEAD";
	private String target;
	private boolean recursive = true;
	
	Command buildCommand() throws CommandException {

		Map args = new HashMap();
		args.put(Update.REVISION, revision);
		args.put(Update.NONRECURSIVE, new Boolean(!recursive));				
		args.put(Update.TARGETS, target);

		Command command = new Update();	
		command.init(args);	
		
		return command;			
	}

	public void setRecursive(boolean boolean1) {
		recursive = boolean1;
	}

	public void setRevision(String string) {
		revision = string;
	}

	public void setTarget(File file) {		
		try {
			target = file.getCanonicalPath();
		} catch (IOException e) {				
			throw new BuildException(e);
		}
	}

}
