package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public interface Parameter {

	public String getName();
	public String getDescription();
	public String getParameterString();
	public void addConstraint(Constraint constraint);
	public ArrayList<Constraint> getConstraints();
	
	public void update(String value) throws ConstraintFailedException;
	public void update(boolean value) throws ConstraintFailedException;
	public void update(int value) throws ConstraintFailedException;
	
	/**
	 * Must be called following construction.
	 * 
	 * @param owner
	 */
	public void setOwner(Component owner);
	
}