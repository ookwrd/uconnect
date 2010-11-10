package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;

public class MapConstraint extends Constraint {

	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	
	public MapConstraint(){}
	
	public MapConstraint(Constraint constraint){
		this.constraints.add(constraint);
	}
	
	public MapConstraint(ArrayList<Constraint> constraints){
		this.constraints.addAll(constraints);
	}
	
	public void addConstraint(Constraint constraint){
		constraints.add(constraint);
	}
	
	public void setConstraints(ArrayList<Constraint> constraints){
		
	}
}
