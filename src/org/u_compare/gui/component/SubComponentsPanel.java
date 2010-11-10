package org.u_compare.gui.component;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.DropTargetJPanel;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DropTargetController;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class SubComponentsPanel extends JPanel {

	private static final int AGGREGATE_PADDING = 2;
	private static final boolean MINIMIZE_SUBCOMPONENTS = true;
	
	private JPanel contentsPane;
	
	public SubComponentsPanel(Component component, ComponentController controller){
		super();
		
		setLayout(new GridLayout());
		setOpaque(false);
		
		//TODO the border setting shouldn't be handled here. This should just be the contentsPane
		
		// Workflow have no border or title around their subcomponents
		if (!component.isWorkflow()) {
			setBorder(new TitledBorder("Subcomponents:"));
		}

		contentsPane = new JPanel();
		BoxLayout aggregateLayout = new BoxLayout(contentsPane,
				BoxLayout.Y_AXIS);
		contentsPane.setLayout(aggregateLayout);
		contentsPane.setOpaque(false);
		contentsPane.setBorder(new EmptyBorder(0, AGGREGATE_PADDING,
				0, AGGREGATE_PADDING));
		add(contentsPane);

		//Setup initial drop target
		DropTargetController initialDropTargetControl = new DropTargetController(
			controller);
		DropTargetJPanel initialDropTarget = new DropTargetJPanel(initialDropTargetControl,true);
		initialDropTargetControl.setView(initialDropTarget);
		controller.addFirstDropTarget(initialDropTargetControl);
		contentsPane.add(initialDropTarget);

		for (Component subModel : component.getSubComponents()) {
			ComponentController subController = new ComponentController(
				subModel);
		
			//Start everything except top level components as minimized
			if(MINIMIZE_SUBCOMPONENTS && !component.isWorkflow()){
				subController.setMinimized(true);
			}//TODO remove this, it overrides maximized things when dragging and dropping
		
			ComponentPanel subView = subController.getView();
			contentsPane.add(subView);
			DropTargetController control = new DropTargetController(
				controller);
			
			DropTargetJPanel following = new DropTargetJPanel(control);
			control.setView(following);
			contentsPane.add(following);
			
			controller.insert(subController, control);
		
		}
	}
	
}
