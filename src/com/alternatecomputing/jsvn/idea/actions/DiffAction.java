package com.alternatecomputing.jsvn.idea.actions;

import com.alternatecomputing.jsvn.gui.Application;
import com.alternatecomputing.jsvn.gui.CommandDialog;
import com.alternatecomputing.jsvn.gui.DiffDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.vfs.VirtualFile;

/**
 *
 */
public class DiffAction extends JsvnAction {
	public void actionPerformed(AnActionEvent event) {
		//To change body of implemented methods use Options | File Templates.

		VirtualFile file = (VirtualFile) event.getDataContext().getData( DataConstants.VIRTUAL_FILE );
		String path = file.getPath();

		CommandDialog dialog = new DiffDialog(Application.getApplicationFrame(), true);
		initializeAndShowDialog(dialog, path );


	}
}
