package org.u_compare.gui.debugging;

import java.util.ArrayList;

import org.u_compare.gui.model.AbstractAggregateComponent;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.Parameter;

/**
 * 
 * This class is for TESTING PURPOSES ONLY.
 * 
 * A stub class for use when testing the model.
 * 
 * @author Luke McCrohon
 * 
 */
public class MockAggregateComponent extends AbstractAggregateComponent
		implements Component, AggregateComponent {

	public MockAggregateComponent() {
		super();
	}

	public MockAggregateComponent(ArrayList<Parameter> params) {
		super();
		setConfigurationParameters(params);
	}
}
