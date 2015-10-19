package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 14/05/2003
 * Time: 23:19:31
 * To change this template use Options | File Templates.
 */
public class DirectoryBrowserTableModel extends AbstractTableModel {
	private File[] files = new File[] {};
	private SVNTreeNodeData _node;
	private List _contents;

	public DirectoryBrowserTableModel() {
	}

	public int getRowCount() {
		return ( _contents == null ) ? 0 : _contents.size();
	}

	public int getColumnCount() {
		return 8;
	}

	public String getColumnName(int column) {
		switch ( column ) {
			case 0:
				return "Filename";
			case 1:
				return "Status";
			case 2:
				return "Type";
			case 3:
				return "Size";
			case 4:
				return "Date Modified";
			case 5:
				return "Revision";
			case 6:
				return "Last Revision";
			case 7:
				return "Last Author";
		}
		return super.getColumnName(column);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		SVNTreeNodeData nodeData;
		nodeData = (SVNTreeNodeData) _contents.get( rowIndex );
		File file = new File(ConfigurationManager.getInstance().getWorkingDirectory() + Constants.SVN_PATH_SEPARATOR + nodeData.getPath());

		switch ( columnIndex ) {
			case 0:
				return formatFilesAndDirectories(nodeData, nodeData.getName());
			case 1:
				return formatFilesAndDirectories(nodeData, nodeData.getStatusText());
			case 2:
				return formatFilesAndDirectories(nodeData, nodeData.getNodeKind() == SVNTreeNodeData.NODE_KIND_FILE ? "File" : "Directory");
			case 3:
				if (file.isDirectory()) {
					return "";
				} else {
					return formatFilesAndDirectories(nodeData, Long.toString(file.length()));
				}
			case 4:
				return formatFilesAndDirectories(nodeData, new Date(file.lastModified()).toString());
			case 5:
				return formatFilesAndDirectories(nodeData, Integer.toString(nodeData.getRevision()));
			case 6:
				return formatFilesAndDirectories(nodeData, Integer.toString(nodeData.getLastChangedRevision()));
			case 7:
				return formatFilesAndDirectories(nodeData, nodeData.getLastChangedAuthor());
			default:
				return null;
		}
	}

	private String formatFilesAndDirectories(SVNTreeNodeData data, String s) {
		if (s == null) {
			return "";
		}
		if (data.getNodeKind() == SVNTreeNodeData.NODE_KIND_DIRECTORY) {
			return "<html><i>" + s + "</i></html>";
		} else {
			return s;
		}
	}

	private SVNTreeNodeData findNamedChildNode(String name) {
		for (Iterator iterator = _node.getChildren().iterator(); iterator.hasNext();) {
			SVNTreeNodeData nodeData = (SVNTreeNodeData) iterator.next();
			if ( name.equals( nodeData.getName() )) {
				return nodeData;
			}
		}

		return null;
	}

	public void setPathToBrowse(SVNTreeNodeData node, File pathToBrowse ) {
		_contents = node.getChildren();
		_node = node;
		fireTableDataChanged();
	}

	public void clearPathToBrowse() {
		files = new File[] {};
		fireTableDataChanged();
	}

	public SVNTreeNodeData getNodeForRow( int rowIndex ) {
		return (SVNTreeNodeData) _contents.get( rowIndex );
	}

}

