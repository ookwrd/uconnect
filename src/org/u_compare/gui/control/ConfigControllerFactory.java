package org.u_compare.gui.control;

import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.StringParameter;

public class ConfigControllerFactory {

	public static ConfigController getController(ComponentController control, Parameter param){
		
		if(param instanceof BooleanParameter){
		
			return new BooleanConfigController(control, (BooleanParameter)param);
			
		}else if (param instanceof StringParameter){
			
			return new StringConfigController(control, (StringParameter)param);
			
		}else if (param instanceof IntegerParameter){
			
			return new IntegerConfigController(control, (IntegerParameter)param);

		}else{
		
			//TODO FIX THIS
			System.out.println("Invalid type passed in to parameter controller factory");
			return null;
			
		}
	} 
	
}
