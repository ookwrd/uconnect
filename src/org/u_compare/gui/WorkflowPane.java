package org.u_compare.gui;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DropTarget;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.component.WorkflowPanel;
import org.u_compare.gui.model.Workflow;

/**
 * View component which contains a single workshop in a scrollpane.
 * 
 * @author Pontus
 * @author Olaf
 * @author Luke 
 * 
 * @version 2009-08-26
 */

@SuppressWarnings("serial")
public class WorkflowPane extends JScrollPane implements Autoscroll {
	
	private static final int AUTOSCROLL_HEIGHT = 100;
	
	private WorkflowPanel topComponent;

	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	private static final String TOOLTIP_TEXT =
		"Drag components from the component library here";

	public WorkflowPane(WorkflowPanel topComponent) {
		this.topComponent = topComponent;

		this.setHorizontalScrollBarPolicy(
				WorkflowPane.HORIZONTAL_SCROLLBAR_POLICY);
		this.setVerticalScrollBarPolicy(
				WorkflowPane.VERTICAL_SCROLLBAR_POLICY);

		this.setToolTipText(WorkflowPane.TOOLTIP_TEXT);

		/**
		 * Inner panel is needed as the scrollPanelLayout doesn't respect
		 * maximum size constraints on components, and it is not possible to use
		 * another layout directly inside a scroll panel.
		 */
		JPanel innerJPanel = new JPanel();
		innerJPanel.setLayout(new BoxLayout(innerJPanel, BoxLayout.Y_AXIS));
		innerJPanel.add(topComponent);
		this.setViewportView(innerJPanel);

		getVerticalScrollBar().setUnitIncrement(8);
		
		topComponent.setAutoscrolls(true);
		innerJPanel.setAutoscrolls(true);
		
		new DropTarget(this, null);//Needed for autoscroll.
		
		//Costs extra memory but dramatically improves scroll performance
		this.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE); 
		
		// Let the user scroll by dragging to outside the window.
		this.setAutoscrolls(true); // enable synthetic drag events
		innerJPanel.setAutoscrolls(true);
	}

	public ComponentPanel getTopWorkflowComponent() {
		return this.topComponent;
	}

	/**
	 * Define area in which to autoscroll while dragging.
	 */
	@Override
	public Insets getAutoscrollInsets() {
		return new Insets(AUTOSCROLL_HEIGHT, 0, AUTOSCROLL_HEIGHT, 0);
	}

	/**
	 * Cause autoscroll to occur when within the defined area.
	 */
	@Override
	public void autoscroll(Point cursorLocn) {
		
		Rectangle vis = topComponent.getVisibleRect();
		
		if(cursorLocn.getY() < AUTOSCROLL_HEIGHT){//Scroll up
			topComponent.scrollRectToVisible(new Rectangle(vis.x, vis.y-20, 1, 1));
		}else if(cursorLocn.getY() > getHeight() - AUTOSCROLL_HEIGHT) {//Scroll down
			topComponent.scrollRectToVisible(new Rectangle(vis.x, vis.y+vis.height+1, 1, 20));
		}
	}

	public Workflow getAssociatedWorkflow() {
		return this.topComponent.getWorkflow();
	}

}