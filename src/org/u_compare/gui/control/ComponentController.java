package org.u_compare.gui.control;

import java.util.ArrayList;
import org.u_compare.gui.DraggableJPanel;
import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.model.InvalidPositionException;
import org.u_compare.gui.model.InvalidStatusException;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

/**
 * 
 *TODO this needs to be refactored inline with ComponentPanel
 * 
 * Controller responsible for handling userinput directed at a specific component/workflow.
 * 
 * @author luke mccrohon
 *
 */
public class ComponentController implements DragAndDropComponentController {

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
	
	public void toggleMinimized(){
		component.setMinimizedStatus(!component.getMinimizedStatus());
	}
	
	public void setMinimized(boolean minStatus){
		component.setMinimizedStatus(minStatus);
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
		
		if(component.getLockedStatus()){
			return false;
		}
		
		if(parent != null){
			return parent.canRemoveSubComponent(this);
		}else{
			return true;//Corresponds to not being in a postion
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
			
			
			System.out.println("start " + subControllers.size());
			
			try {
				if(!subControllers.contains(subComponentController)){
					
					System.out.println("mid1 " + subControllers.size());
					subComponentController.removeComponent();

					System.out.println("mid2 " + subControllers.size());
					//here 
					
					//This seems to be doing an add, which it shouldn't...
					((AggregateComponent)component).addSubComponent(position, subComponentController.component);

					System.out.println("mid3 " + subControllers.size());
					subComponentController.setParent(this);

					System.out.println("mid4 " + subControllers.size());
					
					//and here
					subControllers.add(subComponentController);
					System.out.println("mid5 " + subControllers.size());
					
				}else{

					((AggregateComponent)component).reorderSubComponent(subComponentController.component,position);
				}
			} catch (InvalidPositionException e) {
				//TODO this should never happen
			}
			
		}else{
			throw new InvalidSubComponentException("Cannot add component here\nAggregate:  " + component.isAggregate() + "etc...");//TODO
		}	
		System.out.println("End " + subControllers.size());
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
		}else{
			//TODO some action
		}
	}
	
	/**
	 * Removes a child component from its current position in the workflow in both controller and model.
	 */
	public void removeSubComponent(ComponentController toRemove){
	
		if(!subControllers.contains(toRemove)){
			return;
			//TODO add debugging info
		}
		
		//Remove from controller
		toRemove.setParent(null);
		subControllers.remove(toRemove);
		
		//Remove from model
		((AggregateComponent)component).removeSubComponent(toRemove.component);
	}
	
	
	/* Do I need this?
	 */
	public void dropTargetRemoved(DropTargetController dropTargetController){
		
		System.out.println();
		
		for(int i = 0; i < dropTargets.size(); i++){
			if(dropTargets.get(i).equals(dropTargetController)){
				dropTargets.remove(i);
				break;
			}
		}
		
	}
	
	/**
	 * Responds to a decendent drop target having the currently dragged component dropped on it.
	 * 
	 * @param position The target where the component was dropped.
	 */
	public void somethingDroppedOnChild(DropTargetController position){
	
		System.out.println("something dropped on component drop target " + dropTargetToPosition(position));
		
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
	 * Responds to the currently dragged Component being dropped directly on this component.
	 * 
	 */
	public void somethingDroppedOnComponent(){
		setDragExit();
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
	
	/**
	 * Is it possible to drop the currently dragged component directly on this component?
	 * 
	 * (Used for determining mouse over highlighting when dragging)
	 * 
	 * @return True if currently dragged component can be dropped here.
	 */
	public boolean droppableOnComponent(){
		//Don't currently support direct drops on components.
		return false;
	}
	
	
	
	/**
	 * Called by the 
	 * 
	 */
	public void setDragged(){
		
		//TODO make this Bubble up the tree instead of using a singleton.
		
		DragAndDropController.getController().setDragged(componentView);//TODO not the view, should pass the model
		
	}
	

	
	public void setDragEnter(){
		
		System.out.println("ComponentDragEnter");
		
		if(droppableOnComponent()){
			componentView.setDragOverHighlightingDroppableLight();
		}else{
			componentView.setDragOverHighlightingUndroppable();
		}
		
		for(DropTargetController child : dropTargets){
			
			//TODO these show be a different kind of highlighting
			if(droppableOnChild(child)){
				child.view.highlightLocationsDroppable();
			}else{
				//TODO
			}
		}
	}
	
	
	
	public void setDragExit(){
		
		componentView.clearDragOverHighlighting();
		
		for(DropTargetController child : dropTargets){
			child.view.clearDragOverHighlighting();
		}
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
		//TODO clean this up.
		
		//TODO memoization
		DragAndDropController dndControl = DragAndDropController.getController();
		DraggableJPanel dragged = dndControl.getDragged();
		return (ComponentController)dragged.getController();
	}

	/**
	 * Validation needs to be handled at a level higher than the component that is being added/removed from.
	 */
	public void validateWorkflow(){
		//componentView.getTopLevelAncestor().validate();
		//Are there disadvantages to pushing validation this high?
		if(parent != null){
			parent.validateWorkflow();
		}else{
			componentView.validate();
		}
	}

	public void setTitle(String title) {
		
		assert(component.getLockedStatus()==false);

		component.setName(title);
		
	}

	public void setDescription(String descriptionText) {
		
		assert(component.getLockedStatus()==false);
		
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
