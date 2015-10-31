package com.alternatecomputing.jsvn.model;

import com.alternatecomputing.jsvn.configuration.ConfigurationManager;
import com.alternatecomputing.jsvn.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Comparator;

/**
 * This parser takes the output from an svn status command and returns the root node of a hierarchy of SVNTreeNodeData
 * objects representing a working copy.
 */
public class Parser {
	private static final int FILE_STATUS_POS = 0;
	private static final int PROPERTY_STATUS_POS = 1;
	private static final int LOCKED_STATUS_POS = 2;
	private static final int WITH_HISTORY_POS = 3;
	private static final int SWITCHED_POS = 4;
	private static final int OUTDATED_POS = 7;
	private static final String HEAD_REVISION_PREFIX = "Head revision";	// no longer returned in current svn, but still chcekced (for now) for backwards compatibility
	private static final String STATUS_AGAINST_REVISION_PREFIX = "Status against revision";

	/**
	 * parses the output of a svn status command and returns the root of a JTree that represents the working copy
	 * @param result output of a svn status command
	 * @return root of a JTree that represents the working copy
	 * @throws java.io.IOException
	 */
	public static SVNTreeNodeData parse(String result) throws IOException {
		Map nodeCache = new HashMap();
		String item;
		File localItem;
		SVNTreeNodeData root = null;
		SVNTreeNodeData parent;
		String wkdirpath = new File(ConfigurationManager.getInstance().getConfig().getWorkingDirectory()).getCanonicalPath();
        String[] sortedEntries = sortByFileName(result);
		// loop through the command output
		for (int i = 0; i < sortedEntries.length; i++) {
			item = sortedEntries[i];
			// when using "svn status -u", the last line doesn't contain any file info,
			// just the revision number compared against, so don't parse it.
			if (!item.startsWith(HEAD_REVISION_PREFIX) && !item.startsWith(STATUS_AGAINST_REVISION_PREFIX)) {
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

				// handle the file status
				handleFileStatus(fileStatus, nodeData);
				if (fileStatus.equals("?")) {
					localItem = new File(item.trim());
					nodeData.setPath(getPathRelativeToWorkingCopy(localItem, wkdirpath));
					nodeData.setName(localItem.getName());

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

					StringTokenizer st = new StringTokenizer(item, Constants.SPACE);

					// parse working revision
					String token = st.nextToken();
					if (token.equals("-")) {
						nodeData.setRevision(SVNTreeNodeData.NOT_VERSIONED);
					} else {
						try {
							nodeData.setRevision(Integer.parseInt(token));
						} catch (NumberFormatException e) {
							nodeData.setRevision(SVNTreeNodeData.UNKNOWN_VERSION);
						}
					}

					// parse last change revision
					token = st.nextToken();
					if (token.equals("?")) {
						nodeData.setLastChangedRevision(SVNTreeNodeData.NOT_VERSIONED);
					} else {
						nodeData.setLastChangedRevision(Integer.parseInt(token));
					}

					// parse next field
					token = st.nextToken();
					if (st.hasMoreTokens()) {
						// we just parsed the last changed author
						nodeData.setLastChangedAuthor(token);

						// next field is the filename
						String fileName = st.nextToken("");
						localItem = new File(fileName.trim());
						nodeData.setPath(getPathRelativeToWorkingCopy(localItem, wkdirpath));
						nodeData.setName(localItem.getName());
					} else {
						// the token is the filename
						localItem = new File(token.trim());
						nodeData.setPath(getPathRelativeToWorkingCopy(localItem, wkdirpath));
						nodeData.setName(localItem.getName());
					}
				}

				// determine if it's a file or directory
				if (localItem.isDirectory()) {
					nodeData.setNodeKind(SVNTreeNodeData.NODE_KIND_DIRECTORY);
				} else {
					nodeData.setNodeKind(SVNTreeNodeData.NODE_KIND_FILE);
				}

				// look in cache for parent to link this node to
				parent = (SVNTreeNodeData) nodeCache.get(localItem.getParentFile());
				if (parent == null) {
					// no root exists yet so this node must be the tree root
					parent = nodeData;
					root = nodeData;
				} else {
					// add the new node to the parent
					parent.getChildren().add(nodeData);
				}

				// cache the new node
				nodeCache.put(localItem, nodeData);
			}
		}
		return root;
	}

	/**
	 * returns an array of "svn status" entries, sorted by filename
	 * @param statusOutput the output of a "svn status" command
	 * @return array of status entries, sorted by filename
	 */
	private static String[] sortByFileName(String statusOutput) throws IOException {
		Comparator comparator = new FileNameComparator();
        TreeSet sortedSet = new TreeSet(comparator);
		BufferedReader reader = new BufferedReader(new StringReader(statusOutput));
		String item;
		while ((item = reader.readLine()) != null) {
			sortedSet.add(item);
		}
		return (String[]) sortedSet.toArray(new String[0]);
	}

	/**
	 * returns the path of the given file relative to the working copy
	 * @param localItem given file
	 * @param wkdirpath location of working copy
	 * @return relative path
	 * @throws IOException if errors occur accessing the localItem
	 */
	private static String getPathRelativeToWorkingCopy(File localItem, String wkdirpath) throws IOException {
		return localItem.getCanonicalPath().substring(wkdirpath.length() + 1);
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

	/**
	 * comparator class to compare entries from an svn status command based on their filename
	 */
	private static class FileNameComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			String s1 = ((String) o1).substring(((String) o1).lastIndexOf(" ") +1);
			String s2 = ((String) o2).substring(((String) o2).lastIndexOf(" ") +1);
			return s1.compareTo(s2);
		}

	}
}

