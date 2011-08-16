package org.u_compare.gui;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import org.u_compare.gui.debugging.TestWindow;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.Workflow.WorkflowMessageListener;
import org.u_compare.gui.model.Workflow.WorkflowStatus;
import org.u_compare.gui.model.Workflow.WorkflowStatusListener;

/**
 * Displays console output related to a given workflow.
 * 
 * @author pontus
 * @author luke
 * @version 2009-08-27
 */

@SuppressWarnings("serial")
public class ConsolePane extends JScrollPane
	implements WorkflowStatusListener, WorkflowMessageListener {
	
	/**
	 * TODO:
	 * Add setter for show timestamp, and default value
	 * Listener for the messages arraylist instead of calling it implicitly?
	 * Should follow the output downwards, add set and default.
	 *     Fix bug further down.
	 */
	
	private final String WORKFLOW_STATUS_MSG_BASE =
		"Workflow status change to: ";
	
	private Workflow workflow;
	
	private JEditorPane console;
	private ArrayList<ConsoleMessage> messages;
	
	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	private static final String TOOLTIP_TEXT =
		"Displays output related to this workflow";
	
	private static final String CONSOLE_CONTENTTYPE = "text/html";
	private static final DateFormat CONSOLE_DATEFORMAT =
		new SimpleDateFormat("HH:mm:ss");
	private static final String CONSOLE_ERROR_COLOUR = "#FF0000";
	private static final String CONSOLE_DEFAULT_TEXT_COLOUR = "#000000";
	private static final Color CONSOLE_BACKGROUND_COLOR =
		Color.getColor("#FFFFFF");
	private static final boolean CONSOLE_EDITABLE = false;
	
	//TODO: How do we limit the size and minimization possibilites?
	//The code below won't do it.
	//this.setMinimumSize(new Dimension(0, 50));
	
	public ConsolePane(Workflow workflow) {

		this.workflow = workflow;
		workflow.registerWorkflowStatusListener(this);
		workflow.registerWorkflowMessageListener(this);
		
		this.console = new JEditorPane();
		
		// Required for Auto-Scroll behaviour on console update
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		/**
		 * TODO: Move caret on user scroll so it doesn't force the scroll to
		 * the bottom.
		 */
		
		this.console.setContentType(ConsolePane.CONSOLE_CONTENTTYPE);
		this.console.setEditable(ConsolePane.CONSOLE_EDITABLE);
		this.console.setBackground(ConsolePane.CONSOLE_BACKGROUND_COLOR);
		
		this.messages = new ArrayList<ConsoleMessage>();
		
		this.setViewportView(this.console);
		this.setHorizontalScrollBarPolicy(
				ConsolePane.HORIZONTAL_SCROLLBAR_POLICY);
		this.setVerticalScrollBarPolicy(
				ConsolePane.VERTICAL_SCROLLBAR_POLICY);
		this.setToolTipText(
				ConsolePane.TOOLTIP_TEXT);
		// Required for correct display in MacOS X
		this.setOpaque(false);
	}
	
	private class ConsoleMessage {
		public String text;
		public Date timestamp;
		public boolean isError;
		
		public ConsoleMessage(String text, boolean isError) {
			this.text = text;
			this.timestamp = new Date();
			this.isError = isError;
		}
	}

	public void clearConsole() {
		this.messages.clear();
		this.updateConsoleText(); // Use a listener?
	}
	
	public void addConsoleErrorMessage(String message) {
		this.addConsoleMessage(message, true);
	}
	
	public void addConsoleMessage(String message) {
		this.addConsoleMessage(message, false);
	}
	
	private void addConsoleMessage(String message, boolean isError) {
		this.messages.add(new ConsoleMessage(message, isError));
		this.updateConsoleText(); // Use a listener?
	}
	
	private void updateConsoleText() {
		//TODO: Reuse previous text? So that we don't need a full update.
		String text = "";
		for (ConsoleMessage message: this.messages) {
			String messageText = ConsolePane.CONSOLE_DATEFORMAT.format(
					message.timestamp) + "<b>:</b> " + message.text + "<br/>";
			if (message.isError) {
				messageText = "<font color=" + ConsolePane.CONSOLE_ERROR_COLOUR
						+ ">" + messageText + "</font>";
			} else {
				messageText = "<font color="
					+ ConsolePane.CONSOLE_DEFAULT_TEXT_COLOUR + ">"
					+ messageText + "</font>";
			}
			text += messageText;
		}
		
		this.console.setText(text);
	}
	
	public static void main(String[] argv) {
        ConsolePane consolePane = new ConsolePane(new Workflow());

        consolePane.addConsoleMessage("Testing the ConsolePane...");
        for (int i = 0; i < 100; i++) {
        	consolePane.addConsoleMessage("Message number:" + i + "!");
        }
        consolePane.addConsoleErrorMessage("Error!");
        
        new TestWindow("WorkflowConsolePane Test", consolePane);
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		if(workflow.getStatus() == WorkflowStatus.ERROR){
			addConsoleMessage(WORKFLOW_STATUS_MSG_BASE + workflow.getStatus(), true);
		}else{
			addConsoleMessage(WORKFLOW_STATUS_MSG_BASE + workflow.getStatus());
			}
	}
	
	@Override
	public void workflowMessageSent(Workflow workflow, String message){
		addConsoleMessage(message);
	}
	
	public Workflow getAssociatedWorkflow() {
		return this.workflow;
	}
}