package com.alternatecomputing.jsvn.model;

import com.alternatecomputing.jsvn.Constants;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * models an individual subversion working copy item
 */
public class SVNTreeNodeData {
	// revision constants
	public static final int UNKNOWN_VERSION = -2;
	public static final int NOT_VERSIONED = -1;

	// node kind constants
	public static final int NODE_KIND_FILE = 0;
	public static final int NODE_KIND_DIRECTORY = 1;

	// file status constants
	public static final int FILE_STATUS_NO_CHANGES = 0;
	public static final int FILE_STATUS_ADDITION_SCHEDULED = 1;
	public static final int FILE_STATUS_DELETION_SCHEDULED = 2;
	public static final int FILE_STATUS_MODIFIED = 3;
	public static final int FILE_STATUS_CONFLICT = 4;
	public static final int FILE_STATUS_NOT_VERSIONED = 5;
	public static final int FILE_STATUS_MISSING = 6;
	public static final int FILE_STATUS_WRONG_TYPE = 7;

	// property status constants
	public static final int PROPERTY_STATUS_NO_CHANGE = 0;
	public static final int PROPERTY_STATUS_MODIFIED = 1;
	public static final int PROPERTY_STATUS_CONFLICT = 2;

	public static final String UNKNOWN_LAST_CHANGED_AUTHOR = "";

	private String _path;
	private String _name;
	private int _revision = NOT_VERSIONED;
	private int _nodeKind;
	private int _fileStatus;
	private int _propertyStatus;
	private boolean _hasHistory = false;
	private boolean _isLocked = false;
	private boolean _isSwitched = false;
	private boolean _isOutDated = false;
	private String _lastChangedAuthor = UNKNOWN_LAST_CHANGED_AUTHOR;
	private int _lastChangedRevision = NOT_VERSIONED;

	private SVNTreeNodeData _parent;
	private AbstractList _children;

	/**
	 * constructor
	 */
	public SVNTreeNodeData() {
		_children = new ArrayList();
	}

	/**
	 * gets the path relative to the root of the working copy
	 * @return relative path
	 */
	public String getPath() {
		return _path;
	}

	/**
	 * sets the path relative to the root of the working copy
	 * @param path relative path
	 */
	public void setPath(String path) {
		_path = path;
	}

	/**
	 * gets the revision
	 * @return revision
	 */
	public int getRevision() {
		return _revision;
	}

	/**
	 * sets the revision
	 * @param revision revision
	 */
	public void setRevision(int revision) {
		_revision = revision;
	}

	/**
	 * gets the kind of node (file or directory)
	 * @return node kind
	 */
	public int getNodeKind() {
		return _nodeKind;
	}

	/**
	 * sets the kind of node (file or directory)
	 * @param nodeKind node kind
	 */
	public void setNodeKind(int nodeKind) {
		_nodeKind = nodeKind;
	}

	/**
	 * gets the name of the item
	 * @return name
	 */
	public String getName() {
		return _name;
	}
	public void setName(String n) {
		_name = n;
	}

	/**
	 * gets the name of the last person to modify the item
	 * @return name of the last person to modify the item
	 */
	public String getLastChangedAuthor() {
		return _lastChangedAuthor;
	}

	/**
	 * sets the name of the last person to modify the item
	 * @param lastChangedAuthor name of the last person to modify the item
	 */
	public void setLastChangedAuthor(String lastChangedAuthor) {
		_lastChangedAuthor = lastChangedAuthor;
	}

	/**
	 * gets the revision at which the items was last changes
	 * @return revision at which the items was last changes
	 */
	public int getLastChangedRevision() {
		return _lastChangedRevision;
	}

	/**
	 * sets the revision at which the items was last changes
	 * @param lastChangedRevision revision at which the items was last changes
	 */
	public void setLastChangedRevision(int lastChangedRevision) {
		_lastChangedRevision = lastChangedRevision;
	}

	/**
	 * gets the file status
	 * @return file status
	 */
	public int getFileStatus() {
		return _fileStatus;
	}

	/**
	 * sets the file status
	 * @param fileStatus file status
	 */
	public void setFileStatus(int fileStatus) {
		_fileStatus = fileStatus;
	}

	/**
	 * gets the status of the item's properties
	 * @return status of the item's properties
	 */
	public int getPropertyStatus() {
		return _propertyStatus;
	}

	/**
	 * sets the status of the item's properties
	 * @param propertyStatus status of the item's properties
	 */
	public void setPropertyStatus(int propertyStatus) {
		_propertyStatus = propertyStatus;
	}

	/**
	 * gets an indicator to determine if history will be recorded with the item
	 * @return history indicator
	 */
	public boolean getHistoryIndicator() {
		return _hasHistory;
	}

	/**
	 * sets an indicator to determine if history will be recorded with the item
	 * @param historyIndicator history indicator
	 */
	public void setHistoryIndicator(boolean historyIndicator) {
		_hasHistory = historyIndicator;
	}

	/**
	 * gets the locked indicator
	 * @return locked indicator
	 */
	public boolean getLockedIndicator() {
		return _isLocked;
	}

