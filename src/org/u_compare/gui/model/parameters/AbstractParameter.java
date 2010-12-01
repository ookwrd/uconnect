package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public abstract class AbstractParameter implements
		Parameter {

	protected Component owner;
	private String name; //TODO listeners
	private String description;
	private boolean mandatory;
	private ArrayList<Constraint> constraints;
	
	private ArrayList<ParameterNameDescriptionChangedListener> nameDescriptionChangedListeners = new ArrayList<ParameterNameDescriptionChangedListener>();
	private ArrayList<ParameterSettingsChangedListener> changedListeners = new ArrayList<ParameterSettingsChangedListener>();
	private ArrayList<MandatoryStatusChangedListener> mandatoryStatusChangedListeners = new ArrayList<MandatoryStatusChangedListener>(); 
	
	public AbstractParameter(String name, String description, boolean mandatory){
		constraints = new ArrayList<Constraint>();
		this.name = name;
		this.description = description;
		this.mandatory = mandatory;
	}
	
	@Override
	public void setOwner(Component owner){
		this.owner = owner;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public void setDescription(String description){
		if(!description.equals(this.description)){
			this.description = description;
			notifyParameterNameDescriptionChangedListeners();
		}
	}
	
	@Override 
	public String getName(){
		return name;
	}
	
	@Override
	public void setName(String name){
		if(!name.equals(this.name)){
			this.name = name;
			notifyParameterNameDescriptionChangedListeners();
		}
	}
	
	@Override
	public boolean isMandatory(){
		return mandatory;
	}
	
	public void setMandatory(boolean mandatory){
		if(this.mandatory != mandatory){
			this.mandatory = mandatory;
			//TODO listeners
		}
	}
		
	@Override
	public void addConstraint(Constraint constraint){
		constraints.add(constraint);
	}
	
	@Override
	public ArrayList<Constraint> getConstraints(){
		return constraints;
	}
	
	@Override
	public void update(int value) throws ConstraintFailedException {
		throw new IllegalArgumentException("The parameter: "+ description + ", is not an int.");
	}
	
	@Override
	public void update(String value) throws ConstraintFailedException{
		throw new IllegalArgumentException("The parameter: "+ description + ", is not an String.");
	}
	
	@Override
	public void update(boolean value) throws ConstraintFailedException{
		throw new IllegalArgumentException("The parameter: "+ description + ", is not a boolean.");
	}
	
	public void registerParameterSettingsChangedListener(ParameterSettingsChangedListener listener){
		changedListeners.add(listener);
	}
	
	protected void notifyParameterSettingsChangedListeners(){
		for(ParameterSettingsChangedListener listener : changedListeners){
			listener.parameterSettingsChanged(this);
		}
		owner.setComponentChanged();
	}
	
	public void registerParameterNameDescriptionChangedListener(ParameterNameDescriptionChangedListener listener){
		nameDescriptionChangedListeners.add(listener);
	}
	
	protected void notifyParameterNameDescriptionChangedListeners(){
		for(ParameterNameDescriptionChangedListener listener : nameDescriptionChangedListeners){
			listener.parameterNameDescriptionChanged(this);
		}
		owner.setComponentChanged();
	}
	
	public void registerMandatoryStatusChangedListener(MandatoryStatusChangedListener listener){
		mandatoryStatusChangedListeners.add(listener);
	}
	
	protected void notifyMandatoryStatusChangedListeners(){
		for(MandatoryStatusChangedListener listener : mandatoryStatusChangedListeners){
			listener.mandatoryStatusChanged(this);
		}
		owner.setComponentChanged();
	}
	
	protected void validateConstraints(String parameter) throws ConstraintFailedException {
		for(Constraint con : constraints){
			con.validate(parameter);	
		}
	}
}
