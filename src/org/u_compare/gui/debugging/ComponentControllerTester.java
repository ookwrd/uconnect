package org.u_compare.gui.debugging;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.InvalidSubComponentException;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

@SuppressWarnings("unchecked")//Using PrivilegedAccessor
public class ComponentControllerTester {

	Workflow model;
	ComponentController control;
	
	Component component;
	ComponentController controller;
	
	AggregateComponent componentAggregate;
	ComponentController controllerAggregate;
	
	@Before
	public void setUp(){
		model = ExampleWorkflowFactory.aggregate();
		control = new ComponentController(model, true);
		
		component = new MockComponent();
		controller = new ComponentController(component, true);
	
		componentAggregate = new MockAggregateComponent();
		controllerAggregate = new ComponentController(componentAggregate, true);
	}

	@After
	public void tearDown(){
	}
	

	@Test
	public void checkParentage() throws IllegalAccessException, NoSuchFieldException{
		assertTrue(PrivilegedAccessor.getValue(control, "parent") == null);
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		assertTrue(PrivilegedAccessor.getValue(child, "parent").equals(control));
	} 
	
	@Test
	public void simpleAdd() throws IllegalAccessException, NoSuchFieldException, InvalidSubComponentException{
		control.addSubComponent(controller, 0);
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(controller));
	}
	
	@Test
	public void simpleAdd1() throws InvalidSubComponentException, IllegalAccessException, NoSuchFieldException{
		control.addSubComponent(controllerAggregate, 0);
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(controllerAggregate));
	}
	
	@Test
	public void canAddNonAggregate(){
		assertFalse(controller.canAddSubComponent(control,0));	
	}
	
	@Test
	public void canAddNonEditable(){
		controller.setLocked(true);
		assertFalse(controller.canAddSubComponent(control,0));	
	}
	
	@Test
	public void canRemoveNonEditable() throws IllegalAccessException, NoSuchFieldException{
		control.setLocked(true);
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		assertFalse(child.canRemove());
	}
	
	
	@Test(expected=InvalidSubComponentException.class)
	public void addNonAggregate() throws InvalidSubComponentException{
		controller.addSubComponent(control,0);	
	}
	
	@Test(expected=InvalidSubComponentException.class)
	public void addNonEditable() throws InvalidSubComponentException{
		controllerAggregate.setLocked(true);
		controllerAggregate.addSubComponent(control, 0);
	}	
	
	@Test(expected=InvalidSubComponentException.class)
	public void moveFromUneditable() throws IllegalAccessException, NoSuchFieldException, InvalidSubComponentException{
		control.setLocked(true);
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		controllerAggregate.addSubComponent(child, 0);	
	}
	
	@Test
	public void parentsAdjusted() throws IllegalAccessException, NoSuchFieldException, InvalidSubComponentException{
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		controllerAggregate.addSubComponent(child, 0);
		assertTrue(PrivilegedAccessor.getValue(child, "parent").equals(controllerAggregate));
	}

	@Test
	public void remove() throws IllegalAccessException, NoSuchFieldException{
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		control.removeSubComponent(child);
		assertFalse(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
	}
	
	@Test
	public void remove1() throws IllegalAccessException, NoSuchFieldException{
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		child.removeComponent();
		assertFalse(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
	}
	
	@Test
	public void objectsAdjusted() throws IllegalAccessException, NoSuchFieldException, InvalidSubComponentException{
		ComponentController child = ((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).get(0);
		child.removeComponent();
		control.addSubComponent(child, 1);
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		
		System.out.println(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).size());
		controllerAggregate.addSubComponent(child, 0);
		assertFalse(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).contains(child));
	
		System.out.println(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).size());
		controllerAggregate.addSubComponent(child, 0);
		assertFalse(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).contains(child));
		System.out.println(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).size());
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).size()==1);
	}
	
	@Test
	public void moveToSelf() throws InvalidSubComponentException{
		control.addSubComponent(controller, 41);
	} 
}
