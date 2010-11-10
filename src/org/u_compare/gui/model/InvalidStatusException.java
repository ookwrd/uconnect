package org.u_compare.gui.model;

import org.u_compare.gui.model.Workflow.WorkflowStatus;

/**
 * Exception thrown when an action is performed on the workflow when it is not in a status to process that action.
 * 
 * @author lukemccrohon
 *
 */
@SuppressWarnings("serial")
public class InvalidStatusException extends Exception {
	
	public InvalidStatusException(WorkflowStatus status, WorkflowStatus required){
		super("Invalid workflow status: " + status + ". Status: " + required + " is required to complete this action");
	}

}
