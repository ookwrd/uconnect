package org.u_compare.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.Autoscroll;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.component.WorkflowPanel;
import org.u_compare.gui.model.Workflow;

//TODO is this really needed as a seperate class? Isn't it just to add a scroll pane?

/**
 * Contains all workflow related components.
 * 
 * @author pontus
 * @author olaf
 * @author Luke 
 * @version 2009-08-26
 */

@SuppressWarnings("serial")
public class WorkflowPane extends JScrollPane implements Autoscroll,
		Scrollable, MouseMotionListener {
	private WorkflowPanel topComponent;

	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	private static final String TOOLTIP_TEXT =
		"Drag components from the component library here";

	// TODO: WorkflowTransferManager
	// For dragging, observe the mouse and keep states for it here.
	// Then observe the events and draw accordingly.

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

		System.out.println(this.getAutoscrollInsets());

		/*new DropTarget(this, null);//TODO yeah this...
		
		topComponent.setAutoscrolls(true);
		innerJPanel.setAutoscrolls(true);
		 */
		
		 Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
             @Override
             public void eventDispatched(AWTEvent event) {
                     if(event instanceof MouseEvent){
                             MouseEvent ev = (MouseEvent)event;
                             MouseEvent out =
                            	 SwingUtilities.convertMouseEvent((Component)(ev.getSource()), ev,
                            			 WorkflowPane.this);

                             System.out.println("Output " + out.getX() + " " + out.getY());
                             if(!getBounds().contains(out.getPoint())){//TODO check this
                                     return;
                             }
                     }

             }
		 }, AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
		//Costs extra memory but dramatically improves scroll performance
		this.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE); 
		
		// Let the user scroll by dragging to outside the window.
		this.setAutoscrolls(true); // enable synthetic drag events
		addMouseMotionListener(this); // handle mouse drags

	}

	public ComponentPanel getTopWorkflowComponent() {
		return this.topComponent;
	}

	@Override
	public Insets getAutoscrollInsets() {

		return new Insets(100, 0, 100, 0);
	}

	@Override
	public void autoscroll(Point cursorLocn) {

		Rectangle vis = topComponent.getVisibleRect();
		
		topComponent.scrollRectToVisible(new Rectangle(vis.x, vis.y-20, 1, 1));
		
		System.out.println(cursorLocn.x + "," + cursorLocn.y);
		// TODO why is this here?
	}

	public Workflow getAssociatedWorkflow() {
		return this.topComponent.getWorkflow();
	}

	// methods implementing Scrollable

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		//return super.getPreferredSize();
        return new Dimension(250, 250);
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 100;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	// methods implementing MouseMotionListener

	@Override
	public void mouseDragged(MouseEvent e) {
		// the user is dragging us, so let's scroll !
		Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
		scrollRectToVisible(r);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}