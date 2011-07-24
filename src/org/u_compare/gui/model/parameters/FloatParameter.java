package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.FloatConstraint;

//TODO
public class FloatParameter extends AbstractParameter {

	private Float value; //null represents unset
	
	public FloatParameter(String name, String description, boolean mandatory, Float value) {
		super(name, description, mandatory, false);
		
		this.value = value;
		addConstraint(new FloatConstraint());
	}
	
	public Float getParameter(){
		return value;
	}
	
	@Override
	public void setValue(String input) throws ConstraintFailedException {

		validateConstraints(input);
		
		float inputFloat = Float.parseFloat(input);
	
		if(inputFloat != value){//TODO does this work?
			value = inputFloat;
			notifyParameterValueChangedListeners();
		}
	}

	@Override
	public String getParameterString() {
		if(value!=null){
			return String.valueOf(value);
		}else{
			return "";
		}
	}

}
