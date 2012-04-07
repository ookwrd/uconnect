package org.u_compare.gui.guiElements;

import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.Autoscroll;

import javax.swing.SwingUtilities;

import org.u_compare.gui.WorkflowPane;

/**
 * Helper class for adding Autoscroll redirecting to swing components. Required
 * as droptargets at lower levels general catch autoscroll events preventing
 * them from being passed up the component tree to where they are needed. See
 * AutoscrollTextArea and AutoscrollTextField for examples.
 * 
 * @author Luke McCrohon
 * 
 */
public class AutoScrollSupport implements Autoscroll {

	private Container host;

	public AutoScrollSupport(Container host) {
		this.host = host;
	}

	@Override
	public void autoscroll(Point cursorLocn) {
		// Get the workflowPane into which this droptarget is embedded
		// This should be memoized, but care would need to be taken when drop
		// targets are moved between workflows.
		Container parent = host.getParent();
		while (!(parent == null || parent instanceof Autoscroll)) {
			parent = parent.getParent();
		}
		cursorLocn = SwingUtilities.convertPoint(host, cursorLocn, parent);

		if (parent instanceof WorkflowPane) {
			((WorkflowPane) parent).autoscroll(cursorLocn);

		}
	}

	@Override
	public Insets getAutoscrollInsets() {
		// The entire DropTarget is included.
		return new Insets(host.getHeight(), host.getWidth(), host.getHeight(),
				host.getWidth());
	}

}
