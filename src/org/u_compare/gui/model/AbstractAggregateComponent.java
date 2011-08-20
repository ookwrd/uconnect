package org.u_compare.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.analysis_engine.metadata.FlowConstraints;
import org.apache.uima.analysis_engine.metadata.FlowControllerDeclaration;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.MetaDataObject;

/**
 * Abstract base class implementing functionality common to all aggregate components.
 * 
 * @author Luke McCrohon
 */
public abstract class AbstractAggregateComponent extends
		AbstractComponent implements AggregateComponent {

	@SuppressWarnings("serial")
	public class InvalidPositionException extends Exception {
		public InvalidPositionException(int position, int maxPosition){
			super("Tried to add component in position " + position + ", valid positions are 0<=x<=" + maxPosition);
		}
	}
	
	private ArrayList<SubComponentsChangedListener> subComponentsChangedListeners = new ArrayList<SubComponentsChangedListener>();
	private ArrayList<Component> subComponents = new ArrayList<Component>();
	
	protected FlowControllerDeclaration flowController;
	protected FlowConstraints flowConstraints;
	
	/**
	 * All extending classes should call this constructor.
	 */
	protected AbstractAggregateComponent(){
		super();
	}
	
	
	/**
	 * Components extending this class should be aggregates by definition.
	 * 
	 * @return true
	 */
	@Override
	public boolean isAggregate(){
		return true;
	}
	
	
	@Override
	public ArrayList<Component> getSubComponents(){
		return subComponents;
	}
	
	/**
	 * Adds the passed component to the aggregate at the specified position. Position must be between 0 and 
	 * the number of components. If a component is inserted in an intermediary position, subsequent components
	 * will be moved down.
	 *
	 * @param position to insert at
	 * @param component to insert
	 */
	@Override
	public void addSubComponent(int position, Component component) throws InvalidPositionException{
		
		//Check component is not already a subComponent
		if(subComponents.contains(component)){
			reorderSubComponent(component, position);
			return;
		}
		
		//Check valid position
		int maxPosition = subComponents.size();
		if(position < 0 || position > maxPosition){
			throw new InvalidPositionException(position,maxPosition);
		}
		
		subComponents.add(position, component);
		component.setSuperComponent(this);
		notifySubComponentsChangedListeners();
	}
	

	@Override
	public void reorderSubComponent(Component component, int position) throws InvalidPositionException {
		
		//Check subComponents contains component
		if(!subComponents.contains(component)){
			return;
		}
		
		//Check valid position
		int maxPosition = subComponents.size();
		if(position < 0 || position > maxPosition){
			throw new InvalidPositionException(position,maxPosition);
		}
		
		//Check if reordering is necessary
		int currentPosition = subComponents.indexOf(component);
		if(position == currentPosition || position == currentPosition+1){
			return;
		}
		
		if(position<currentPosition){
			subComponents.remove(component);
			subComponents.add(position, component);
		}else {
			subComponents.remove(component);
			subComponents.add(position-1,component);
		}
		
		notifySubComponentsChangedListeners();
	}


	/**
	 * Adds the passed component to the list of subcomponents if it is not already present. Will be added to end by default.
	 * 
	 * @param component to add
	 */
	@Override
	public void addSubComponent(Component component){

		assert(component != null);
		
		//Check component is not already a subComponent
		if(subComponents.contains(component)){
			return;
		}
		
		subComponents.add(component);
		component.setSuperComponent(this);
		notifySubComponentsChangedListeners();
	}
	
	@Override
	public void removeSubComponent(Component component){
		
		assert(component != null);
		
		if(!subComponents.contains(component)){
			return;
		}
		
		subComponents.remove(component);
		component.setSuperComponent(null);
		notifySubComponentsChangedListeners();
	}
	
	@Override
	public void setSubComponents(ArrayList<Component> components){
		
		assert(components != null);
		
		if(subComponents.equals(components)){
			return;
		}
		
		for(Component oldComponent : subComponents){
			oldComponent.setSuperComponent(null);
		}
		
		subComponents = new ArrayList<Component>();
		subComponents.addAll(components);
		for(Component newComponent : subComponents){
			newComponent.setSuperComponent(this);
		}
		
		notifySubComponentsChangedListeners();
	}
	
	@Override
	public void registerSubComponentsChangedListener(SubComponentsChangedListener listener){
		subComponentsChangedListeners.add(listener);	
	}
	
	
	protected void notifySubComponentsChangedListeners(){
	
		for(SubComponentsChangedListener listener : subComponentsChangedListeners){
			listener.subComponentsChanged();
		}

		setComponentChanged();
		
	}
	

	//The following methods default to always allowing modifications to the model, override this behaviour when appropriate.
	@Override
	public boolean canAddSubComponent(Component component, int position){
		return true;
	}
	
	@Override
	public boolean canAddSubComponent(Component component){
		return true;
	}
	
	@Override
	public boolean canRemoveSubComponent(Component component){
		return true;
	}
	
	@Override
	public boolean canSetSubComponents(ArrayList<Component> components){
		return true;
	}

	@Override
	public boolean canReorderSubComponent(Component component, int position){
		return true;
	}
	
	@Override
	public void setComponentSaved(){
		if(checkUnsavedChanges()){
			super.setComponentSaved();
			for(Component subComponent : subComponents){
				subComponent.setComponentSaved();
			}
		}
	}
	
	@Override
	public void setLocked(){
		setLocked(LockStatusEnum.DIRECTLOCK);
		
		for(Component child : subComponents){
			child.indirectlyLocked();
		}
	}
	
	@Override
	public void setUnlocked(){
		setLocked(LockStatusEnum.UNLOCKED);
		
		for(Component child : subComponents){
			child.indirectlyUnlocked();
		}
	}
	
	@Override
	public void indirectlyLocked(){
		if(lockStatus!=LockStatusEnum.DIRECTLOCK){
			setLocked(LockStatusEnum.INDIRECTLOCK);
			
			for(Component child : subComponents){
				child.indirectlyLocked();
			}
		}
	}
	
	@Override
	public void indirectlyUnlocked(){
		if(lockStatus!=LockStatusEnum.DIRECTLOCK){
			setLocked(LockStatusEnum.UNLOCKED);
			
			for(Component child : subComponents){
				child.indirectlyUnlocked();
			}
		}
	}
	
	protected ArrayList<Import> imports = new ArrayList<Import>();
	
	@Override
	public ResourceCreationSpecifier getResourceCreationSpecifier(){
		AnalysisEngineDescription retVal = (AnalysisEngineDescription)super.getResourceCreationSpecifier();
		
		if(flowConstraints instanceof FixedFlow){
			String[] flowStrings = new String[subComponents.size()];
			for(int i = 0; i < subComponents.size(); i++){
				
				//Can we use the current identifier? Not strictly needed, but can't hurt
				if(subComponents.get(i).getFlowControllerIdentifier()!=null){
					String current = subComponents.get(i).getFlowControllerIdentifier();
					
					//Check it hasn't been used already
					if(!Arrays.asList(flowStrings).contains(current)){
						flowStrings[i] = current;
						continue;
					}
				}
				
				String id = ""+i;
				flowStrings[i] = id;
				subComponents.get(i).setFlowControllerIdentifier(id);
			}
			FixedFlow flow = (FixedFlow)flowConstraints;
			flow.setFixedFlow(flowStrings);
			retVal.getAnalysisEngineMetaData().setFlowConstraints(flow);
		}else{
			System.err.println("Unknown flow Constraints type");
			retVal.getAnalysisEngineMetaData().setFlowConstraints(flowConstraints);
		}
		
		retVal.setFlowControllerDeclaration(flowController);
		if(flowController!= null){
			System.err.println("Flow controller not null. Specifier type " + flowController.getSpecifier());
			//TODO How do we handle the constraints in this case?
		}
		
		Map<String, MetaDataObject> metaData;
		metaData = retVal.getDelegateAnalysisEngineSpecifiersWithImports();
		
		/*for(Import imp : imports){
			//UIMAFramework.getResourceSpecifierFactory().createMe
			//metaData.put(comp.getName(),comp.getUIMADescription()); //TODO if I do it this way can I run??
			//TODO obviously
			
			/*
			 * So can I avoid saving everything to the file system piecemeal by making use of
			 * components with their subcomponents built?
			 * 
			 *  Similarly, can I save the xml like this in a single file? Rather than seperately.
			 *  
			 *  Lets try.
			 */
		//}
		
		for(Component comp : getSubComponents()){
			metaData.put(comp.getFlowControllerIdentifier(),comp.getResourceCreationSpecifier());
		}
		
		return retVal;
	}
	
	protected void extractFromProcessingResouceMetaData(
			AnalysisEngineMetaData metaData){
		super.extractFromProcessingResouceMetaData(metaData);
		
		flowConstraints = metaData.getFlowConstraints();
	}
	
	@Override
	protected void notifyLockedStatusChangeListeners(){
		super.notifyLockedStatusChangeListeners();
		for(Component component : subComponents){
			component.notifyParentLockedStatusChangeListeners(this);
		}
	}
}
