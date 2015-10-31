package com.alternatecomputing.jsvn.configuration;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.util.StringUtil;

/**
 * provides handling of configuration properties
 */
public class DefaultConfiguration implements Cloneable, Configuration {

	private String _workingCopy;
	private String _workingDirectory;
	private String _executablePath;

	protected DefaultConfiguration() { }

	/**
	 * returns the working copy directory
	 * @return working copy directory
	 */
	public String getWorkingCopy() {
		return _workingCopy;
	}

	/**
	 * sets the working copy directory
	 * @param workingCopy Working directory
	 */
	public void setWorkingCopy(String workingCopy) {
		_workingCopy = StringUtil.normalizeFileEncoding(workingCopy);
		if (_workingCopy != null) {
			_workingDirectory = _workingCopy.substring(0, _workingCopy.lastIndexOf(Constants.SVN_PATH_SEPARATOR) + 1);
		} else {
			_workingDirectory  = null;
		}
	}

	/**
	 * get the working directory value from the Configuration object
	 * @return working directory value or null if not defined
	 */
	public String getWorkingDirectory() {
		return _workingDirectory;
	}

	/**
	 * get the executable path from the Configuration object
	 * @return executable path or null if not defined
	 */
	public String getExecutablePath() {
		return _executablePath;
	}

	/**
	 * sets the executable path
	 * @param executablePath executable path
	 */
	public void setExecutablePath(String executablePath) {
		this._executablePath = executablePath;
	}

	/**
	 * return a copy of the object
	 * @return Copy of the object
	 */
	public Object clone() {
		Configuration c = new ConfigBuilder().buildConfig(getWorkingCopy(), getExecutablePath());
		return c;
	}
}
