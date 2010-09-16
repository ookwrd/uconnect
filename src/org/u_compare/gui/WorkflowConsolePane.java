package org.u_compare.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.model.UIMAWorkflow;

/**
 * Displays console output related to a given workflow.
 * 
 * @author pontus
 * @version 2009-08-27
 */
@SuppressWarnings("serial")
public class WorkflowConsolePane extends JScrollPane {
	//TODO: Add setter for show timestamp, and default value
	//TODO: Listener for the messages arraylist instead of calling it implicitly?
	//TODO: Should follow the output downwards, add set and default. Fix bug further down.
	
	private JEditorPane console;
	private ArrayList<ConsoleMessage> messages;
	
	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	private static final String BORDER_TITLE = "Workflow console";
	private static final String TOOLTIP_TEXT =
		"Displays output related to this workflow";
	
	private static final String CONSOLE_CONTENTTYPE = "text/html";
	private static final DateFormat CONSOLE_DATEFORMAT =
		new SimpleDateFormat("HH:mm:ss");
	private static final String CONSOLE_ERROR_COLOUR = "#FF0000";
	private static final boolean CONSOLE_EDITABLE = false;
	
	//TODO: How do we limit the size and minimization possibilites?
	//The code below won't do it.
	//this.setMinimumSize(new Dimension(0, 50));
	
	public WorkflowConsolePane() { //UIMAWorkflow workflow) {
		//TODO: Should take what to listen to as an argument?
		
		this.console = new JEditorPane();
		this.console.setContentType(WorkflowConsolePane.CONSOLE_CONTENTTYPE);
		this.console.setEditable(WorkflowConsolePane.CONSOLE_EDITABLE);
		
		this.messages = new ArrayList<ConsoleMessage>();
		
		this.setViewportView(this.console);
		this.setBorder(new TitledBorder(new EtchedBorder(),
				WorkflowConsolePane.BORDER_TITLE));
		this.setHorizontalScrollBarPolicy(
				WorkflowConsolePane.HORIZONTAL_SCROLLBAR_POLICY);
		this.setVerticalScrollBarPolicy(
				WorkflowConsolePane.VERTICAL_SCROLLBAR_POLICY);
		this.setToolTipText(
				WorkflowConsolePane.TOOLTIP_TEXT);
		this.setOpaque(false);//Required for correct display in Mac OSX
	}
	
	private class ConsoleMessage {
		public String text;
		public Date timestamp;
		public boolean isError;
		
		public ConsoleMessage(String text) {
			this(text, false);
		}
		
		public ConsoleMessage(String text, boolean isError) {
			this.text = text;
			this.timestamp = new Date();
			this.isError = isError;
		}
	}

	public void clearConsole() {
		this.messages.clear();
		this.updateConsoleText(); // Listener?
	}
	
	public void addConsoleErrorMessage(String message) {
		this.addConsoleMessage(message, true);
	}
	
	public void addConsoleMessage(String message) {
		this.addConsoleMessage(message, false);
	}
	
	private void addConsoleMessage(String message, boolean isError) {
		this.messages.add(new ConsoleMessage(message, isError));
		this.updateConsoleText(); // Listener?
	}
	
	private void updateConsoleText() {
		//TODO: Reuse previous text?
		String text = "";
		for (ConsoleMessage message: this.messages) {
			String messageText = WorkflowConsolePane.CONSOLE_DATEFORMAT.format(
					message.timestamp) + "<b>:</b> " + message.text + "<br/>";
			if (message.isError) {
				messageText = "<font color=" + WorkflowConsolePane.CONSOLE_ERROR_COLOUR
						+ ">" + messageText + "</font>";
			}
			text += messageText;
		}
		// Decide if the user has affected the current state of the scroll
		boolean scrollToBottom = false;
		//System.out.println(this.verticalScrollBar.getValue());
		//System.out.println(this.verticalScrollBar.getMaximum());
		if (! this.verticalScrollBar.getValueIsAdjusting() && // We are not being dragged
				this.verticalScrollBar.getValue() == this.verticalScrollBar.getMaximum()) {
			scrollToBottom = true;
		}
		
		this.console.setText(text);
		
		if (scrollToBottom) {
			//TODO: Make this work!
			//this.viewport.setViewPosition(new Point(0, 1000));
			//this.verticalScrollBar.setValue(100);//this.verticalScrollBar.getMaximum());
		}
	}
	
	public static void main(String[] argv) {
//        WorkflowConsolePane consolePane = new WorkflowConsolePane();
//
//        for (int i = 0; i < 100; i++) {
//        	consolePane.addConsoleMessage("Message number:" + i + "!");
//        }
//        consolePane.addConsoleErrorMessage("Error!");
//        
//        new TestWindow("WorkflowConsolePane Test", consolePane);
	}
}