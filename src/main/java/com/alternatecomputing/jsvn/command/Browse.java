package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.gui.ComponentCommandable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * ls: list URL content.
 * usage: ls URL
 *
 */
public class Browse extends Ls implements ComponentCommandable {

	private String url = null;
	private DefaultMutableTreeNode root;
	private JTree tree;

	public void setRoot(DefaultMutableTreeNode root) {
		this.root = root;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JComponent getComponent() {
		String result = getResult();

		boolean isNull = (root == null);
		if (isNull) {
			root = new DefaultMutableTreeNode(url);
		}

		DocumentBuilderFactory factory =
				DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(result.getBytes("utf-8"))));

			NodeList nList = doc.getElementsByTagName("entry");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					NodeList nameList = element.getElementsByTagName("name");
					String name = nameList.item(0).getTextContent();
					RepoNode repoNode = new RepoNode(name);
					String kind = element.getAttribute("kind");
					if (kind.equals("dir")) {
						repoNode.setDir(true);
                        repoNode.add(new DefaultMutableTreeNode("..."));
					} else if (kind.equals("file")) {
						repoNode.setDir(false);
					}
					root.add(repoNode);
				}
			}
		} catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!isNull) {
            tree.expandPath(new TreePath(root.getPath()));
            tree.repaint();
        } else {
            tree = new JTree(root);

            tree.addTreeWillExpandListener(new TreeWillExpandListener() {

                public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                    TreePath path = event.getPath();
                    DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) path.getLastPathComponent();
                    DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode) lastPathComponent.getFirstChild();
                    if (firstChild.getUserObject().equals("...")) {
                        lastPathComponent.removeAllChildren();

                        Browse browse = new Browse();
                        browse.setUrl(getNodeAsString(lastPathComponent));
                        browse.setRoot(lastPathComponent);
                        browse.setTree(tree);
                        try {
                            HashMap args = new HashMap();
                            args.put(Browse.URL, getNodeAsString(lastPathComponent));
                            args.put(Browse.STYLE, "xml");
                            browse.init(args);
                            browse.execute();
                            browse.getComponent();
                        } catch (CommandException e1) {
                            e1.printStackTrace();
                        }

                        ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(lastPathComponent);
                    }
                }

                public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

                }
            });
        }
		return tree;
	}

	private String getSelectedTargetsAsString(JTree tree) {

		TreePath[] paths = tree.getSelectionPaths();
		if (paths == null) {
			return null;
		}

        TreePath treepath = paths[0];
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treepath.getLastPathComponent();

        return getNodeAsString(dmtn);
    }

    private String getNodeAsString(DefaultMutableTreeNode dmtn) {
        String pathString = (String) dmtn.getUserObject();
        DefaultMutableTreeNode parent;
        while ((parent = (DefaultMutableTreeNode) dmtn.getParent()) != null) {
            pathString = parent.getUserObject() + "/" + pathString;
            dmtn = parent;
        }
        return pathString;
    }

}
