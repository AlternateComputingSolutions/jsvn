package com.alternatecomputing.jsvn.gui;

public class CopyDialog extends MoveDialog {

	/**
	 * constructor
	 * @param parent parent frame
	 * @param modal boolean indicating whether or not this dialog is modal
	 */
	public CopyDialog(Frame parent, boolean modal) {
		super(parent, modal);
	}

	/**
	 * gets the title of the dialog
	 * @return dialog title
	 */
	protected String getDialogTitle() {
		return "Copy";
	}

	/**
	 * gets the captions to be displayed in the options panel
	 * @return options panel caption
	 */
	protected String getDialogCaption() {
		return "Copy Options";	}
}
