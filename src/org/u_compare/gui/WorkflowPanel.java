package org.u_compare.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

@SuppressWarnings("serial")
public class WorkflowPanel extends ComponentPanel {

	public WorkflowPanel(Workflow component,
			ComponentController controller){
		super(controller);
		
		initialConfiguration(component, controller);
		
		component.registerSubComponentsChangedListener(this);
		
		topPanel = new JPanel();
		BorderLayout topLayout = new BorderLayout();
		topPanel.setLayout(topLayout);
		topPanel.setOpaque(false);

		setupInnerPanel();
		
		setupTitlePanel(topPanel);
		this.add(topPanel, BorderLayout.NORTH);
		setupDescriptionPanel(innerPanel);
		setupWorkflowControlPanel(innerPanel);
		
		setupSubComponentsPanel(innerPanel);

		this.add(innerPanel);
		
	}
	
	public void closeWorkflow(){
		
	}
	
}
