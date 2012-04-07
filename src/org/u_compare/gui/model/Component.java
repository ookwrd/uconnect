package org.u_compare.gui.model;

import java.util.ArrayList;

import org.apache.uima.resource.ResourceSpecifier;
import org.u_compare.gui.model.AbstractComponent.MinimizedStatusEnum;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterGroup;

/**
 * Interface defining required features of model classes representing components.
 * 
 * See also UIMAAggregateComponent.
 * 
 * See also AbstractUIMAComponent.
 * 
 * @author Luke McCrohon
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
	
	public String getImplementationName();
	public void setImplementationName(String implementationName);
	
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
	 * Registers a new DescriptionChangeListener component to be notified if the title or description are changed.
	 * 
	 * @param listener	The listener to add.
	 */
	public void registerComponentDescriptionChangeListener(DescriptionChangeListener listener);

	public interface DescriptionChangeListener {
		public void ComponentDescriptionChanged(Component component);	
	}
	
	public String getVendor();
	public void setVendor(String vendor);
	
	public String getCopyright();
	public void setCopyright(String copyright);
	
	public String getVersion();
	public void setVersion(String version);
	
	public void registerDistibutionInformationChangedListener(DistributionInformationChangeListener listener);
	
	public interface DistributionInformationChangeListener {
		public void distributionInformationChanged(Component component);
	}
	
	public void setLanguageTypes(ArrayList<String> languages);
	public void addLanguageType(String language);
	public void removeLanguageType(String language);
	public ArrayList<String> getLanguageTypes();
	
	public void registerLanguagesChangeListener(LanguageChangeListener listener);
	
	public interface LanguageChangeListener {
		public void languagesChanged(Component component);	
	}
	
	/**
	 * Adds an input type to this component.
	 * 
	 * @param inputType	Type to be added.
	 */
	public void addInputType(AnnotationTypeOrFeature inputType);
	public void removeInputType(AnnotationTypeOrFeature inputType);
	public void setInputTypes(ArrayList<AnnotationTypeOrFeature> inputTypes);
	public ArrayList<AnnotationTypeOrFeature> getInputTypes();
	
	public void addOutputType(AnnotationTypeOrFeature outputType);
	public void removeOutputType(AnnotationTypeOrFeature outputType);
	public void setOutputTypes(ArrayList<AnnotationTypeOrFeature> outputTypes);
	public ArrayList<AnnotationTypeOrFeature> getOutputTypes();
	
	public void registerInputOutputChangeListener(InputOutputChangeListener listener);
	
	public interface InputOutputChangeListener {
		public void inputOutputChanged(Component component);	
	}
	
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
	
	
	public MinimizedStatusEnum getMinimizedStatus();
	
	public void setMinimizedStatus(MinimizedStatusEnum minimized);
	
	
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
	
	public interface SavedStatusChangeListener {
		public void savedStatusChanged(Component component);	
	}
	
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
	
	public ArrayList<Parameter> getCommonParameters();
	public void setCommonParameters(ArrayList<Parameter> params);
	
	/**
	 * Register listener to be notified when the parameter list of this component is altered.
	 * 
	 * @param listener
	 */
	public void registerParametersChangedListener(ParametersChangedListener listener);

	public interface ParametersChangedListener {
		public void parametersChanged(Component component);
	}
	
	public void registerMinimizedStatusChangeListener(MinimizedStatusChangeListener listnener);
	
	public interface MinimizedStatusChangeListener {
		public void minimizedStatusChanged(Component component);
	}
	
	public void registerLockedStatusChangeListener(LockedStatusChangeListener listener);
	public void registerParentLockedStatusChangeListener(LockedStatusChangeListener listener);
	
	//Only to be called by parents
	void notifyParentLockedStatusChangeListeners(Component parent);
	
	public interface LockedStatusChangeListener {
		public void lockStatusChanged(Component component);	
	}
	
	public String getParameterSearchStratergy();
	public void setParameterSearchStratergy(String stratergy);
	
	public String getDefaultParameterGroup();
	public void setDefaultParameterGroup(String defaultGroup);
	
	public void registerParameterConfigurationChangeListener(ParameterConfigurationChangeListener listener);
	
	public interface ParameterConfigurationChangeListener{
		public void parameterConfigurationChanged(Component component);
	}
	
	public ArrayList<ParameterGroup> getParameterGroups();
	public void setParameterGroups(ArrayList<ParameterGroup> parameterGroups);
	
	public void registerParameterGroupsChangeListener(ParameterGroupsChangeListener listener);
	
	public interface ParameterGroupsChangeListener{
		public void parameterGroupsChanged(Component component);
	}
	
	public ResourceSpecifier getResourceCreationSpecifier();
	
	public void setFlowControllerIdentifier(String identifier);
	public String getFlowControllerIdentifier();
	
	public void registerFlowControlChangedListener(FlowControlChangeListener listener);
	
	public interface FlowControlChangeListener {
		public void flowControlChanged(Component component);
	}
	
	/**
	 * Adaptor class to make implementation of listeners easier.
	 * 
	 * @author Luke McCrohon
	 *
	 */
	public class ListenerAdaptor implements FlowControlChangeListener, ParameterGroupsChangeListener, ParameterConfigurationChangeListener, LockedStatusChangeListener, MinimizedStatusChangeListener, ParametersChangedListener, SavedStatusChangeListener, InputOutputChangeListener, LanguageChangeListener, DistributionInformationChangeListener, DescriptionChangeListener{
		@Override
		public void ComponentDescriptionChanged(Component component) {}
		@Override
		public void distributionInformationChanged(Component component) {}
		@Override
		public void languagesChanged(Component component) {}
		@Override
		public void inputOutputChanged(Component component) {}
		@Override
		public void savedStatusChanged(Component component) {}
		@Override
		public void parametersChanged(Component component) {}
		@Override
		public void minimizedStatusChanged(Component component) {}
		@Override
		public void lockStatusChanged(Component component) {}
		@Override
		public void parameterConfigurationChanged(Component component) {}
		@Override
		public void parameterGroupsChanged(Component component) {}
		@Override
		public void flowControlChanged(Component component) {}
	}
}
