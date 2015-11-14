package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.command.*;
import com.alternatecomputing.jsvn.configuration.Configuration;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.configuration.DefaultConfiguration;
import com.alternatecomputing.jsvn.model.SVNTreeModel;
import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
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
import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * main GUI for JSVN
 */
public class Frame extends CenterableFrame implements JSVNEventListener, ActionListener {
	private static final char MNEMONIC_CHECKOUT = 'C';
    private static final char MNEMONIC_WORKING_COPY = 'W';
    private static final char MNEMONIC_SET_WORKING_COPY = 'S';
    private static final char MNEMONIC_REFRESH_WORKING_COPY = 'R';
    private static final char MNEMONIC_REPO = 'R';
    private static final char MNEMONIC_REPO_BROWSE = 'B';
	private static final char MNEMONIC_HELP = 'H';
	private static final char MNEMONIC_ABOUT = 'A';
	private static final char MNEMONIC_EXIT = 'E';
	private static final char MNEMONIC_FILE = 'F';
	private static final String CONFIRMATION = "Confirmation";
	private static final String OVERWRITE_EXISTING_FILE = "Overwrite existing file?";
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
	private static final String MENU_ITEM_REPO = "Repository";
	private static final String MENU_ITEM_REPO_BROWSE = "Browse";
	private static final String MENU_ITEM_HELP = "Help";
	private static final String MENU_ITEM_ABOUT = "About";
	private static final String MENU_ITEM_CLOSE = "Close";
	private static final String MENU_ITEM_CLOSE_ALL = "Close All";
	private static final String MENU_ITEM_CLOSE_ALL_BUT_THIS = "Close All But This";
	private static final String MENU_ITEM_SAVE = "Save To File";
	private static final String STATUS_READY = "Ready";
	private static final String STATUS_EXECUTING = "Executing...";
	private static final String STATUS_REFRESHING = "Refreshing...";
	private static final String TAB_COMMAND_HISTORY = "Command History";
	private static final String TAB_DIRECTORY_BROWSER = "Directory Browser";
	private static final int DIRECTORY_BROWSER_TABLE_INDEX = 0;
	private JPanel content;
	private JLabel _statusBar = new JLabel(STATUS_READY);
	private JTabbedPane _outputTabbedPane = new JTabbedPane();
	private JTextPane _historyTextPane = new JTextPane();
	private JSVNTree _svnTree;
	private JPopupMenu _popupMenu;
	private JMenuItem _miCloseTab, _miCloseAllTab, _miCloseAllButThisTab, _miSaveTab;
	private JTable _directoryBrowserTable;
	private DirectoryBrowserTableModel _directoryBrowserTableModel;


	/**
	 * constructor
	 */
	public Frame() {
		initGUI();
		pack();
	}

	/**
	 * this method is called from within the constructor to initialize the form
	 */
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
						String workingDirectory = ConfigurationManager.getInstance().getConfig().getWorkingDirectory();
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

						setWorkingCopy(ConfigurationManager.getInstance().getConfig().getWorkingCopy());
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


        // create About menu item
		JMenu menuRepo = new JMenu(MENU_ITEM_REPO);
		menuHelp.setMnemonic(MNEMONIC_REPO);
		JMenuItem repoBrowse = new JMenuItem(MENU_ITEM_REPO_BROWSE);
        repoBrowse.setMnemonic(MNEMONIC_REPO_BROWSE);
        repoBrowse.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Browse browse = new Browse();
                        HashMap args = new HashMap();
                        browse.setUrl("^/");
                        args.put(Browse.URL, "^/");
                        args.put(Browse.STYLE, "xml");
                        JSVNCommandExecutor executor = new JSVNCommandExecutor(browse, args);

                        // add interested listeners
                        executor.addJSVNEventListener(Application.getApplicationFrame());

