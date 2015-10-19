package com.alternatecomputing.jsvn.idea;

import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.components.ProjectComponent;

import com.alternatecomputing.jsvn.command.Commit;
import com.alternatecomputing.jsvn.command.Executable;
import com.alternatecomputing.jsvn.command.Command;
import com.alternatecomputing.jsvn.command.CommandException;
import com.alternatecomputing.jsvn.gui.CommandDialog;
import com.alternatecomputing.jsvn.gui.CommitDialog;
import com.alternatecomputing.jsvn.gui.Application;

import javax.swing.*;
import java.util.Map;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 27/04/2003
 * Time: 14:02:39
 * To change this template use Options | File Templates.
 */
public class JsvnVcsAdapter extends AbstractVcs implements ProjectComponent, Executable {
    public String getName() {
        return "Subversion";
    }

    public String getDisplayName() {
        return "Subversion";
    }

    public void projectOpened() {
    }

    public void projectClosed() {
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
		initializeAndShowDialog(dialog, s, this);

    }

    public void addFile(String s, String s1, Object o) throws VcsException {
    }

    public void removeFile(String s, Object o) throws VcsException {
    }

    public void addDirectory(String s, String s1, Object o) throws VcsException {
    }

    public void removeDirectory(String s, Object o) throws VcsException {
    }



    // TODO: Copied from jSVNTree - should be extracted to common code
    private void initializeAndShowDialog(CommandDialog dialog, String targets, Executable executor) {
        dialog.setTargets(targets);
        dialog.setExecutor(executor);
        dialog.setVisible(true);
    }

    /**
	 * executes the given command with the given arguments
	 * @param command command to be run
	 * @param args arguments for the command
     * TODO: Copied from Frame - should be extracted to IdeaExecutor
	 */
	public void executeCommand(Command command, Map args) throws CommandException {
		try {
			// _statusBar.setText(STATUS_EXECUTING);
			command.init(args);
			command.execute();
			String result = command.getResult();
			// _historyTextPane.setText(_historyTextPane.getText() + command.getCommand() + NEWLINE_CHARACTER);
			JTextPane newPane = new JTextPane();
			newPane.setText(result);
			newPane.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(newPane);
			String commandName = command.getClass().getName();
			// _outputTabbedPane.add(commandName.substring(commandName.lastIndexOf(PERIOD_CHARACTER) + 1), scrollPane);
			// _outputTabbedPane.setSelectedIndex(_outputTabbedPane.getTabCount() - 1);
		} finally {
			// _statusBar.setText(STATUS_READY);
		}
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
