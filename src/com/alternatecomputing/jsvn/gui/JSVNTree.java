package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.Add;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Diff;
import com.alternatecomputing.jsvn.command.Executable;
import com.alternatecomputing.jsvn.command.Info;
import com.alternatecomputing.jsvn.command.Resolve;
import com.alternatecomputing.jsvn.command.Revert;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.model.SVNTreeModel;
import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * graphical representation of a SVNTreeModel
 */
public class JSVNTree extends JTree implements ActionListener {
	private static final String CONFIRMATION = "Confirmation";
	private static final String RESOLVE_WARNING = "You are about to resolve the following items:\n\n";
	private static final String REVERT_WARNING = "You are about to revert the following items:\n\n";
	private static final String ACTION_ADD = "Add";
	private static final String ACTION_CHECKOUT = "Checkout";
	private static final String ACTION_COMMIT = "Commit";
	private static final String ACTION_COPY = "Copy";
	private static final String ACTION_DELETE = "Delete";
	private static final String ACTION_DIFF = "Diff";
	private static final String ACTION_INFO = "Info";
	private static final String ACTION_IMPORT = "Import";
	private static final String ACTION_LOG = "Log";
	private static final String ACTION_MERGE = "Merge";
	private static final String ACTION_MKDIR = "Mkdir";
	private static final String ACTION_MOVE = "Move";
	private static final String ACTION_PROPERTY = "Property";
	private static final String ACTION_PROPDEL = "PropDel";
	private static final String ACTION_PROPEDIT = "PropEdit";
	private static final String ACTION_PROPGET = "PropGet";
	private static final String ACTION_PROPLIST = "PropList";
	private static final String ACTION_PROPSET = "PropSet";
	private static final String ACTION_REFRESH_RECURSIVELY = "Refresh Recursively";
	private static final String ACTION_REFRESH_ONLINE = "Online";
	private static final String ACTION_REFRESH_OFFLINE = "Local";
	private static final String ACTION_RESOLVE = "Resolve";
	private static final String ACTION_REVERT = "Revert";
	private static final String ACTION_STATUS = "Status";
	private static final String ACTION_SWITCH = "Switch";
	private static final String ACTION_UPDATE = "Update";
	private JPopupMenu mainPopup;
	private JMenuItem mi;

