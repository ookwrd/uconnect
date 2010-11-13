package org.u_compare.gui.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.control.WorkflowController;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.WorkflowStatusListener;
import org.u_compare.gui.model.Workflow.WorkflowStatus;

/**
 * TODO:
 * 
 * @author luke
 * @version 2010-11-10
 */

@SuppressWarnings("serial")
public class WorkflowControlPanel extends JPanel implements
		WorkflowStatusListener {

	public final static String STATUS_PREFIX = "Status: ";
	private static final int BUTTON_DECREMENT = 0;
	public static final String ICON_RUN_PATH = "../gfx/icon_start.png";
	public static final String ICON_STOP_PATH = "../gfx/icon_stop.png";

	private static boolean iconsLoaded = false;

	private static ImageIcon runIcon;
	private static ImageIcon stopIcon;

	private Workflow component;
	private WorkflowController controller;

	private ActionListener playListener;
	private ActionListener stopListener;

	private JLabel statusLabel;

	private JButton runButton;
	private JButton stopButton;

	public WorkflowControlPanel(Workflow component,
			WorkflowController controller) {
		super();

		this.component = component;
		this.controller = controller;

		playListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playWorkflow();
			}
		};

		stopListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopWorkflow();
			}
		};

		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		statusLabel = new JLabel(STATUS_PREFIX
				+ ((Workflow) component).getStatus());
		this.add(statusLabel);

		this.loadIcons();

		Dimension buttonSize;
		
		//run button
		System.out.println("button");
		runButton = new JButton(runIcon);
		buttonSize = new Dimension(runIcon.getIconWidth() - BUTTON_DECREMENT,
				runIcon.getIconHeight() - BUTTON_DECREMENT);
		System.out.println("button");
		//stopButton.setPreferredSize(buttonSize);
		System.out.println("butn");
		runButton.setFocusPainted(false); // This may be needed for a mac
											// specific behaviou
		System.out.println("tton");
		runButton.addActionListener(playListener);
		this.add(runButton);

		//stop button
		stopButton = new JButton(stopIcon);
		buttonSize = new Dimension(stopIcon.getIconWidth() - BUTTON_DECREMENT,
				stopIcon.getIconHeight() - BUTTON_DECREMENT);
		//stopButton.setPreferredSize(buttonSize);
		stopButton.setFocusPainted(false);
		stopButton.addActionListener(stopListener);
		this.add(stopButton);
		System.out.println("stop button ok");

		component.registerWorkflowStatusListener(this);

		setBorder(new EtchedBorder());
	}

	protected static synchronized void loadIcons() {
		if (WorkflowControlPanel.iconsLoaded == true) {
			return;
		}

		URL image_url;
		image_url = ComponentPanel.class
				.getResource(WorkflowControlPanel.ICON_RUN_PATH);
		assert image_url != null;
		WorkflowControlPanel.runIcon = new ImageIcon(image_url, "Run");

		image_url = ComponentPanel.class
				.getResource(WorkflowControlPanel.ICON_STOP_PATH);
		assert image_url != null;
		WorkflowControlPanel.stopIcon = new ImageIcon(image_url, "Stop");

		WorkflowControlPanel.iconsLoaded = true;
		return;
	}

	private void stopWorkflow() {
		WorkflowStatus currentStatus = ((Workflow) component).getStatus();
		if (currentStatus == WorkflowStatus.READY
				|| currentStatus == WorkflowStatus.PAUSED) {
			controller.workflowPlayRequest();
		} else if (currentStatus == WorkflowStatus.RUNNING
				|| currentStatus == WorkflowStatus.INITIALIZING) {
			controller.workflowPauseRequest();
		}
	}

	private void playWorkflow() {
		controller.workflowStopRequest();
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		statusLabel.setText(STATUS_PREFIX + workflow.getStatus());
		// TODO: Update Buttons
	}

}
