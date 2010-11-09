package org.u_compare.gui.model;

import java.util.ArrayList;

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
public class MockAggregateComponent extends AbstractAggregateComponent implements Component, AggregateComponent {

	public MockAggregateComponent(){
		super();
		
	}

	public MockAggregateComponent(ArrayList<Parameter> params) {
		super();
	
		setConfigurationParameters(params);
	}
	
	
}
