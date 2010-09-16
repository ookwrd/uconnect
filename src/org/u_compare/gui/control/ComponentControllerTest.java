package org.u_compare.gui.control;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;
import org.u_compare.gui.debugging.ExampleWorkflowFactory;
import org.u_compare.gui.debugging.PrivilegedAccessor;
import org.u_compare.gui.model.MockAggregateUIMAComponent;
import org.u_compare.gui.model.MockUIMAComponent;
import org.u_compare.gui.model.UIMAAggregateComponent;
import org.u_compare.gui.model.UIMAComponent;
import org.u_compare.gui.model.UIMAWorkflow;

@SuppressWarnings("unchecked")//Using PrivilegedAccessor
public class ComponentControllerTest {

	UIMAWorkflow model;
	ComponentController control;
	
	UIMAComponent component;
	ComponentController controller;
	
	UIMAAggregateComponent componentAggregate;
	ComponentController controllerAggregate;
	
	@Before
	public void setUp(){
		model = ExampleWorkflowFactory.aggregate();
		control = new ComponentController(model);
		
		component = new MockUIMAComponent();
		controller = new ComponentController(component);
	
		componentAggregate = new MockAggregateUIMAComponent();
		controllerAggregate = new ComponentController(componentAggregate);
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
		controllerAggregate.addSubComponent(control, 0);
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).contains(control));
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
		
		controllerAggregate.addSubComponent(child, 0);
		assertFalse(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).contains(child));
	
		controllerAggregate.addSubComponent(child, 0);
		assertFalse(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(control, "subControllers")).contains(child));
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).contains(child));
		assertTrue(((ArrayList<ComponentController>)PrivilegedAccessor.getValue(controllerAggregate, "subControllers")).size()==1);
	}
	
	@Test
	public void moveToSelf() throws InvalidSubComponentException{
		control.addSubComponent(controller, 41);
	} 
}
