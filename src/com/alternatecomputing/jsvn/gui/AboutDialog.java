package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.Constants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AboutDialog extends CenterableDialog {

	private final String TITLE = "About";
	private final String CAPTION = "Java Subversion Client\nhttp://jsvn.alternatecomputing.com/";
	private JPanel contentPane = new JPanel();
	private JLabel prodLabel = new JLabel();
	private JLabel verLabel = new JLabel();
	private JLabel copLabel = new JLabel();
	private JTextArea commentField = new JTextArea();
	private JPanel btnPanel = new JPanel();
	private JButton okButton = new JButton();
	private JLabel image = new JLabel();
	private BorderLayout formLayout = new BorderLayout();
	private GridBagLayout contentPaneLayout = new GridBagLayout();
	private FlowLayout btnPaneLayout = new FlowLayout();

	/** creates new About Dialog */
	public AboutDialog(Frame parent, String title, boolean modal) {
		super(parent, title, modal);
		initGUI();
		pack();
	}

	public AboutDialog(Frame parent, boolean modal) {
		this(parent, "", modal);
	}

	public AboutDialog() {
		super();
		setModal(true);
		initGUI();
		pack();
	}

	/** this method is called from within the constructor to initialize the dialog. */
	private void initGUI() {
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						closeDialog(evt);
					}
				});

		getContentPane().setLayout(formLayout);
		contentPane.setLayout(contentPaneLayout);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		prodLabel.setText(Constants.PRODUCT_NAME);
		contentPane.add(prodLabel, new GridBagConstraints(GridBagConstraints.RELATIVE,
														  GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
														  GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
														  new Insets(5, 0, 0, 0), 0, 0));
		verLabel.setText(Constants.PRODUCT_VERSION);
		contentPane.add(verLabel, new GridBagConstraints(GridBagConstraints.RELATIVE,
														 GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
														 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
														 new Insets(5, 0, 0, 0), 0, 0));
		copLabel.setText(Constants.COPYRIGHT);
		contentPane.add(copLabel, new GridBagConstraints(GridBagConstraints.RELATIVE,
														 GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
														 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
														 new Insets(5, 0, 0, 0), 0, 0));

		commentField.setBackground(getBackground());
		commentField.setForeground(copLabel.getForeground());
		commentField.setFont(copLabel.getFont());

		commentField.setText(CAPTION);
		commentField.setEditable(false);

		contentPane.add(commentField, new GridBagConstraints(GridBagConstraints.RELATIVE,
															 GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 3, 0.0, 1.0,
															 GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
															 new Insets(5, 0, 0, 0), 0, 0));

		image.setText("[JSVN logo]");
		getContentPane().add(image, BorderLayout.WEST);

		getContentPane().add(contentPane, BorderLayout.CENTER);
		btnPanel.setLayout(btnPaneLayout);
		okButton.setText("OK");
		okButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
		btnPanel.add(okButton);
		getContentPane().add(btnPanel, BorderLayout.SOUTH);

		setTitle(TITLE);
		setResizable(false);
	}

	/** closes the dialog */
	private void closeDialog(WindowEvent evt) {
		setVisible(false);
		dispose();
	}
}
