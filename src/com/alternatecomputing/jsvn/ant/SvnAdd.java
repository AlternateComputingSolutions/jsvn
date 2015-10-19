package com.alternatecomputing.jsvn.ant;

import com.alternatecomputing.jsvn.command.Add;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Commandable;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SvnAdd extends AbstractSvnTask {

	private String target;

	Commandable buildCommand() throws CommandException {
		Map args = new HashMap();
		args.put(Add.TARGETS, target);

		Commandable command = new Add();
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
