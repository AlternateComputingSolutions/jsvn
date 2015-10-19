package com.alternatecomputing.jsvn.idea;

import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 27/04/2003
 * Time: 18:54:20
 * To change this template use Options | File Templates.
 */
public class JsvnFileListner implements VirtualFileListener {
    public JsvnFileListner() {
    }

    public void propertyChanged(VirtualFilePropertyEvent event) {
    }

    public void contentsChanged(VirtualFileEvent event) {
        System.out.println("File saved: " + event.getFile().getPath() );
    }

    public void fileCreated(VirtualFileEvent event) {
    }

    public void fileDeleted(VirtualFileEvent event) {
    }

    public void fileMoved(VirtualFileMoveEvent event) {
    }

    public void beforePropertyChange(VirtualFilePropertyEvent event) {
    }

    public void beforeContentsChange(VirtualFileEvent event) {
    }

    public void beforeFileDeletion(VirtualFileEvent event) {
    }

    public void beforeFileMovement(VirtualFileMoveEvent event) {
    }
}
