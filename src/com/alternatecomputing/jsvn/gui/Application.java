package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * JSVN main application
 */
public class Application {

	/** reference for the application's main GUI frame */
	private static Frame _applicationFrame = null;

	public Application() {
		ConfigurationManager.getInstance().loadConfig();
		_applicationFrame = new Frame();

		// center the frame on screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = _applicationFrame.getSize();
		frameSize.height = ((frameSize.height > screenSize.height) ? screenSize.height : frameSize.height);
		frameSize.width = ((frameSize.width > screenSize.width) ? screenSize.width : frameSize.width);
		_applicationFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		_applicationFrame.setVisible(true);
	}

	public static void main(String[] argv) {

		// set up system Look&Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// create and run the application
		new Application();
	}

	/**
	 * returns a reference to the application's main GUI frame
	 * @return application frame
	 */
	public static Frame getApplicationFrame() {
		return _applicationFrame;
	}
}
