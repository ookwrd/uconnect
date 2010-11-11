package org.u_compare.gui;

import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

@SuppressWarnings("serial")
public class WorkflowPanel extends ComponentPanel {

	public WorkflowPanel(Workflow component,
			ComponentController controller){
		super(controller);
		
		initialConfiguration(component, controller);
		
		component.registerSubComponentsChangedListener(this);
		
		
		
		
		
	}
	
}