	public JSVNTree(SVNTreeModel root) {
		this.setModel(new DefaultTreeModel(buildTreeNode(root).getRoot()));
		// define the mainPopup
		mainPopup = new JPopupMenu();
		mi = new JMenuItem(ACTION_ADD);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_ADD);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_CHECKOUT);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_CHECKOUT);
		mi.setEnabled(true);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_COMMIT);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_COMMIT);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_COPY);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_COPY);
		mi.setEnabled(true);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_DELETE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_DELETE);
		mi.setEnabled(false);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_DIFF);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_DIFF);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_IMPORT);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_IMPORT);
		mi.setEnabled(false);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_MERGE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_MERGE);
		mi.setEnabled(false);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_MKDIR);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_MKDIR);
		mi.setEnabled(false);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_MOVE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_MOVE);
		mi.setEnabled(true);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_RESOLVE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_RESOLVE);
		mi.setEnabled(true);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_REVERT);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_REVERT);
		mi.setEnabled(true);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_SWITCH);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_SWITCH);
		mi.setEnabled(false);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_UPDATE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_UPDATE);
		mi.setEnabled(true);
		mainPopup.add(mi);

		mainPopup.add(new JPopupMenu.Separator());
		mi = new JMenuItem(ACTION_INFO);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_INFO);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_LOG);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_LOG);
		mainPopup.add(mi);
		mi = new JMenuItem(ACTION_STATUS);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_STATUS);
		mainPopup.add(mi);

		// define property menu
		mainPopup.add(new JPopupMenu.Separator());
		JMenu propertyMenu = new JMenu(ACTION_PROPERTY);
		mi = new JMenuItem(ACTION_PROPLIST);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_PROPLIST);
		mi.setEnabled(false);
		propertyMenu.add(mi);
		mi = new JMenuItem(ACTION_PROPGET);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_PROPGET);
		mi.setEnabled(false);
		propertyMenu.add(mi);
		mi = new JMenuItem(ACTION_PROPSET);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_PROPSET);
		mi.setEnabled(false);
		propertyMenu.add(mi);
		mi = new JMenuItem(ACTION_PROPEDIT);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_PROPEDIT);
		mi.setEnabled(false);
		propertyMenu.add(mi);
		mi = new JMenuItem(ACTION_PROPDEL);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_PROPDEL);
		mi.setEnabled(false);
		propertyMenu.add(mi);
		mainPopup.add(propertyMenu);

		mainPopup.add(new JPopupMenu.Separator());
		JMenu refreshMenu = new JMenu(ACTION_REFRESH_RECURSIVELY);
		mi = new JMenuItem(ACTION_REFRESH_OFFLINE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_REFRESH_OFFLINE);
		refreshMenu.add(mi);
		mi = new JMenuItem(ACTION_REFRESH_ONLINE);
		mi.addActionListener(this);
		mi.setActionCommand(ACTION_REFRESH_ONLINE);
		refreshMenu.add(mi);
		mainPopup.add(refreshMenu);

		mainPopup.setOpaque(true);
		mainPopup.setLightWeightPopupEnabled(true);

		addMouseListener(
				new MouseAdapter() {
					private void popup(MouseEvent e) {
						if (e.isPopupTrigger() && (getSelectedTargetsAsString() != null))
							mainPopup.show((JComponent) e.getSource(), e.getX(), e.getY());
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
		this.setCellRenderer(new JSVNTreeCellRenderer());
	}

	/**
	 * returns a default mutable tree representation of a given working copy model
	 * @param svnTreeModel model of an svn working copy
	 * @return root DefaultMutableTreeNode of the given model
	 */
	public static DefaultMutableTreeNode buildTreeNode(SVNTreeModel svnTreeModel) {
		return buildTreeNode(svnTreeModel.getRoot());
	}

	/**
	 * returns a default mutable tree representation of a given working copy node
	 * @param svnTreeNode mode of an svn working copy model
	 * @return root DefaultMutableTreeNode of the given model
	 */
	public static DefaultMutableTreeNode buildTreeNode(SVNTreeNodeData svnTreeNode) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(svnTreeNode);
		for (Iterator iterator = svnTreeNode.getChildren().iterator(); iterator.hasNext();) {
			SVNTreeNodeData svnTreeNodeData = (SVNTreeNodeData) iterator.next();
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
			newNode.setUserObject(svnTreeNodeData);
			DefaultMutableTreeNode newChild = buildTreeNode(svnTreeNodeData);
			if (newChild != null) {
				rootNode.add(newChild);
			}
		}
		return rootNode;
	}

	/**
	 *
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		String targets = getSelectedTargetsAsString();
		if (targets == null) {
			// user hasn't selected anything -- pop-up a error message
		} else {
			Executable executor =  (Executable) this.getTopLevelAncestor();
			if (ae.getActionCommand().equals(ACTION_ADD)) {
				// process add request
				Command command = new Add();
				Map args = new HashMap();
				args.put(Diff.TARGETS, targets);
				try {
					executor.executeCommand(command, args);
				} catch (CommandException e) {
					e.printStackTrace();
				}
				// process recursive offline refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_CHECKOUT)) {
				// process commit request
				CheckoutDialog dialog = new CheckoutDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
			} else if (ae.getActionCommand().equals(ACTION_COMMIT)) {
				// process commit request
				CommitDialog dialog = new CommitDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
				// process offline recursive refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_COPY)) {
				// process status request
				CopyDialog dialog = new CopyDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_DIFF)) {
				// process diff request
				DiffDialog dialog = new DiffDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
			} else if (ae.getActionCommand().equals(ACTION_INFO)) {
				// process info request
				Command command = new Info();
				Map args = new HashMap();
				args.put(Info.TARGETS, targets);
				try {
					executor.executeCommand(command, args);
				} catch (CommandException e) {
					e.printStackTrace();
				}
			} else if (ae.getActionCommand().equals(ACTION_LOG)) {
				// process log request
				LogDialog dialog = new LogDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
			} else if (ae.getActionCommand().equals(ACTION_MOVE)) {
				// process status request
				MoveDialog dialog = new MoveDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_REFRESH_ONLINE)) {
				// process recursive online refresh request
				refreshSelectedTargets(true);
			} else if (ae.getActionCommand().equals(ACTION_REFRESH_OFFLINE)) {
				// process recursive offline refresh request
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_RESOLVE)) {
				String message = RESOLVE_WARNING;
				String[] targetArray = getSelectedTargetsAsArray();
				for (int i = 0; i < targetArray.length; i++) {
					String s = targetArray[i];
					message += s + "\n";
				}
				int confirmation = JOptionPane.showConfirmDialog(this, message, CONFIRMATION,JOptionPane.OK_CANCEL_OPTION);
                if (confirmation == JOptionPane.OK_OPTION) {
					// process add request
					Command command = new Resolve();
					Map args = new HashMap();
					args.put(Diff.TARGETS, targets);
					try {
						executor.executeCommand(command, args);
					} catch (CommandException e) {
						e.printStackTrace();
					}
					// process recursive offline refresh request so new working copy status can be displayed
					refreshSelectedTargets(false);
				}
			} else if (ae.getActionCommand().equals(ACTION_REVERT)) {
				String message = REVERT_WARNING;
				String[] targetArray = getSelectedTargetsAsArray();
				for (int i = 0; i < targetArray.length; i++) {
					String s = targetArray[i];
					message += s + "\n";
				}
				int confirmation = JOptionPane.showConfirmDialog(this, message, CONFIRMATION,JOptionPane.OK_CANCEL_OPTION);
                if (confirmation == JOptionPane.OK_OPTION) {
					// process add request
					Command command = new Revert();
					Map args = new HashMap();
					args.put(Diff.TARGETS, targets);
					try {
						executor.executeCommand(command, args);
					} catch (CommandException e) {
						e.printStackTrace();
					}
					// process recursive offline refresh request so new working copy status can be displayed
					refreshSelectedTargets(false);
				}
			} else if (ae.getActionCommand().equals(ACTION_STATUS)) {
				// process status request
				StatusDialog dialog = new StatusDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
			} else if (ae.getActionCommand().equals(ACTION_UPDATE)) {
				// process update request
				UpdateDialog dialog = new UpdateDialog(Application.getApplicationFrame(), true);
				dialog.setTargets(targets);
				dialog.setExecutor(executor);
				dialog.setVisible(true);
				// process offline recursive refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			}
		}
	}

	/**
	 * returns a string of all selected targets separated by spaces, or null if nothing is selected
	 * @return space-separated string of targets
	 */
	private String getSelectedTargetsAsString() {
		TreePath[] paths = this.getSelectionPaths();
		if (paths == null) {
			return null;
		}

		StringBuffer targets = new StringBuffer();
		for (int i = 0; i < paths.length; i++) {
			TreePath path = paths[i];
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
			SVNTreeNodeData svnNode = (SVNTreeNodeData) dmtn.getUserObject();
			targets.append(ConfigurationManager.getInstance().getWorkingDirectory());
			targets.append(svnNode.getPath()).append(" ");
		}
		return targets.toString().trim();
	}

	/**
	 * returns a array of strings of all selected targets, or null if nothing is selected
	 * @return array of string of targets
	 */
	private String[] getSelectedTargetsAsArray() {
		TreePath[] paths = this.getSelectionPaths();
		if (paths == null) {
			return null;
		}

		String targets[] = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			TreePath path = paths[i];
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
			SVNTreeNodeData svnNode = (SVNTreeNodeData) dmtn.getUserObject();
			targets[i] = (ConfigurationManager.getInstance().getWorkingDirectory()) + svnNode.getPath();
		}
		return targets;
	}

	/**
	 * refreshes the tree
	 * @param online indicator as to whether an online refresh should be performed
	 */
	public void refresh(boolean online) {
		refresh((DefaultMutableTreeNode) this.getModel().getRoot(), online);
	}

	/**
	 * recursively refreshes all selected targets
	 */
	private void refreshSelectedTargets(boolean online) {
		TreePath[] paths = this.getSelectionPaths();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
				refresh(dmtn, online);
			}
		}
	}

	/**
	 * recursively refreshes a given node within the tree
	 * @param node node at which to begin the resursive refresh
	 * @param online
	 */
	private void refresh(DefaultMutableTreeNode node, boolean online) {
		SVNTreeNodeData nodeData = (SVNTreeNodeData) node.getUserObject();
		// if a userObject doesn't exist for the given node, then just return
		if (nodeData == null) {
			return;
		}
		String workingCopy = ConfigurationManager.getInstance().getWorkingDirectory() + nodeData.getPath();
		SVNTreeModel partialModel = new SVNTreeModel(workingCopy, online);
		DefaultMutableTreeNode foo = buildTreeNode(partialModel);
		// reconcile updated tree with current tree
		reconcileNodes(node, foo);
	}

	/**
	 * Performs a "live-update" to the original DefaultMutableTreeNode by reconsiling the differences between it and
	 * a given updated DefaultMutableTreeNode.  The resulting original DefaultMutableTreeNode will look identical to
	 * the updated node.  Appropriate events will fire to keep the GUI consistant with any changes that were made to the
	 * model.
	 * @param original the model's existing node
	 * @param updated the node representing the model node's new state
	 */
	private void reconcileNodes(DefaultMutableTreeNode original, DefaultMutableTreeNode updated) {
		SVNTreeNodeData originalData = (SVNTreeNodeData) original.getUserObject();
		SVNTreeNodeData updatedData = (SVNTreeNodeData) updated.getUserObject();
		if (!originalData.equals(updatedData)) {
			originalData.copyNodeValues(updatedData);
		} else {
			int originalChildCount = original.getChildCount();
			int insertionPoint = 0;

			// loop through the original node's children
			for (int i = insertionPoint; i < originalChildCount; i++) {
				// loop through remaining updated node's children
				boolean found = false;
				DefaultMutableTreeNode originalChildNode = (DefaultMutableTreeNode) original.getChildAt(insertionPoint);
				SVNTreeNodeData originalChildData = (SVNTreeNodeData) originalChildNode.getUserObject();
				while (updated.children().hasMoreElements()) {
					DefaultMutableTreeNode updatedChildNode = (DefaultMutableTreeNode) updated.children().nextElement();
					SVNTreeNodeData updatedChildData = (SVNTreeNodeData) updatedChildNode.getUserObject();
					if (originalChildData.getPath().equals(updatedChildData.getPath())) {
						// same resource, check equality
						insertionPoint++;
						found = true;
						// remove from updated list so we know we handled this node
						updated.remove(updatedChildNode);
						if (!originalChildData.equals(updatedChildData)) {
							// same resource, different values -- update
							originalChildData.copyNodeValues(updatedChildData);
							reconcileNodes(originalChildNode, updatedChildNode);
							((DefaultTreeModel) this.getModel()).nodeChanged(originalChildNode);
							break;
						} else {
							// same values, do nothing
							reconcileNodes(originalChildNode, updatedChildNode);
							break;
						}
					} else {
						// different resources
						// does updated node's child need to be inserted before original node's child?
						if (updatedChildData.getPath().compareTo(originalChildData.getPath()) < 0) {
							original.insert(updatedChildNode, insertionPoint);
							((DefaultTreeModel) this.getModel()).nodesWereInserted(original, new int[]{insertionPoint});
							insertionPoint++;
							// inserting the node into the original has the side-effect of removing it from the updated
							// node, just keep going until we've handled all children, or until the remaining children
							// come after the last original node
						} else {
							break;
						}
					}
				}
				if (!found) {
					original.remove(insertionPoint);
					((DefaultTreeModel) this.getModel()).nodesWereRemoved(original, new int[]{insertionPoint}, new Object[]{originalChildNode});
				}
			}
			// add any remaining new child nodes
			while (updated.children().hasMoreElements()) {
				DefaultMutableTreeNode updatedChildNode = (DefaultMutableTreeNode) updated.children().nextElement();
				original.add(updatedChildNode);
				// adding the new node to the original has the side-effect of removing it from the updated node's children
				// so we just keep going until no children remain
				((DefaultTreeModel) this.getModel()).nodesWereInserted(original, new int[]{insertionPoint++});
			}
		}
	}
}
