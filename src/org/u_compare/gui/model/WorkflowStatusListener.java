package org.u_compare.gui.model;

/**
 * 
 * Listener for changes in the status of the workflow processing.
 * 
 * Ready/Initializing/Running/Error etc.
 * 
 * @author lukemccrohon
 *
 */
public interface WorkflowStatusListener {

	public void workflowStatusChanged(UIMAWorkflow workflow);
	
}
