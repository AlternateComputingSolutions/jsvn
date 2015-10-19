package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Executable;
import com.alternatecomputing.jsvn.configuration.Configuration;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.model.SVNTreeModel;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;


public class Frame extends CenterableFrame implements ActionListener, Executable{
	private static final String ACTION_CLOSE_TAB = "CloseTab";
	private static final String MENU_ITEM_FILE = "File";
	private static final String MENU_ITEM_EXIT = "Exit";
	private static final String MENU_ITEM_WORKING_COPY = "Working Copy";
	private static final String MENU_ITEM_WORKING_COPY_SET = "Set";
	private static final String MENU_ITEM_WORKING_COPY_CHECKOUT = "Checkout";
	private static final String MENU_ITEM_WORKING_COPY_REFRESH = "Refresh";
	private static final String MENU_ITEM_HELP = "Help";
	private static final String MENU_ITEM_ABOUT = "About";
	private static final String MENU_ITEM_CLOSE = "Close";
	private static final String STATUS_READY = "Ready";
	private static final String STATUS_EXECUTING = "Executing...";
	private static final String TAB_COMMAND_HISTORY = "Command History";
	private JLabel _statusBar = new JLabel(STATUS_READY);
	private JTabbedPane _outputTabbedPane = new JTabbedPane();
	private JTextPane _historyTextPane = new JTextPane();
	private JSVNTree _svnTree;
	private JPopupMenu popupMenu;

	/** constructor */
	public Frame() {
		initGUI();
		pack();
	}

	/** this method is called from within the constructor to initialize the form */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		JPanel content = new JPanel();

		content.setPreferredSize(new Dimension(1000, 600));
		content.setLayout(new BorderLayout());
		JSplitPane mainSplitPane = new JSplitPane();
		content.add(mainSplitPane, BorderLayout.CENTER);
		getContentPane().add(content, BorderLayout.CENTER);

		// set title
		setTitle(Constants.PRODUCT_NAME + " " + Constants.PRODUCT_VERSION);

		// add status bar
		getContentPane().add(_statusBar, BorderLayout.SOUTH);

		// add menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu(MENU_ITEM_FILE);
		menuFile.setMnemonic('F');

