package com.alternatecomputing.jsvn.configuration;

import com.alternatecomputing.jsvn.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * manages a Configuration instance
 */
public class ConfigurationManager {

	private static final String CONFIG_LOCATION = ".jsvn" + File.separator + "config";
	private static final String JSVN_WORKING_COPY = "jsvn.working_copy";
	private static final String JSVN_EXECUTABLE = "jsvn.executable";
	private static final String DEFAULT_SVN_EXECUTABLE = "svn";
	private static ConfigurationManager _instance;
	private Configuration _config;
	private Properties _props = new Properties();
	private String _userHome = System.getProperty(Constants.USER_HOME);
	private File _configFile = new File(_userHome + File.separator + CONFIG_LOCATION);

	/**
	 * returns the instance of ConfigurationManager
	 * @return instance of ConfigurationManager
	 */
	public static ConfigurationManager getInstance() {
		if (_instance == null) {
			synchronized (ConfigurationManager.class) {
				if (_instance == null) {
					_instance = new ConfigurationManager();
					_instance.loadConfig();
				}
			}
		}
		return _instance;
	}

	/**
	 * Indicates whether a .jsvn directory had to be created.  If one did not exist, this method will create it.
	 * @return boolean indicating whether the .jsvn directory was created as a result of this method call
	 */
	private boolean createConfigDir() {
		boolean status = true;

		// point to the config dir
		File configDir = new File(_userHome + File.separator + ".jsvn");

		// if it doesn't exist, create it
		if (!configDir.exists()) {
			status = configDir.mkdir();
		}

		return status;
	}

	/**
	 * loads the configuration values from disk
	 */
	public void loadConfig() {

		try {
			if (_configFile.exists()) {
				_props.load(new FileInputStream(_configFile));
				_config = new ConfigBuilder().buildConfig(_props.getProperty(JSVN_WORKING_COPY)
						, _props.getProperty(JSVN_EXECUTABLE));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes the configuration out to disk
	 */
	public void saveConfig() {

		try {

			// update the configuration properties
			_props.setProperty(JSVN_WORKING_COPY, _config.getWorkingCopy());
			_props.setProperty(JSVN_EXECUTABLE, _config.getExecutablePath());

			// write to file
			if (createConfigDir()) {
				_props.store(new FileOutputStream(_configFile), "JSVN Config");
			} else {
				throw new IOException("Could not save configuration");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set the working Configuration object to proccess
	 * @param config new configuration to use
	 */
	public void setConfig(Configuration config) {
		_config = config;
	}

	/**
	 * get a copy of the configuration object
	 * @return Configuration
	 */
	public Configuration getConfig() {
		return (Configuration)_config.clone();
	}
}

