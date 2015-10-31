package com.alternatecomputing.jsvn.configuration;

/**
 * Created by alberto on 31/10/15.
 */
public class ConfigBuilder {
    public Configuration buildConfig(String workingCopy, String executablePath) {
        Configuration config = new DefaultConfiguration();
        config.setWorkingCopy(workingCopy);
        config.setExecutablePath(executablePath);
        return config;
    }
}
