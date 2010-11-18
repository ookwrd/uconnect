package org.u_compare.gui.model.parameters;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public abstract class AbstractParameter implements
		Parameter {

	protected Component owner;
	private String description;
	private ArrayList<Constraint> constraints;
	
	private ArrayList<ParameterSettingsChangedListener> changedListeners = new ArrayList<ParameterSettingsChangedListener>();
	
	public AbstractParameter(String description){

		constraints = new ArrayList<Constraint>();
		this.description = description;
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
	public void addConstraint(Constraint constraint){
		constraints.add(constraint);
	}
	
	@Override
	public ArrayList<Constraint> getConstraints(){
		return constraints;
	}
	
	@Override
	public boolean valid(String value){
		return false;
	}
	
	@Override
	public boolean valid(boolean value){
		return false;
	}
	
	@Override
	public boolean valid(int value){
		return false;
	}
	
	@Override
	public void update(int value) throws ConstraintFailedException {
		throw new InvalidParameterException("The parameter: "+ description + ", is not an int.");
	}
	
	@Override
	public void update(String value) throws ConstraintFailedException{
		throw new InvalidParameterException("The parameter: "+ description + ", is not an String.");
	}
	
	@Override
	public void update(boolean value) throws ConstraintFailedException{
		throw new InvalidParameterException("The parameter: "+ description + ", is not a boolean.");
	}
	
	public void registerParameterSettingsChangedListener(ParameterSettingsChangedListener listener){
		changedListeners.add(listener);
	}
	
	public void notifyParameterSettingsChangedListeners(){
		for(ParameterSettingsChangedListener listener : changedListeners){
			listener.parameterSettingsChanged(this);
		}
		
		owner.setComponentChanged();
	}
}
