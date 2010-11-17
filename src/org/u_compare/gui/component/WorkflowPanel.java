package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

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
		

		
		setupInnerPanel();
		
		if(showWorkflowDetails){
			setupTopPanel(this, true);
			setupDescriptionPanel(innerPanel);
		}
		 		
		if(showWorkflowControlPanel){
			setupWorkflowControlPanel(innerPanel);
		}
		setupSubComponentsPanel(innerPanel);

		this.add(innerPanel);
		
	}
	
	protected void setupWorkflowControlPanel(JPanel target){
		
		//Necessary due to ComponentPanels LayoutManager 
		JPanel spacer = new JPanel();
		
		JPanel etchedBorder = new JPanel();
		etchedBorder.setLayout(new BorderLayout());
		etchedBorder.setBorder(new EtchedBorder());
		
		workflowControlPanel = new WorkflowControlPanel((Workflow)component,
				(WorkflowController)controller);
		etchedBorder.add(workflowControlPanel);

		spacer.add(etchedBorder);
		
		target.add(spacer);
	}
	
}