                        // invoke the executor in a separate thread
                        Thread t = new Thread(executor);
                        t.start();
                    }
                });
        menuRepo.add(repoBrowse);

		menuFile.add(fileExit);
		menuBar.add(menuFile);
		menuBar.add(menuWorkingCopy);
		menuBar.add(menuRepo);
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
		SVNTreeModel model = new SVNTreeModel(ConfigurationManager.getInstance().getConfig().getWorkingCopy(), false);
		_svnTree = new JSVNTree(model);
		_svnTree.addTreeSelectionListener(new WorkingCopySelectionListener());
		JScrollPane svnPane = new JScrollPane();
		svnPane.setViewportView(_svnTree);
		mainSplitPane.add(svnPane, JSplitPane.LEFT);

		_directoryBrowserTableModel = new DirectoryBrowserTableModel();
		_directoryBrowserTable = new JTable(_directoryBrowserTableModel);
		DirectoryBrowserCellRenderer renderer = new DirectoryBrowserCellRenderer();
		for (int i = 0; i < _directoryBrowserTable.getColumnModel().getColumnCount(); i++) {
			TableColumn column = _directoryBrowserTable.getColumnModel().getColumn(i);
			column.setCellRenderer(renderer);
		}
		_outputTabbedPane.add(new JScrollPane(_directoryBrowserTable), TAB_DIRECTORY_BROWSER);


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
								if (tabCount > 1) {
									_miCloseAllTab.setEnabled(true);
									_miCloseAllButThisTab.setEnabled(true);
								} else {
									_miCloseAllTab.setEnabled(false);
									_miCloseAllButThisTab.setEnabled(false);
								}
								_popupMenu.show((JComponent) e.getSource(), e.getX(), e.getY());
							}
							if (_outputTabbedPane.getSelectedIndex() == DIRECTORY_BROWSER_TABLE_INDEX) {
								_miSaveTab.setEnabled(false);
								_miCloseTab.setEnabled(false);
							} else {
								_miSaveTab.setEnabled(true);
								_miCloseTab.setEnabled(true);
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

	/**
	 * sets the working copy and updates UI with the new tree model
	 * @param workingCopy
	 */
	public void setWorkingCopy(String workingCopy) {
		SVNTreeModel svnTreeModel = new SVNTreeModel(workingCopy, false);
		_svnTree.setModel(new DefaultTreeModel(JSVNTree.buildTreeNode(svnTreeModel)));
	}

	/**
	 * adds a highlighter to the given editor pane (useful only for Diff output)
	 * @param newPane pane to be highlighted
	 * @param addition character signifying an addition
	 * @param removal character signifying a removal
	 */
	private void addHighlightersToTextPane(JEditorPane newPane, char addition, char removal) {
		Highlighter.HighlightPainter addPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(196, 255, 196));
		Highlighter.HighlightPainter removePainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(196, 196, 255));
		Document doc = newPane.getDocument();
		try {
			String body = doc.getText(0, doc.getLength());
			int startPos = body.indexOf(addition);
			int endPos = 0;

			while (startPos != -1) {
				endPos = body.indexOf(Constants.NEWLINE_CHAR, startPos);
				if (body.charAt(startPos - 1) == Constants.NEWLINE_CHAR) {
					newPane.getHighlighter().addHighlight(startPos, endPos, addPainter);
				}
				startPos = body.indexOf(addition, endPos);
			}
			startPos = body.indexOf(removal);

			while (startPos != -1) {
				endPos = body.indexOf(Constants.NEWLINE_CHAR, startPos);
				int pos = (startPos > 0) ? startPos - 1 : 0;
				if (body.charAt(pos) == Constants.NEWLINE_CHAR || pos == 0) {
					newPane.getHighlighter().addHighlight(startPos, endPos, removePainter);
				}
				startPos = body.indexOf(removal, endPos);
			}
		} catch (BadLocationException e) {
			// XXX - log error
			e.printStackTrace();
		}
	}

	/**
	 * exit the Application
	 */
	private void exitForm(WindowEvent evt) {
		System.exit(0);
	}

	/**
	 * invoked when an action occurs.
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_CLOSE_TAB)) {
			_outputTabbedPane.remove(_outputTabbedPane.getSelectedIndex());
		} else if (e.getActionCommand().equals(ACTION_CLOSE_ALL_TAB)) {
			for (int i = _outputTabbedPane.getTabCount(); i > 1; i--) {
				_outputTabbedPane.remove(i - 1);
			}
		} else if (e.getActionCommand().equals(ACTION_CLOSE_ALL_BUT_THIS_TAB)) {
			int index = _outputTabbedPane.getSelectedIndex();
			// delete tabs after this one
			for (int i = _outputTabbedPane.getTabCount() - 1; i > index; i--) {
				_outputTabbedPane.remove(i);
			}
			// delete all preceding tabs
			for (int i = 1; i < index; i++) {
				_outputTabbedPane.remove(1);
			}
		} else if (e.getActionCommand().equals(ACTION_SAVE_TAB)) {
			JFileChooser chooser;
			String workingDirectory = ConfigurationManager.getInstance().getConfig().getWorkingDirectory();
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

	/**
	 * returns the content panel
	 * @return
	 */
	public JPanel getContent() {
		return content;
	}

	/**
	 * handles a JSVN user interface event
	 * @param event UI event to handle
	 */
	public void processJSVNEvent(JSVNUIEvent event) {
		// handle status events
		if (event instanceof JSVNStatusEvent) {
			if (((JSVNStatusEvent) event).getStatus() == JSVNStatusEvent.EXECUTING) {
				_statusBar.setText(STATUS_EXECUTING);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			} else if (((JSVNStatusEvent) event).getStatus() == JSVNStatusEvent.READY) {
				_statusBar.setText(STATUS_READY);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else if (((JSVNStatusEvent) event).getStatus() == JSVNStatusEvent.REFRESHING) {
				_statusBar.setText(STATUS_REFRESHING);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			} else {
				// XXX - log error
			}
			return;
		}

		// handle command events
		if (event instanceof JSVNCommandEvent) {
			JSVNCommandEvent jsvnCommandEvent = (JSVNCommandEvent) event;
			String error = jsvnCommandEvent.getError();
			if (error != null) {
				JOptionPane.showMessageDialog(Application.getApplicationFrame().getContentPane(), error);
			} else {
				// create a new output tab
				Commandable command = jsvnCommandEvent.getCommand();
				_historyTextPane.setText(_historyTextPane.getText() + command.getCommand() + Constants.NEWLINE);
				JComponent component;
				if(command instanceof ComponentCommandable) {
					ComponentCommandable componentCommandable = (ComponentCommandable) command;
					component = componentCommandable.getComponent();
				} else {
					JTextPane pane = new JTextPane();
					Font fixedFont = new Font("Monospaced", Font.PLAIN, 10);
					pane.setFont(fixedFont);
					String result = jsvnCommandEvent.getResult();
					pane.setText(result);
					pane.setEditable(false);
					component = pane;
				}

				// only highlight diff output
				if (command instanceof Diff) {
					JEditorPane editorPane = (JEditorPane) component;
					if (command.getArgs().get(Diff.EXTENSIONS) != null) {
						addHighlightersToTextPane(editorPane, '>', '<');
					} else {
						addHighlightersToTextPane(editorPane, '+', '-');
					}
				}

				JScrollPane scrollPane = new JScrollPane(component);
				String commandName = command.getClass().getName();
				_outputTabbedPane.add(commandName.substring(commandName.lastIndexOf(Constants.PERIOD) + 1), scrollPane);
				_outputTabbedPane.setSelectedIndex(_outputTabbedPane.getTabCount() - 1);

				// refresh the model only for commans that may have changed the contents of the working copy
				if (command instanceof WorkingCopyModifiable) {
					_svnTree.refresh(false);
				}
			}
		}
	}

	/**
	 *
	 * @param node
	 * @return
	 */
	private String buildFileName(DefaultMutableTreeNode node) {
		StringTokenizer token = new StringTokenizer(node.toString(), " ");
		String name = token.nextToken();

		switch (node.getLevel()) {
			case 0:
				return ConfigurationManager.getInstance().getConfig().getWorkingCopy();
			case 1:
				return ConfigurationManager.getInstance().getConfig().getWorkingCopy() + Constants.SVN_PATH_SEPARATOR + name;
			default:
				return buildFileName((DefaultMutableTreeNode) node.getParent()) + Constants.SVN_PATH_SEPARATOR + name;
		}
	}

	/**
	 *
	 * @return
	 */
	public String getWorkingCopy() {
		return ConfigurationManager.getInstance().getConfig().getWorkingCopy();
	}


	/**
	 *
	 */
	private class WorkingCopySelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) _svnTree.getLastSelectedPathComponent();

			if (node == null) {
				return;
			}

			SVNTreeNodeData svnNode = (SVNTreeNodeData) node.getUserObject();
			String fileName = buildFileName(node);
			File file = new File(fileName);
			if (_directoryBrowserTableModel != null) {
				if (file.isDirectory()) {
					_directoryBrowserTableModel.setPathToBrowse(svnNode, file);
				} else {
					_directoryBrowserTableModel.clearPathToBrowse();
				}
			}
		}
	}
}
