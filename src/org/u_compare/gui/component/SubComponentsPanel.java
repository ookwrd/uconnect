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
	
	//private JPanel contentsPane;
	
	public SubComponentsPanel(Component component, ComponentController controller){
		super();

		BoxLayout aggregateLayout = new BoxLayout(this,
				BoxLayout.Y_AXIS);
		setLayout(aggregateLayout);
		setOpaque(false);
		setBorder(new EmptyBorder(0, AGGREGATE_PADDING,
				0, AGGREGATE_PADDING));
		
		//Setup initial drop target
		DropTargetController initialDropTargetControl = new DropTargetController(
			controller);
		DropTargetJPanel initialDropTarget = new DropTargetJPanel(initialDropTargetControl,true);
		initialDropTargetControl.setView(initialDropTarget);
		controller.addFirstDropTarget(initialDropTargetControl);
		add(initialDropTarget);

		for (Component subModel : component.getSubComponents()) {
			ComponentController subController = new ComponentController(
				subModel);
		
			//Start everything except top level components as minimized
			if(MINIMIZE_SUBCOMPONENTS && !component.isWorkflow()){
				subController.setMinimized(true);
			}//TODO remove this, it overrides maximized things when dragging and dropping
		
			ComponentPanel subView = subController.getView();
			add(subView);
			DropTargetController control = new DropTargetController(
				controller);
			
			DropTargetJPanel following = new DropTargetJPanel(control);
			control.setView(following);
			add(following);
			
			controller.insert(subController, control);
		
		}
	}
	
}
