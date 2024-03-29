package org.u_compare.gui;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

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
public class ConsolePane extends JScrollPane implements WorkflowStatusListener,
		WorkflowMessageListener {

	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	private static final String TOOLTIP_TEXT = "Displays output related to this workflow";

	private static final String CONSOLE_CONTENTTYPE = "text/html";
	private static final DateFormat CONSOLE_DATEFORMAT = new SimpleDateFormat(
			"HH:mm:ss");
	private static final Color CONSOLE_ERROR_COLOUR = Color.red;
	private static final Color CONSOLE_DEFAULT_TEXT_COLOUR = Color.black;
	private static final Color CONSOLE_BACKGROUND_COLOR = Color.white;

	/**
	 * Change this value to disable the inclusion of a timestamp in messages.
	 */
	public boolean SHOW_TIMESTAMP = true;

	private final String WORKFLOW_STATUS_MSG_BASE = "Workflow status change to: ";

	private JTextPane console;
	private ArrayList<ConsoleMessage> messages = new ArrayList<ConsoleMessage>();
	private StyleContext sc;

	public ConsolePane(Workflow workflow) {
		workflow.registerWorkflowStatusListener(this);
		workflow.registerWorkflowMessageListener(this);

		this.console = new JTextPane();

		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		this.console.setContentType(ConsolePane.CONSOLE_CONTENTTYPE);
		this.console.setBackground(ConsolePane.CONSOLE_BACKGROUND_COLOR);

		this.setViewportView(this.console);
		this.setHorizontalScrollBarPolicy(ConsolePane.HORIZONTAL_SCROLLBAR_POLICY);
		this.setVerticalScrollBarPolicy(ConsolePane.VERTICAL_SCROLLBAR_POLICY);
		this.setToolTipText(ConsolePane.TOOLTIP_TEXT);

		sc = StyleContext.getDefaultStyleContext();

		this.setOpaque(false);
	}

	public class ConsoleMessage {
		String text;
		Date timestamp;
		boolean isError;

		public ConsoleMessage(String text, boolean isError) {
			this.text = text;
			this.timestamp = new Date();
			this.isError = isError;
		}
	}

	private void addConsoleMessage(String messageIn, boolean isError) {
		ConsoleMessage message = new ConsoleMessage(messageIn, isError);
		this.messages.add(message);

		console.setCaretPosition(console.getDocument().getLength());

		AttributeSet aset = console.getCharacterAttributes();
		if (isError) {
			aset = sc.addAttribute(aset, StyleConstants.Foreground,
					ConsolePane.CONSOLE_ERROR_COLOUR);
		} else {
			aset = sc.addAttribute(aset, StyleConstants.Foreground,
					ConsolePane.CONSOLE_DEFAULT_TEXT_COLOUR);
		}
		AttributeSet asetBold = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Bold, true);

		if (SHOW_TIMESTAMP) {
			console.setCharacterAttributes(aset, true);
			console.replaceSelection(ConsolePane.CONSOLE_DATEFORMAT
					.format(message.timestamp));

			console.setCharacterAttributes(asetBold, false);
			console.replaceSelection(": ");
		}

		console.setCharacterAttributes(aset, true);
		console.replaceSelection(messageIn + "\n");
	}

	public void clearConsole() {
		this.messages.clear();
		this.console.setText("");
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		if (workflow.getStatus() == WorkflowStatus.ERROR) {
			addConsoleErrorMessage(WORKFLOW_STATUS_MSG_BASE
					+ workflow.getStatus());
		} else {
			addConsoleMessage(WORKFLOW_STATUS_MSG_BASE + workflow.getStatus());
		}
	}

	public void addConsoleErrorMessage(String message) {
		this.addConsoleMessage(message, true);
	}

	public void addConsoleMessage(String message) {
		this.addConsoleMessage(message, false);
	}

	@Override
	public void workflowMessageSent(Workflow workflow, String message) {
		if (workflow.getStatus() == WorkflowStatus.ERROR) {
			addConsoleErrorMessage(message);
		} else {
			addConsoleMessage(message);
		}
	}

	public static void main(String[] argv) {
		ConsolePane consolePane = new ConsolePane(new Workflow());
		consolePane.addConsoleMessage("Testing the ConsolePane...");

		UCompareWindow testWindow = new UCompareWindow(
				"WorkflowConsolePane Test", consolePane);
		testWindow.setVisible(true);

		for (int i = 0; i < 1000; i++) {
			consolePane.addConsoleMessage("Message number:" + i + "!");
		}
		consolePane.addConsoleErrorMessage("Error!");

		consolePane.clearConsole();
	}
}