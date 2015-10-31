package com.alternatecomputing.jsvn.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * JDialog subclass that automatically centers itself on the screen
 */
public class CenterableDialog extends JDialog implements KeyListener {
	private JPanel _panel = new JPanel();
	private BorderLayout _layout = new BorderLayout();

	public CenterableDialog(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public CenterableDialog(Frame frame, boolean modal) {
		this(frame, "", modal);
	}

	public CenterableDialog(boolean modal) {
		this(null, "", modal);
	}

	public CenterableDialog() {
		this(null, "", false);
	}

	private void jbInit() throws Exception {
		addKeyListener(this);
		_panel.setLayout(_layout);
		getContentPane().add(_panel);
	}

	/**
	 * centers the Dialog on the screen
	 */
	private void center() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();

		// never allow the window to be bigger than the screen.
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		// center the window.
		setLocation((screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);
	}

	/**
	 * overridden method to call the center method
	 */
	public void show() {
		center();
		super.show();
	}

	/**
	 * overrideen method to call the center method
	 *
	 * @param visible indicator to tell to show Dialog or not
	 */
	public void setVisible(boolean visible) {
		center();
		super.setVisible(visible);
	}

	// Methods needed for KeyListener Interface
	// This is so the dialog can respond to ESC, or ENTER key presses
	public void keyPressed(KeyEvent evt) {
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

}
