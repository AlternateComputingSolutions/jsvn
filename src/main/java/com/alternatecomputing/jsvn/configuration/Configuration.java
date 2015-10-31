package com.alternatecomputing.jsvn.configuration;

/**
 * Created by alberto on 31/10/15.
 */
public interface Configuration {
	String getWorkingCopy();

	void setWorkingCopy(String workingCopy);

	String getExecutablePath();

	void setExecutablePath(String executable);

	Object clone();

	String getWorkingDirectory();
}
