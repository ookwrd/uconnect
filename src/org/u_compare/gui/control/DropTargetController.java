package org.u_compare.gui.control;

import org.u_compare.gui.DropTargetJPanel;

public class DropTargetController implements DragAndDropComponentController {

	private ComponentController parent;
	public DropTargetJPanel view;
	
	/**
	 * Must also call the set view method.
	 * 
	 * @param parent
	 */
	public DropTargetController(ComponentController parent){
		this.parent = parent;
	}
	
	public void setView(DropTargetJPanel view) {
		this.view = view;
	}
	
	public DropTargetController(ComponentController parent, DropTargetJPanel view){
		this.parent = parent;
		this.view = view;
	}
	
	/**
	 * This drop target is no longer necessary in the view so is deleted.
	 */
	public void dropTargetRemoved(){
		parent.dropTargetRemoved(this);
	}

	@Override
	public void setDragged() {
	
		// TODO throw an error	
	}

	@Override
	public void somethingDroppedOnComponent() {
		
		parent.somethingDroppedOnChild(this);
		
	}

	@Override
	public void setDragEnter() {

		if(parent.droppableOnChild(this)){
			view.setDragOverHighlightingDroppable();
		}else{
			view.setDragOverHighlightingUndroppable();
		}
		
	}

	@Override
	public void setDragExit() {

		view.clearDragOverHighlighting();
		
	}

	public void setIntermediate() {
		
		view.setIntermediate();
		
	}


	
}
