package org.u_compare.gui.model;

import java.util.ArrayList;
import org.u_compare.gui.debugging.Debug;
import org.u_compare.gui.model.parameters.Parameter;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Abstract base class implementing much of the functionality common to all components.
 * 
 * See also AbstractUIMAAggregateComponent.
 * 
 * @author Luke McCrohon
 */
public abstract class AbstractComponent implements Component {

	private Component superComponent;

	//private to ensure use of proper set methods by extending classes
	private String name = "Unnamed";
	private String description = "Undescribed";
	private ArrayList<AnnotationType> inputTypes = new ArrayList<AnnotationType>();
	private ArrayList<AnnotationType> outputTypes = new ArrayList<AnnotationType>();
	private ArrayList<Parameter> configurationParameters = new ArrayList<Parameter>();
	private boolean unsavedChanges = false;
	private boolean minimized = false;
	protected LockStatusEnum lockStatus = LockStatusEnum.UNLOCKED;
	
	/**
	 * Possible values of the components internal locked status
	 * UNLOCKED 
	 * DIRECTLOCK a lock was directly placed on this component
	 * INDIRECTLOCK component is locked due to a lock being placed on a parent component
	 */
	public enum LockStatusEnum {UNLOCKED,DIRECTLOCK,INDIRECTLOCK};
	
	private ArrayList<DescriptionChangeListener> componentDescriptionChangeListeners = new ArrayList<DescriptionChangeListener>();
	private ArrayList<InputOutputChangeListener> inputOutputChangeListeners = new ArrayList<InputOutputChangeListener>();
	private ArrayList<SavedStatusChangeListener> savedStatusChangeListeners = new ArrayList<SavedStatusChangeListener>();
	private ArrayList<ParameterSettingsChangeListener> parameterSettingsChangeListeners = new ArrayList<ParameterSettingsChangeListener>();
	private ArrayList<MinimizedStatusChangeListener> minimizedStatusChangeListeners = new ArrayList<MinimizedStatusChangeListener>();
	private ArrayList<LockedStatusChangeListener> lockedStatusChangeListeners = new ArrayList<LockedStatusChangeListener>();
	
	public AbstractComponent(){

	}
	
	/**
	 * Components directly extending this class should not be aggregate. Aggregates should 
	 * extend UIMAAggregateComponent.
	 */
	@Override
	public boolean isAggregate() {
		//False by default
		return false;
	}
	
	@Override
	public ArrayList<Component> getSubComponents(){
		//No subcomponents by default
		return new ArrayList<Component>();
	}

	/**
	 * Return false as by default implementing components won't be workflows. Override as necessary.
	 */
	@Override
	public boolean isWorkflow() {
		//False by default
		return false;
	}

	/**
	 * Returns the Name of the component.
	 */
	@Override
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the Name of the component.
	 */
	@Override
	public void setName(String name){
		
		if(this.name != null && this.name.equals(name)){
			return;
		}
		
		this.name = name;
		notifyComponentDescriptionChangeListeners();
	}
	
	/**
	 * Returns the Description of the Component.
	 */
	@Override
	public String getDescription(){
		return description;
	}
	
	/**
	 * Sets the Description of the Component.
	 */
	@Override
	public void setDescription(String description){
		
		if(this.description != null && this.description.equals(description)){
			return;
		}
		
		this.description = description;
		notifyComponentDescriptionChangeListeners();
	}
	
	@Override
	public void setSuperComponent(Component superComp){
		this.superComponent = superComp;
	}
	
	@Override
	public Component getSuperComponent(){
		return superComponent;
	}
	
	@Override
	public ArrayList<AnnotationType> getInputTypes(){
		return inputTypes;
	}
	
	@Override
	public void addInputType(AnnotationType inputType){
		
		if(inputTypes.contains(inputType)){
			return;
		}
		
		inputTypes.add(inputType);
		notifyInputOutputChangeListeners();
		
	}
	
	@Override
	public void removeInputType(AnnotationType inputType){
		
		if(!inputTypes.contains(inputType)){
			return;
		}
		
		inputTypes.remove(inputType);
		notifyInputOutputChangeListeners();
		
	}
	
