package org.u_compare.gui.model;

import java.util.ArrayList;
import org.u_compare.gui.debugging.Debug;
import org.u_compare.gui.model.AbstractUIMAComponent.LockStatusEnum;

/**
 * Abstract base class implementing functionality common to all aggregate components.
 * 
 * @author Luke McCrohon
 */
public abstract class AbstractUIMAAggregateComponent extends
		AbstractUIMAComponent implements UIMAAggregateComponent {
	


	private ArrayList<SubComponentsChangedListener> subComponentAddedRemovedListeners = new ArrayList<SubComponentsChangedListener>();
	
	private ArrayList<UIMAComponent> subComponents = new ArrayList<UIMAComponent>();
	
	/**
	 * All extending classes should call this constructor.
	 * 
	 */
	protected AbstractUIMAAggregateComponent(){
		super();
	}
	
	
	/**
	 * Components extending this class should be aggregates by definition.
	 * 
	 * @return true
	 */
	@Override
	public boolean isAggregate(){
		return true;
	}
	
	
	@Override
	public ArrayList<UIMAComponent> getSubComponents(){
		
		if(subComponents == null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: Abstract getSubComponents method called on " + getName() + " when subComponents has not been initialised.");
			}
			subComponents = new ArrayList<UIMAComponent>();
		}
		return subComponents;
	}
	
	/**
	 * Adds the passed component to the aggregate at the specified position. Position must be between 0 and 
	 * the number of components. If a component is inserted in an intermediary position, subsequent components
	 * will be moved right.
	 *
	 * @param position to insert at
	 * @param component to insert
	 */
	@Override
	public void addSubComponent(int position, UIMAComponent component) throws InvalidPositionException{
		
		//Check subComponents is initialised
		if(subComponents==null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: Abstract addSubComponents method called on " + getName() + " when subComponents has not been initialised.");
			}
			subComponents = new ArrayList<UIMAComponent>();
		}
		
		//Check component is not already a subComponent
		if(subComponents.contains(component)){
			reorderSubComponent(component, position);
			return;
		}
		
		//Check valid position
		int maxPosition = subComponents.size();
		if(position < 0 || position > maxPosition){
			throw new InvalidPositionException(position,maxPosition);
		}
		
		subComponents.add(position, component);
		component.setSuperComponent(this);
		notifySubComponentsChangedListeners();
	}
	

	@Override
	public void reorderSubComponent(UIMAComponent component, int position) throws InvalidPositionException {
		
		//Check subComponents is initialised
		if(subComponents==null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: Abstract reorderSubComponent method called on " + getName() + " when subComponents has not been initialised.");
			}
			subComponents = new ArrayList<UIMAComponent>();
		}
		
		//Check subComponents contains component
		if(!subComponents.contains(component)){
			if(Debug.DEBUGLEVEL >= Debug.ERROR){
				Debug.out.println("Warning: Abstract reorderSubComponent method called on " + getName() + " when component to be added is not in subComponents list.");
			}
			return;
		}
		
		//Check valid position
		int maxPosition = subComponents.size();
		if(position < 0 || position > maxPosition){
			throw new InvalidPositionException(position,maxPosition);
		}
		
		//Check if reordering is necessary
		int currentPosition = subComponents.indexOf(component);
		if(position == currentPosition || position == currentPosition+1){
			return;
		}
		
		if(position<currentPosition){
			subComponents.remove(component);
			subComponents.add(position, component);
		}else {
			subComponents.remove(component);
			subComponents.add(position-1,component);
		}
		
		notifySubComponentsChangedListeners();
	}


	/**
	 * Adds the passed component to the list of subcomponents if it is not already present. Will be added to end by default.
	 * 
	 * @param component to add
	 */
	@Override
	public void addSubComponent(UIMAComponent component){
		
		//Check subComponents is initialised
		if(subComponents==null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: Abstract addSubComponents method called on " + getName() + " when subComponents has not been initialised.");
			}
			subComponents = new ArrayList<UIMAComponent>();
		}
		
		//Check component is not already a subComponent
		if(subComponents.contains(component)){
			return;
		}
		
		subComponents.add(component);
		component.setSuperComponent(this);
		notifySubComponentsChangedListeners();
	}
	
	@Override
	public void removeSubComponent(UIMAComponent component){
		
		if(!subComponents.contains(component)){
			return;
		}
		
		subComponents.remove(component);
		component.setSuperComponent(null);
		notifySubComponentsChangedListeners();
	}
	
	@Override
	public void setSubComponents(ArrayList<UIMAComponent> components){
		
		if(subComponents.equals(components)){
			return;
		}
		
		for(UIMAComponent oldComponent : subComponents){
			oldComponent.setSuperComponent(null);
		}
		
		subComponents = new ArrayList<UIMAComponent>();
		subComponents.addAll(components);
		for(UIMAComponent newComponent : subComponents){
			newComponent.setSuperComponent(this);
		}
		
		notifySubComponentsChangedListeners();
		
	}
	
	@Override
	public void registerSubComponentsChangedListener(SubComponentsChangedListener listener){
		subComponentAddedRemovedListeners.add(listener);	
	}
	
	
	protected void notifySubComponentsChangedListeners(){
	
		setComponentChanged();
		
		for(SubComponentsChangedListener listener : subComponentAddedRemovedListeners){
			listener.subComponentAddRemoved(this.subComponents);
		}
		
	}
	

	//The following methods default to always allowing modifications to the model, override this behaviour when appropriate.
	@Override
	public boolean canAddSubComponent(UIMAComponent component, int position){
		
		return true;
	}
	
	@Override
	public boolean canAddSubComponent(UIMAComponent component){
		return true;
	}
	
	@Override
	public boolean canRemoveSubComponent(UIMAComponent component){
		return true;
	}
	
	@Override
	public boolean canSetSubComponents(ArrayList<UIMAComponent> components){
		return true;
	}

	@Override
	public boolean canReorderSubComponent(UIMAComponent component, int position){
		return true;
	}
	
	@Override
	public void setComponentSaved(){
		if(checkUnsavedChanges()){
			super.setComponentSaved();
			for(UIMAComponent subComponent : subComponents){
				subComponent.setComponentSaved();
			}
		}
	}
	
	@Override
	public void setLocked(){
		setLocked(LockStatusEnum.DIRECTLOCK);
		
		for(UIMAComponent child : subComponents){
			child.indirectlyLocked();
		}
	}
	
	@Override
	public void setUnlocked(){
		setLocked(LockStatusEnum.UNLOCKED);
		
		for(UIMAComponent child : subComponents){
			child.indirectlyUnlocked();
		}
	}
	
	@Override
	public void indirectlyLocked(){
		if(lockStatus!=LockStatusEnum.DIRECTLOCK){
			setLocked(LockStatusEnum.INDIRECTLOCK);
			
			for(UIMAComponent child : subComponents){
				child.indirectlyLocked();
			}
		}
	}
	
	@Override
	public void indirectlyUnlocked(){
		if(lockStatus!=LockStatusEnum.DIRECTLOCK){
			setLocked(LockStatusEnum.UNLOCKED);
			
			for(UIMAComponent child : subComponents){
				child.indirectlyUnlocked();
			}
		}
	}
}
