package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DragAndDropController;
import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.guiElements.EditableTextPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.DescriptionChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.Component.FlowControlChangeListener;;

/**
 * Panel for displaying/editing a component/workflow's description.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class ComponentDescriptionPanel extends JPanel implements
		DescriptionChangeListener, LockedStatusChangeListener {

	private final ComponentController controller;
	private final Component component;
	private final EditableTextPanel textPanel;

	public ComponentDescriptionPanel(final ComponentController controller,
			final Component component) {
		super();

		this.controller = controller;
		this.component = component;

		textPanel = new EditableTextPanel(component.getDescription());
		textPanel.registerActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textChangeRequest(textPanel.getContentText());
			}
		});

		if (controller.allowEditing() && !component.isWorkflow()) {
			textPanel.setDragGestureRecognizer(DragAndDropController.registerDragSource(textPanel.getContent(),
					controller));
		}

		setLayout(new BorderLayout());
		setOpaque(false);
		
		if (WorkflowViewerController.SHOW_FLOWCONTROLLER_ICON == true && component.isAggregate() && !component.isWorkflow()) {
			
			JPanel flowControllerPanel = new JPanel();
			flowControllerPanel.setBorder(new TitledBorder(new EtchedBorder(),
					"Flow:"));
			
			//TODO This is where you should add the handling for whatever icons you want.
			final JLabel flowIcon = new JLabel(component.getFlowControllerIdentifier()==null?"Undefined":component.getFlowControllerIdentifier());
			flowControllerPanel.add(flowIcon);
			
			//Make the icon responsive to changes in the flow controller
			component.registerFlowControlChangedListener(new FlowControlChangeListener() {	
				@Override
				public void flowControlChanged(Component component) {
					//TODO You will want to change here as well.
					flowIcon.setText(component.getFlowControllerIdentifier()==null?"Undefined":component.getFlowControllerIdentifier());
				}
			});
			
			this.add(flowControllerPanel, BorderLayout.WEST);
		}
		
		this.add(textPanel, BorderLayout.CENTER);
		
		// Register Listeners
		component.registerComponentDescriptionChangeListener(this);
		component.registerLockedStatusChangeListener(this);
	}

	protected void textChangeRequest(String descriptionText) {
		descriptionText = descriptionText.trim();
		textPanel.setContentText(component.getDescription());
		this.controller.setDescription(descriptionText);
	}

	@Override
	public void ComponentDescriptionChanged(Component component) {
		textPanel.setContentText(component.getDescription());
	}

	@Override
	public void lockStatusChanged(Component component) {
		textPanel.setEnabled(!component.getLockedStatus());
	}

}
