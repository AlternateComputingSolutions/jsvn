package com.alternatecomputing.jsvn.configuration;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.util.StringUtil;

/**
 * provides handling of configuration properties
 */
public class Configuration implements Cloneable {

	private String _workingCopy;
	private String _workingDirectory;
	private String _username;
	private String _password;

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
	 * returns the username
	 * @return username
	 */
	public String getUsername() {
		return _username;
	}

	/**
	 * sets the username
	 * @param username username to use for authentication
	 */
	public void setUsername(String username) {
		this._username = StringUtil.normalizeFileEncoding(username);
	}

	/**
	 * returns the password
	 * @return password
	 */
	public String getPassword() {
		return _password;
	}

	/**
	 * sets the password
	 * @param password password to use for authentication
	 */
	public void setPassword(String password) {
		_password = StringUtil.normalizeFileEncoding(password);
	}

	/**
	 * sets all properties from another Configuration object
	 * @param config Configuration object which contains values to use
	 */
	public void setValues(Configuration config) {
		setWorkingCopy(config.getWorkingCopy());
		setUsername(config.getUsername());
		setPassword(config.getPassword());
	}

	/**
	 * return a copy of the object
	 * @return Copy of the object
	 */
	public Object clone() {
		Configuration c = new Configuration();
		c.setWorkingCopy(getWorkingCopy());
		c.setUsername(getUsername());
		c.setPassword(getPassword());
		return c;
	}
}
