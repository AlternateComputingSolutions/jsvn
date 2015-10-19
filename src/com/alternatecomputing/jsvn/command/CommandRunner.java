package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CommandRunner {
	private static final int BUFFER_SIZE = 1024;
	private static final long ONE_MINUTE = 60000;
	private long _maxExecutionTime = 3 * ONE_MINUTE;
	private String _output;
	private String _error;
	private int _runningSinks;
	private byte[] _lock = new byte[0];

	/**
	 * returns the output from stdout from an executed command
	 * @return output from stdout
	 */
	public String getOutput() {
		return _output;
	}

	/**
	 * returns the output from stderr from an executed command
	 * @return output from stderr
	 */
	public String getError() {
		return _error;
	}

	/**
	 * runs the given command
	 * @param command subversion command to run
	 */
	public synchronized void runCommand(String command) throws CommandException {
		Runtime rt = Runtime.getRuntime();
		try {

			// execute the command
			String workingCopy = ConfigurationManager.getInstance().getWorkingCopy();
			File dir;
			if (workingCopy != null) {
				dir = new File(workingCopy);
				if (!dir.exists()) {
					dir = new File(System.getProperty("user.home"));
				}
			} else {
				dir = new File(System.getProperty("user.home"));
			}
			Process proc = rt.exec(command, null, dir);

			// get standard stream references
			InputStream stdout = proc.getInputStream();
			InputStream stderr = proc.getErrorStream();

			// hook up input stream sinks
			StringBuffer output = new StringBuffer(BUFFER_SIZE);
			StringBuffer error = new StringBuffer(BUFFER_SIZE);
			_runningSinks = 2;
			Thread stdOutSink = new Thread(new InputStreamSink(stdout, output, this));
			Thread stdErrSink = new Thread(new InputStreamSink(stderr, error, this));
			stdOutSink.start();
			stdErrSink.start();

			// wait for all sinks to notify they're done

			while (true) {
				try {
					synchronized (_lock) {
						if (_runningSinks == 0) {
							break;
						}
						_lock.wait(_maxExecutionTime);
					}
				} catch (InterruptedException e) {

					// command execution interrupted
					e.printStackTrace();
				}
			}

			// save results
			_output = output.toString();
			_error = error.toString();
			proc.destroy();
		} catch (IOException e) {
			// error getting stream references
			throw new CommandException(e.getMessage());
		}
	}

	/**
	 * decerement running sink counter and notify that a running sink has completed
	 */
	public void finished() {
		synchronized (_lock) {
			_runningSinks--;
			_lock.notify();
		}
	}
}