	@Override
	public void setInputTypes(ArrayList<AnnotationType> newInputTypes){
		
		if(inputTypes.containsAll(newInputTypes) && inputTypes.size() == newInputTypes.size()){
			return;
		}
		
		inputTypes = new ArrayList<AnnotationType>();
		inputTypes.addAll(newInputTypes);
		notifyInputOutputChangeListeners();
		
	}
	
	@Override
	public ArrayList<AnnotationType> getOutputTypes(){
		return outputTypes;
	}
	
	@Override
	public void addOutputType(AnnotationType outputType){
		
		if(outputTypes.contains(outputType)){
			//TODO Warning here, this should never happen
			return;
		}
		
		outputTypes.add(outputType);
		notifyInputOutputChangeListeners();
		
	}
	
	@Override
	public void removeOutputType(AnnotationType outputType){
		
		if(!outputTypes.contains(outputType)){
			return;
		}
		
		outputTypes.remove(outputType);
		notifyInputOutputChangeListeners();
		//TODO set changed
		
	}
	
	@Override
	public boolean getMinimizedStatus(){
		return minimized;
	}
	
	@Override
	public void setMinimizedStatus(boolean minimized){
		
		if(this.minimized != minimized){
			this.minimized = minimized;
			notifyMinimizedStatusChangeListeners();
			//This is only a UI property so no need to update unsaved Changes 
		}
	}
	
	@Override
	public boolean getLockedStatus(){
		return toBooleanLock(lockStatus);
	}
	
	/**
	 * Converts the internal lock status value to a boolean representing whether it represents a lock or not.
	 * 
	 * @param in
	 * @return
	 */
	private boolean toBooleanLock(LockStatusEnum in){
		if(lockStatus == LockStatusEnum.DIRECTLOCK || lockStatus == LockStatusEnum.INDIRECTLOCK){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void setLocked(){
		setLocked(LockStatusEnum.DIRECTLOCK);
	}
	
	@Override
	public void setUnlocked(){
		setLocked(LockStatusEnum.UNLOCKED);
	}
	
	@Override
	public void indirectlyLocked(){
		if(lockStatus != LockStatusEnum.DIRECTLOCK){
			setLocked(LockStatusEnum.INDIRECTLOCK);
		}
	}
	
	@Override
	public void indirectlyUnlocked(){
		if(lockStatus != LockStatusEnum.DIRECTLOCK){
			setLocked(LockStatusEnum.UNLOCKED);
		}
	}
	
	/**
	 * Use this method to update the lockStatus field as it ensures correct listeners are notified.
	 * 
	 * @param val
	 */
	protected void setLocked(LockStatusEnum val) {
		if(lockStatus != val){
			lockStatus = val;
			notifyLockedStatusChangeListeners();
			//TODO change listeners
		}
	}
	
	
	@Override
	public void setOutputTypes(ArrayList<AnnotationType> newOutputTypes){
		
		if(outputTypes.containsAll(newOutputTypes) && newOutputTypes.size() == outputTypes.size()){//Only works because its a set.
			return;
		}
		
		outputTypes = new ArrayList<AnnotationType>();
		outputTypes.addAll(newOutputTypes);
		notifyInputOutputChangeListeners();	
	}
	
	/**
	 * Registers a new component to be notified if the general description of the
	 * component is changed. See listener interface for list of parameters covered.
	 * 
	 * @param The Component to register
	 */
	@Override
	public void registerComponentDescriptionChangeListener(DescriptionChangeListener listener){
		
		if(componentDescriptionChangeListeners == null){//TODO asserts?
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: registerComponentDescriptionChangeListener method called on " + getName() + " when ComponentDescriptionChangeListener has not been initialised. Someone has probably forgotten to call super().");
			}
			componentDescriptionChangeListeners = new ArrayList<DescriptionChangeListener>();
		}
		componentDescriptionChangeListeners.add(listener);
	}
	
	/**
	 * 
	 */
	protected void notifyComponentDescriptionChangeListeners(){
		
		for(DescriptionChangeListener listener : componentDescriptionChangeListeners){
			listener.ComponentDescriptionChanged(this);
		}
		
		setComponentChanged();
		
	}
	
	@Override
	public void registerInputOutputChangeListener(InputOutputChangeListener listener){
		
		if(inputOutputChangeListeners == null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: registerInputOutputChangeListener method called on " + getName() + " when inputOutputChangeListener has not been initialised. Someone has probably forgotten to call super().");
			}
			inputOutputChangeListeners = new ArrayList<InputOutputChangeListener>();
		}
		inputOutputChangeListeners.add(listener);
	}
	
