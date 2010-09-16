package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.UIMAComponent;
import org.u_compare.gui.model.parameters.constraints.Constraint;

public interface Parameter {

	public String getDescription();
	public String getParameterString();
	public void addConstraint(Constraint constraint);
	public ArrayList<Constraint> getConstraints();
	
	public boolean valid(String value);
	public boolean valid(boolean value);
	public boolean valid(int value);
	
	public void update(String value) throws InvalidInputException;
	public void update(boolean value) throws InvalidInputException;
	public void update(int value) throws InvalidInputException;
	
	/**
	 * Must be called following construction.
	 * 
	 * @param owner
	 */
	public void setOwner(UIMAComponent owner);
	
}