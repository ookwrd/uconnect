package org.u_compare.gui.model;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.Parameter;

/**
 * Interface defining required features of model classes representing components.
 * 
 * See also UIMAAggregateComponent.
 * 
 * See also AbstractUIMAComponent.
 * 
 * @author Luke Mccrohon
 */
public interface Component{

	
	/**
	 * Checks if the Component is an aggregate or not.
	 * 
	 * @return	True if this component is an aggregate component, false otherwise.
	 */
	public boolean isAggregate();
	
	/**
	 * Get a list of this components sub components. 
	 * 
	 * Will return an empty list if this component is not an aggregate.
	 * 
	 * @return	An arraylist containing this components direct subcomponents. 
	 */
	public ArrayList<Component> getSubComponents();
	
	/**
	 * Checks if this component represents a workflow. 
	 * 
	 * @return	 True if this component represents a workflow.
	 */
	public boolean isWorkflow();
	
	/**
	 * Get the super component of this component.
	 * 
	 * @return	This components superComponent if one exists, null otherwise.
	 */
	public Component getSuperComponent();
	
	/**
	 * Set the super component of this component.
	 * 
	 * Setting the super component to null is equivalent to saying it has no super component.
	 * 
	 * @param superComponent	The new super component.
	 */
	public void setSuperComponent(Component superComponent);
	
	/**
	 * Return the name of this component.
	 * 
	 * Changes can be followed by {@link #registerComponentDescriptionChangeListener(DescriptionChangeListener)}.
	 *  
	 * @return 	This component's name.
	 */
	public String getName();
	
	/**
	 * Sets the name of this component to the specified name.
	 * 
	 * @param name	 The new name.
	 */
	public void setName(String name);
	
	/**
	 * Return the description of this component.
	 *
	 * Changes can be followed by {@link #registerComponentDescriptionChangeListener(DescriptionChangeListener)}.
	 * 
	 * @return	The description of this component.
	 */
	public String getDescription();
	
	/**
	 * Sets the description of this component to the specified description. 
	 * 
	 * @param description	The new description.
	 */
	public void setDescription(String description);
	
	/**
	 * Registers a new DescriptionChangeListener component to be notified if the name or description are changed.
	 * 
	 * @param listener	The listener to add.
	 */
	public void registerComponentDescriptionChangeListener(DescriptionChangeListener listener);
	
	/**
	 * Adds an input type to this component.
	 * 
	 * @param inputType	Type to be added.
	 */
	public void addInputType(AnnotationType inputType);
	
	public void removeInputType(AnnotationType inputType);
	
	public void setInputTypes(ArrayList<AnnotationType> inputTypes);
	
	public void addOutputType(AnnotationType outputType);
	
	public void removeOutputType(AnnotationType outputType);
	
	public void setOutputTypes(ArrayList<AnnotationType> outputTypes);
	
	/**
	 * Gets list of this components output types.
	 * 
	 * Changes can be followed via {@link #registerInputOutputChangeListener(InputOutputChangeListener)}
	 * 
	 * @return	List of output types.
	 */
	public ArrayList<AnnotationType> getOutputTypes();

	/**
	 * Gets list of this components input types.
	 * 
	 * Changes can be followed via {@link #registerInputOutputChangeListener(InputOutputChangeListener)}
	 * 
	 * @return	List of this components input types.
	 */
	public ArrayList<AnnotationType> getInputTypes();
	
	/**
	 * Register listener to be notified of changes to Input and Output lists.
	 * 
	 * @param listener	Listener to be added.
	 */
	public void registerInputOutputChangeListener(InputOutputChangeListener listener);
	
	/**
	 * Set this component as having been changed since saving.
	 */
	public void setComponentChanged();
	
	/**
	 * Set this component as having been saved in current state.
	 */
	public void setComponentSaved();
	
	/**
	 * Check whether this component has any unsaved changes.
	 * 
	 * Changes can be followed via {@link #registerSavedStatusChangeListener(SavedStatusChangeListener)}
	 * 
	 * @return	True if unsaved changes exist, false otherwise.
	 */
	public boolean checkUnsavedChanges();
	
	
	public boolean getMinimizedStatus();
	
	public void setMinimizedStatus(boolean minimized);
	
	
	public boolean getLockedStatus();
	
	public void setLocked();
	
	public void setUnlocked();
	
	public void indirectlyLocked();
	
	public void indirectlyUnlocked();
	
	/**
	 * Register listener to be notified of changes to the UnsavedChanges status of this component.
	 * 
	 * @param listener	Listener to be added.
	 */
	public void registerSavedStatusChangeListener(SavedStatusChangeListener listener); 
	
	/**
	 * Get a list of this components configuration parameters.
	 * 
	 * @return A list of this components configuration parameters.
	 */
	public ArrayList<Parameter> getConfigurationParameters();
	
	/**
	 * Sets this components list of configuration parameters.
	 * 
	 * @param params	The configuration parameters to set.
	 */
	public void setConfigurationParameters(ArrayList<Parameter> params);
	
	/**
	 * Register listener to be notified when the parameter settings of this component are altered.
	 * 
	 * @param listener
	 */
	public void registerParameterSettingsChangeListener(ParameterSettingsChangeListener listener);

	public void registerMinimizedStatusChangeListener(MinimizedStatusChangeListener listnener);
	
	public void registerLockedStatusChangeListener(LockedStatusChangeListener listener);
}