	/**
	 * sets the locked indicator
	 * @param lockedIndicator locked indicator
	 * @param lockedIndicator locked indicator
	 */
	public void setLockedIndicator(boolean lockedIndicator) {
		_isLocked = lockedIndicator;
	}

	/**
	 * gets the switched indicator
	 * @return switched indicator
	 */
	public boolean getSwitchedIndicator() {
		return _isSwitched;
	}

	/**
	 * sets the switched indicator
	 * @param lockedSwitched switched indicator
	 */
	public void setSwitchedIndicator(boolean lockedSwitched) {
		_isSwitched = lockedSwitched;
	}

	/**
	 * determines if the item is out of date
	 * @return out-of-date indicator
	 */
	public boolean getOutDatedIndicator() {
		return _isOutDated;
	}

	/**
	 * sets the out-of-date indicator
	 * @param outDatedSwitched out-of-date indicator
	 */
	public void setOutDatedIndicator(boolean outDatedSwitched) {
		_isOutDated = outDatedSwitched;
	}

	/**
	 * returns this node's parent node
	 * @return parent node
	 */
	public SVNTreeNodeData getParent() {
		return _parent;
	}

	/**
	 * set this node's partent node
	 * @param parent parent node
	 */
	public void setParent(SVNTreeNodeData parent) {
		_parent = parent;
	}

	/**
	 * returns a list of child nodes
	 * @return list of child nodes
	 */
	public AbstractList getChildren() {
		return _children;
	}

	/**
	 * sets the list of child nodes
	 * @param children ist of child nodes
	 */
	public void setChildren(AbstractList children) {
		_children = children;
	}

	public String toString() {
		String statusText = getStatusText();
		if (statusText.length() > 0) {
			return getName() + "  [" + statusText + "]";
		} else {
			return getName();
		}
	}

	/**
	 * returns the textual representation of the given node's status
	 * @return node's status text
	 */
	public String getStatusText() {
		StringBuffer result = new StringBuffer();
		switch (_fileStatus) {
			case FILE_STATUS_NOT_VERSIONED:
				result.append("not versioned");
				return result.toString();
			case FILE_STATUS_NO_CHANGES:
				if (!_isOutDated) {
					result.append("up-to-date");
					break;
				}
			case FILE_STATUS_ADDITION_SCHEDULED:
				result.append("addition scheduled");
				break;
			case FILE_STATUS_DELETION_SCHEDULED:
				result.append("deletion scheduled");
				break;
			case FILE_STATUS_MODIFIED:
				result.append("locally modified");
				break;
			case FILE_STATUS_CONFLICT:
				result.append("locally conflicted");
				break;
			case FILE_STATUS_MISSING:
				result.append("locally deleted");
				break;
			case FILE_STATUS_WRONG_TYPE:
				result.append("wrong type");
				break;
			default:
				System.err.println("Unexpected local status: " + _fileStatus);
				return "";
		}
		if (_isOutDated) {
			if (_revision == UNKNOWN_VERSION) {
				result.append("new repository file");
			} else if (_fileStatus == FILE_STATUS_NO_CHANGES) {
				result.append("out-of-date");
			} else {
				result.append(", out-of-date");
			}
		}
		switch (_propertyStatus) {
			case PROPERTY_STATUS_NO_CHANGE:
				break;
			case PROPERTY_STATUS_MODIFIED:
				result.append(", locally modified properties");
				break;
			case PROPERTY_STATUS_CONFLICT:
				result.append(", conflicting local properties");
				break;
			default:
				System.err.println("Unexpected local property status: " + _propertyStatus);
				return "";
		}
		if (_isLocked) {
			result.append(", locked");
		}
		if (_hasHistory) {
			result.append(", with history");
		}
		if (_isSwitched) {
			result.append(", switched");
		}
		return result.toString();

	}

	/**
	 * determines if two SVNTreeNodeData objects are equivalent
	 * @param obj instance to compare to
	 * @return true if the two instances are equivalent
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		SVNTreeNodeData o = (SVNTreeNodeData) obj;
		return (getPath().equals(o.getPath())
				&& getRevision() == o.getRevision()
				&& getNodeKind() == o.getNodeKind()
				&& getFileStatus() == o.getFileStatus())
				&& getOutDatedIndicator() == o.getOutDatedIndicator()
				&& getLockedIndicator() == o.getLockedIndicator()
				&& getSwitchedIndicator() == o.getSwitchedIndicator();
	}

	/**
	 * copies the values from a given SVNTreeNodeData instance to this instance
	 * @param node instance from which to copy values
	 */
	public void copyNodeValues(SVNTreeNodeData node) {
		if (node == null) {
			return;
		}
		setPath(node.getPath());
		setRevision(node.getRevision());
		setNodeKind(node.getNodeKind());
		setLastChangedAuthor(node.getLastChangedAuthor());
		setLastChangedRevision(node.getLastChangedRevision());
		setFileStatus(node.getFileStatus());
		setOutDatedIndicator(node.getOutDatedIndicator());
		setLockedIndicator(node.getLockedIndicator());
		setSwitchedIndicator(node.getSwitchedIndicator());
	}
}