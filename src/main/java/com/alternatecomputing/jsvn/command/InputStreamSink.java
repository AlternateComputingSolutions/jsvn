package com.alternatecomputing.jsvn.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class InputStreamSink implements Runnable {
	private static final int BLOCK_SIZE = 4 * 1024;
	private StringBuffer _sink;
	private InputStream _in;
	private CommandRunner _callback;

	public InputStreamSink(InputStream in, StringBuffer result, CommandRunner callback) {
		_in = in;
		_sink = result;
		_callback = callback;
	}

	public void run() {
		// using a buffered reader will cause occasion hangs since the server seems to not send a
		// terminating "\n" sometimes
		Reader in = new InputStreamReader(_in);
		int count;
		char buffer[] = new char[BLOCK_SIZE];
		try {
			while ((count = in.read(buffer)) != -1) {
				_sink.append(new String(buffer, 0, count));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			_callback.finished();
		}
	}
}
