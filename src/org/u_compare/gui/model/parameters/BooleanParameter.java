package org.u_compare.gui.model.parameters;

public class BooleanParameter extends
		AbstractParameter<Boolean> {

	public BooleanParameter(String name, String description, boolean mandatory, Boolean parameter) {
		super(name, description, mandatory, false);
		setInitial(parameter);
	}
	
	public BooleanParameter(String name, String description, boolean mandatory, Boolean[] values) {
		super(name, description, mandatory, true);
		//this.parameter = value; TODO
	}
	
	@Override
	public void setValue(Boolean value){
		simpleSet(value);
	}
}
