package com.alternatecomputing.jsvn.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.Cursor;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Description:  Component that limits the number of characters entered into a JTextField
 * and optionally converts all the text to UPPERCASE. The cursor is also
 * changed to reflect whether or not the component is editable.
 */
public class JLimitedTextField extends JTextField implements Serializable, Cloneable {

	private int maxLength;
	private boolean upperCase = false;
	private JTextFieldLimit doc;

	private final static int DEFAULT_MAX_LENGTH = 255;

	// needed to workaround a Mnemonic bug in JDK1.2.2
	private boolean pbLastKeyEventWasAlt = false;

	/**
	 * default Contructor that builds a regular JTextField
	 */
	public JLimitedTextField() {
		super();
		try {
			setDocument(doc = new JTextFieldLimit(DEFAULT_MAX_LENGTH));
			jbInit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * construtor that builds a JTextfield and sets the max num of chars.
	 * @param maxlength Max number of characters
	 */
	public JLimitedTextField(int maxlength) {
		super();
		try {
			setDocument(doc = new JTextFieldLimit(maxlength));
			setMaxLength(maxlength);
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * construtor that builds a JTextfield and sets the max num of chars and
	 * optionally converts the text to UPPERCASE
	 * @param maxlength Max number of characters
	 * @param makeUpperCase Converts to uppercase if true
	 */
	public JLimitedTextField(int maxlength, boolean makeUpperCase) {
		super();
		try {
			setDocument(new JTextFieldLimit(maxlength, makeUpperCase));
			setMaxLength(maxlength);
			setUpperCase(makeUpperCase);
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				this_mouseEntered(e);
			}

			public void mouseExited(MouseEvent e) {
				this_mouseExited(e);
			}
		});
	}

	void this_mouseEntered(MouseEvent e) {
		if (isEditable()) {
			setCursor(new Cursor(Cursor.TEXT_CURSOR));
		}
	}

	void this_mouseExited(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}

	/**
	 * sets the maxlength of the component
	 * @param newMaxLength Max num of characters to accept
	 */
	public void setMaxLength(int newMaxLength) {
		maxLength = newMaxLength;
		doc.setMaxLength(getMaxLength());
	}

	/**
	 * returns the maxlength of this component
	 * @return Max number of characters this component accepts
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * Sets whether or not the component should convert its text to
	 * UPPERCASE. Modified underlying document.
	 * @param newIsUpperCase
	 */
	public void setUpperCase(boolean newIsUpperCase) {
		upperCase = newIsUpperCase;
		doc.setIsUpperCase(isUpperCase());
	}

	/**
	 * retruens whether or not this component is UPPERCASE
	 * @return true is converting, false otherwise
	 */
	public boolean isUpperCase() {
		return upperCase;
	}

	public void setEnabled(boolean parm1) {
		// if false then set background to LightGray
		if (parm1 == false)
			setBackground(java.awt.Color.lightGray);

		super.setEnabled(parm1);
	}

	public void setEditable(boolean parm1) {
		// if false then set background to LightGray
		if (parm1 == false)
			setBackground(java.awt.Color.lightGray);

		super.setEditable(parm1);
	}

	/**
	 * returns a boolean indicating whether the text has changed
	 * @return True or false
	 */
	public boolean isDirty() {
		return doc.isDirty();
	}

	/**
	 * Method to reset the dirty flag to False
	 */
	public void resetDirtyFlag() {
		doc.setIsDirty(false);
	}

	// Overriden to fix Mnemonic bug in JDK1.2.2
	// This should NOT be needed in JDK1.3
	protected void processComponentKeyEvent(KeyEvent evt) {
		//TODO: override this javax.swing.text.JTextComponent method;
		super.processComponentKeyEvent(evt);

		// if the alt key is pressed, we don't want any input in the textfield
		// This is a workaround for the Mnemonic bug in JDK1.2.2
		// This should be fixed in JDK1.3
		pbLastKeyEventWasAlt = evt.isAltDown();

	}

	// This gets called when a NON-Special ket is typed which means
	// any keys beside ALT, ENTER, ESC, DELETE, etc. will be processed here
	protected void processInputMethodEvent(InputMethodEvent evt) {

		// if alt was pressed consume event
		if (pbLastKeyEventWasAlt)
			evt.consume();

		// tell the document it is dirty
		doc.setIsDirty(true);

		// process key event
		super.processInputMethodEvent(evt);

	}
}


class JTextFieldLimit extends PlainDocument {
	private int limit;

	// optional uppercase conversion
	private boolean toUppercase = false;

	// Dirty flag
	private boolean wasChanged = false;

	JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	JTextFieldLimit(int limit, boolean upper) {
		super();
		this.limit = limit;
		toUppercase = upper;
	}

	// overriden method of the PlainDocument class
	public void insertString
			(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		if (str == null) return;


		// if current text length + new string length < max limit
		if ((getLength() + str.length()) <= limit) {
			if (toUppercase)
				str = str.toUpperCase();

			super.insertString(offset, str, attr);
		}
	}

	public void setMaxLength(int maxLength) {
		limit = maxLength;
	}

	public int getMaxLength() {
		return limit;
	}

	public void setIsUpperCase(boolean b) {
		toUppercase = b;
	}

	public boolean getIsUppercase() {
		return toUppercase;
	}

	public boolean isDirty() {
		return wasChanged;
	}

	public void setIsDirty(boolean bIsDirty) {
		wasChanged = bIsDirty;
	}
}

