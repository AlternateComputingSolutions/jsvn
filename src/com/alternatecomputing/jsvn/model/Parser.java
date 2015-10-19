package com.alternatecomputing.jsvn.model;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Parser {
	private static final int FILE_STATUS_POS = 0;
	private static final int PROPERTY_STATUS_POS = 1;
	private static final int LOCKED_STATUS_POS = 2;
	private static final int WITH_HISTORY_POS = 3;
	private static final int SWITCHED_POS = 4;
	private static final int OUTDATED_POS = 7;

	/**
	 * parses the output of a svn status command and returns the root of a JTree that represents the working copy
	 * @param result output of a svn status command
	 * @return root of a JTree that represents the working copy
	 * @throws java.io.IOException
	 */
	public static SVNTreeNodeData parse(String result) throws IOException {
		Map nodeCache = new HashMap();
		BufferedReader reader = new BufferedReader(new StringReader(result));
		int baseIndex = -1;
		String item;
		SVNTreeNodeData root = null;
		SVNTreeNodeData parent;
		// loop through the command output
		while ((item = reader.readLine()) != null) {
			// pick out the first few pieces of info from the status
			String fileStatus = item.substring(FILE_STATUS_POS, FILE_STATUS_POS + 1);
			String propertyStatus = item.substring(PROPERTY_STATUS_POS, PROPERTY_STATUS_POS + 1);
			String lockedIndicator = item.substring(LOCKED_STATUS_POS, LOCKED_STATUS_POS + 1);
			String historyIndicator = item.substring(WITH_HISTORY_POS, WITH_HISTORY_POS + 1);
			String switchedIndicator = item.substring(SWITCHED_POS, SWITCHED_POS + 1);
			String outdatedIndicator = item.substring(OUTDATED_POS, OUTDATED_POS + 1);
			// grab the rest of the status info
			item = item.substring(OUTDATED_POS + 1);
			// create new node with revision attributes
			SVNTreeNodeData nodeData = new SVNTreeNodeData();
			// when using "svn status -u", the last line doesn't contain any file info, just the revision number, so don't parse it.
			if (item.indexOf(ConfigurationManager.getInstance().getWorkingDirectory()) > -1) {
				baseIndex = item.indexOf(ConfigurationManager.getInstance().getWorkingDirectory()) + ConfigurationManager.getInstance().getWorkingDirectory().length();
				// extract working copy portion from the absolue path
				String resource = item.substring(baseIndex);
				nodeData.setPath(resource);

				// handle the file status
				handleFileStatus(fileStatus, nodeData);
				if (fileStatus.equals("?")) {
					// item is not under revision control
					nodeData.setRevision(SVNTreeNodeData.NOT_VERSIONED);
					nodeData.setLastChangedRevision(SVNTreeNodeData.NOT_VERSIONED);
					nodeData.setLastChangedAuthor(null);
				} else {

					// handle the property status
					handlePropertyStatus(propertyStatus, nodeData);

					// handle the locked status
					handleLockedIndicator(lockedIndicator, nodeData);

					// handle the history indicator
					handleHistoryIndicator(historyIndicator, nodeData);

					// handle switched indicator
					handleSwitchedIndicator(switchedIndicator, nodeData);

					// handle outdated indicator
					handleOutDatedIndicator(outdatedIndicator, nodeData);

					StringTokenizer st = new StringTokenizer(item, " ");

					// parse working revision
					String token = st.nextToken();
					if (token.equals("-")) {
						nodeData.setRevision(SVNTreeNodeData.NOT_VERSIONED);
					} else {
						nodeData.setRevision(Integer.parseInt(token));
					}

					// parse last change revision
					token = st.nextToken();
					if (token.equals("?")) {
						nodeData.setLastChangedRevision(SVNTreeNodeData.NOT_VERSIONED);
					} else {
						nodeData.setLastChangedRevision(Integer.parseInt(token));
					}

					// parse last change author
					nodeData.setLastChangedAuthor(st.nextToken());
				}
				// determine if it's a file or directory
				File file = new File(ConfigurationManager.getInstance().getWorkingDirectory() + resource);
				if (file.isDirectory()) {
					nodeData.setNodeKind(SVNTreeNodeData.NODE_KIND_DIRECTORY);
				} else {
					nodeData.setNodeKind(SVNTreeNodeData.NODE_KIND_FILE);
				}
				// grab everything up to the resource name to use as hash key
				// for the parent node we need to link this node to
				String parentDir = resource.lastIndexOf(Constants.SVN_PATH_SEPARATOR) == -1 ? null : resource.substring(0, resource.lastIndexOf(Constants.SVN_PATH_SEPARATOR));
				// look in cache for parent to link this node to
				parent = (SVNTreeNodeData) nodeCache.get(parentDir);
				if (parent == null) {
					// no root exists yet so this node must be the tree root
					parent = nodeData;
					root = nodeData;
				} else {
					// add the new node to the parent
					parent.getChildren().add(nodeData);
				}
				// cache the new node
				nodeCache.put(resource, nodeData);
			}
		}
		return root;
	}

	/**
	 * updates a given node based on the value of a given fileStatus
	 * @param fileStatus
	 * @param nodeData
	 */
	private static void handleFileStatus(String fileStatus, SVNTreeNodeData nodeData) {
		fileStatus = fileStatus.intern();
		if (fileStatus == " ") {
			// no modifications
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_NO_CHANGES);
		} else if (fileStatus == "A") {
			// added
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_ADDITION_SCHEDULED);
		} else if (fileStatus == "D") {
			// deleted
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_DELETION_SCHEDULED);
		} else if (fileStatus == "M") {
			// modified
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_MODIFIED);
		} else if (fileStatus == "C") {
			// conflicted
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_CONFLICT);
		} else if (fileStatus == "?") {
			// item is not under revision control
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_NOT_VERSIONED);
		} else if (fileStatus == "!") {
			// item is missing and was removed via a non-svn command
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_MISSING);
		} else if (fileStatus == "~") {
			// versioned as a directory, but is a file, or vice versa
			nodeData.setFileStatus(SVNTreeNodeData.FILE_STATUS_WRONG_TYPE);
		} else {
			// XXX - log error
		}
	}

	/**
	 * updates a given node based on the value of a given propertyStatus
	 * @param propertyStatus
	 * @param nodeData
	 */
	private static void handlePropertyStatus(String propertyStatus, SVNTreeNodeData nodeData) {
		propertyStatus = propertyStatus.intern();
		if (propertyStatus == " ") {
			// no modifications
			nodeData.setPropertyStatus(SVNTreeNodeData.PROPERTY_STATUS_NO_CHANGE);
		} else if (propertyStatus == "M") {
			// modified
			nodeData.setPropertyStatus(SVNTreeNodeData.PROPERTY_STATUS_MODIFIED);
		} else if (propertyStatus == "C") {
			// conflicted
			nodeData.setPropertyStatus(SVNTreeNodeData.PROPERTY_STATUS_CONFLICT);
		} else {
			// XXX - log error
		}
	}

	/**
	 * updates a given node based on the value of a given lockedIndicator
	 * @param lockedIndicator
	 * @param nodeData
	 */
	private static void handleLockedIndicator(String lockedIndicator, SVNTreeNodeData nodeData) {
		lockedIndicator = lockedIndicator.intern();
		if (lockedIndicator == " ") {
			// not locked
			nodeData.setLockedIndicator(false);
		} else if (lockedIndicator == "L") {
			// locked
			nodeData.setLockedIndicator(true);
		} else {
			// XXX - log error
		}
	}

	/**
	 * updates a given node based on the value of a given historyIndicator
	 * @param historyIndicator
	 * @param nodeData
	 */
	private static void handleHistoryIndicator(String historyIndicator, SVNTreeNodeData nodeData) {
		historyIndicator = historyIndicator.intern();
		if (historyIndicator == " ") {
			// no history scheduled with commit
			nodeData.setHistoryIndicator(false);
		} else if (historyIndicator == "+") {
			// history scheduled with commit
			nodeData.setHistoryIndicator(true);
		} else {
			// XXX - log error
		}
	}

	/**
	 * updates a given node based on the value of a given switchedIndicator
	 * @param switchedIndicator
	 * @param nodeData
	 */
	private static void handleSwitchedIndicator(String switchedIndicator, SVNTreeNodeData nodeData) {
		switchedIndicator = switchedIndicator.intern();
		if (switchedIndicator == " ") {
			nodeData.setSwitchedIndicator(false);
		} else if (switchedIndicator == "S") {
			nodeData.setSwitchedIndicator(true);
		} else {
			// XXX - log error
		}
	}

	/**
	 * updates a given node based on the value of a given outdatedIndicator
	 * @param outdatedIndicator
	 * @param nodeData
	 */
	private static void handleOutDatedIndicator(String outdatedIndicator, SVNTreeNodeData nodeData) {
		outdatedIndicator = outdatedIndicator.intern();
		if (outdatedIndicator == " ") {
			// up-to-date
			nodeData.setOutDatedIndicator(false);
		} else if (outdatedIndicator == "*") {
			// out-of-date
			nodeData.setOutDatedIndicator(true);
		} else {
			// XXX - log error
		}
	}


}
