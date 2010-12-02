package org.u_compare.gui.model;

/**
 * Exception thrown following attempt to add component to invalid location of an aggregate component.
 * 
 * See UIMAAggregateComponent
 * 
 * @author lukemccrohon
 */
@SuppressWarnings("serial")
public class InvalidPositionException extends Exception {

	/**
	 * Construct new exception. Requires the invalid position, and the maxPosition possible 
	 * in the destination component.
	 * 
	 * @param position
	 * @param maxPosition
	 */
	public InvalidPositionException(int position, int maxPosition){
		
		super("Tried to add component in position " + position + ", valid positions are 0<=x<=" + maxPosition);
		
	}
}
