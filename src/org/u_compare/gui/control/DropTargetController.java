package org.u_compare.gui.control;

import org.u_compare.gui.component.SubComponentDropTarget;

/**
 * Controller for a SubComponentDropTarget.
 * 
 * @author Luke McCrohon
 */
public class DropTargetController implements
		DragAndDropController.DropController {

	private ComponentController parent;
	public SubComponentDropTarget view;

	/**
	 * Must also call the set view method.
	 * 
	 * @param parent
	 */
	public DropTargetController(ComponentController parent) {
		this.parent = parent;
	}

	public void setView(SubComponentDropTarget view) {
		this.view = view;
	}

	public DropTargetController(ComponentController parent,
			SubComponentDropTarget view) {
		this.parent = parent;
		this.view = view;
	}

	/**
	 * This drop target is no longer necessary in the view so is deleted.
	 */
	public void dropTargetRemoved() {
		parent.dropTargetRemoved(this);
	}

	@Override
	public void somethingDroppedOnComponent() {
		view.clearDragOverHighlighting();
		parent.somethingDroppedOnChild(this);
	}

	/**
	 * Used to enable highlighting of the drop target based on whether it is a
	 * valid drop location or not.
	 */
	@Override
	public void setDragEnter() {
		if (parent.droppableOnChild(this)) {
			view.highlightMouseDroppable();
		} else {
			view.setDragOverHighlightingUndroppable();
		}
	}

	/**
	 * Clears highlighting of the drop target.
	 */
	@Override
	public void setDragExit() {
		view.clearDragOverHighlighting();
	}

	public void setIntermediate() {
		view.setIntermediate();
	}

	public void setSolitaryDropTarget() {
		view.setSolitaryDropTarget();
	}

	public void clearSolitaryDropTarget() {
		view.clearSolitaryDropTarget();
	}

}
