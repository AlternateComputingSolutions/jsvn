package com.alternatecomputing.jsvn.ant;

import java.util.HashMap;
import java.util.Map;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Copy;

public class SvnCopy extends AbstractSvnTask {

	private String source;
	private String destination;	
	private String revision;

	Command buildCommand() throws CommandException {

		Map args = new HashMap();
		args.put(Copy.REVISION, revision);		
		args.put(Copy.DESTINATION, destination);
		args.put(Copy.SOURCE, source);
		
		Command command = new Copy();	
		command.init(args);		
		return command;			
	}

	public void setDestination(String string) {
		destination = string;
	}

	public void setRevision(String string) {
		revision = string;
	}

	public void setSource(String string) {
		source = string;
	}

}
