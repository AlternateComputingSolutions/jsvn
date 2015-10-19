package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Executable;
import com.alternatecomputing.jsvn.configuration.Configuration;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.model.SVNTreeModel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;


public class Frame extends CenterableFrame implements ActionListener, Executable {
	private static final char MNEMONIC_CHECKOUT = 'C';
	private static final char MNEMONIC_SET_WORKING_COPY = 'S';
	private static final char MNEMONIC_WORKING_COPY = 'W';
	private static final char MNEMONIC_REFRESH_WORKING_COPY = 'R';
	private static final char MNEMONIC_HELP = 'H';
	private static final char MNEMONIC_ABOUT = 'A';
	private static final char MNEMONIC_EXIT = 'E';
	private static final char MNEMONIC_FILE = 'F';
	private static final String CONFIRMATION = "Confirmation";
	private static final String OVERWRITE_EXISTING_FILE = "Overwrite existing file?";
	private static final String NEWLINE_CHARACTER = "\n";
	private static final String PERIOD_CHARACTER = ".";
	private static final String ACTION_CLOSE_TAB = "CloseTab";
	private static final String ACTION_CLOSE_ALL_TAB = "CloseAllTab";
	private static final String ACTION_CLOSE_ALL_BUT_THIS_TAB = "CloseAllButThisTab";
	private static final String ACTION_SAVE_TAB = "SaveTab";
	private static final String MENU_ITEM_FILE = "File";
	private static final String MENU_ITEM_EXIT = "Exit";
	private static final String MENU_ITEM_WORKING_COPY = "Working Copy";
	private static final String MENU_ITEM_WORKING_COPY_SET = "Set";
	private static final String MENU_ITEM_WORKING_COPY_CHECKOUT = "Checkout";
	private static final String MENU_ITEM_WORKING_COPY_REFRESH = "Refresh";
	private static final String MENU_ITEM_HELP = "Help";
	private static final String MENU_ITEM_ABOUT = "About";
	private static final String MENU_ITEM_CLOSE = "Close";
	private static final String MENU_ITEM_CLOSE_ALL = "Close All";
	private static final String MENU_ITEM_CLOSE_ALL_BUT_THIS = "Close All But This";
	private static final String MENU_ITEM_SAVE = "Save To File";
	private static final String STATUS_READY = "Ready";
	private static final String STATUS_EXECUTING = "Executing...";
	private static final String TAB_COMMAND_HISTORY = "Command History";
    private JPanel content;
	private JLabel _statusBar = new JLabel(STATUS_READY);
	private JTabbedPane _outputTabbedPane = new JTabbedPane();
	private JTextPane _historyTextPane = new JTextPane();
	private JSVNTree _svnTree;
	private JPopupMenu _popupMenu;
	private JMenuItem _miCloseTab, _miCloseAllTab, _miCloseAllButThisTab, _miSaveTab;
    private Executable _executor;

    /** constructor with provided executor */
    public Frame( Executable executor ) {
        _executor = executor;
        initGUI();
        pack();
    }

    /** constructor */
	public Frame() {
        _executor = this;
		initGUI();
		pack();
	}

	/** this method is called from within the constructor to initialize the form */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		content = new JPanel();

		content.setPreferredSize(new Dimension(1000, 600));
		content.setLayout(new BorderLayout());
		JSplitPane mainSplitPane = new JSplitPane();
		content.add(mainSplitPane, BorderLayout.CENTER);
		getContentPane().add(content, BorderLayout.CENTER);

		// set title
		setTitle(Constants.PRODUCT_NAME + " " + Constants.PRODUCT_VERSION);

		// add status bar
		content.add(_statusBar, BorderLayout.SOUTH);

		// add menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu(MENU_ITEM_FILE);
		menuFile.setMnemonic(MNEMONIC_FILE);

