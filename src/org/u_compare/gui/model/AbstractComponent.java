package org.u_compare.gui.model;

import java.util.ArrayList;
import org.u_compare.gui.model.parameters.Parameter;

/**
 * Abstract base class implementing much of the functionality common to all components.
 * 
 * See also AbstractUIMAAggregateComponent.
 * 
 * @author Luke McCrohon
 */
public abstract class AbstractComponent implements Component {

	private Component parentComponent;

	//private (rather than protected) to ensure use of proper set methods by extending classes
	private String title = "Unnamed";
	private String description = "Undescribed";
	private String vendor = "Unknown";
	private String version = "Unspecified";
	private String copyright = "Copyright information unknown";
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
	public static enum LockStatusEnum {UNLOCKED,DIRECTLOCK,INDIRECTLOCK};
	
	//Change listeners
	private ArrayList<DescriptionChangeListener> componentDescriptionChangeListeners = new ArrayList<DescriptionChangeListener>();
	private ArrayList<DistributionInformationChangeListener> distributionInformationChangeListeners = new ArrayList<DistributionInformationChangeListener>();
	private ArrayList<InputOutputChangeListener> inputOutputChangeListeners = new ArrayList<InputOutputChangeListener>();
	private ArrayList<SavedStatusChangeListener> savedStatusChangeListeners = new ArrayList<SavedStatusChangeListener>();
	private ArrayList<ParametersChangedListener> parametersChangedListeners = new ArrayList<ParametersChangedListener>();
	private ArrayList<MinimizedStatusChangeListener> minimizedStatusChangeListeners = new ArrayList<MinimizedStatusChangeListener>();
	private ArrayList<LockedStatusChangeListener> lockedStatusChangeListeners = new ArrayList<LockedStatusChangeListener>();
	
	public AbstractComponent(){

	}
	
	/**
	 * Components directly extending this class are generally not aggregate. Aggregates will
	 * usually indirectly extend this class via UIMAAggregateComponent.
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
	public String getTitle(){
		return title;
	}
	
	/**
	 * Sets the Name of the component.
	 */
	@Override
	public void setTitle(String title){
		
		if(this.title != null && this.title.equals(title)){
			return;
		}
		
		this.title = title;
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
	public String getVendor(){
		return vendor;
	}
	
	@Override
	public void setVendor(String vendor){
		if(this.vendor != null && this.vendor.equals(vendor)){
			return;
		}
		
		this.vendor = vendor;
		notifyDistributionInformationChangedListeners();
	}
	
	@Override
	public String getCopyright(){
		return copyright;
	}
	
	@Override
	public void setCopyright(String copyright){
		if(this.copyright != null && this.copyright.equals(copyright)){
			return;
		}
		
		this.copyright = copyright;
		notifyDistributionInformationChangedListeners();
	}
	
	@Override
	public String getVersion(){
		return version;
	}
	
	@Override
	public void setVersion(String version){
		if(this.version != null && this.version.equals(version)){
			return;
		}
		
		this.version = version;
		notifyDistributionInformationChangedListeners();
		
	}

	@Override
	public Component getSuperComponent(){
		return parentComponent;
	}
	
	@Override
	public void setSuperComponent(Component superComp){
		this.parentComponent = superComp;
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
	
	@Override
	public boolean getMinimizedStatus(){
		return minimized;
	}
	
	@Override
	public void setMinimizedStatus(boolean minimized){
		
		if(this.minimized != minimized){
			this.minimized = minimized;
			notifyMinimizedStatusChangeListeners();
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
		}
	}
	
	
	@Override
	public ArrayList<Parameter> getConfigurationParameters(){
		return configurationParameters;
	}
	
	@Override
	public void setConfigurationParameters(ArrayList<Parameter> params){
		
		//Remove old parameters
		for(Parameter param : configurationParameters){
			param.setOwner(null);
		}
		
		for(Parameter param : params){
			param.setOwner(this);
		}
		
		this.configurationParameters = params;
		
		notifyParametersChangedListeners();
		
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
			if(parentComponent != null){
				parentComponent.setComponentChanged();
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
	
	/**
	 * Registers a new component to be notified if the general description of the
	 * component is changed. See listener interface for list of parameters covered.
	 * 
	 * @param The Component to register
	 */
	@Override
	public void registerComponentDescriptionChangeListener(DescriptionChangeListener listener){
		assert(listener != null);
		componentDescriptionChangeListeners.add(listener);
	}
	
	protected void notifyComponentDescriptionChangeListeners(){
		
		for(DescriptionChangeListener listener : componentDescriptionChangeListeners){
			listener.ComponentDescriptionChanged(this);
		}
		setComponentChanged();
		
	}
	
	@Override
	public void registerDistibutionInformationChangedListener(DistributionInformationChangeListener listener){
		
		assert(listener != null);
		distributionInformationChangeListeners.add(listener);
		
	}
	
	protected void notifyDistributionInformationChangedListeners(){
		
		for(DistributionInformationChangeListener listener : distributionInformationChangeListeners){
			listener.distributionInformationChanged(this);
		}
		setComponentChanged();
		
	}
	
	@Override
	public void registerInputOutputChangeListener(InputOutputChangeListener listener){
		
		assert(listener!=null);
		inputOutputChangeListeners.add(listener);
		
	}
	
	protected void notifyInputOutputChangeListeners(){
		
		for(InputOutputChangeListener listener : inputOutputChangeListeners){
			listener.inputOutputChanged(this);
		}
		setComponentChanged();
		
	}
	
	@Override
	public void registerSavedStatusChangeListener(SavedStatusChangeListener listener){
		
		assert(listener != null);
		savedStatusChangeListeners.add(listener);
		
	}
	
	protected void notifySavedStatusChangeListeners(){
		
		for(SavedStatusChangeListener listener : savedStatusChangeListeners){
			listener.savedStatusChanged(this);
		}
	}
	
	@Override 
	public void registerParametersChangedListener(ParametersChangedListener listener){
		
		assert(listener != null);
		parametersChangedListeners.add(listener);	
	}
	
	/**
	 * Not to be confused with ParameterSettingsChangeListener in the Parameter package.
	 */
	protected void notifyParametersChangedListeners(){
		
		for(ParametersChangedListener listener : parametersChangedListeners){
			listener.parametersChanged(this);
		}
		
		setComponentChanged();
	}
	
	@Override
	public void registerMinimizedStatusChangeListener(MinimizedStatusChangeListener listener){

		assert(listener != null);
		minimizedStatusChangeListeners.add(listener);
		
	}
	
	protected void notifyMinimizedStatusChangeListeners() {
		
		for(MinimizedStatusChangeListener listener : minimizedStatusChangeListeners){
			listener.minimizedStatusChanged(this);
		}
		
		//This is only a display property so no need to notify saved change listeners.
		
	}
	
	@Override
	public void registerLockedStatusChangeListener(LockedStatusChangeListener listener){
		lockedStatusChangeListeners.add(listener);	
	}
	
	protected void notifyLockedStatusChangeListeners(){
		
		for(LockedStatusChangeListener listener : lockedStatusChangeListeners){
			listener.lockStatusChanged(this);
		}
		
		//This is only a display property so no need to notify saved change listeners.
		
	}
}
