package org.u_compare.gui.control;

import java.util.ArrayList;
import java.util.Iterator;

import org.u_compare.gui.DraggableJPanel;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.WorkflowComponent;
import org.u_compare.gui.model.InvalidPositionException;
import org.u_compare.gui.model.InvalidStatusException;
import org.u_compare.gui.model.UIMAAggregateComponent;
import org.u_compare.gui.model.UIMAComponent;
import org.u_compare.gui.model.UIMAWorkflow;
import org.u_compare.gui.model.parameters.Parameter;

/**
 * 
 * Controller responsible for handling userinput directed at a specific component/workflow.
 * 
 * @author luke mccrohon
 *
 */
public class ComponentController implements DragAndDropComponentController {

	//The model corresponding to this class
	private UIMAComponent component;
	
	//The view corresponding to this class
	private WorkflowComponent componentView;
	
	//The parent controller of this object if it exists
	private ComponentController parent;
	
	private ArrayList<ComponentController> subControllers = new ArrayList<ComponentController>();
	private ArrayList<DropTargetController> dropTargets = new ArrayList<DropTargetController>();
	private ArrayList<ConfigController> parameterControllers = new ArrayList<ConfigController>();
	
	//private boolean allowChanges = true;
	
	/**
	 * Create a controller object for the specified component.
	 * 
	 * @param componentModel
	 * @throws NullPointerException
	 */
	public ComponentController(UIMAComponent component) {
		
		this.component = component;
		
		/*ArrayList<WorkflowComponent> subViews = new ArrayList<WorkflowComponent>();
		for (UIMAComponent subModel : component.getSubComponents()) {
			ComponentController subController = new ComponentController(subModel);
			subViews.add(subController.getView());
			subControllers.add(subController);
			
			subController.setParent(this);
		}*/
		
		ArrayList<ParameterPanel> paramViews = new ArrayList<ParameterPanel>();
		for (Parameter param : component.getConfigurationParameters()){
			ConfigController paramController = ConfigControllerFactory.getController(this, param);
			paramViews.add(paramController.getView());
			parameterControllers.add(paramController);
		}
		
		this.componentView = new WorkflowComponent(/*subViews,*/ paramViews, component, this);
		
	}
	
	private void setParent(ComponentController parent){
		this.parent = parent;
	}
	
	/**
	 * Needed so the constructor can collect subviews to pass to view constructor.
	 * 
	 * @return
	 */
	public WorkflowComponent getView(){
	
		return this.componentView;
	}
	
	
	public void addFirstDropTarget(DropTargetController control){
		assert(dropTargets.size()==0);
		addDropTarget(control);
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
		component.setLockedStatus(lockedStatus);
	}
	
	public void toggleMinimized(){
		component.setMinimizedStatus(!component.getMinimizedStatus());
	}
	
	/**
	 * Checks if the specified sub component can be added at the specified position. Needed to update graphics when dragging.
	 * 
	 * @return true if it can be added at this location
	 */
	public boolean canAddSubComponent(ComponentController newControl, int position){
		
		//TODO prohibit dropping of self on descendents
		
		if(!isLocked() && component.isAggregate()){
			if(!subControllers.contains(newControl)){
				return ((UIMAAggregateComponent)component).canAddSubComponent(newControl.component, position);
			}else{
				return ((UIMAAggregateComponent)component).canReorderSubComponent(newControl.component, position);
			}
		}else{
			return false;
		}
		
	}
	
	/**
	 * Check whether this component can be removed from its current position.
	 */
	public boolean canRemove(){
		
		//TODO check editable.
		if(parent != null){
			return parent.canRemoveSubComponent(this);
		}else{
			return true;
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
			return ((UIMAAggregateComponent)component).canRemoveSubComponent(toRemove.component);
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
	public void addSubComponent(ComponentController subComponent, int position) throws InvalidSubComponentException{
		
		if(component.isAggregate() && canAddSubComponent(subComponent, position) && subComponent.canRemove()){
		
			try {
				if(!subControllers.contains(subComponent)){
					subComponent.removeComponent();
					((UIMAAggregateComponent)component).addSubComponent(position, subComponent.component);
					subComponent.setParent(this);
					subControllers.add(subComponent);
				}else{
					((UIMAAggregateComponent)component).reorderSubComponent(subComponent.component,position);
				}
			} catch (InvalidPositionException e) {
				//TODO this should never happen
			}
			
		}else{
			throw new InvalidSubComponentException("Cannot add component here\nAggregate:  " + component.isAggregate() + "etc...");//TODO
		}	
	}
	
	/**
	 * Removes this component from its current position in the model and controller.
	 */
	public void removeComponent(){
		
		if(parent == null){
			//Should never be called on the top level workflow component, so parent should not be null.
			//TODO debug info
			return;
		}
		
		if(canRemove()){
			parent.removeSubComponent(this);
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
		((UIMAAggregateComponent)component).removeSubComponent(toRemove.component);
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
		
		if(!droppableOn(position)){//Ignore drop
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
	public void setDropLocation(){
		
		//TODO

		System.out.println("Controller: something dropped on component.");
		
		
		
		setDragExit();
	}
	
	
	/**
	 * Called by the 
	 * 
	 */
	public void setDragged(){
		
		System.out.println("Controller: component being dragged.");
		
		//TODO make this Bubble up the tree instead of using a singleton.
		
		DragAndDropController.getController().setDragged(componentView);//TODO not the view, should pass the model
		
	}
	

	
	public void setDragEnter(){
		
		System.out.println("ComponentDragEnter");
		
		if(droppableOnComponent()){
			componentView.setDragOverHighlightingDroppable();
		}else{
			componentView.setDragOverHighlightingUndroppable();
		}
		
		for(DropTargetController child : dropTargets){
			
			//TODO these show be a different kind of highlighting
			if(droppableOn(child)){
				child.view.setDragOverHighlightingDroppable();
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
	 * Is it possible to drop the currently dragged component at the specified position?
	 * 
	 * (Used for determining mouse over highlighting when dragging)
	 * 
	 * @param position location to drop at.
	 * @return True if currently dragged component can be dropped there.
	 */
	public boolean droppableOn(DropTargetController position) {
		
		System.out.println("Hovering over drop target:" + dropTargetToPosition(position));
		
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

		System.out.println("Hovering over drop component");
		
		/* Check to see if it can be dropped on any of the 
		 * descendant drop targets, return true if it can
		 * false otherwise. 
		 */
		for(int i = 0; i < dropTargets.size();i++){
			canAddSubComponent(getCurrentlyDragged(),i);
		}
		
		//TODO make this compatible with the actual drop method
		
		return false;
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
		// TODO implement this method (and make the corresponding corrections to the component)
		
		//TODO what happens if title can't be changed?
	}

	public void setDescription(String descriptionText) {
		// TODO implement this method (and make the corresponding corrections to the component)
		
		
		//TODO what happens if description cant be changed?
	}
	
	
	/**
	 * 
	 * 
	 * TODO I think we need to abstract all this out to a workflow controller class
	 * 
	 */
	public void workflowPlayRequest(){

		assert(component.isWorkflow());
	
		try {
			((UIMAWorkflow)component).runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void workflowPauseRequest(){
		
		assert(component.isWorkflow());
		
		try {
			((UIMAWorkflow)component).runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void workflowStopRequest(){
		
		assert(component.isWorkflow());
		
		try {
			((UIMAWorkflow)component).runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
