package com.alternatecomputing.jsvn.command;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by alberto on 16/11/15.
 */
public class RepoNode extends DefaultMutableTreeNode {
    private boolean dir;

    public RepoNode(String name) {
        super(name);
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }
}
