package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public interface Parameter {

	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public boolean isMandatory();
	public void setMandatory(boolean mandatory);
	
	public boolean isMultivalued();
	
	public String getParameterString();
	
	public void addConstraint(Constraint constraint);
	public ArrayList<Constraint> getConstraints();
	
	public void setValue(String value) throws ConstraintFailedException;//String, float, integer
	public void setValue(Boolean value) throws ConstraintFailedException;
	//public void setValue(int value) throws ConstraintFailedException;
	
	
	
	/**
	 * Must be called following construction.
	 * 
	 * @param owner
	 */
	public void setOwner(Component owner);
	
}