package org.u_compare.gui.model.parameters.constraints;

@SuppressWarnings("serial")
public class ConstraintFailedException extends Exception {

	private String msgString;
	
	public ConstraintFailedException(String msgString){
		this.msgString = msgString;
	}
	
	public ConstraintFailedException(String msgString, Throwable cause){
		super(cause);
		this.msgString = msgString;
	}
	
	public String getUserReadableError(){
		return msgString;
	}
	
}
