package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public abstract class AbstractParameter
		implements Parameter {

	protected Component owner;
	private String name;
	private String description;
	private boolean mandatory;
	private boolean multivalued;
	private ArrayList<Constraint> constraints;
	
//	private ArrayList<Parameter> values = new ArrayList<Parameter>();
	
	private ArrayList<ParameterNameDescriptionChangedListener> nameDescriptionChangedListeners = new ArrayList<ParameterNameDescriptionChangedListener>();
	private ArrayList<ParameterValueChangedListener> changedListeners = new ArrayList<ParameterValueChangedListener>();
	private ArrayList<ParameterConfigurationChangedListener> parameterConfigurationChangedListeners = new ArrayList<ParameterConfigurationChangedListener>(); 
	
	public AbstractParameter(String name, String description,
			boolean mandatory, boolean multivalued) {
		constraints = new ArrayList<Constraint>();
		this.name = name;
		this.description = description;
		this.mandatory = mandatory;
		this.multivalued = multivalued;
	}
	
	@Override
	public void setOwner(Component owner) {
		this.owner = owner;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public void setDescription(String description){
		if(description != null && !description.equals(this.description)) {
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
		if(description != null && !name.equals(this.name)){
			this.name = name;
			notifyParameterNameDescriptionChangedListeners();
		}
	}
	
	@Override
	public boolean isMandatory(){
		return mandatory;
	}
	
	@Override
	public void setMandatory(boolean mandatory){
		if(this.mandatory != mandatory){
			this.mandatory = mandatory;
			notifyParameterConfigurationChangedListeners();
		}
	}
	
	@Override
	public boolean isMultivalued() {
		return multivalued;
	}
	
	@Override
	public void setMultivalued(boolean multivalued){
		if(this.multivalued != multivalued){
			this.multivalued = multivalued;
			notifyParameterConfigurationChangedListeners();
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
	public void setValue(String value) throws ConstraintFailedException{
		throw new IllegalArgumentException("The parameter: "+ description
				+ ", is not an String.");
	}
	
	@Override
	public void setValue(Boolean value) throws ConstraintFailedException{
		throw new IllegalArgumentException("The parameter: "+ description
				+ ", is not a boolean.");
	}
	
	@Override
	public void registerParameterValueChangedListener(
			ParameterValueChangedListener listener) {
		assert(listener != null);
		changedListeners.add(listener);
	}
	
	protected void notifyParameterValueChangedListeners() {
		for(ParameterValueChangedListener listener : changedListeners) {
			listener.parameterSettingsChanged(this);
		}
		owner.setComponentChanged();
	}
	
	@Override
	public void registerParameterNameDescriptionChangedListener(
			ParameterNameDescriptionChangedListener listener) {
		assert(listener != null);
		nameDescriptionChangedListeners.add(listener);
	}
	
	protected void notifyParameterNameDescriptionChangedListeners() {
		for(ParameterNameDescriptionChangedListener listener : 
				nameDescriptionChangedListeners) {
			listener.parameterNameDescriptionChanged(this);
		}
		owner.setComponentChanged();
	}
	
	@Override
	public void registerParameterConfigurationChangedListener(
			ParameterConfigurationChangedListener listener) {
		assert(listener != null);
		parameterConfigurationChangedListeners.add(listener);
	}
	
	protected void notifyParameterConfigurationChangedListeners() {
		for(ParameterConfigurationChangedListener listener :
				parameterConfigurationChangedListeners) {
			listener.parameterConfigurationChanged(this);
		}
		owner.setComponentChanged();
	}
	
	protected void validateConstraints(String parameter)
		throws ConstraintFailedException {
		for(Constraint con : constraints) {
			con.validate(parameter);	
		}
	}
	
	//TODO documentation
	public static Parameter constructParameter(ConfigurationParameter param,
			Object value){
		
		Parameter retVal;
		
		String name = param.getName();
		String description = param.getDescription();
		boolean multivalued = param.isMultiValued();
		boolean mandatory = param.isMandatory();
		
		// Why are the types Strings? I cant do a switch statement..
		String type = param.getType();
		if(type.equals(ConfigurationParameter.TYPE_BOOLEAN)) {
			if(multivalued){
				retVal = new BooleanParameter(name, description, mandatory,
						value!=null?(Boolean[])value:null);
			}else{
				retVal = new BooleanParameter(name, description, mandatory,
						value!=null?(Boolean)value:null);
			}
		} else if (type.equals(ConfigurationParameter.TYPE_FLOAT)) {
			if(multivalued){
				retVal = new FloatParameter(name, description, mandatory,
						value!=null?(Float[])value:null);
			}else{
				retVal = new FloatParameter(name, description, mandatory,
						value!=null?(Float)value:null);
			}
		} else if (type.equals(ConfigurationParameter.TYPE_INTEGER)) {
			if(multivalued){
				retVal = new IntegerParameter(name, description, mandatory,
						value!=null?(Integer[])value:null);
			}else{
				retVal = new IntegerParameter(name, description, mandatory,
						value!=null?(Integer)value:null);
			}
		} else if (type.equals(ConfigurationParameter.TYPE_STRING)) {
			if(multivalued){
				
				System.out.println("Here in the multivalued string constructor");
				System.out.println(value == null);
			//	System.out.println("Type" + value.getClass().getName());
				
				retVal = new StringParameter(name, description, mandatory,
						value!=null?(String[])value:null);
			}else{
				retVal = new StringParameter(name, description, mandatory,
						value!=null?(String)value:null);
			}
		} else {
			retVal = null; //TODO throw an error here. 
		}
		
		return retVal;
	}
}
