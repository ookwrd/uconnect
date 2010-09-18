package org.u_compare.gui.debugging;

import java.util.ArrayList;

import org.u_compare.gui.model.MockAggregateUIMAComponent;
import org.u_compare.gui.model.MockUIMAComponent;
import org.u_compare.gui.model.UIMAAggregateComponent;
import org.u_compare.gui.model.UIMAComponent;
import org.u_compare.gui.model.UIMAWorkflow;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.StringParameter;

import com.sun.xml.internal.ws.api.pipe.NextAction;

/**
 * THIS CLASS IS FOR TESTING PURPOSES ONLY
 * 
 * A factory class for constructing different example workflows useful for testing/debugging.
 * 
 * @author Luke Mccrohon
 *
 */
public class ExampleWorkflowFactory {

	//Cannot be constructed
	private ExampleWorkflowFactory(){}
	
	/**
	 * Returns a blank workflow with no components in it.
	 * 
	 * @return a blank workflow
	 */
	public static UIMAWorkflow blank(){
		
		UIMAWorkflow workflow = new UIMAWorkflow();
		
		workflow.setName("Blank Workflow");
		workflow.setDescription("As you can see this workflow is blank, but think of all the wonderful possibilities");
		
		return workflow;
		
	}
	
	/**
	 * Returns a workflow comprised of 3 non-aggregate components.
	 * 
	 * @return returns a simple workflow.
	 */
	public static UIMAWorkflow simple(){
		
		UIMAWorkflow workflow = new UIMAWorkflow();
		
		workflow.setName("Simple Workflow");
		workflow.setDescription("Some people might say I am simple, but I am happy being who I am. Look at my 3 wonderful (simple) childrem.");
		
		UIMAComponent component1 = new MockUIMAComponent();
		UIMAComponent component2 = new MockUIMAComponent();
		UIMAComponent component3 = new MockUIMAComponent();
		
		component1.setName("One");
		component2.setName("Two");
		component3.setName("Three");
		
		component1.setDescription("Im the first and therefore the best, my siblings don't realise this simple logic.");
		component2.setDescription("Honestly, I don't know who I hate more.");
		component2.setDescription("Lucky number 3! wait what? 7? Thats not even a number!");
		
		workflow.addSubComponent(component1);
		workflow.addSubComponent(component2);
		workflow.addSubComponent(component3);
		
		return workflow;
	}
	
	/**
	 * Returns a workflow comprised of 3 non-aggregate components with parameters
	 * 
	 * @return returns a simple workflow.
	 */
	public static UIMAWorkflow simpleWithParameters(){
		


		
		UIMAWorkflow workflow = new UIMAWorkflow();
		
		workflow.setName("Simple Workflow");
		workflow.setDescription("Some people might say I am simple, but I am happy being who I am. Look at my 3 wonderful (simple) childrem.");
		
		ArrayList<Parameter> c1params = new ArrayList<Parameter>();
		c1params.add(new BooleanParameter("A true boolean",true));
		c1params.add(new BooleanParameter("A false boolean",false));
		
		ArrayList<Parameter> c2params = new ArrayList<Parameter>();
		c2params.add(new StringParameter("A String to configure", "default value"));
		c2params.add(new IntegerParameter("An Integer to configure", 5));
		
		ArrayList<Parameter> c3params = new ArrayList<Parameter>();
		c3params.add(new BooleanParameter("A true boolean",true));
		c3params.add(new BooleanParameter("A false boolean",false));
		c3params.add(new StringParameter("A String to configure", "default value"));
		
		
		UIMAComponent component1 = new MockUIMAComponent(c1params);
		UIMAComponent component2 = new MockUIMAComponent(c2params);
		UIMAComponent component3 = new MockUIMAComponent(c3params);
		
		component1.setName("One");
		component2.setName("Two");
		component3.setName("Three");
		
		component1.setDescription("Im the first and therefore the best, my siblings don't realise this simple logic.");
		component2.setDescription("Honestly, I don't know who I hate more.");
		component2.setDescription("Lucky number 3! wait what? 7? Thats not even a number!");
		
		workflow.addSubComponent(component1);
		workflow.addSubComponent(component2);
		workflow.addSubComponent(component3);
		
		return workflow;
	}
	
	/**
	 * Returns a workflow comprised of two toplevel components, the first of which is an aggregate of two simple components.
	 * 
	 * @return
	 */
	public static UIMAWorkflow aggregate(){
		
		UIMAWorkflow workflow = new UIMAWorkflow();
		
		ArrayList<Parameter> workflowParams = new ArrayList<Parameter>();
		workflowParams.add(new BooleanParameter("A true boolean",true));
		workflowParams.add(new BooleanParameter("A false boolean",false));
		
		UIMAComponent simplea = new MockUIMAComponent(workflowParams);
		UIMAComponent simpleb = new MockUIMAComponent(workflowParams);
		UIMAComponent simplec = new MockUIMAComponent(workflowParams);
		
		simplea.setName("Simple Component a");
		simplea.setDescription("This is a component. For some reason it is called a");
		
		simpleb.setName("Simple Component b");
		simpleb.setDescription("This is a component. For some reason it is called b");

		simplec.setName("Simple Component c");
		simplec.setDescription("This is a component. For some reason it is called c");
		
		UIMAAggregateComponent aggregate = new MockAggregateUIMAComponent(workflowParams);
		
		aggregate.setName("The nasty nasty Aggregate Component");
		aggregate.setDescription("Not afraid of aggregate components? Well what a silly little boy you are then.");
		
		aggregate.addSubComponent(simplea);
		aggregate.addSubComponent(simpleb);
		
		workflow.addSubComponent(aggregate);
		workflow.addSubComponent(simplec);
		
		return workflow;
		
	}
	
	public static UIMAWorkflow deepAggregate(int depth, int branchingfactor){

		UIMAWorkflow workflow = new UIMAWorkflow();
		workflow.setDescription("W");
		
		ArrayList<UIMAAggregateComponent> bottomLevel = new ArrayList<UIMAAggregateComponent>();
		bottomLevel.add(workflow);
		ArrayList<UIMAAggregateComponent> nextLevel = new ArrayList<UIMAAggregateComponent>();
		
		for(int i = 0; i < depth; i++){
			for(UIMAAggregateComponent current : bottomLevel){
				String currentDesc = current.getDescription();
				for(int j = 0; j < branchingfactor; j++){
					UIMAAggregateComponent toAdd = new MockAggregateUIMAComponent();
					toAdd.setDescription(currentDesc + j);
					current.addSubComponent(toAdd);
					nextLevel.add(toAdd);
				}
			}
			
			bottomLevel = nextLevel;
			nextLevel = new ArrayList<UIMAAggregateComponent>();
		}
		
		return workflow;
	}
}
