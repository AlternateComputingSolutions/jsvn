package com.alternatecomputing.jsvn.ant;

import com.alternatecomputing.jsvn.command.Checkout;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SvnCheckout extends AbstractSvnTask {

	private int revision = -1;
	private String username = "";
	private String password = "";

	/** for acceptable formats see http://svnbook.red-bean.com/html-chunk/ch03s03.html#svn-ch-3-sect-3.3 */
	private String date;
	private boolean verbose = false;
	private boolean recursive = true;
	private File destination;

	public Command buildCommand() throws CommandException{

		Map args = new HashMap();
		args.put(Checkout.REPOS_URL,  getRepositoryUrl());
		args.put(Checkout.DESTINATION, getDestinationPath());
		args.put(Checkout.USERNAME, getUsername());
		args.put(Checkout.PASSWORD, getPassword());
		args.put(Checkout.REVISION, Integer.toString(revision));
		args.put(Checkout.NONRECURSIVE, new Boolean(!recursive));
		Command command = new Checkout();
		command.init(args);

		return command;
	}

	private String getDestinationPath() throws CommandException{
		if (destination == null) destination = project.getBaseDir();
		String destinationPath = null;

		try {
			if (!destination.exists()) destination.mkdirs();
			destinationPath = destination.getCanonicalPath();
		} catch (IOException e) {
			throw new CommandException(e.toString());
		}

		return destinationPath;
	}

	public String getDate() {
		return date;
	}

	public int getRevision() {
		return revision;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	//	currently ignored
	public void setDate(String string) {
		date = string;
		throw new UnsupportedOperationException("sorry. checkout by date not supported yet");
	}

	public void setDestination(File file) {
		destination = file;
	}

	public void setRecursive(boolean b) {
		recursive = b;
	}

	public void setRevision(int i) {
		revision = i;
	}

	public void setUsername(String s) {
		username = s;
	}

	public void setPassword(String s) {
		password = s;
	}

	//currently ignored
	public void setVerbose(boolean b) {
		verbose = b;
	}
}
