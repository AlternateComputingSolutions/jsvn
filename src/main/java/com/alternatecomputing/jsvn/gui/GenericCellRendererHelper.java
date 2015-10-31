package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import java.awt.Color;

/**
 *
 */
public class GenericCellRendererHelper {
	/**
	 * returns the standard color for a given node based on its status
	 * @param data node data
	 * @return standard color
	 */
	public static Color getStatusColor(SVNTreeNodeData data) {
		if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_WRONG_TYPE) {
			return Color.red;
		} else if (data.getOutDatedIndicator()) {
			return Color.red;
		} else if (data.getLockedIndicator()) {
			return Color.red;
		} else if (data.getSwitchedIndicator()) {
			return Color.red;
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_CONFLICT) {
			return Color.red;
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_ADDITION_SCHEDULED) {
			return Color.blue;
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_DELETION_SCHEDULED) {
			return Color.blue;
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_MISSING) {
			return Color.red;
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_MODIFIED) {
			return Color.blue;
		} else if (data.getFileStatus() == SVNTreeNodeData.FILE_STATUS_NOT_VERSIONED) {
			return Color.gray;
		} else {
			return Color.black;
		}
	}
}
