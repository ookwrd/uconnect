package org.u_compare.gui.model;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.u_compare.gui.model.UIMAWorkflow.WorkflowStatus;
import org.u_compare.gui.debugging.PrivilegedAccessor;


/**
 * JUnit Tests for UIMAWorkflow and associated abstract classes.
 * 
 * @author lukemccrohon1
 *
 */
public class UIMAWorkflowTest{

	private ArrayList<UIMAComponent> inputs= new ArrayList<UIMAComponent>();
	private UIMAWorkflow testWorkflow; 

	@Before
	public void setUp(){
		inputs.add(new MockUIMAComponent("a"));
		inputs.add(new MockUIMAComponent("b"));
		inputs.add(new MockUIMAComponent("c"));
		testWorkflow = new UIMAWorkflow(inputs);
	}

	@After
	public void tearDown(){
	}

	@Test
	public void simpleWorkflowConstruction(){
		assertTrue(testWorkflow.getSubComponents().size() == 3);
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(0)).getIdentifier().equals("a"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(1)).getIdentifier().equals("b"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(2)).getIdentifier().equals("c"));
	}

	@Test
	public void settersAndStatuses(){
		assertTrue(testWorkflow.isAggregate());
		assertTrue(testWorkflow.isWorkflow());

		String name = "Component Name";
		testWorkflow.setName(name);
		assertTrue(testWorkflow.getName().equals(name));

		String description = "This is the components description";
		testWorkflow.setDescription(description);
		assertTrue(testWorkflow.getDescription().equals(description));	
	}

	@Test(expected=InvalidPositionException.class)
	public void boundsCheck1() throws InvalidPositionException{
		testWorkflow.addSubComponent(-1, new MockUIMAComponent());
	}

	@Test(expected=InvalidPositionException.class)
	public void boundsCheck2() throws InvalidPositionException{
		testWorkflow.addSubComponent(4, new MockUIMAComponent());
	}

	@Test
	public void finalInsertion() throws InvalidPositionException{
		testWorkflow.addSubComponent(3, new MockUIMAComponent("X"));
		assertTrue(testWorkflow.getSubComponents().size() == 4);
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(0)).getIdentifier().equals("a"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(1)).getIdentifier().equals("b"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(2)).getIdentifier().equals("c"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(3)).getIdentifier().equals("X"));
	}

	@Test
	public void intermediaryInsertion1() throws InvalidPositionException{
		testWorkflow.addSubComponent(1, new MockUIMAComponent("X"));
		assertTrue(testWorkflow.getSubComponents().size() == 4);
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(0)).getIdentifier().equals("a"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(1)).getIdentifier().equals("X"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(2)).getIdentifier().equals("b"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(3)).getIdentifier().equals("c"));
	}

	@Test
	public void intermediaryInsertion2() throws InvalidPositionException{
		testWorkflow.addSubComponent(3, new MockUIMAComponent("X"));
		assertTrue(testWorkflow.getSubComponents().size() == 4);
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(0)).getIdentifier().equals("a"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(1)).getIdentifier().equals("b"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(2)).getIdentifier().equals("c"));
		assertTrue(((MockUIMAComponent)testWorkflow.getSubComponents().get(3)).getIdentifier().equals("X"));
	}

	private static class TestListener implements WorkflowStatusListener{
		private ArrayList<WorkflowStatus> statuses;

		public TestListener(){
			statuses = new ArrayList<WorkflowStatus>();
		}

		public void workflowStatusChanged(UIMAWorkflow workflow) {
			statuses.add(workflow.getStatus());
		}

		public ArrayList<WorkflowStatus> getStatuses(){
			return statuses;
		}
	}

	@Test
	public void testListeners() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		TestListener listener = new TestListener();

		testWorkflow.registerWorkflowStatusListener(listener);
		
		PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.ERROR);
		PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.LOADING);

		ArrayList<WorkflowStatus> statuses = listener.getStatuses();

		//Don't want to risk out of bounds exceptions
		assertTrue(statuses.size() == 2);
		assertTrue(statuses.get(0) == WorkflowStatus.ERROR);
		assertTrue(statuses.get(1) == WorkflowStatus.LOADING);

		listener = new TestListener();

		testWorkflow.registerWorkflowStatusListener(listener);
		testWorkflow.registerWorkflowStatusListener(listener);

		PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.ERROR);
		PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.LOADING);

		statuses = listener.getStatuses();

		assertTrue(statuses.size() == 4);
		assertTrue(statuses.get(0) == WorkflowStatus.ERROR);
		assertTrue(statuses.get(1) == WorkflowStatus.ERROR);
		assertTrue(statuses.get(2) == WorkflowStatus.LOADING);
		assertTrue(statuses.get(3) == WorkflowStatus.LOADING);
	}

	@Test(expected=InvalidStatusException.class)
	public void failOnErrorStatus() throws Throwable{
		PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.ERROR);
		try{
			PrivilegedAccessor.invokeMethod(testWorkflow,"runWorkflow", new Object[]{});
		}catch(Exception e){
			if(e.getCause() != null){
				throw e.getCause();
			}else{
				throw e;	
			}
		}
	}	
	
	@Test(expected=InvalidStatusException.class)
	public void stopOnLoading() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InvalidStatusException{
		try{
			PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.LOADING);
			PrivilegedAccessor.invokeMethod(testWorkflow,"stopWorkflow", new Object[]{});
		}catch(InvocationTargetException e){
			if(e.getCause() instanceof InvalidStatusException){
				throw (InvalidStatusException)e.getCause();
			}else{
				throw e;
			}
		}
	}
	
	@Test(expected=InvalidStatusException.class)
	public void runOnError() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InvalidStatusException{		
		try{
		PrivilegedAccessor.invokeMethod(testWorkflow,"setStatus",WorkflowStatus.ERROR);
		PrivilegedAccessor.invokeMethod(testWorkflow,"runWorkflow");
		}catch(InvocationTargetException e){
			if(e.getCause() instanceof InvalidStatusException){
				throw (InvalidStatusException)e.getCause();
			}else{
				throw e;
			}
		}
	}
	
	private static class TestListener1 implements DescriptionChangeListener, InputOutputChangeListener, SubComponentsChangedListener, SavedStatusChangeListener, MinimizedStatusChangeListener, LockedStatusChangeListener{

		private ArrayList<String> names;
		private ArrayList<String> descriptions;
		private ArrayList<Boolean> saves;
		
		private ArrayList<ArrayList<AnnotationType>> inputs;
		private ArrayList<ArrayList<AnnotationType>> outputs;
		
		private ArrayList<ArrayList<UIMAComponent>> subComponents;
		
		ArrayList<Boolean> minimizeds = new ArrayList<Boolean>();
		ArrayList<Boolean> lockeds = new ArrayList<Boolean>();
		
		public TestListener1(){
			names = new ArrayList<String>();
			descriptions = new ArrayList<String>();
			inputs = new ArrayList<ArrayList<AnnotationType>>();
			outputs = new ArrayList<ArrayList<AnnotationType>>();
			subComponents = new ArrayList<ArrayList<UIMAComponent>>();
			saves = new ArrayList<Boolean>();
		}

		public void ComponentDescriptionChanged(UIMAComponent component) {
			names.add(component.getName());
			descriptions.add(component.getDescription());
		}
		
		public ArrayList<String> getNames(){
			return names;
		}
		
		public ArrayList<String> getDescriptions(){
			return descriptions;
		}

		public void inputOutputChanged(UIMAComponent component) {
			
			inputs.add(component.getInputTypes());
			outputs.add(component.getOutputTypes());
			
		}
		
		public ArrayList<ArrayList<AnnotationType>> getInputs(){
			
			return inputs;
		}
		
		public ArrayList<ArrayList<AnnotationType>> getOutputs(){
			return outputs;
		}

		public void subComponentAddRemoved(ArrayList<UIMAComponent> components) {
			subComponents.add(components);
		}
		
		public ArrayList<ArrayList<UIMAComponent>> getSubComponents(){
			return subComponents;
		}

		@Override
		public void savedStatusChanged(UIMAComponent component) {
			saves.add(component.checkUnsavedChanges());
		}
		
		public ArrayList<Boolean> getSaves(){
			return saves;
		}

		@Override
		public void lockStatusChanged(UIMAComponent component) {
			lockeds.add(component.getLockedStatus());
		}

		@Override
		public void minimizedStatusChanged(UIMAComponent component) {
			minimizeds.add(component.getMinimizedStatus());
		}
	}
	
	@Test
	public void testNamesAndDescriptions(){
		
		testWorkflow.setName("OriginalName");
		testWorkflow.setDescription("OriginalDescription");
		
		TestListener1 listener = new TestListener1();
		
		testWorkflow.registerComponentDescriptionChangeListener(listener);
		
		testWorkflow.setName("NewName");
		testWorkflow.setDescription("NewDescription");
		
		ArrayList<String> names = listener.getNames();
		ArrayList<String> descriptions = listener.getDescriptions();
		
		assertTrue(names.size() == 2);
		assertTrue(names.get(0).equals("NewName"));
		assertTrue(descriptions.get(0).equals("OriginalDescription"));
		assertTrue(names.get(1).equals("NewName"));
		assertTrue(descriptions.get(1).equals("NewDescription"));
	}
	
	@Test
	public void testInputsOutputsSimple(){
		
		TestListener1 listener = new TestListener1();
		testWorkflow.registerInputOutputChangeListener(listener);
		
		ArrayList<AnnotationType> input = new ArrayList<AnnotationType>();
		
		AnnotationType type1 = new AnnotationType("type1");
		
		input.add(type1);
		input.add(new AnnotationType("type2"));
		testWorkflow.setInputTypes(input);
		
		assertTrue(listener.getInputs().size() == 1);
		assertTrue(listener.getOutputs().size() == 1);
		assertTrue(listener.getInputs().get(0).size() == 2);
		assertTrue(listener.getOutputs().get(0).size() == 0);
		assertTrue(listener.getInputs().get(0).contains(type1));
		
		AnnotationType type3 = new AnnotationType("Type3");
		testWorkflow.addOutputType(type3);
		
		assertTrue(listener.getInputs().size() == 2);
		assertTrue(listener.getOutputs().size() == 2);
		assertTrue(listener.getInputs().get(1).size() == 2);
		assertTrue(listener.getOutputs().get(1).size() == 1);
		assertTrue(listener.getOutputs().get(1).contains(type3));
		
		testWorkflow.removeInputType(type1);
		
		assertTrue(listener.getInputs().size() == 3);
		assertTrue(listener.getOutputs().size() == 3);
		assertTrue(listener.getInputs().get(2).size() == 1);
		assertTrue(listener.getOutputs().get(2).size() == 1);
		assertFalse(listener.getInputs().get(2).contains(type1));
		
	}
	
	@Test
	public void testInputsOutputsDontTriggerWithoutChange(){
		
		TestListener1 listener = new TestListener1();
		testWorkflow.registerInputOutputChangeListener(listener);
		
		ArrayList<AnnotationType> input = new ArrayList<AnnotationType>();
		
		AnnotationType type1 = new AnnotationType("type1");
		
		input.add(type1);
		input.add(new AnnotationType("type2"));
		testWorkflow.setInputTypes(input);
		
		testWorkflow.removeInputType(new AnnotationType("Not Real"));
		
		assertTrue(listener.getInputs().size() == 1);
		assertTrue(listener.getOutputs().size() == 1);
		
		testWorkflow.removeOutputType(type1);
		
		assertTrue(listener.getInputs().size() == 1);
		assertTrue(listener.getOutputs().size() == 1);
		
		testWorkflow.addInputType(type1);
		
		assertTrue(listener.getInputs().size() == 1);
		assertTrue(listener.getOutputs().size() == 1);
		
		testWorkflow.setInputTypes(input);
		
		assertTrue(listener.getInputs().size() == 1);
		assertTrue(listener.getOutputs().size() == 1);
		
	}
	
	@Test
	public void testSubComponentChangedListener(){
		
		TestListener1 listener = new TestListener1();
		testWorkflow.registerSubComponentsChangedListener(listener);
		
		assertTrue(testWorkflow.getSubComponents().size() == 3);
		
		UIMAComponent d = new MockUIMAComponent("d");
		
		testWorkflow.addSubComponent(d);
		
		assertTrue(testWorkflow.getSubComponents().size() == 4);
		assertTrue(listener.getSubComponents().size() == 1);
		assertTrue(listener.getSubComponents().get(0).size() == 4);
		
		testWorkflow.removeSubComponent(d);
		assertTrue(listener.getSubComponents().size() == 2);
		assertTrue(listener.getSubComponents().get(1).size() == 3);
		
		testWorkflow.removeSubComponent(d);
		assertTrue(listener.getSubComponents().size() == 2);
		
		testWorkflow.setSubComponents(inputs);
		assertTrue(listener.getSubComponents().size() == 2);
		
		testWorkflow.setSubComponents(new ArrayList<UIMAComponent>());
		assertTrue(listener.getSubComponents().size() == 3);
		assertTrue(listener.getSubComponents().get(2).size() == 0);
	}

	@Test
	public void testSubComponentReordered() throws InvalidPositionException{
		
		TestListener1 listener = new TestListener1();
		testWorkflow.registerSubComponentsChangedListener(listener);
		
		assertTrue(testWorkflow.getSubComponents().size() == 3);
		
		UIMAComponent d = new MockUIMAComponent("d");
		
		testWorkflow.addSubComponent(d);
		
		assertTrue(testWorkflow.getSubComponents().size() == 4);
		assertTrue(testWorkflow.getSubComponents().indexOf(d) == 3);
		assertTrue(listener.getSubComponents().size() == 1);
		assertTrue(listener.getSubComponents().get(0).indexOf(d) == 3);
		
		//Correct reorder earlier
		testWorkflow.reorderSubComponent(d, 1);
		assertTrue(listener.getSubComponents().size() == 2);
		assertTrue(listener.getSubComponents().get(1).size() == 4);
		assertTrue(listener.getSubComponents().get(1).indexOf(d) == 1);
		
		//No unnecessary updates
		testWorkflow.reorderSubComponent(d, 1);
		testWorkflow.reorderSubComponent(d, 2);
		assertTrue(listener.getSubComponents().size() == 2);
		
		//Correct reorder later
		testWorkflow.reorderSubComponent(d, 4);
		assertTrue(listener.getSubComponents().get(2).indexOf(d) == 3);
	}
	
	@Test
	public void lockedStatusListenerTest(){
		TestListener1 listener = new TestListener1();
		testWorkflow.registerLockedStatusChangeListener(listener);
		
		assertTrue(testWorkflow.getLockedStatus()==false);
		
		testWorkflow.setUnlocked();
		
		assertTrue(testWorkflow.getLockedStatus()==false);
		assertTrue(listener.lockeds.size() == 0);
		
		testWorkflow.setLocked();
		
		assertTrue(testWorkflow.getLockedStatus()==true);
		assertTrue(listener.lockeds.size() == 1);
		
		testWorkflow.setUnlocked();
		
		assertTrue(testWorkflow.getLockedStatus()==false);
		assertTrue(listener.lockeds.size() == 2);
	}
	
	@Test
	public void minimizedStatusListeners(){
		
		TestListener1 listener = new TestListener1();
		testWorkflow.registerMinimizedStatusChangeListener(listener);
		
		assertTrue(testWorkflow.getMinimizedStatus() == false);
		
		testWorkflow.setMinimizedStatus(false);

		assertTrue(testWorkflow.getMinimizedStatus() == false);
		assertTrue(listener.minimizeds.size()== 0);
		
		testWorkflow.setMinimizedStatus(true);
		
		assertTrue(testWorkflow.getMinimizedStatus() == true);
		assertTrue(listener.minimizeds.size() == 1);
		assertTrue(listener.minimizeds.get(0)==true);
		
	}
	
	
	@Test(expected=InvalidPositionException.class)
	public void reorderPositionConstraints1() throws InvalidPositionException{
		TestListener1 listener = new TestListener1();
		testWorkflow.registerSubComponentsChangedListener(listener);
		assertTrue(testWorkflow.getSubComponents().size() == 3);
		UIMAComponent d = new MockUIMAComponent("d");
		testWorkflow.addSubComponent(d);
		testWorkflow.reorderSubComponent(d, -1);
	}
	
	@Test(expected=InvalidPositionException.class)
	public void reorderPositionConstraints2() throws InvalidPositionException{
		TestListener1 listener = new TestListener1();
		testWorkflow.registerSubComponentsChangedListener(listener);
		assertTrue(testWorkflow.getSubComponents().size() == 3);
		UIMAComponent d = new MockUIMAComponent("d");
		testWorkflow.addSubComponent(d);
		testWorkflow.reorderSubComponent(d, 5);
	}
	
	@Test
	public void superComponentsInitilized(){
		assertTrue(testWorkflow.getSuperComponent()==null);
		UIMAComponent child = testWorkflow.getSubComponents().get(1);
		assertTrue(child.getSuperComponent().equals(testWorkflow));
	}
	
	@Test
	public void superComponentsSimpleOperations() throws InvalidPositionException{
		MockAggregateUIMAComponent agg = new MockAggregateUIMAComponent();
		testWorkflow.addSubComponent(agg);
		assertTrue(agg.getSuperComponent().equals(testWorkflow));
		MockUIMAComponent simple = new MockUIMAComponent();
		agg.addSubComponent(simple);
		assertTrue(simple.getSuperComponent().equals(agg));
		testWorkflow.reorderSubComponent(agg, 0);
		assertTrue(agg.getSuperComponent().equals(testWorkflow));
		assertTrue(simple.getSuperComponent().equals(agg));
		testWorkflow.addSubComponent(simple);
		assertTrue(simple.getSuperComponent().equals(testWorkflow));
		testWorkflow.removeSubComponent(agg);
		assertTrue(agg.getSuperComponent()==null);
		ArrayList<UIMAComponent> comps = new ArrayList<UIMAComponent>();
		comps.add(agg);
		testWorkflow.setSubComponents(comps);
		assertTrue(simple.getSuperComponent()==null);
		assertTrue(agg.getSuperComponent().equals(testWorkflow));
	}
	
	@Test
	public void savedStatusListener(){
		TestListener1 listener = new TestListener1();
		testWorkflow.setComponentSaved();
		testWorkflow.registerSavedStatusChangeListener(listener);

		
		testWorkflow.setComponentChanged();
		assertTrue(listener.getSaves().size() == 1);
		assertTrue(listener.getSaves().get(0) == true);
		
		testWorkflow.setComponentSaved();
		assertTrue(listener.getSaves().size() == 2);
		assertTrue(listener.getSaves().get(1) == false);
		
		MockAggregateUIMAComponent agg = new MockAggregateUIMAComponent();
		MockUIMAComponent simple = new MockUIMAComponent();
		agg.addSubComponent(simple);
		testWorkflow.addSubComponent(agg);
		simple.setComponentChanged();
		
		assertTrue(listener.getSaves().size() == 3);
		assertTrue(listener.getSaves().get(2) == true);
		
		simple.setComponentSaved();
		assertTrue(listener.getSaves().size() == 3);
		assertTrue(listener.getSaves().get(2) == true);	
	}
	
	@Test
	public void savedStatusListenerBubblingTest(){
		TestListener1 listener = new TestListener1();
		
		MockAggregateUIMAComponent agg = new MockAggregateUIMAComponent();
		MockUIMAComponent simple = new MockUIMAComponent();
		agg.addSubComponent(simple);
		testWorkflow.addSubComponent(agg);
		testWorkflow.setComponentSaved();
		simple.registerSavedStatusChangeListener(listener);
		
		agg.setComponentChanged();
		assertTrue(listener.getSaves().size() == 0);
		agg.setComponentSaved();
		assertTrue(listener.getSaves().size() == 0);
		testWorkflow.setComponentChanged();
		assertTrue(listener.getSaves().size() == 0);
		simple.setComponentChanged();
		assertTrue(listener.getSaves().size() == 1);
		assertTrue(listener.getSaves().get(0) == true);	
		testWorkflow.setComponentSaved();
		assertTrue(listener.getSaves().size() == 2);
		assertTrue(listener.getSaves().get(1) == false);	
	}
}
