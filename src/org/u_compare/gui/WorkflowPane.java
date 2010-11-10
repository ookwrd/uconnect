package org.u_compare.gui;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.model.Workflow;

/**
 * Contains all workflow related components.
 * 
 * @author pontus
 * @author olaf
 * @version 2009-08-26
 */

@SuppressWarnings("serial")
public class WorkflowPane extends JScrollPane implements Autoscroll {
	//TODO: Fix border handling, now there are two borders to the layout.
	private ComponentPanel topComponent;
	
	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	//private static final String BORDER_TITLE = "Workflow";
	private static final String TOOLTIP_TEXT =
		"Drag components from the component library here";
	//TODO: LayoutManager
	//TODO: WorkflowTransferManager
	// For dragging, observe the mouse and keep states for it here.
	// Then observe the events and draw accordingly.
	
	public WorkflowPane(ComponentPanel topComponent) {
		this.topComponent = topComponent;

		this.setHorizontalScrollBarPolicy(
				WorkflowPane.HORIZONTAL_SCROLLBAR_POLICY);
		this.setVerticalScrollBarPolicy(
				WorkflowPane.VERTICAL_SCROLLBAR_POLICY);

		//this.setBorder(new TitledBorder(new EtchedBorder(),
		//		WorkflowPane.BORDER_TITLE));
		this.setToolTipText(WorkflowPane.TOOLTIP_TEXT);
		
		/**
		 * Inner panel is needed as the scrollPanelLayout doesn't respect
		 * maximum size constraints on components, and it is not possible
		 * to use another layout directly inside a scroll panel.
		 */
		JPanel innerJPanel = new JPanel();
		innerJPanel.setLayout(new BoxLayout(innerJPanel, BoxLayout.Y_AXIS));
		innerJPanel.add(topComponent);
		this.setViewportView(innerJPanel);
		
		new DnDVerticalScrollerPanel(getViewport());
		
		 MouseMotionListener doScrollRectToVisible = new MouseMotionAdapter() {
		     public void mouseDragged(MouseEvent e) {
		    	 System.out.println("something happening");
		        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
		        ((JPanel)e.getSource()).scrollRectToVisible(r);
		    }
		 };
		 
		 topComponent.setAutoscrolls(true);
		 innerJPanel.setAutoscrolls(true);
		 topComponent.addMouseMotionListener(doScrollRectToVisible);

	}
	
	protected ComponentPanel getTopWorkflowComponent() {
		return this.topComponent;
	}

	@Override
	public Insets getAutoscrollInsets() {

		return new Insets(50, 0, 50, 0);
	}

	@Override
	public void autoscroll(Point cursorLocn) {
		
		System.out.println(cursorLocn.x + "," + cursorLocn.y);
		
	}
	
	public Workflow getAssociatedWorkflow() {
		return this.topComponent.getWorkflow();
	}
}