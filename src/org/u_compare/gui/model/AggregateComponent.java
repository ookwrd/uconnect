package org.u_compare.gui.model;

import java.util.ArrayList;

import org.u_compare.gui.model.AbstractAggregateComponent.InvalidPositionException;

/**
 * Interface specifying common features of all aggregate components.
 * 
 * @author Luke McCrohon
 *
 */
public interface AggregateComponent extends Component {

	/**
	 * Adds a new sub component to this aggregate at the specified position.
	 * 
	 * Components already at positions >= position will be shifted along to make room. If the specified component is already a direct subcomponent of this aggregate {@link #reorderSubComponent(int, Component)} should be used instead.
	 * 
	 * @param position	Position at which to add the subcomponent.
	 * @param component	The component to add.
	 */
	public void addSubComponent(int position, Component component) throws InvalidPositionException;
	
	/**
	 * Validates whether the specified subcomponent can be added at the specified position.
	 * 
	 * If the specified component is already a direct subcomponent of this aggregate {@link #canReorderSubComponent(int, Component)} should be used instead.
	 * 
	 * Use {@link #addSubComponent(int, Component)} to actually add.
	 * 
	 * @param component	The component to be added.
	 * @param position	The position to be added to.
	 * @return			True if component can be added, false otherwise.
	 */
	public boolean canAddSubComponent(Component component, int position);
	
	/**
	 * Adds a new sub component at an unspecified position.
	 * 
	 * @param component	The component to add.
	 */
	public void addSubComponent(Component component);
	
	/**
	 * Validates whether the specified subcomponent can be added at an unspecified position.
	 * 
	 * If the specified component is already a direct subcomponent of this aggregate {@link #reorderSubComponent(int, Component)} should be used instead.
	 * 
	 * Use {@link #addSubComponent(Component)} to actually add.
	 * 
	 * @param component	The component to be added.
	 * @param position	The position to be added to.
	 * @return			True if component can be added, false otherwise.
	 */
	public boolean canAddSubComponent(Component component);
	

	/**
	 * Removes the specified component from this aggregates subcomponents.
	 * 
	 * @param component	The component to remove.
	 */
	public void removeSubComponent(Component component);

	/**
	 * Validates whether the specified subcomponent can be removed from this aggregate.
	 * 
	 * Use {@link #removeSubComponent(Component)} to actually delete it.
	 * 
	 * @param component	The component to be deleted.
	 * @return			True if this component can be deleted from subcomponents list.
	 */
	public boolean canRemoveSubComponent(Component component);
	
	/**
	 * Replace existing subcomponents with the specified set of components.
	 * 
	 * @param components The list of subcomponents to replace existing subcomponents with.
	 */
	public void setSubComponents(ArrayList<Component> components);
	
	/**
	 * Validates whether the existing subcomponents can be replaced by the specified set.
	 *
	 * Use {@link #setSubComponents(ArrayList)}} to actually perform the replacement.
	 * 
	 * @param components	Set of Components to replace with.
	 * @return				True if existing components can be replaced, false otherwise.
	 */
	public boolean canSetSubComponents(ArrayList<Component> components);
	
	/**
	 * Reorders the specified component to the specified position in this aggregates subcomponents list.
	 * 
	 * @param component	The component to reorder.
	 * @param position	The position to be reordered to.
	 * @throws InvalidPositionException Thrown when adding to a position outside 0<x<size.
	 */
	public void reorderSubComponent(Component component, int position) throws InvalidPositionException;
	
	/**
	 * Verifies whether the specified component can be relocated to the specified position in this components subcomponents list.
	 * 
	 * Use {@link #reorderSubComponent(Component, int)} to actually perform reordering.
	 * 
	 * @param component	The component to reorder.
	 * @param position	The position to be reordered to.
	 * @return			True if the reordering is possible, false otherwise.
	 */
	public boolean canReorderSubComponent(Component component, int position);
	
	/**
	 * Register a listener to be notified whenever this aggregates subcomponent list changes. 
	 * 
	 * @param listener	Listener to be registered.
	 */
	public void registerSubComponentsChangedListener(SubComponentsChangedListener listener);
	
	public interface SubComponentsChangedListener {
		public void subComponentsChanged();	
	}
}