		// create Exit menu item
		JMenuItem fileExit = new JMenuItem(MENU_ITEM_EXIT);
		fileExit.setMnemonic('E');
		fileExit.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});

		// create Working Copy menu item
		JMenu menuWorkingCopy = new JMenu(MENU_ITEM_WORKING_COPY);
		menuWorkingCopy.setMnemonic('W');
		JMenuItem workingCopySet = new JMenuItem(MENU_ITEM_WORKING_COPY_SET);
		workingCopySet.setMnemonic('S');
		workingCopySet.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						Configuration c = ConfigurationManager.getInstance().getConfig();

						PreferencesDialog d = new PreferencesDialog(new JFrame(), true, c);
						d.setVisible(true);

						ConfigurationManager.getInstance().saveConfig();
						SVNTreeModel svnTreeModel = new SVNTreeModel(ConfigurationManager.getInstance().getWorkingCopy(), false);
						_svnTree.setModel(new DefaultTreeModel(JSVNTree.buildTreeNode(svnTreeModel)));
					}
				});
		menuWorkingCopy.add(workingCopySet);
		JMenuItem workingCopyCheckout = new JMenuItem(MENU_ITEM_WORKING_COPY_CHECKOUT);
		workingCopyCheckout.setMnemonic('C');
		workingCopyCheckout.setEnabled(true);
		workingCopyCheckout.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// process commit request
						CheckoutDialog dialog = new CheckoutDialog(Application.getApplicationFrame(), true);
						dialog.setExecutor(Application.getApplicationFrame());
						dialog.setVisible(true);
					}
				});
		menuWorkingCopy.add(workingCopyCheckout);
		JMenuItem workingCopyRefresh = new JMenuItem(MENU_ITEM_WORKING_COPY_REFRESH);
		workingCopyRefresh.setMnemonic('R');
		workingCopyRefresh.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						_svnTree.refresh(false);
					}
				});
		menuWorkingCopy.add(workingCopyRefresh);

		// create About menu item
		JMenu menuHelp = new JMenu(MENU_ITEM_HELP);
		menuHelp.setMnemonic('H');
		JMenuItem helpAbout = new JMenuItem(MENU_ITEM_ABOUT);
		helpAbout.setMnemonic('A');
		helpAbout.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AboutDialog aboutDialog = new AboutDialog(Frame.this, true);
						aboutDialog.setVisible(true);
					}
				});
		menuHelp.add(helpAbout);

		menuFile.add(fileExit);
		menuBar.add(menuFile);
		menuBar.add(menuWorkingCopy);
		menuBar.add(menuHelp);

		// sets menu bar
		setJMenuBar(menuBar);
		setBounds(new Rectangle(0, 0, 713, 384));
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						exitForm(evt);
					}
				});

		// set up the basic pane geometry
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setDividerLocation(375);
		JSplitPane rightSplitPane = new JSplitPane();
		mainSplitPane.add(rightSplitPane, JSplitPane.RIGHT);

		// install the new tree model
		SVNTreeModel model = new SVNTreeModel(ConfigurationManager.getInstance().getWorkingCopy(), false);
		_svnTree = new JSVNTree(model);
		JScrollPane svnPane = new JScrollPane();
		svnPane.setViewportView(_svnTree);
		mainSplitPane.add(svnPane, JSplitPane.LEFT);

		// create the right split pane
		rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightSplitPane.setOneTouchExpandable(true);
		rightSplitPane.setDividerLocation(450);
		rightSplitPane.setResizeWeight(1);
		JTabbedPane historyTabbedPane = new JTabbedPane();
		rightSplitPane.add(historyTabbedPane, JSplitPane.BOTTOM);
		rightSplitPane.add(_outputTabbedPane, JSplitPane.TOP);

		// create the message pane for svn command history display
		_historyTextPane.setText("");
		_historyTextPane.setEditable(false);
		JScrollPane historyPane = new JScrollPane();
		historyPane.setViewportView(_historyTextPane);
		historyTabbedPane.setSize(new Dimension(633, 174));
		historyTabbedPane.setSize(new Dimension(633, 174));
		historyTabbedPane.add(historyPane, TAB_COMMAND_HISTORY);

		// create the scrollable text pane for svn console output
		popupMenu = new JPopupMenu();
		JMenuItem mi = new JMenuItem(MENU_ITEM_CLOSE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_CLOSE_TAB);
		popupMenu.add(mi);
		_outputTabbedPane.addMouseListener(
				new MouseAdapter() {
					private void popup(MouseEvent e) {
						if (e.isPopupTrigger()) {
							popupMenu.show((JComponent) e.getSource(), e.getX(), e.getY());
						}
					}

					public void mousePressed(MouseEvent e) {
						popup(e);
					}

					public void mouseClicked(MouseEvent e) {
						popup(e);
					}

					public void mouseReleased(MouseEvent e) {
						popup(e);
					}
				}
		);
	}

	/**
	 * executes the given command with the given arguments
	 * @param command command to be run
	 * @param args arguments for the command
	 */
	public void executeCommand(Command command, Map args) throws CommandException {
		try {
			_statusBar.setText(STATUS_EXECUTING);
			command.init(args);
			command.execute();
			String result = command.getResult();
			_historyTextPane.setText(_historyTextPane.getText() + command.getCommand() + "\n");
			JTextPane newPane = new JTextPane();
			newPane.setText(result);
			newPane.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(newPane);
			String commandName = command.getClass().getName();
			_outputTabbedPane.add(commandName.substring(commandName.lastIndexOf(".") +1), scrollPane);
			_outputTabbedPane.setSelectedIndex(_outputTabbedPane.getTabCount() -1);
		} finally {
			_statusBar.setText(STATUS_READY);
		}
	}

	/**
	 * exit the Application
	 */
	private void exitForm(WindowEvent evt) {
		System.exit(0);
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_CLOSE_TAB)) {
			_outputTabbedPane.remove(_outputTabbedPane.getSelectedIndex());
		}
	}
}
