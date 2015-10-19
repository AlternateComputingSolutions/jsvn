package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * abstract class that all Command dialogs should extend to maintain a consistant look-and-feel
 */
public abstract class CommandDialog extends CenterableDialog implements JSVNEventListener, ActionListener {
	private static final String OK_TEXT = "OK";
	private static final char OK_MNEMONIC = 'O';
	private static final String CANCEL_TEXT = "Cancel";
	private static final char CANCEL_MNEMONIC = 'C';
	private String _targets;
	private JPanel _optionsPanel;
	private JPanel _buttonPanel = new JPanel();
	private JButton _okButton = new JButton();
	private JButton _cancelButton = new JButton();
	private FlowLayout _buttonPaneLayout = new FlowLayout(FlowLayout.RIGHT);
	CenterableDialog _thisDialog;

	/**
	 * constructor
	 * @param parent parent frame
	 * @param modal boolean indicating whether or not this dialog is modal
	 */
	public CommandDialog(Frame parent, boolean modal) {
		super(parent, modal);
		initGUI();
		pack();
	}

	/**
	 * determines whether the selected options are valid before the command is allowed to execute
	 * @return options validity indicator
	 */
	protected abstract boolean isValidOptions();

	/**
	 * gets the title of the dialog
	 * @return dialog title
	 */
	protected abstract String getDialogTitle();

	/**
	 * gets the captions to be displayed in the options panel
	 * @return options panel caption
	 */
	protected abstract String getDialogCaption();

    /**
	 * creates the appropriate command to run and populated the given Map with appropriate arguments to configure the command
	 * @param args map of arguments to be passed into the returned command
	 * @return implementation of Command to execute with the given configured args
	 */
	protected abstract Command buildCommand(Map args);

	/**
	 * gets the panel in which all options for the command are configured
	 * @return options panel
	 */
	protected abstract JPanel getOptionsPanel();

	/**
	 * executes the desired command
	 * @throws CommandException
	 */
	protected void runCommand() throws CommandException {
		Map args = new HashMap();
		Command command = buildCommand(args);

		// create the executor
		JSVNCommandExecutor executor = new JSVNCommandExecutor(command, args);

		// add interested listeners
		executor.addJSVNEventListener(this);
		executor.addJSVNEventListener(Application.getApplicationFrame());

		// invoke the executor in a separate thread
		Thread t = new Thread(executor);
		t.start();
	}

	/**
	 * post command-execution hook for use by dialog
	 * @param success command success indicator
	 */
	public void postExecute(boolean success) {
		if (success) {
			this.setVisible(false);
			this.dispose();
		}
	}
	/**
	 * gets the targets upon which the command should run
	 * @return targets
	 */
	protected String getTargets() {
		return _targets;
	}

	/**
	 * sets the targets upon which the command should run
	 * @param targets
	 */
	public void setTargets(String targets) {
		_targets = targets;
	}

	/**
	 * this method is called from within the constructor to initialize the dialog
	 */
	private void initGUI() {
		_thisDialog = this;
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						closeDialog(evt);
					}
				});

		getContentPane().setLayout(new BorderLayout());

		// set up the command options panel
		_optionsPanel = getOptionsPanel();
		_optionsPanel.setBorder(new javax.swing.border.TitledBorder(getDialogCaption()));
		getContentPane().add(_optionsPanel, BorderLayout.CENTER);

		// set up the buttons
		_buttonPanel.setLayout(_buttonPaneLayout);
		_okButton.setText(OK_TEXT);
		_okButton.setMnemonic(OK_MNEMONIC);
		_okButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (isValidOptions()) {
							try {
								runCommand();
							} catch (CommandException ce) {
								JOptionPane.showMessageDialog(_thisDialog.getContentPane(), ce.getMessage());
							}
						}
					}
				});
		_buttonPanel.add(_okButton);
		_cancelButton.setText(CANCEL_TEXT);
		_cancelButton.setMnemonic(CANCEL_MNEMONIC);
		_cancelButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
		_buttonPanel.add(_cancelButton);
		getContentPane().add(_buttonPanel, BorderLayout.SOUTH);

		setTitle(getDialogTitle());
		setResizable(true);
		getRootPane().setDefaultButton(_okButton);
	}

	/**
	 * closes the dialog
	 * @param evt
	 */
	private void closeDialog(WindowEvent evt) {
		setVisible(false);
		dispose();
	}

	/**
	 * handles a JSVN user interface event
	 * @param event UI event to handle
	 */
	public void processJSVNEvent(JSVNUIEvent event) {
		// handle status events
		if (event instanceof JSVNStatusEvent) {

			// set the cursor appropriately
			if (((JSVNStatusEvent) event).getStatus() == JSVNStatusEvent.EXECUTING) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			} else if (((JSVNStatusEvent) event).getStatus() == JSVNStatusEvent.READY) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else {
				// XXX - log error
			}
			return;
		}

		// handle command events
		if (event instanceof JSVNCommandEvent) {
			String error = ((JSVNCommandEvent) event).getError();

			// inform postExecute() of the command's success
			if (error != null) {
				postExecute(false);
			} else {
				postExecute(true);
			}
		}
	}
}
