package org.u_compare.gui.model.parameters;

import org.u_compare.gui.BooleanParameterPanel;
import org.u_compare.gui.control.BooleanParameterController;
import org.u_compare.gui.model.Component;

public class BooleanParameter extends
		AbstractParameter {

	private boolean parameter;
	private BooleanParameterPanel panel;
	
	public BooleanParameter(String description, boolean value){
		super(description);
		parameter = value;
	}
	
	public boolean getParameter(){
		return parameter;
	}

	
	public BooleanParameterPanel getConfigurationPanel(BooleanParameterController controller, Component component) {
		
		//TODO what if its already constructed?
		//TODO should this be here?
		
		panel = new BooleanParameterPanel(this, controller, component);
		return panel;
	}

	public void update(boolean input) throws InvalidInputException {
		
		System.out.println("setting:" + input);
		
	}

	public String getParameterString() {
		return String.valueOf(parameter);
	}
	

}
