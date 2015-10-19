package com.alternatecomputing.jsvn.idea;

import com.alternatecomputing.jsvn.Constants;
import com.alternatecomputing.jsvn.command.Add;
import com.alternatecomputing.jsvn.command.Commandable;
import com.alternatecomputing.jsvn.command.Delete;
import com.alternatecomputing.jsvn.command.Move;
import com.alternatecomputing.jsvn.gui.Application;
import com.alternatecomputing.jsvn.gui.JSVNCommandExecutor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class JsvnFileListner implements VirtualFileListener {
    public JsvnFileListner() {
    }

    public void propertyChanged(VirtualFilePropertyEvent event) {

        if ( event.getPropertyName().equals( VirtualFile.PROP_NAME )) {
	        String path = event.getFile().getParent().getPath() + Constants.SVN_PATH_SEPARATOR;
	        String oldName = (String) event.getOldValue();
	        String newName = (String) event.getNewValue();

	        Commandable command = new Move();
	        Map args = new HashMap();
	        args.put(Move.SOURCE, path + oldName );
	        args.put(Move.DESTINATION, path + newName );
			try {
				executeDirectCommand(command, args );
			} catch (InvocationTargetException e) {
				showMessageDialog(e.getMessage());
			} catch (InterruptedException e) {
				showMessageDialog(e.getMessage());
			}
		}
    }

    public void contentsChanged(VirtualFileEvent event) {
        System.out.println("File saved: " + event.getFile().getPath() );
    }

    public void fileCreated(VirtualFileEvent event) {
	    String file = event.getFile().getPath();

		Commandable command = new Add();
	    Map args = new HashMap();
	    args.put(Add.TARGETS, file );
		try {
			executeDirectCommand(command, args);
		} catch (InvocationTargetException e) {
			showMessageDialog(e.getMessage());
		} catch (InterruptedException e) {
			showMessageDialog(e.getMessage());
		}
	}

    public void fileDeleted(VirtualFileEvent event) {
	    String file = event.getFile().getPath();

		Commandable command = new Delete();
	    Map args = new HashMap();
	    args.put(Add.TARGETS, file );
		try {
			executeDirectCommand(command, args);
		} catch (InvocationTargetException e) {
			showMessageDialog(e.getMessage());
		} catch (InterruptedException e) {
			showMessageDialog(e.getMessage());
		}
	}

    public void fileMoved(VirtualFileMoveEvent event) {
        Messages.showMessageDialog("Subversion", event.getOldParent().getPath(), Messages.getInformationIcon() );
		Messages.showMessageDialog("Subversion", event.getNewParent().getPath(), Messages.getInformationIcon() );
//	    String file = event.getFile().getPath();
//
//		Command command = new Add();
//	    Map args = new HashMap();
//	    args.put(Add.TARGETS, file );
//	    executeDirectCommand(com.alternatecomputing.jsvn.gui.Frame.getInstance(), command, args);
    }

    public void beforePropertyChange(VirtualFilePropertyEvent event) {
    }

    public void beforeContentsChange(VirtualFileEvent event) {
    }

    public void beforeFileDeletion(VirtualFileEvent event) {
    }

    public void beforeFileMovement(VirtualFileMoveEvent event) {
    }




	/**
	 * executes a command that does not need any additional options from a dialog
	 * @param command command to be run
	 * @param args parameters to configure the command correctly
	 * @throws java.lang.reflect.InvocationTargetException
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

}
