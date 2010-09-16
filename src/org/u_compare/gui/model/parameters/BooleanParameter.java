package org.u_compare.gui.model.parameters;

import org.u_compare.gui.BooleanConfigPanel;
import org.u_compare.gui.control.BooleanConfigController;

public class BooleanParameter extends
		AbstractParameter {

	private boolean parameter;
	private BooleanConfigPanel panel;
	
	public BooleanParameter(String description, boolean value){
		super(description);
		parameter = value;
	}
	
	public boolean getParameter(){
		return parameter;
	}
	
	/*public void setParameter(boolean parameter){
		if(parameter != this.parameter){
			this.parameter = parameter;
			setChanged();
		}
	}*/
	
	public BooleanConfigPanel getConfigurationPane(BooleanConfigController controller) {
		
		//TODO what if its already constructed?
		
		panel = new BooleanConfigPanel(this, controller,false);//TODO make final argument sensible
		return panel;
	}

	public void update(boolean input) throws InvalidInputException {
		
		System.out.println("setting:" + input);
		
	}

	public String getParameterString() {
		return String.valueOf(parameter);
	}
	

}
