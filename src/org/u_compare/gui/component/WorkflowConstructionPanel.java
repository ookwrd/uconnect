package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.WorkflowController;
import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.guiElements.DragAndDrop;
import org.u_compare.gui.model.Workflow;

@SuppressWarnings("serial")
public class WorkflowConstructionPanel extends ComponentPanel {

	private WorkflowControlPanel workflowControlPanel;
	private WorkflowSavePanel workflowSavePanel;
	
	public WorkflowConstructionPanel(Workflow component,
			ComponentController controller, boolean showWorkflowControlPanel,
				boolean showWorkflowDetails, boolean showSavePanel){
	
		setOpaque(true);
		setBackground(Color.GREEN);
		
		setLayout(new BorderLayout());
		
		initialConfiguration(component, controller);
		
		component.registerSubComponentsChangedListener(this);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setBorder(new EtchedBorder());
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setBorder(new EtchedBorder());
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
		
		setupInnerPanel();
		
		if(showWorkflowDetails){
			setupTopPanel(upperPanel, true);
			setupDescriptionPanel(upperPanel);
		}
		 		
		if(showWorkflowControlPanel){
			setupBorderPanel(upperPanel,new WorkflowControlPanel((Workflow)component,
					(WorkflowController)controller));
		}
		
		setupSubComponentsPanel(innerPanel);

		if(showWorkflowDetails || showWorkflowControlPanel){
			this.add(upperPanel,BorderLayout.NORTH);
		}
		this.add(innerPanel, BorderLayout.CENTER);
		
		if(showSavePanel){
			setupBorderPanel(lowerPanel, new WorkflowSavePanel((Workflow)component));
			this.add(lowerPanel, BorderLayout.SOUTH);
		}	
		
		DragAndDrop.registerDragSource(this, controller);
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
