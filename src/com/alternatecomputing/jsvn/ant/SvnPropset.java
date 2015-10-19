package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.PropSet;

public class SvnPropset extends AbstractSvnTask {

	private String target;
	private String propertyName;
	private String propertyValue;
	private boolean recursive = true;
	private boolean propValueIsFile = false;
	private boolean quiet = false;
	
	Command buildCommand() throws CommandException {

		Map args = new HashMap();
		
		args.put(PropSet.TARGETS,target);
		args.put(PropSet.PROPERTY_NAME ,propertyName);
		args.put(PropSet.PROPERTY_VALUE ,propertyValue);
		args.put(PropSet.NONRECURSIVE, new Boolean(!recursive));
		args.put(PropSet.PROPVALUE_IS_FILE, new Boolean(propValueIsFile));
		args.put(PropSet.QUIET, new Boolean(quiet));
				
		Command command = new PropSet();	
		command.init(args);			
		return command;			
	}

	public void setPropertyValueIsFile(boolean b) {
		propValueIsFile = b;
	}

	public void setRecursive(boolean b) {
		recursive = b;
	}

	public void setQuiet(boolean b) {
		quiet = b;
	}

	public void setPropertyName(String string) {
		propertyName = string;
	}

	public void setPropertyValue(String string) {
		propertyValue = string;
	}

	public void setTarget(File file) {		
		try {
			target = file.getCanonicalPath();
		} catch (IOException e) {				
			throw new BuildException(e);
		}
	}
}
