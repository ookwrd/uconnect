package org.u_compare.gui.control;

/**
 * Thrown when attempting to add a subcomponent
 * 
 * @author luke mccrohon
 *
 */
@SuppressWarnings("serial")
public class InvalidSubComponentException extends Exception {

	public InvalidSubComponentException(String msg){
		super(msg);
	}
	
}
