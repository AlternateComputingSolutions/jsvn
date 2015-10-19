package com.alternatecomputing.jsvn.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.configuration.*;


public abstract class AbstractSvnTask extends Task {
	
	String repositoryUrl;
	String username;
	String password;
	Command command;
	boolean mustAuthenticate = false;
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
				log(e.getMessage());
				log(""+e.getStackTrace());					
			}
		} finally{
			ConfigurationManager.getInstance().setConfig(backupConfig);
		}
	}

	public Configuration getConfiguration(){
		
		Configuration config = new Configuration();
		config.setUsername(getUsername());
		config.setPassword(getPassword());
		config.setWorkingCopy(project.getBaseDir().getPath());
		return config;
	}

	public void setRepositoryUrl(String string) {
		repositoryUrl = string;
	}

	public String getPassword() {
		return password;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String string) {
		password = string;
	}

	public void setUsername(String string) {
		username = string;
	}

	public void setFailOnError(boolean b) {
		failOnError = b;
	}

	public void setMustAuthenticate(boolean b) {
		mustAuthenticate = b;
	}
}