	protected void notifyInputOutputChangeListeners(){
		
		for(InputOutputChangeListener listener : inputOutputChangeListeners){
			listener.inputOutputChanged(this);
		}
		
		setComponentChanged();
		
	}
	
	@Override
	public ArrayList<Parameter> getConfigurationParameters(){
		return configurationParameters;
	}
	
	@Override
	public void setConfigurationParameters(ArrayList<Parameter> params){
		
		for(Parameter param : configurationParameters){
			param.setOwner(null);
		}
		
		for(Parameter param : params){
			param.setOwner(this);
		}
		
		this.configurationParameters = params;
		
		notifyParameterSettingsChangeListeners();
		//TODO add check that a change has actually taken place.
	}
	
	@Override
	public boolean checkUnsavedChanges(){
		return unsavedChanges;
	}
	
	@Override
	public void setComponentChanged() {
		if (!unsavedChanges) {
			unsavedChanges = true;
			notifySavedStatusChangeListeners();	
			if(superComponent != null){
				superComponent.setComponentChanged();
			}
		}
	}
	
	@Override
	public void setComponentSaved(){
		if(unsavedChanges){
			unsavedChanges = false;
			notifySavedStatusChangeListeners();
		}
	}
	
	@Override
	public void registerSavedStatusChangeListener(SavedStatusChangeListener listener){
		
		if(savedStatusChangeListeners == null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: registerSavedStatusChangeListener method called on " + getName() + " when registerSavedStatusChangeListeners has not been initialised. Someone has probably forgotten to call super().");
			}
			savedStatusChangeListeners = new ArrayList<SavedStatusChangeListener>();
		}
	
		savedStatusChangeListeners.add(listener);
	}
	
	protected void notifySavedStatusChangeListeners(){
		System.out.println("Notifying Saved status changed listeners");
		
		for(SavedStatusChangeListener listener : savedStatusChangeListeners){
			listener.savedStatusChanged(this);
		}
	}
	
	@Override 
	public void registerParameterSettingsChangeListener(ParameterSettingsChangeListener listener){
		
		if(parameterSettingsChangeListeners == null){
			if(Debug.DEBUGLEVEL >= Debug.WARNING){
				Debug.out.println("Warning: parameterSettingsChangeListener method called on " + getName() + " when parameterSettingsChangeListeners has not been initialised. Someone has probably forgotten to call super().");
			}
			parameterSettingsChangeListeners = new ArrayList<ParameterSettingsChangeListener>();
		}
		parameterSettingsChangeListeners.add(listener);	
	}
	
	/**
	 *TODO Make this called when parameters change!
	 */
	protected void notifyParameterSettingsChangeListeners(){
		
		for(ParameterSettingsChangeListener listener : parameterSettingsChangeListeners){
			listener.parameterSettingsChanged(this);
		}
		
		setComponentChanged();
	}
	
	@Override
	public void registerMinimizedStatusChangeListener(MinimizedStatusChangeListener listener){

		minimizedStatusChangeListeners.add(listener);
		
	}
	
	protected void notifyMinimizedStatusChangeListeners() {
		
		for(MinimizedStatusChangeListener listener : minimizedStatusChangeListeners){
			listener.minimizedStatusChanged(this);
		}
		
	}
	
	@Override
	public void registerLockedStatusChangeListener(LockedStatusChangeListener listener){
		lockedStatusChangeListeners.add(listener);	
	}
	
	protected void notifyLockedStatusChangeListeners(){
		
		for(LockedStatusChangeListener listener : lockedStatusChangeListeners){
			listener.lockStatusChanged(this);
		}
	}
}
