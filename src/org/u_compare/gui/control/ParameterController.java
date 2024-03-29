package org.u_compare.gui.control;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.u_compare.gui.component.BooleanParameterPanel;
import org.u_compare.gui.component.ParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.StringParameter;
import org.u_compare.gui.model.parameters.constraints.Constraint.ConstraintFailedException;

/**
 * Controller for a parameter associated with either a BooleanParameterPanel or
 * a ParameterPanel depending on type.
 * 
 * @author Luke McCrohon
 */
public class ParameterController {

	protected Parameter param;
	protected ParameterPanel view;

	public ParameterController(ComponentController control, Parameter param,
			Component component) {

		//Choose which kind of ParameterPanel to use.
		if (param instanceof BooleanParameter) {//Special BooleanParameterPanel
			this.view = new BooleanParameterPanel(param, this, component);
		} else if (param instanceof StringParameter
				|| param instanceof IntegerParameter
				|| param instanceof FloatParameter) {//Standard ParameterPanel
			this.view = new ParameterPanel(param, this, component);
		} else {
			assert (false);
		}

		this.param = param;
	}

	public ParameterPanel getView() {
		return view;
	}

	/**
	 * Method for consistent handling of ConstraintFailedExceptions
	 * 
	 * @param e
	 */
	protected void processConstraintFailure(ConstraintFailedException e) {
		JOptionPane.showMessageDialog(null, e.getUserReadableError());
	}

	public void setValue(String parameterValue) {
		try {
			param.setValue(parameterValue);
		} catch (ConstraintFailedException ex) {
			processConstraintFailure(ex);
		}
	}

	public void addValue() {
		String value = JOptionPane
				.showInputDialog("Please enter the value to add:");

		if (value == null) {
			assert (false);
			System.out.println("Invalid value returned by string chooser.");
			return;
		}

		ArrayList<String> strings = new ArrayList<String>(Arrays.asList(param
				.getParameterStrings()));
		strings.add(value);

		try {
			param.setValues(strings.toArray(new String[strings.size()]));
		} catch (ConstraintFailedException e) {
			processConstraintFailure(e);
		}

	}

	public void removeValues(int[] indexs) {
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList(param
				.getParameterStrings()));
		
		ArrayList<String> adjustedStrings = new ArrayList<String>();
		constructionLoop:
		for(int i = 0; i < strings.size(); i++){
			for(int index : indexs){
				if(i == index){//Don't add one of the values to remove
					continue constructionLoop;
				}
			}
			adjustedStrings.add(strings.get(i));
		}

		try {
			param.setValues(adjustedStrings.toArray(new String[adjustedStrings.size()]));
		} catch (ConstraintFailedException e) {
			processConstraintFailure(e);
		}
	}
	
	public enum Direction {UP, DOWN};
	
	public void reorderValue(int index, Direction direction){
		
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList(param
				.getParameterStrings()));
		
		ArrayList<String> adjustedStrings = new ArrayList<String>();
		if(direction == Direction.UP){//Moving item up
			for(int i = 0; i < strings.size(); i++){
				if(i + 1 == index){
					//Move top item up automatically handled
					adjustedStrings.add(strings.get(i+1));
					adjustedStrings.add(strings.get(i));
					i++;
					continue;
				}
				adjustedStrings.add(strings.get(i));
			}
		}else if (direction == Direction.DOWN){//Move item down
			for(int i = 0; i < strings.size(); i++){
				if(i == index){
					if(i+1 < strings.size() && strings.get(i+1) != null){//move final item down
						adjustedStrings.add(strings.get(i+1));
					}
					adjustedStrings.add(strings.get(i));
					i++;
					continue;
				}
				adjustedStrings.add(strings.get(i));
			}
		}else{
			assert(false);
		}
		
		try {
			param.setValues(adjustedStrings.toArray(new String[adjustedStrings.size()]));
		} catch (ConstraintFailedException e) {
			processConstraintFailure(e);
		}
	}
}
