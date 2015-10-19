package com.alternatecomputing.jsvn.idea.actions;

import com.alternatecomputing.jsvn.gui.Application;
import com.alternatecomputing.jsvn.gui.CommandDialog;
import com.alternatecomputing.jsvn.gui.CommitDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;

/**
 *
 */
public class CommitProjectAction extends JsvnAction {

	public void actionPerformed(AnActionEvent event) {
		Project project = (Project) event.getDataContext().getData( DataConstants.PROJECT );
		String path = project.getProjectFile().getParent().getPath();

		CommandDialog dialog = new CommitDialog(Application.getApplicationFrame(), true);
		initializeAndShowDialog(dialog, path );
	}


}