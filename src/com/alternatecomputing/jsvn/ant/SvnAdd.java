package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.alternatecomputing.jsvn.command.Add;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;

public class SvnAdd extends AbstractSvnTask {

	private String target;
	
	Command buildCommand() throws CommandException {
		Map args = new HashMap();
		args.put(Add.TARGETS, target);
		
		Command command = new Add();	
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
}
