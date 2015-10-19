package com.alternatecomputing.jsvn.idea.actions;

import com.alternatecomputing.jsvn.gui.CommandDialog;
import com.intellij.openapi.actionSystem.AnAction;

/**
 *
 */
public abstract class JsvnAction extends AnAction  {
	protected void initializeAndShowDialog(CommandDialog dialog, String targets ) {
		dialog.setTargets(targets);
		dialog.setVisible(true);
	}
}
