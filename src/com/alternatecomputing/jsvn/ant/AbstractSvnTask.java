package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.configuration.*;

public abstract class AbstractSvnTask extends Task {

	String repositoryUrl;
	Command command;
	boolean failOnError = true;

	public AbstractSvnTask(){
		super();
	}

	/** return a customized JSVN Command object */
	abstract Command buildCommand() throws CommandException;

	public void execute() throws BuildException {
		Configuration backupConfig = ConfigurationManager.getInstance().getConfig();

		try{
			ConfigurationManager.getInstance().setConfig(getConfiguration());
			Command command = buildCommand();
			command.execute();
		} catch (CommandException e) {
			if (failOnError){
				throw new BuildException (e);
			}else{
				log(e.getMessage(),Project.MSG_ERR);
				log(""+e.getStackTrace(),Project.MSG_ERR);
			}
		} finally{
			ConfigurationManager.getInstance().setConfig(backupConfig);
		}
	}

	public Configuration getConfiguration(){

		Configuration config = new Configuration();
		config.setWorkingCopy(project.getBaseDir().getPath());
		return config;
	}

	public void setRepositoryUrl(String string) {
		repositoryUrl = string;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public void setFailOnError(boolean b) {
		failOnError = b;
	}
}