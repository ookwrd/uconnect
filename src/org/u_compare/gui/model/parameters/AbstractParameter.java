package org.u_compare.gui.model.parameters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.Constraint.ConstraintFailedException;

/**
 * Base class for the implementation of various parameter types.
 * 
 * @author Luke McCrohon
 * 
 * @param <T>
 *            The type of value to store.
 */
public abstract class AbstractParameter<T> implements Parameter {

	protected Component owner;
	private String name;
	private String description;
	private boolean mandatory;
	private boolean multivalued;
	private ArrayList<Constraint> constraints;

	protected ArrayList<T> parametersArrayList = new ArrayList<T>(); // empty
																		// represents
																		// unset.

	private ArrayList<ParameterNameDescriptionChangedListener> nameDescriptionChangedListeners = new ArrayList<ParameterNameDescriptionChangedListener>();
	private ArrayList<ParameterValueChangedListener> changedListeners = new ArrayList<ParameterValueChangedListener>();
	private ArrayList<ParameterConfigurationChangedListener> parameterConfigurationChangedListeners = new ArrayList<ParameterConfigurationChangedListener>();

	public AbstractParameter(String name, String description,
			boolean mandatory, boolean multivalued) {
		constraints = new ArrayList<Constraint>();
		this.name = name;
		this.description = description;
		this.mandatory = mandatory;
		this.multivalued = multivalued;
	}

	@Override
	public void setOwner(Component owner) {
		this.owner = owner;
	}

	@Override
	public String getDescription() {
		assert (description != null);
		return description;
	}

	@Override
	public void setDescription(String description) {
		if (description != null && !description.equals(this.description)) {
			this.description = description;
			notifyParameterNameDescriptionChangedListeners();
		}
	}

	@Override
	public String getName() {
		assert (name != null);
		return name;
	}

	@Override
	public void setName(String name) {
		if (description != null && !name.equals(this.name)) {
			this.name = name;
			notifyParameterNameDescriptionChangedListeners();
		}
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public void setMandatory(boolean mandatory) {
		if (this.mandatory != mandatory) {
			this.mandatory = mandatory;
			notifyParameterConfigurationChangedListeners();
		}
	}

	@Override
	public boolean isMultivalued() {
		return multivalued;
	}

	@Override
	public void setMultivalued(boolean multivalued) {
		if (this.multivalued != multivalued) {
			this.multivalued = multivalued;
			notifyParameterConfigurationChangedListeners();
		}
	}

	@Override
	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
	}

	@Override
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public T getParameter() {
		if (parametersArrayList.size() > 0) {
			return parametersArrayList.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T[] getParameters() {
		if (parametersArrayList.size() == 0) {
			return null;
		} else {
			return (T[]) parametersArrayList.toArray();
		}
	}

	protected void setInitial(T value) {
		if (value != null) {
			parametersArrayList.add(0, value);
		}
	}

	protected void setInitials(T[] values) {
		if (values != null) {
			parametersArrayList.addAll(Arrays.asList(values));
		}
	}

	protected void simpleSet(T input) {
		assert (!owner.getLockedStatus());
		if (parametersArrayList.size() == 0) {
			setInitial(input);
			notifyParameterValueChangedListeners();
		} else if (!input.equals(getParameter())) {
			parametersArrayList.set(0, input);
			notifyParameterValueChangedListeners();
		}
	}

	protected void simpleSet(T[] inputs) {
		assert (!owner.getLockedStatus());
		parametersArrayList = new ArrayList<T>(Arrays.asList(inputs));
		notifyParameterValueChangedListeners();
	}

	@Override
	public String getParameterString() {
		assert (!isMultivalued());
		T value = getParameter();
		if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}

	@Override
	public String[] getParameterStrings() {
		assert (isMultivalued());
		T[] values = getParameters();
		if (values != null) {
			String[] retVals = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				retVals[i] = values[i].toString();// TODO nulls?
			}
			return retVals;
		} else {
			return new String[0];
		}
	}

	@Override
	public void registerParameterValueChangedListener(
			ParameterValueChangedListener listener) {
		assert (listener != null);
		changedListeners.add(listener);
	}

	protected void notifyParameterValueChangedListeners() {
		for (ParameterValueChangedListener listener : changedListeners) {
			listener.parameterSettingsChanged(this);
		}
		owner.setComponentChanged();
	}

	@Override
	public void registerParameterNameDescriptionChangedListener(
			ParameterNameDescriptionChangedListener listener) {
		assert (listener != null);
		nameDescriptionChangedListeners.add(listener);
	}

	protected void notifyParameterNameDescriptionChangedListeners() {
		for (ParameterNameDescriptionChangedListener listener : nameDescriptionChangedListeners) {
			listener.parameterNameDescriptionChanged(this);
		}
		owner.setComponentChanged();
	}

	@Override
	public void registerParameterConfigurationChangedListener(
			ParameterConfigurationChangedListener listener) {
		assert (listener != null);
		parameterConfigurationChangedListeners.add(listener);
	}

	protected void notifyParameterConfigurationChangedListeners() {
		for (ParameterConfigurationChangedListener listener : parameterConfigurationChangedListeners) {
			listener.parameterConfigurationChanged(this);
		}
		owner.setComponentChanged();
	}

	protected void validateConstraints(String parameter)
			throws ConstraintFailedException {
		for (Constraint con : constraints) {
			con.validate(parameter);
		}
	}

	protected void validateConstraints(String[] parameters)
			throws ConstraintFailedException {
		for (String string : parameters) {
			validateConstraints(string);
		}
	}

	// TODO documentation
	public static Parameter constructParameter(ConfigurationParameter param,
			Object value) {

		Parameter retVal;

		String name = param.getName();
		String description = param.getDescription();

		// Replace null values
		name = name == null ? "" : name;
		description = description == null ? "" : description;

		boolean multivalued = param.isMultiValued();
		boolean mandatory = param.isMandatory();

		// Why are the types Strings? I cant do a switch statement..
		String type = param.getType();
		if (type.equals(ConfigurationParameter.TYPE_BOOLEAN)) {
			if (multivalued) {
				retVal = new BooleanParameter(name, description, mandatory,
						value != null ? (Boolean[]) value : null);
			} else {
				retVal = new BooleanParameter(name, description, mandatory,
						value != null ? (Boolean) value : null);
			}

		} else if (type.equals(ConfigurationParameter.TYPE_FLOAT)) {
			if (multivalued) {
				retVal = new FloatParameter(name, description, mandatory,
						value != null ? (Float[]) value : null);
			} else {
				retVal = new FloatParameter(name, description, mandatory,
						value != null ? (Float) value : null);
			}

		} else if (type.equals(ConfigurationParameter.TYPE_INTEGER)) {
			if (multivalued) {
				retVal = new IntegerParameter(name, description, mandatory,
						value != null ? (Integer[]) value : null);
			} else {
				retVal = new IntegerParameter(name, description, mandatory,
						value != null ? (Integer) value : null);
			}

		} else if (type.equals(ConfigurationParameter.TYPE_STRING)) {
			if (multivalued) {
				retVal = new StringParameter(name, description, mandatory,
						value != null ? (String[]) value : null);
			} else {
				retVal = new StringParameter(name, description, mandatory,
						value != null ? (String) value : null);
			}

		} else {
			retVal = null; // TODO throw an error here.
		}

		return retVal;
	}
}
