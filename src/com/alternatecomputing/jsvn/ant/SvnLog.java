package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Log;

public class SvnLog extends AbstractSvnTask {

	private boolean verbose = false;
	private boolean quiet = false;
	private String revision = "";
	private String target;

	Command buildCommand() throws CommandException {
		Map args = new HashMap();
		args.put(Log.QUIET, new Boolean(quiet));
		args.put(Log.VERBOSE, new Boolean(verbose));
		args.put(Log.REVISION, revision);
		args.put(Log.TARGETS, target);

		Command command = new Log();	
		command.init(args);	
		
		return command;			
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

	public void setQuiet(boolean b) {
		quiet = b;
	}

	public void setVerbose(boolean b) {
		verbose = b;
	}

}
