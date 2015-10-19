package com.alternatecomputing.jsvn.idea;

import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Commandable;
import com.alternatecomputing.jsvn.gui.Frame;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.JPanel;
import java.util.Map;

/**
 *
 */
public class JsvnComponent implements ProjectComponent {

	public static final String TOOL_WINDOW_ID = "Subversion";

	Project project;
	ToolWindow toolWindow;
	Frame frame;
	JPanel panel;

	public JsvnComponent( Project project ) {
		this.project = project;
	}

	public String getComponentName() {
		return "Jsvn.JsvnComponent";
	}

	public void projectOpened() {
		frame = new Frame( );
		panel = frame.getContent();

        // Default view to parent of .ipr file
        frame.setWorkingCopy( project.getProjectFile().getParent().getPath() );

        LocalFileSystem.getInstance().addVirtualFileListener( new JsvnFileListner() );


		toolWindow = ToolWindowManager.getInstance( project ).
			registerToolWindow( TOOL_WINDOW_ID, panel, ToolWindowAnchor.BOTTOM );

	}

	public void projectClosed() {
		ToolWindowManager.getInstance( project ).
			unregisterToolWindow( TOOL_WINDOW_ID );
		panel = null;
		frame = null;
	}

	public void initComponent() {
		//
	}

	public void disposeComponent() {
		//
	}

    public void executeCommand(Commandable command, Map args) throws CommandException {
        //
    }

}

