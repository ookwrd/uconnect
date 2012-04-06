package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.Constraint.ConstraintFailedException;

/**
 * Interface specifying the common behaviour of all parameter types. Based on
 * the definition of UIMA parameters.
 * 
 * @author Luke McCrohon
 */
public interface Parameter {

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public boolean isMandatory();

	public void setMandatory(boolean mandatory);

	public boolean isMultivalued();

	public void setMultivalued(boolean multivalued);

	public String getParameterString();

	public String[] getParameterStrings();

	public void addConstraint(Constraint constraint);

	public ArrayList<Constraint> getConstraints();

	public void setValue(String value) throws ConstraintFailedException;

	public void setValues(String[] values) throws ConstraintFailedException;

	public void registerParameterValueChangedListener(
			ParameterValueChangedListener listener);

	public interface ParameterValueChangedListener {
		public void parameterSettingsChanged(Parameter param);
	}

	public void registerParameterNameDescriptionChangedListener(
			ParameterNameDescriptionChangedListener listener);

	public interface ParameterNameDescriptionChangedListener {
		public void parameterNameDescriptionChanged(Parameter param);
	}

	public void registerParameterConfigurationChangedListener(
			ParameterConfigurationChangedListener listener);

	public interface ParameterConfigurationChangedListener {
		public void parameterConfigurationChanged(Parameter param);
	}

	/**
	 * Must be called following construction.
	 * 
	 * @param owner
	 */
	public void setOwner(Component owner);

}