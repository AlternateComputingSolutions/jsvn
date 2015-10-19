package com.alternatecomputing.jsvn.gui;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * JFrame subclass that automatically centers itself on the screen
 */
public class CenterableFrame extends JFrame {

	/**
	 * default constructor
	 */
	public CenterableFrame() {
		super();
	}

	/**
	 * constructor that sets the title on creation
	 * @param title String title of JFrame
	 */
	public CenterableFrame(String title) {
		super(title);
	}

	/**
	 * centers itself on the screen
	 */
	private void center() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();

		// never allow the window to be bigger than the screen
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		// center the window
		setLocation((screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);
	}

	/**
	 * overridden method in Frame to center the Frame
	 * @see java.awt.Frame
	 */
	public void show() {
		center();
		super.show();
	}

	/**
	 * overridden method in Frame to center the Frame
	 * @see java.awt.Frame
	 */
	public void setVisible(boolean b) {
		center();
		super.setVisible(b);
	}

}