package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.Add;
import com.alternatecomputing.jsvn.command.Cleanup;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Delete;
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
import javax.swing.ToolTipManager;
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
	private static final String CLEANUP_ERROR = "The following item is not a directory:\n\n";
	private static final String RESOLVE_WARNING = "You are about to resolve the following items:\n\n";
	private static final String REVERT_WARNING = "You are about to revert the following items:\n\n";
	private static final String ACTION_ADD = "Add";
	private static final String ACTION_CAT = "Cat";
	private static final String ACTION_CHECKOUT = "Checkout";
	private static final String ACTION_CLEANUP = "Cleanup";
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
	private static final String NEWLINE_CHARACTER = "\n";
    private Executable executor;
	private JPopupMenu mainPopup;
	private JMenuItem miAdd, miCat, miCheckout, miCleanup, miCommit, miDelete, miDiff, miImport, miCopy, miResolve, miMerge,
	miMkDir, miMove, miRevert, miSwitch, miUpdate, miInfo, miLog, miStatus, miPropList, miPropGet, miPropSet,
	miPropEdit, miPropDel, miRefreshOnline, miRefreshOffline;

	public JSVNTree(SVNTreeModel root, Executable executor ) {
		this.setModel(new DefaultTreeModel(buildTreeNode(root).getRoot()));
        this.executor = executor;
		// define the mainPopup
		mainPopup = new JPopupMenu();
		miAdd = new JMenuItem(ACTION_ADD);
		miAdd.addActionListener(this);
		miAdd.setActionCommand(ACTION_ADD);
		mainPopup.add(miAdd);
		miCat = new JMenuItem(ACTION_CAT);
		miCat.addActionListener(this);
		miCat.setActionCommand(ACTION_CAT);
		miCat.setEnabled(true);
		mainPopup.add(miCat);
		miCheckout = new JMenuItem(ACTION_CHECKOUT);
		miCheckout.addActionListener(this);
		miCheckout.setActionCommand(ACTION_CHECKOUT);
		miCheckout.setEnabled(true);
		mainPopup.add(miCheckout);
		miCleanup = new JMenuItem(ACTION_CLEANUP);
		miCleanup.addActionListener(this);
		miCleanup.setActionCommand(ACTION_CLEANUP);
		miCleanup.setEnabled(true);
		mainPopup.add(miCleanup);
		miCommit = new JMenuItem(ACTION_COMMIT);
		miCommit.addActionListener(this);
		miCommit.setActionCommand(ACTION_COMMIT);
		mainPopup.add(miCommit);
		miCopy = new JMenuItem(ACTION_COPY);
		miCopy.addActionListener(this);
		miCopy.setActionCommand(ACTION_COPY);
		miCopy.setEnabled(true);
		mainPopup.add(miCopy);
		miDelete = new JMenuItem(ACTION_DELETE);
		miDelete.addActionListener(this);
		miDelete.setActionCommand(ACTION_DELETE);
		miDelete.setEnabled(true);
		mainPopup.add(miDelete);
		miDiff = new JMenuItem(ACTION_DIFF);
		miDiff.addActionListener(this);
		miDiff.setActionCommand(ACTION_DIFF);
		mainPopup.add(miDiff);
		miImport = new JMenuItem(ACTION_IMPORT);
		miImport.addActionListener(this);
		miImport.setActionCommand(ACTION_IMPORT);
		miImport.setEnabled(false);
		mainPopup.add(miImport);
		miMerge = new JMenuItem(ACTION_MERGE);
		miMerge.addActionListener(this);
		miMerge.setActionCommand(ACTION_MERGE);
		miMerge.setEnabled(true);
		mainPopup.add(miMerge);
		miMkDir = new JMenuItem(ACTION_MKDIR);
		miMkDir.addActionListener(this);
		miMkDir.setActionCommand(ACTION_MKDIR);
		miMkDir.setEnabled(false);
		mainPopup.add(miMkDir);
		miMove = new JMenuItem(ACTION_MOVE);
		miMove.addActionListener(this);
		miMove.setActionCommand(ACTION_MOVE);
		miMove.setEnabled(true);
		mainPopup.add(miMove);
		miResolve = new JMenuItem(ACTION_RESOLVE);
		miResolve.addActionListener(this);
		miResolve.setActionCommand(ACTION_RESOLVE);
		miResolve.setEnabled(true);
		mainPopup.add(miResolve);
		miRevert = new JMenuItem(ACTION_REVERT);
		miRevert.addActionListener(this);
		miRevert.setActionCommand(ACTION_REVERT);
		miRevert.setEnabled(true);
		mainPopup.add(miRevert);
		miSwitch = new JMenuItem(ACTION_SWITCH);
		miSwitch.addActionListener(this);
		miSwitch.setActionCommand(ACTION_SWITCH);
		miSwitch.setEnabled(true);
		mainPopup.add(miSwitch);
		miUpdate = new JMenuItem(ACTION_UPDATE);
		miUpdate.addActionListener(this);
		miUpdate.setActionCommand(ACTION_UPDATE);
		miUpdate.setEnabled(true);
		mainPopup.add(miUpdate);

		mainPopup.add(new JPopupMenu.Separator());
		miInfo = new JMenuItem(ACTION_INFO);
		miInfo.addActionListener(this);
		miInfo.setActionCommand(ACTION_INFO);
		mainPopup.add(miInfo);
		miLog = new JMenuItem(ACTION_LOG);
		miLog.addActionListener(this);
		miLog.setActionCommand(ACTION_LOG);
		mainPopup.add(miLog);
		miStatus = new JMenuItem(ACTION_STATUS);
		miStatus.addActionListener(this);
		miStatus.setActionCommand(ACTION_STATUS);
		mainPopup.add(miStatus);

		// define property menu
		mainPopup.add(new JPopupMenu.Separator());
		JMenu propertyMenu = new JMenu(ACTION_PROPERTY);
		miPropList = new JMenuItem(ACTION_PROPLIST);
		miPropList.addActionListener(this);
		miPropList.setActionCommand(ACTION_PROPLIST);
		miPropList.setEnabled(false);
		propertyMenu.add(miPropList);
		miPropGet = new JMenuItem(ACTION_PROPGET);
		miPropGet.addActionListener(this);
		miPropGet.setActionCommand(ACTION_PROPGET);
		miPropGet.setEnabled(false);
		propertyMenu.add(miPropGet);
		miPropSet = new JMenuItem(ACTION_PROPSET);
		miPropSet.addActionListener(this);
		miPropSet.setActionCommand(ACTION_PROPSET);
		miPropSet.setEnabled(false);
		propertyMenu.add(miPropSet);
		miPropEdit = new JMenuItem(ACTION_PROPEDIT);
		miPropEdit.addActionListener(this);
		miPropEdit.setActionCommand(ACTION_PROPEDIT);
		miPropEdit.setEnabled(false);
		propertyMenu.add(miPropEdit);
		miPropDel = new JMenuItem(ACTION_PROPDEL);
		miPropDel.addActionListener(this);
		miPropDel.setActionCommand(ACTION_PROPDEL);
		miPropDel.setEnabled(false);
		propertyMenu.add(miPropDel);
		mainPopup.add(propertyMenu);

		mainPopup.add(new JPopupMenu.Separator());
		JMenu refreshMenu = new JMenu(ACTION_REFRESH_RECURSIVELY);
		miRefreshOffline = new JMenuItem(ACTION_REFRESH_OFFLINE);
		miRefreshOffline.addActionListener(this);
		miRefreshOffline.setActionCommand(ACTION_REFRESH_OFFLINE);
		refreshMenu.add(miRefreshOffline);
		miRefreshOnline = new JMenuItem(ACTION_REFRESH_ONLINE);
		miRefreshOnline.addActionListener(this);
		miRefreshOnline.setActionCommand(ACTION_REFRESH_ONLINE);
		refreshMenu.add(miRefreshOnline);
		mainPopup.add(refreshMenu);

		mainPopup.setOpaque(true);
		mainPopup.setLightWeightPopupEnabled(true);

		addMouseListener(
				new MouseAdapter() {
					private void popup(MouseEvent e) {
						if (e.isPopupTrigger() && (getSelectedTargetsAsString() != null)) {

							// These can't operate on multi selections
							if (getSelectedTargetsAsArray().length > 1) {
								miCat.setEnabled(false);
								miCopy.setEnabled(false);
								miMerge.setEnabled(false);
								miMove.setEnabled(false);
							} else {

								// Turn them back on
								miCat.setEnabled(true);
								miCopy.setEnabled(true);
								miMerge.setEnabled(true);
								miMove.setEnabled(true);
							}

							mainPopup.show((JComponent) e.getSource(), e.getX(), e.getY());
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

		// register the tree cell renderer
		setCellRenderer(new JSVNTreeCellRenderer());

		// register tree component with tooltip manager
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	/**
	 * returns a default mutable tree representation of a given working copy model
	 * @param svnTreeModel model of an svn working copy
	 * @return root DefaultMutableTreeNode of the given model
	 */
	public static DefaultMutableTreeNode buildTreeNode(SVNTreeModel svnTreeModel) {
		if (svnTreeModel.getRoot() == null) {
			return new DefaultMutableTreeNode();
		} else {
			return buildTreeNode(svnTreeModel.getRoot());
		}
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
			// Executable executor = (Executable) this.getTopLevelAncestor();
			if (ae.getActionCommand().equals(ACTION_ADD)) {
				// process add request
				Command command = new Add();
				Map args = new HashMap();
				args.put(Add.TARGETS, targets);
				executeDirectCommand(executor, command, args);
				// process recursive offline refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_CAT)) {
				// process cat request
				CommandDialog dialog = new CatDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
			} else if (ae.getActionCommand().equals(ACTION_CHECKOUT)) {
				// process checkout request
				CommandDialog dialog = new CheckoutDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
			} else if (ae.getActionCommand().equals(ACTION_CLEANUP)) {
				// check that all targets are directories
				TreePath[] paths = this.getSelectionPaths();
				for (int i = 0; i < paths.length; i++) {
					TreePath path = paths[i];
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) path.getLastPathComponent();
					SVNTreeNodeData svnNode = (SVNTreeNodeData) dmtn.getUserObject();
					if (svnNode.getNodeKind() != SVNTreeNodeData.NODE_KIND_DIRECTORY) {
						showMessageDialog(CLEANUP_ERROR + svnNode.getPath());
						return;
					}
				}
				// process cleanup request
				Command command = new Cleanup();
				Map args = new HashMap();
				args.put(Cleanup.TARGETS, targets);
				executeDirectCommand(executor, command, args);
				// process recursive offline refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_COMMIT)) {
				// process commit request
				CommandDialog dialog = new CommitDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
				// process offline recursive refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_COPY)) {
				// process copy request
				CommandDialog dialog = new CopyDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_DELETE)) {
				// process delete request
				Command command = new Delete();
				Map args = new HashMap();
				args.put(Delete.TARGETS, targets);
				executeDirectCommand(executor, command, args);
				// process recursive offline refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_DIFF)) {
				// process diff request
				CommandDialog dialog = new DiffDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
			} else if (ae.getActionCommand().equals(ACTION_INFO)) {
				// process info request
				Command command = new Info();
				Map args = new HashMap();
				args.put(Info.TARGETS, targets);
				executeDirectCommand(executor, command, args);
			} else if (ae.getActionCommand().equals(ACTION_LOG)) {
				// process log request
				CommandDialog dialog = new LogDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
			} else if (ae.getActionCommand().equals(ACTION_MERGE)) {
				// process merge request
				CommandDialog dialog = new MergeDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_MOVE)) {
				// process move request
				CommandDialog dialog = new MoveDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_REFRESH_ONLINE)) {
				// process recursive online refresh request
				refreshSelectedTargets(true);
			} else if (ae.getActionCommand().equals(ACTION_REFRESH_OFFLINE)) {
				// process recursive offline refresh request
				refreshSelectedTargets(false);
			} else if (ae.getActionCommand().equals(ACTION_RESOLVE)) {
				// process resolve request
				String message = RESOLVE_WARNING;
				String[] targetArray = getSelectedTargetsAsArray();
				for (int i = 0; i < targetArray.length; i++) {
					String s = targetArray[i];
					message += s + NEWLINE_CHARACTER;
				}
				int confirmation = JOptionPane.showConfirmDialog(this, message, CONFIRMATION, JOptionPane.OK_CANCEL_OPTION);
				if (confirmation == JOptionPane.OK_OPTION) {
					// process add request
					Command command = new Resolve();
					Map args = new HashMap();
					args.put(Resolve.TARGETS, targets);
					executeDirectCommand(executor, command, args);
					// process recursive offline refresh request so new working copy status can be displayed
					refreshSelectedTargets(false);
				}
			} else if (ae.getActionCommand().equals(ACTION_REVERT)) {
				// process revert request
				String message = REVERT_WARNING;
				String[] targetArray = getSelectedTargetsAsArray();
				for (int i = 0; i < targetArray.length; i++) {
					String s = targetArray[i];
					message += s + NEWLINE_CHARACTER;
				}
				int confirmation = JOptionPane.showConfirmDialog(this, message, CONFIRMATION, JOptionPane.OK_CANCEL_OPTION);
				if (confirmation == JOptionPane.OK_OPTION) {
					// process add request
					Command command = new Revert();
					Map args = new HashMap();
					args.put(Revert.TARGETS, targets);
					executeDirectCommand(executor, command, args);
					// process recursive offline refresh request so new working copy status can be displayed
					refreshSelectedTargets(false);
				}
			} else if (ae.getActionCommand().equals(ACTION_STATUS)) {
				// process status request
				CommandDialog dialog = new StatusDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
			} else if (ae.getActionCommand().equals(ACTION_SWITCH)) {
				// process switch request
				CommandDialog dialog = new SwitchDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
			} else if (ae.getActionCommand().equals(ACTION_UPDATE)) {
				// process update request
				CommandDialog dialog = new UpdateDialog(Application.getApplicationFrame(), true);
				initializeAndShowDialog(dialog, targets, executor);
				// process offline recursive refresh request so new working copy status can be displayed
				refreshSelectedTargets(false);
			}
		}
	}

	/**
	 * initialized a dialog and makes it visible
	 * @param dialog CommandDialog to be initialized and shown
	 * @param targets targets for the command
	 * @param executor command executor
	 */
	private void initializeAndShowDialog(CommandDialog dialog, String targets, Executable executor) {
		dialog.setTargets(targets);
		dialog.setExecutor(executor);
		dialog.setVisible(true);
	}

	/**
	 * executes a command that does not need any additional options from a dialog
	 * @param executor command executor
	 * @param command command to be run
	 * @param args parameters to configure the command correctly
	 */
	private void executeDirectCommand(Executable executor, Command command, Map args) {
		try {
			executor.executeCommand(command, args);
		} catch (CommandException e) {
			showMessageDialog(e.getMessage());
		}
	}

	/**
	 * displays a dialog with the given message text
	 * @param message text to be displayed
	 */
	private void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(Application.getApplicationFrame().getContentPane(), message);
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
	 * returns an array of strings of all selected targets, or null if nothing is selected
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
	 * @param online indicator as to whether an online refresh should be performed
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
	 * @param online indicator as to whether an online refresh should be performed
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
		}
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