		// create Exit menu item
		JMenuItem fileExit = new JMenuItem(MENU_ITEM_EXIT);
		fileExit.setMnemonic(MNEMONIC_EXIT);
		fileExit.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});

		// create Working Copy menu item
		JMenu menuWorkingCopy = new JMenu(MENU_ITEM_WORKING_COPY);
		menuWorkingCopy.setMnemonic(MNEMONIC_WORKING_COPY);
		JMenuItem workingCopySet = new JMenuItem(MENU_ITEM_WORKING_COPY_SET);
		workingCopySet.setMnemonic(MNEMONIC_SET_WORKING_COPY);
		workingCopySet.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						Configuration c = ConfigurationManager.getInstance().getConfig();
                        JFileChooser chooser;
						String workingDirectory = ConfigurationManager.getInstance().getWorkingDirectory();
						if (workingDirectory != null) {
							File currentWorkingDirectory = new File(workingDirectory);
							if (currentWorkingDirectory.exists()) {
								chooser = new JFileChooser(currentWorkingDirectory);
							} else {
								chooser = new JFileChooser();
							}
						} else {
							chooser = new JFileChooser();
						}

						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int action = chooser.showOpenDialog(new JFrame());
						if (action == JFileChooser.APPROVE_OPTION) {
							try {
								File file = new File(chooser.getSelectedFile().getCanonicalPath());
								c.setWorkingCopy(file.toString());
								ConfigurationManager.getInstance().setConfig(c);
								ConfigurationManager.getInstance().saveConfig();
							} catch (IOException e1) {
								// XXX - log error here
								e1.printStackTrace();
							}
						}

                        setWorkingCopy(  ConfigurationManager.getInstance().getWorkingCopy() );
                    }
				});

		menuWorkingCopy.add(workingCopySet);
		JMenuItem workingCopyCheckout = new JMenuItem(MENU_ITEM_WORKING_COPY_CHECKOUT);
		workingCopyCheckout.setMnemonic(MNEMONIC_CHECKOUT);
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
		workingCopyRefresh.setMnemonic(MNEMONIC_REFRESH_WORKING_COPY);
		workingCopyRefresh.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						_svnTree.refresh(false);
					}
				});
		menuWorkingCopy.add(workingCopyRefresh);

		// create About menu item
		JMenu menuHelp = new JMenu(MENU_ITEM_HELP);
		menuHelp.setMnemonic(MNEMONIC_HELP);
		JMenuItem helpAbout = new JMenuItem(MENU_ITEM_ABOUT);
		helpAbout.setMnemonic(MNEMONIC_ABOUT);
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
		_svnTree = new JSVNTree(model, _executor);
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

		// create the popup menu & listener for the tabbed command output
		_popupMenu = new JPopupMenu();
		_miCloseTab = new JMenuItem(MENU_ITEM_CLOSE);
		_miCloseTab.addActionListener(this);
		_miCloseTab.setActionCommand(ACTION_CLOSE_TAB);
		_popupMenu.add(_miCloseTab);
		_miCloseAllButThisTab = new JMenuItem(MENU_ITEM_CLOSE_ALL_BUT_THIS);
		_miCloseAllButThisTab.addActionListener(this);
		_miCloseAllButThisTab.setActionCommand(ACTION_CLOSE_ALL_BUT_THIS_TAB);
		_popupMenu.add(_miCloseAllButThisTab);
		_miCloseAllTab = new JMenuItem(MENU_ITEM_CLOSE_ALL);
		_miCloseAllTab.addActionListener(this);
		_miCloseAllTab.setActionCommand(ACTION_CLOSE_ALL_TAB);
		_popupMenu.add(_miCloseAllTab);
		_miSaveTab = new JMenuItem(MENU_ITEM_SAVE);
		_miSaveTab.addActionListener(this);
		_miSaveTab.setActionCommand(ACTION_SAVE_TAB);
		_popupMenu.add(_miSaveTab);
		_outputTabbedPane.addMouseListener(
				new MouseAdapter() {
					private void popup(MouseEvent e) {
						if (e.isPopupTrigger()) {
							// only show popup if at least one tab exists
							int tabCount = _outputTabbedPane.getTabCount();
							if (tabCount > 0) {
								_miCloseTab.setEnabled(true);
								_miSaveTab.setEnabled(true);
								if (tabCount > 1) {
									_miCloseAllTab.setEnabled(true);
									_miCloseAllButThisTab.setEnabled(true);
								} else {
									_miCloseAllTab.setEnabled(false);
									_miCloseAllButThisTab.setEnabled(false);
								}
								_popupMenu.show((JComponent) e.getSource(), e.getX(), e.getY());
							}
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

    public void setWorkingCopy( String workingCopy ) {
        SVNTreeModel svnTreeModel = new SVNTreeModel( workingCopy, false);
        _svnTree.setModel(new DefaultTreeModel(JSVNTree.buildTreeNode(svnTreeModel)));
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
			_historyTextPane.setText(_historyTextPane.getText() + command.getCommand() + NEWLINE_CHARACTER);
			JTextPane newPane = new JTextPane();

            Font fixedFont = new Font("Monospaced", Font.PLAIN, 10 );
            newPane.setFont( fixedFont );
			newPane.setText(result);
			newPane.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(newPane);
			String commandName = command.getClass().getName();
			_outputTabbedPane.add(commandName.substring(commandName.lastIndexOf(PERIOD_CHARACTER) + 1), scrollPane);
			_outputTabbedPane.setSelectedIndex(_outputTabbedPane.getTabCount() - 1);
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
		} else if (e.getActionCommand().equals(ACTION_CLOSE_ALL_TAB)) {
			for (int i = _outputTabbedPane.getTabCount(); i > 0; i--) {
				_outputTabbedPane.remove(i - 1);
			}
		} else if (e.getActionCommand().equals(ACTION_CLOSE_ALL_BUT_THIS_TAB)) {
			int index = _outputTabbedPane.getSelectedIndex();
			// delete tabs after this one
			for (int i = _outputTabbedPane.getTabCount() - 1; i > index; i--) {
				_outputTabbedPane.remove(i);
			}
			// delete all preceding tabs
			for (int i = 0; i < index; i++) {
				_outputTabbedPane.remove(0);
			}
		} else if (e.getActionCommand().equals(ACTION_SAVE_TAB)) {
			JFileChooser chooser;
			String workingDirectory = ConfigurationManager.getInstance().getWorkingDirectory();
			if (workingDirectory != null) {
				File currentWorkingDirectory = new File(workingDirectory);
				if (currentWorkingDirectory.exists()) {
					chooser = new JFileChooser(currentWorkingDirectory);
				} else {
					chooser = new JFileChooser();
				}
			} else {
				chooser = new JFileChooser();
			}

			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int action = chooser.showOpenDialog(new JFrame());
			if (action == JFileChooser.APPROVE_OPTION) {
				try {
					File file = new File(chooser.getSelectedFile().getCanonicalPath());
					if (file.exists()) {
						// prompt to overwrite
						int confirmation = JOptionPane.showConfirmDialog(this, OVERWRITE_EXISTING_FILE, CONFIRMATION, JOptionPane.OK_CANCEL_OPTION);
						if (confirmation == JOptionPane.OK_OPTION) {
							writeSelectedTabToFile(file);
						}
					} else {
						writeSelectedTabToFile(file);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * writes a selected tab's text to a given file
	 * @param file
	 * @throws IOException
	 */
	private void writeSelectedTabToFile(File file) throws IOException {
		Writer writer = new BufferedWriter(new FileWriter(file));
		JScrollPane scrollpane = (JScrollPane) _outputTabbedPane.getSelectedComponent();
		JTextPane textPane = (JTextPane) scrollpane.getViewport().getComponent(0);
		writer.write(textPane.getText());
		writer.flush();
		writer.close();
	}

    public JPanel getContent() {
        return content;
    }

}
