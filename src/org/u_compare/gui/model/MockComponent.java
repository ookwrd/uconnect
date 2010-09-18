package org.u_compare.gui.model;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;


/**
 * 
 * This class is for TESTING PURPOSES ONLY.
 * 
 * A stub class for use when testing the model.
 * 
 * @author lukemccrohon
 *
 */
public class MockComponent extends AbstractComponent implements Component {

	//Special Identifier to track the location of the component.
	private String identifier;
	
	/**
	 * Construct new placeholder component without a specified identifier.
	 */
	public MockComponent(){
		super();
		identifier = "unspecified";
		
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new BooleanParameter("This is a boolean configuration Parameter", false));
		setConfigurationParameters(params);
	}
	
	/**
	 * Construct new placeholder component with a list of configuration Parameters
	 */
	public MockComponent(ArrayList<Parameter> params){
		super();
		identifier = "unspecified";
		
		setConfigurationParameters(params);
	}
	
	/**
	 * Construct new placeholder component and assign an identifier for testing purposes.
	 * @param identifier
	 */
	public MockComponent(String identifier){
		super();
		this.identifier = identifier;
		setDescription(identifier);
		
	}
	
	/**
	 * @return the identifier given when this place holder was constructed.
	 */
	public String getIdentifier(){
		return identifier;
	}
	
}
