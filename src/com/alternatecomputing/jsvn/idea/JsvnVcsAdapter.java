package com.alternatecomputing.jsvn.idea;

import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.command.Commandable;
import com.alternatecomputing.jsvn.gui.Application;
import com.alternatecomputing.jsvn.gui.CommandDialog;
import com.alternatecomputing.jsvn.gui.CommitDialog;
import com.alternatecomputing.jsvn.gui.Frame;
import com.alternatecomputing.jsvn.gui.JSVNCommandExecutor;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.VcsManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 */
public class JsvnVcsAdapter extends AbstractVcs implements ProjectComponent
	/*, FileRenameProvider, DirectoryRenameProvider, DirectoryMoveProvider,
	FileMoveProvider, DifferencesProvider */  {

	public static final String TOOL_WINDOW_ID = "Subversion";

	private Frame frame;
	private JPanel panel;
	private ToolWindow toolWindow;
	private Project project;

    public JsvnVcsAdapter( Project project ) {
        super(project);
        this.project = project;
	}

	public String getName() {
        return "Subversion";
    }

    public String getDisplayName() {
        return "Subversion";
    }

    public void projectOpened() {

	    if ( VcsManager.getInstance(project).getActiveVcs() == this ) {

			frame = new Frame( );
			panel = frame.getContent();
            Application.setApplicationFrame( frame );

			// Default view to parent of .ipr file
			frame.setWorkingCopy( project.getProjectFile().getParent().getPath() );

			LocalFileSystem.getInstance().addVirtualFileListener( new JsvnFileListner() );

			toolWindow = ToolWindowManager.getInstance( project ).
				registerToolWindow( TOOL_WINDOW_ID, panel, ToolWindowAnchor.BOTTOM );
	    }
    }

    public void projectClosed() {
	    if ( VcsManager.getInstance(project).getActiveVcs() == this ) {
			ToolWindowManager.getInstance( project ).
				unregisterToolWindow( TOOL_WINDOW_ID );
			panel = null;
			frame = null;
	    }
    }

    public String getComponentName() {
        return "Jsvn.JsvnVcsAdapter";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public Configurable getConfigurable() {
        return new JsvnConfigurable();
    }

    public void start() throws VcsException {
    }

    public void shutdown() throws VcsException {
    }

    public byte[] getFileContent(String s) throws VcsException {
        return new byte[0];
    }

    public void checkinFile(String s, Object o) throws VcsException {
        CommandDialog dialog = new CommitDialog(Application.getApplicationFrame(), true);
		initializeAndShowDialog(dialog, s);
    }

    public void addFile(String s, String s1, Object o) throws VcsException {
	    showMessageDialog(s);
	    showMessageDialog(s1);

/*
		Command command = new Add();
	    Map args = new HashMap();
	    args.put(Add.TARGETS, targets);
	    executeDirectCommand(com.alternatecomputing.jsvn.gui.Frame.getInstance(), command, args);
*/
    }

    public void removeFile(String s, Object o) throws VcsException {
    }

    public void addDirectory(String s, String s1, Object o) throws VcsException {
    }

    public void removeDirectory(String s, Object o) throws VcsException {
    }



	// XXX - Copied from jSVNTree - should be extracted to common code
	/**
	 * initialized a dialog and makes it visible
	 * @param dialog CommandDialog to be initialized and shown
	 * @param targets targets for the command
	 */
	private void initializeAndShowDialog(CommandDialog dialog, String targets) {
		dialog.setTargets(targets);
		dialog.setVisible(true);
	}

    /**
	 * executes the given command with the given arguments
	 * @param command command to be run
	 * @param args arguments for the command
     * TODO: mostly copied from Frame - should be refactored
	 */
	public void executeCommand(Commandable command, Map args) throws CommandException {
		// _statusBar.setText(STATUS_EXECUTING);
		try {
			executeDirectCommand(command, args);
			String result = command.getResult();
			// _historyTextPane.setText(_historyTextPane.getText() + command.getCommand() + NEWLINE);
			JTextPane newPane = new JTextPane();
			newPane.setText(result);
			newPane.setEditable(false);
			// JScrollPane scrollPane = new JScrollPane(newPane);
			// String commandName = command.getClass().getName();
			// _outputTabbedPane.add(commandName.substring(commandName.lastIndexOf(PERIOD) + 1), scrollPane);
			// _outputTabbedPane.setSelectedIndex(_outputTabbedPane.getTabCount() - 1);
		} catch (InterruptedException e) {
			showMessageDialog(e.getMessage());
		} catch (InvocationTargetException e) {
			showMessageDialog(e.getMessage());
		}
	}


	/**
	 * executes a command that does not need any additional options from a dialog
	 * @param command command to be run
	 * @param args parameters to configure the command correctly
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private void executeDirectCommand(Commandable command, Map args) throws InvocationTargetException, InterruptedException {
		JSVNCommandExecutor executor = new JSVNCommandExecutor(command, args);
		executor.addJSVNEventListener(Application.getApplicationFrame());
		SwingUtilities.invokeAndWait(executor);
	}

	/**
	 * displays a dialog with the given message text
	 * @param message text to be displayed
	 */
	private void showMessageDialog(String message) {
		Messages.showMessageDialog( message, "", Messages.getInformationIcon() );
	}

    private static class JsvnConfigurable implements Configurable {
        public String getDisplayName() {
            return null;
        }

        public Icon getIcon() {
            return null;
        }

        public String getHelpTopic() {
            return null;
        }

        public JComponent createComponent() {
            final JPanel result = new JPanel( new BorderLayout());
            result.setBorder( BorderFactory.createEmptyBorder(5,5,5,5));
            result.add( new JLabel("<html>" +
                    "<font size='3'><font color='red'>" +
                    "Subversion integration requires the svn command.</font></font></html>"
                    ));

            return result;
        }

        public boolean isModified() {
            return false;
        }

        public void apply() throws ConfigurationException {
        }

        public void reset() {
        }

        public void disposeUIResources() {
        }


    }

}
