package org.u_compare.gui.component;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DragAndDropController;
import org.u_compare.gui.control.WorkflowController;
import org.u_compare.gui.model.Workflow;

@SuppressWarnings("serial")
public class WorkflowPanel extends ComponentPanel {
	
	public WorkflowPanel(Workflow component,
			ComponentController controller, boolean showWorkflowControlPanel,
				boolean showWorkflowDetails, boolean showSavePanel){
	
		setOpaque(true);
		setLayout(new BorderLayout());
		
		initialConfiguration(component, controller);
		
		component.registerSubComponentsChangedListener(this);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setBorder(new EtchedBorder());
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setBorder(new EtchedBorder());
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
		
		innerPanel = setupCardPanel();
		
		setupSubComponentsPanel(innerPanel);
		
		if(showWorkflowDetails){
			upperPanel.add(getTitlePanel(true),BorderLayout.NORTH);
			upperPanel.add(getDescriptionPanel());
		}
		 		
		if(showWorkflowControlPanel){
			setupBorderPanel(upperPanel,new WorkflowControlPanel((Workflow)component,
					(WorkflowController)controller));
		}
		

		if(showWorkflowDetails || showWorkflowControlPanel){
			this.add(upperPanel,BorderLayout.NORTH);
		}
		this.add(innerPanel, BorderLayout.CENTER);
		
		if(showSavePanel){
			setupBorderPanel(lowerPanel, new WorkflowSavePanel((Workflow)component));
			this.add(lowerPanel, BorderLayout.SOUTH);
		}	
		
		DragAndDropController.registerDragSource(this, controller);
	}
	
	protected void setupBorderPanel(JPanel target, JPanel inner){
		//Necessary due to ComponentPanels LayoutManager 
		JPanel spacer = new JPanel();
		
		JPanel etchedBorder = new JPanel();
		etchedBorder.setLayout(new BorderLayout());
		etchedBorder.setBorder(new EtchedBorder());
		
		etchedBorder.add(inner);
		spacer.add(etchedBorder);
		
		target.add(spacer);
	}
}
