package org.u_compare.gui.control;

import java.awt.Container;
import java.util.ArrayList;
import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.model.AbstractAggregateComponent.InvalidPositionException;
import org.u_compare.gui.model.AbstractComponent.MinimizedStatusEnum;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;

/**
 * 
 * Controller responsible for handling user input directed at a specific component/workflow.
 * 
 * @author Luke McCrohon
 *
 */
public class ComponentController implements DragAndDropController.DragController {

	//The model corresponding to this class
	protected Component component;
	
	//The view corresponding to this class
	protected ComponentPanel componentView;
	
	//The parent controller of this object if it exists
	private ComponentController parent;
	
	private ArrayList<ComponentController> subControllers = new ArrayList<ComponentController>();
	private ArrayList<DropTargetController> dropTargets = new ArrayList<DropTargetController>();
	private ArrayList<ParameterController> parameterControllers = new ArrayList<ParameterController>();
	
	protected final boolean allowEditing;
	
	protected ComponentController(boolean allowEditing){
		this.allowEditing = allowEditing;
	}
	
	/**
	 * Create a controller object for the specified component.
	 * 
	 * @param componentModel
	 * @throws NullPointerException
	 */
	public ComponentController(Component component, boolean allowEditing) {
		
		this.allowEditing = allowEditing;
		
		this.component = component;
		this.componentView = new ComponentPanel(component, this);
		
		if(allowEditing){
			DragAndDropController.registerDragSource(componentView, this);
		}
	}
	
	private void setParent(ComponentController parent){
		this.parent = parent;
	}
	
	/**
	 * Needed so the constructor can collect subviews to pass to view constructor.
	 * 
	 * @return
	 */
	public ComponentPanel getView(){
	
		return this.componentView;
	}
	
	
	public void addFirstDropTarget(DropTargetController control){
		assert(dropTargets.size()==0);
		addDropTarget(control);
	}
	
	/**
	 * Called by componentView to set relevant controllers.
	 * 
	 * @param param
	 */
	public void addParamaterController(ParameterController param){
		parameterControllers.add(param);
	}
	
	public void insert(ComponentController component, DropTargetController following){
		
		addSubComponent(component);
		addDropTarget(following);
	}
	
	/**
	 * For use during building view. Where possible use the insert method for adding a component and its following drop target.
	 * 
	 * @param controller
	 */
	private void addDropTarget(DropTargetController control) {
		
		dropTargets.add(control);
		
		//special formatting for intermediate drop targets
		if (dropTargets.size() > 2) {
			dropTargets.get(dropTargets.size()-2).setIntermediate();
		} else if (dropTargets.size() == 2) {
			dropTargets.get(0).clearSolitaryDropTarget();
		}
		
	}
	
	private void addSubComponent(ComponentController subComponent){
		subControllers.add(subComponent);
		subComponent.setParent(this);
	}
	
	public void resetSubComponents(){
		
		dropTargets = new ArrayList<DropTargetController>();
		subControllers = new ArrayList<ComponentController>();
		
		//TODO do i need to reset subComponents parents?
	}
	
	public boolean isLocked(){
		return component.getLockedStatus();
	}
	
	public void setLocked(boolean lockedStatus){
		if(lockedStatus){
			component.setLocked();
		}else{
			component.setUnlocked();
		}
	}
	
	public void toggleLocked(){
		setLocked(!isLocked());
	}
	
	public void toggleMinimized(){
		switch (component.getMinimizedStatus()) {
		case MINIMIZED:
			component.setMinimizedStatus(MinimizedStatusEnum.PARTIAL);
			break;
		case PARTIAL:
			component.setMinimizedStatus(MinimizedStatusEnum.MAXIMIZED);
			break;
		case MAXIMIZED:
			component.setMinimizedStatus(MinimizedStatusEnum.MINIMIZED);
			break;
		}
	}
	
	/**
	 * Checks if the specified sub component can be added at the specified position. Needed to update graphics when dragging.
	 * 
	 * @return true if it can be added at this location
	 */
	public boolean canAddSubComponent(ComponentController newControl, int position){
		
		if(
				!newControl.canRemove()			//check if newControl is able to be moved from current location
				||	isLocked()					//check if this component is locked.
				||	!component.isAggregate()	//make sure this component is an aggregate
				|| 	isAnscestor(newControl)		//Prohibit dropping of anscestors
			)
		{
			return false;
		}
		
		if(!subControllers.contains(newControl)){
			return ((AggregateComponent)component).canAddSubComponent(newControl.component, position);
		}else{
			return ((AggregateComponent)component).canReorderSubComponent(newControl.component, position);
		}
			
	}
	
	/**
	 * Check whether the specified components is an anscestor of this component.
	 * 
	 * @param comp
	 * @return
	 */
	private boolean isAnscestor(ComponentController comp){
		if(this == comp){
			return true;
		}
		if(parent == null){
			return false;
		}else{
			return parent.isAnscestor(comp);
		}
	}
	
	/**
	 * Check whether this component can be removed from its current position.
	 */
	public boolean canRemove(){
		if(parent != null){
			return parent.canRemoveSubComponent(this);
		}else{
			return !component.isWorkflow();//if a non-workflow component corresponds to not being in a postion
		}
	}
	
