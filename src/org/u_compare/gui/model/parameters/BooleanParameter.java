package org.u_compare.gui.model.parameters;

import org.u_compare.gui.BooleanParameterPanel;
import org.u_compare.gui.control.BooleanParameterController;
import org.u_compare.gui.model.Component;

public class BooleanParameter extends
		AbstractParameter {

	private boolean parameter;
	
	public BooleanParameter(String description, boolean value){
		super(description);
		parameter = value;
	}
	
	public boolean getParameter(){
		return parameter;
	}

	public void update(boolean input) throws InvalidInputException {
		
		System.out.println("setting:" + input);
		
	}

	public String getParameterString() {
		return String.valueOf(parameter);
	}
	

}
