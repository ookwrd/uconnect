package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

public class MultivaluedParameter<T> extends AbstractParameter {

	private ArrayList<T> valuesArrayList = new ArrayList<T>();
	
	public MultivaluedParameter(String name, String description,
			boolean mandatory) {
		
		super(name, description, mandatory);
	}

	@Override
	public boolean isMultivalued(){
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Parameter> getValues(){
		return (ArrayList<Parameter>) valuesArrayList;
	}
	
	public void addValue(T value){
		valuesArrayList.add(value);
	}
	
	public void removeValue(int index){
		//TODO
	}
	
	public void setValues(ArrayList<T> values){
		//TODO
	}
	
	@Override
	public String getParameterString() {
		// TODO Auto-generated method stub
		return null;
	}

}
