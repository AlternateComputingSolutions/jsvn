package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.CommandRunner;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Toolkit;


/**
 * JSVN main application
 */
public class Application {

	/** reference for the application's main GUI frame */
	private static Frame _applicationFrame = null;
	private static final String ERR_NO_SVN = "Could not locate \"svn\" in your PATH, please correct this and try again";
	private static final String ERR_MISSING_PROGRAM = "Missing Program";
	public static String SVN_CMD_VERSION;

	public Application() {

		ConfigurationManager.getInstance().loadConfig();

		try {
			CommandRunner runner = new CommandRunner();
			runner.runCommand(ConfigurationManager.getInstance().getWorkingCopy(), "svn --version");
			SVN_CMD_VERSION = runner.getOutput();

		} catch (CommandException ex) {
			JOptionPane.showMessageDialog(new JFrame(),ERR_NO_SVN,ERR_MISSING_PROGRAM,JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		new SplashScreen("/com/alternatecomputing/jsvn/gui/jsvn_logo.png", _applicationFrame = new Frame(),5000);

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

    /**
     * Set the application's main GUI frame
     * @param frame
     */
    public static void setApplicationFrame( Frame frame ) {
        _applicationFrame = frame;
    }
}
