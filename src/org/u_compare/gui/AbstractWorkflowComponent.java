package org.u_compare.gui;

import java.awt.datatransfer.Transferable;

/**
 * Specifies behaviour common to all workflow components.
 * 
 * @author pontus
 * @version 2009-08-26
 */
//XXX: Unused for now, only single class for view right now.
//TODO: Which Java class to extend?
public abstract class AbstractWorkflowComponent implements Transferable {
	//TODO: What else should be added here?
	
	/**
	 * Allows other GUI components to decide wether or not the component can
	 * handle aggregation. If so, the inheriting class should override this method.
	 * 
	 * @return True if the component supports workflow components being added to it.
	 */
	// Replace with a getTransferHandler that returns null?
	public boolean isAggregateComponent() {
		return false;
	}
}