package com.alternatecomputing.jsvn.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class SplashScreen extends JWindow {

	/**
	 *
	 * @param filename
	 * @param f
	 * @param waitTime
	 */
	public SplashScreen(String filename, Frame f, int waitTime) {
		super(f);
		JLabel l = new JLabel(new ImageIcon(getClass().getResource(filename)));
		getContentPane().add(l, BorderLayout.CENTER);
		pack();
		Dimension screenSize =
				Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = l.getPreferredSize();
		setLocation(screenSize.width / 2 - (labelSize.width / 2), screenSize.height / 2 - (labelSize.height / 2));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		final int pause = waitTime;
		final Runnable closerRunner = new Runnable() {
			public void run() {
				setVisible(false);
				dispose();
			}
		};
		Runnable waitRunner = new Runnable() {
			public void run() {
				try {
					Thread.sleep(pause);
					SwingUtilities.invokeAndWait(closerRunner);
				} catch (InvocationTargetException e) {
					// XXX - log error
					e.printStackTrace();
				} catch (InterruptedException e) {
					// XXX - log error
					e.printStackTrace();
				}
			}
		};
		setVisible(true);
		Thread splashThread = new Thread(waitRunner, "SplashThread");
		splashThread.start();
	}
}
