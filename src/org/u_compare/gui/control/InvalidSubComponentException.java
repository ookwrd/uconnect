package org.u_compare.gui.control;

/**
 * Thrown when attempting to add a subcomponent in the wrong place.
 * 
 * @author luke mccrohon
 * 
 *         TODO integrate with another class.
 */
@SuppressWarnings("serial")
public class InvalidSubComponentException extends Exception {

	public InvalidSubComponentException(String msg) {
		super(msg);
	}

}
