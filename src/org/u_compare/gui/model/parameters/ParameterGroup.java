package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;

public class ParameterGroup {

	private ArrayList<ParameterGroupConfigurationChangeListener> parameterGroupConfigurationChangeListeners = new ArrayList<ParameterGroupConfigurationChangeListener>();
	
	private String[] names = new String[0];
	private ArrayList<Parameter> configurationParameters = new ArrayList<Parameter>();
	
	private Component parent;
	
	public ParameterGroup(Component parent){
		this.parent = parent;
	}
	
	public String[] getNames(){
		return names;
	}
	
	public void setNames(String[] names){
		this.names = names;
		notifyParameterGroupConfigurationChangedListeners();
	}
	
	public ArrayList<Parameter> getConfigurationParameters(){
		return configurationParameters;
	}

	public void setConfigurationParameters(ArrayList<Parameter> params){
		
		//Remove old parameters
		for(Parameter param : configurationParameters){
			param.setOwner(null);
		}
		
		for(Parameter param : params){
			param.setOwner(parent);
		}
		
		this.configurationParameters = params;
		
		notifyParametersChangedListeners();
	}
	
	
	public void registerParameterGroupConfigurationChangedListener(ParameterGroupConfigurationChangeListener listener){
		assert(listener != null);
		parameterGroupConfigurationChangeListeners.add(listener);
	}
	
	protected void notifyParameterGroupConfigurationChangedListeners(){
		for(ParameterGroupConfigurationChangeListener listener : parameterGroupConfigurationChangeListeners){
			listener.parameterGroupConfigurationChanged(this);
		}
		parent.setComponentChanged();
	}
	
	public interface ParameterGroupConfigurationChangeListener{
		public void parameterGroupConfigurationChanged(ParameterGroup group);
	}
	
	/**
	 * Not to be confused with ParameterSettingsChangeListener in the Parameter package.
	 */
	protected void notifyParametersChangedListeners(){
		((AbstractComponent)parent).notifyParametersChangedListeners();
	}
}