	/**
	 * Checks whether the specified subComponent can be removed or not.
	 * 
	 * @param toRemove
	 * @return True if it can be removed, false otherwise.
	 */
	public boolean canRemoveSubComponent(ComponentController toRemove){
		if(!isLocked() && component.isAggregate()){
			return ((AggregateComponent)component).canRemoveSubComponent(toRemove.component);
		}else{
			return false;
		}
	}
	
	
	/**
	 * Called when a component is dragged from either another position in the workflow or from the component library.
	 * 
	 * @param component
	 * @param position
	 * @throws InvalidSubComponentException 
	 */
	public void addSubComponent(ComponentController subComponentController, int position) throws InvalidSubComponentException{
		
		if(canAddSubComponent(subComponentController, position)){
		
			//TODO this method is adding too many copies of the subComponentController
			//Doesn't seem to have any negative affects, but likely to leave 
			//dangling memory references
			
			
		//	System.out.println("start " + subControllers.size());
			
			try {
				if(!subControllers.contains(subComponentController)){
					
				//	System.out.println("mid1 " + subControllers.size());
					subComponentController.removeComponent();

					//System.out.println("mid2 " + subControllers.size());
					//here 
					
					//This seems to be doing an add, which it shouldn't...
					((AggregateComponent)component).addSubComponent(position, subComponentController.component);

					///System.out.println("mid3 " + subControllers.size());
					subComponentController.setParent(this);

					//System.out.println("mid4 " + subControllers.size());
					
					//and here
					subControllers.add(subComponentController);
					//System.out.println("mid5 " + subControllers.size());
					
				}else{

					((AggregateComponent)component).reorderSubComponent(subComponentController.component,position);
				}
			} catch (InvalidPositionException e) {
				//TODO this should never happen
			}
			
		}else{
			throw new InvalidSubComponentException("Cannot add component here\nAggregate:  " + component.isAggregate());
		}	
	//	System.out.println("End " + subControllers.size());
	}
	
	/**
	 * Removes this component from its current position in the model and controller.
	 */
	public void removeComponent(){
		
		//Should never be called on the top level workflow component
		assert(parent != null);
	
		if(canRemove()){
			if(parent != null){//If it has a parent already, remove it
				parent.removeSubComponent(this);
			}
		}
	}
	
	/**
	 * Removes a child component from its current position in the workflow in both controller and model.
	 */
	public void removeSubComponent(ComponentController toRemove){
	
		if(!subControllers.contains(toRemove)){
			System.err.println("ERROR: trying to remove non existing component.");
			Thread.dumpStack();
			return;
		}
		
		//Remove from controller
		toRemove.setParent(null);
		subControllers.remove(toRemove);
		
		//Remove from model
		((AggregateComponent)component).removeSubComponent(toRemove.component);
	}
	
	public void dropTargetRemoved(DropTargetController dropTargetController){
		
		for(int i = 0; i < dropTargets.size(); i++){
			if(dropTargets.get(i).equals(dropTargetController)){
				dropTargets.remove(i);
				break;
			}
		}
		
	}
	
	/**
	 * Responds to a descendant drop target having the currently dragged component dropped on it.
	 * 
	 * @param position The target where the component was dropped.
	 */
	public void somethingDroppedOnChild(DropTargetController position){
	
		if(!droppableOnChild(position)){//Ignore drop
			System.out.println("Ignoring drop on invalid target");
			return;
		}
		
		int index = dropTargetToPosition(position);
		
		try {
			addSubComponent(getCurrentlyDragged(), index);
			System.out.println("just added Subcomponent");
		} catch (InvalidSubComponentException e) {
			System.out.println("Invalid Sub Component Exception");
			e.printStackTrace();
		}
	}
	
	/**
	 * Used for dropping on New Workflow tab
	 */
	public void somethingDroppedOnTop(){
		somethingDroppedOnChild(dropTargets.get(0));
	}
	
	/**
	 * Is it possible to drop the currently dragged component at the specified position?
	 * 
	 * (Used for determining mouse over highlighting when dragging)
	 * 
	 * @param position location to drop at.
	 * @return True if currently dragged component can be dropped there.
	 */
	public boolean droppableOnChild(DropTargetController position) {
		return canAddSubComponent(getCurrentlyDragged(),dropTargetToPosition(position));
	}
	
	@Override
	public void setDragged(){
		componentView.requestFocusInWindow();
		DragAndDropController.getController().setDragged(this);
	}

	/**
	 * Converts a drop target controller to its position index.
	 * 
	 * @param position
	 * @return The index of the specified position.
	 */
	private int dropTargetToPosition(DropTargetController position){
		
		for (int i = 0; i < dropTargets.size(); i++) {
			if(dropTargets.get(i).equals(position)){
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the controller for the currently dragged component.
	 * 
	 * @return Currently dragged component.
	 */
	private ComponentController getCurrentlyDragged(){
			return DragAndDropController.getController().getDraggedComponent();
	}

	/**
	 * Validation needs to be handled at a level higher than the component that is being added/removed from.
	 */
	public void validateWorkflow(){
		if(parent != null){
			parent.validateWorkflow();
		}else{
			
			Container parent = componentView.getParent();
			if(parent!=null){
				parent.validate();	
			}else{
				System.out.println("Component Controller: Validate changes failed as view's parent is null. Should not occur in full system, but may occur during unittests.");
			}
			
		}
	}

	public void setName(String title) {
		
		assert(component.getLockedStatus()==false);

		component.setName(title);
		
	}

	public void setDescription(String descriptionText) {
		assert(component.getLockedStatus()==false);
		//TODO assertion will fail if the component is locked in the middle of editing the text.
		
		component.setDescription(descriptionText);
	}
	
	/**
	 * Used only at construction time
	 * @return
	 */
	public boolean allowEditing(){
		return allowEditing;
	}
}
