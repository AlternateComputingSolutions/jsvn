package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.PropGet;

public class SvnPropget extends AbstractSvnTask {

	private boolean recursive = true;
	private String target;
	
	Command buildCommand() throws CommandException {
		
		Map args = new HashMap();
		args.put(PropGet.NONRECURSIVE, new Boolean(!recursive));
		args.put(PropGet.TARGETS, target);
		Command command = new PropGet();	
		command.init(args);	
		
		return command;			
	}

	public void setTarget(File file) {		
		try {
			target = file.getCanonicalPath();
		} catch (IOException e) {				
			throw new BuildException(e);
		}
	}
	
	public void setRecursive(boolean b) {
		recursive = b;
	}
}
