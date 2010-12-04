package org.u_compare.gui.model;


/**
 * 
 * Interface for components interested in being notified of changes to a components
 * list of parameters (not the values of those parameters see ParameterSettingsChangedListener)
 * 
 * @author lukemccrohon
 *
 */
public interface ParametersChangedListener {

	public void parametersChanged(Component component);
	
}
