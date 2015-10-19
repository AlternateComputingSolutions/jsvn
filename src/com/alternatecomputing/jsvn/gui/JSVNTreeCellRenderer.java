package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.io.File;

/**
 *
 */
public class JSVNTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * constructor
	 */
	public JSVNTreeCellRenderer() {
	}

	/**
	 *
	 * @param tree
	 * @param value
	 * @param sel
	 * @param expanded
	 * @param leaf
	 * @param row
	 * @param hasFocus
	 * @return tree cell renderer
	 */
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {

		SVNTreeNodeData data = (SVNTreeNodeData) ((DefaultMutableTreeNode) value).getUserObject();

		// if node data is null, just return
		if (data == null) {
			return this;
		}

		super.getTreeCellRendererComponent(
				tree, value, sel,
				expanded, leaf, row,
				hasFocus);

		// make sure we render the correct icon, regardless of whether the node has children or not
		if (data.getNodeKind() == SVNTreeNodeData.NODE_KIND_DIRECTORY) {
			if (expanded) {
				setIcon((Icon) UIManager.get("Tree.openIcon"));
			} else {
				setIcon((Icon) UIManager.get("Tree.closedIcon"));
			}
		}

		// change the text color to reflect the file's status
		if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_WRONG_TYPE) {
			setForeground(Color.red);
		} else if (data.getOutDatedIndicator()) {
			setForeground(Color.red);
		} else if (data.getLockedIndicator()) {
			setForeground(Color.red);
		} else if (data.getSwitchedIndicator()) {
			setForeground(Color.red);
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_CONFLICT) {
			setForeground(Color.red);
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_ADDITION_SCHEDULED) {
			setForeground(Color.blue);
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_DELETION_SCHEDULED) {
			setForeground(Color.blue);
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_MISSING) {
			setForeground(Color.red);
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_MODIFIED) {
			setForeground(Color.blue);
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_NOT_VERSIONED) {
			setForeground(Color.gray);
		}

		
		StringBuffer tipText = new StringBuffer("<html>Path: ").append(data.getPath()).append("<br>Name: ").append(data.getName());
		if (data.getRevision() != SVNTreeNodeData.NOT_VERSIONED && data.getRevision() != SVNTreeNodeData.UNKNOWN_VERSION) {
			tipText.append("<br>Revision: ").append(data.getRevision());
		}
		tipText.append("<br>Node kind: ").append(data.getNodeKind() == SVNTreeNodeData.NODE_KIND_DIRECTORY ? "directory" : "file");

		if (data.getNodeKind() == SVNTreeNodeData.NODE_KIND_FILE) {
			File f = new File(ConfigurationManager.getInstance().getWorkingDirectory()+data.getPath());
			tipText.append("<br>Length: ").append(f.length()).append(" bytes");
		}
		if (data.getLastChangedAuthor() != null) {
			tipText.append("<br>Last Changed Author: ").append(data.getLastChangedAuthor());
		}
		if (data.getLastChangedRevision() != SVNTreeNodeData.NOT_VERSIONED && data.getLastChangedRevision() != SVNTreeNodeData.UNKNOWN_VERSION) {
			tipText.append("<br>Last Changed Rev: ").append(data.getLastChangedRevision()).append("</html>");
		}
		
		setToolTipText(tipText.toString());

		return this;
	}
}
