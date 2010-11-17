package org.u_compare.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.component.WorkflowControlPanel;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.WorkflowController;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

@SuppressWarnings("serial")
public class WorkflowPanel extends ComponentPanel {

	private WorkflowControlPanel workflowControlPanel;
	
	public WorkflowPanel(Workflow component,
			ComponentController controller, boolean showWorkflowControlPanel, boolean showWorkflowDetails){
		super(controller);
		
		initialConfiguration(component, controller);
		
		component.registerSubComponentsChangedListener(this);
		
		//TODO Luke refactor
		/*topPanel = new JPanel();
		BorderLayout topLayout = new BorderLayout();
		topPanel.setLayout(topLayout);
		topPanel.setOpaque(false);
*/
		setupInnerPanel();
		/*
		if(showWorkflowDetails){
			setupTitlePanel(topPanel, false);
			this.add(topPanel, BorderLayout.NORTH);
			setupDescriptionPanel(innerPanel);
		}
		 */		
		if(showWorkflowControlPanel){
			setupWorkflowControlPanel(innerPanel);
		}
		setupSubComponentsPanel(innerPanel);

		this.add(innerPanel);
		
	}
	
	protected void setupWorkflowControlPanel(JPanel target){
		
		workflowControlPanel = new WorkflowControlPanel((Workflow)component,
				(WorkflowController)controller);
		target.add(workflowControlPanel);
		
	}
	
}
