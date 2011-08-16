package org.u_compare.gui.control;

/**
 * In practice, this interface defines a common controller interface for Drop Targets and Components 
 * 
 * @author olaf
 *
 */
public interface DragAndDropComponentController {

	/**
	 * This component has been dragged. Should notify central component dragged location //TODO is this common behaviour?
	 */
	public void setDragged();
	
	/**
	 * The current dragged component has been dropped on this component.
	 */
	public void somethingDroppedOnComponent();
	
	/**
	 * The currently dragged component is now being dragged over this component.
	 */
	public void setDragEnter();
	
	/**
	 * The currently dragged component is no longer being dragged over this component.
	 */
	public void setDragExit();
	
}