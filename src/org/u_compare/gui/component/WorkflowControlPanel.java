package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.control.ComponentController;
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
public class WorkflowControlPanel extends JPanel
	implements WorkflowStatusListener {

	public final static String STATUS_PREFIX = "Status: ";
	
	private Workflow component;
	private ComponentController controller;
	
	private ActionListener playListener;
	private ActionListener stopListener;
	
	private JLabel statusLabel;
	private JButton runButton;
	private JButton stopButton;
	
	public WorkflowControlPanel(Workflow component,
			ComponentController controller) {
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
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		statusLabel = new JLabel(STATUS_PREFIX
				+ ((Workflow)component).getStatus());
		this.add(statusLabel);
			
		runButton = new JButton("start");
		runButton.addActionListener(playListener);
		this.add(runButton);
			
		stopButton = new JButton("stop");
		stopButton.addActionListener(stopListener);
		add(stopButton);
		
		component.registerWorkflowStatusListener(this);
		
		setBorder(new EtchedBorder());
	}
	
	private void stopWorkflow() {
		WorkflowStatus currentStatus = ((Workflow)component).getStatus();
		if(currentStatus == WorkflowStatus.READY
				|| currentStatus == WorkflowStatus.PAUSED) {
			controller.workflowPlayRequest();
		}
		else if (currentStatus == WorkflowStatus.RUNNING
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
		//TODO: Update Buttons
	}
	
}
