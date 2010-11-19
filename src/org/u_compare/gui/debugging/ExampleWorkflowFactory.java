package org.u_compare.gui.debugging;

import java.util.ArrayList;

import org.u_compare.gui.model.AnnotationType;
import org.u_compare.gui.model.MockAggregateComponent;
import org.u_compare.gui.model.MockComponent;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.StringParameter;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;
import org.u_compare.gui.model.parameters.constraints.StringConstraint;

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
	public static Workflow blank(){
		
		Workflow workflow = new Workflow();
		
		workflow.setName("Blank Workflow");
		workflow.setDescription("As you can see this workflow is blank, but think of all the wonderful possibilities");
		
		return workflow;
		
	}
	
	/**
	 * Returns a workflow comprised of 3 non-aggregate components.
	 * 
	 * @return returns a simple workflow.
	 */
	public static Workflow simple(){
		
		Workflow workflow = new Workflow();
		
		workflow.setName("Simple Workflow");
		workflow.setDescription("Some people might say I am simple, but I am happy being who I am. Look at my 3 wonderful (simple) childrem.");
		
		Component component1 = new MockComponent();
		Component component2 = new MockComponent();
		Component component3 = new MockComponent();
		
		component1.setName("One");
		component2.setName("Two");
		component3.setName("Three");
		
		component1.setDescription("Im the first and therefore the best, my siblings don't realise this simple logic.");
		component2.setDescription("Honestly, I don't know who I hate more.");
		component2.setDescription("Lucky number 3! wait what? 7? Thats not even a number!");
		
		component1.addInputType(new AnnotationType("input1"));
		component1.addInputType(new AnnotationType("input2"));
		component1.addInputType(new AnnotationType("input3"));
		
		component1.addOutputType(new AnnotationType("output1"));
		
		
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
	public static Workflow simpleWithParameters(){
		
		Workflow workflow = new Workflow();
		
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
		
		//Add a constrained parameter
		StringParameter constrainedParameter = new StringParameter("Four character string", "four");
		StringConstraint cons = new StringConstraint();
		cons.setLengthRange(4, 4);
		constrainedParameter.addConstraint(cons);
		c1params.add(constrainedParameter);
		
		IntegerParameter constrainedParameter1 = new IntegerParameter("less than 500", 500);
		IntegerConstraint cons1 = new IntegerConstraint();
		cons1.setMax(500);
		constrainedParameter1.addConstraint(cons1);
		c1params.add(constrainedParameter1);
		
		Component component1 = new MockComponent(c1params);
		Component component2 = new MockComponent(c2params);
		Component component3 = new MockComponent(c3params);
		
		component1.setName("One");
		component2.setName("Two");
		component3.setName("Three");
		
		component1.setDescription("Im the first and therefore the best, my siblings don't realise this simple logic.");
		component2.setDescription("Honestly, I don't know who I hate more.");
		component2.setDescription("Lucky number 3! wait what? 7? Thats not even a number!");
		
		component1.addInputType(new AnnotationType("input1"));
		component1.addInputType(new AnnotationType("input2"));
		component1.addInputType(new AnnotationType("input3"));
		
		component1.addOutputType(new AnnotationType("output1"));
		
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
	public static Workflow aggregate(){
		
		Workflow workflow = new Workflow();
		
		ArrayList<Parameter> workflowParams = new ArrayList<Parameter>();
		workflowParams.add(new BooleanParameter("A true boolean",true));
		workflowParams.add(new BooleanParameter("A false boolean",false));
		
		Component simplea = new MockComponent(workflowParams);
		Component simpleb = new MockComponent(workflowParams);
		Component simplec = new MockComponent(workflowParams);
		
		simplea.setName("Simple Component a");
		simplea.setDescription("This is a component. For some reason it is called a");
		
		simpleb.setName("Simple Component b");
		simpleb.setDescription("This is a component. For some reason it is called b");

		simplec.setName("Simple Component c");
		simplec.setDescription("This is a component. For some reason it is called c");
		
		AggregateComponent aggregate = new MockAggregateComponent(workflowParams);
		
		aggregate.setName("The nasty nasty Aggregate Component");
		aggregate.setDescription("Not afraid of aggregate components? Well what a silly little boy you are then.");
		
		aggregate.addSubComponent(simplea);
		aggregate.addSubComponent(simpleb);
		
		workflow.addSubComponent(aggregate);
		workflow.addSubComponent(simplec);
		
		return workflow;
		
	}
	
	public static Workflow deepAggregate(int depth, int branchingfactor){

		Workflow workflow = new Workflow();
		workflow.setDescription("W");
		
		ArrayList<AggregateComponent> bottomLevel = new ArrayList<AggregateComponent>();
		bottomLevel.add(workflow);
		ArrayList<AggregateComponent> nextLevel = new ArrayList<AggregateComponent>();
		
		for(int i = 0; i < depth; i++){
			for(AggregateComponent current : bottomLevel){
				String currentDesc = current.getDescription();
				for(int j = 0; j < branchingfactor; j++){
					AggregateComponent toAdd = new MockAggregateComponent();
					toAdd.setDescription(currentDesc + j);
					current.addSubComponent(toAdd);
					nextLevel.add(toAdd);
				}
			}
			
			bottomLevel = nextLevel;
			nextLevel = new ArrayList<AggregateComponent>();
		}
		
		return workflow;
	}
}
