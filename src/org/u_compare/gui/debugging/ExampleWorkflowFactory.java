package org.u_compare.gui.debugging;

import java.util.ArrayList;

import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.AnnotationTypeOrFeature;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.StringParameter;
import org.u_compare.gui.model.parameters.constraints.FloatConstraint;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;
import org.u_compare.gui.model.parameters.constraints.StringConstraint;

/**
 * THIS CLASS IS FOR TESTING PURPOSES ONLY
 * 
 * A factory class for constructing different example workflows useful for
 * testing/debugging.
 * 
 * @author Luke McCrohon
 * 
 */
public class ExampleWorkflowFactory {

	// Cannot be constructed
	private ExampleWorkflowFactory() {
	}

	/**
	 * Returns a blank workflow with no components in it.
	 * 
	 * @return a blank workflow
	 */
	public static Workflow blank() {

		Workflow workflow = new Workflow();

		workflow.setName("Blank Workflow");
		workflow.setDescription("As you can see this workflow is blank, "
				+ "but think of all the wonderful possibilities");

		return workflow;

	}

	/**
	 * Returns a workflow comprised of 3 non-aggregate components.
	 * 
	 * @return returns a simple workflow.
	 */
	public static Workflow simple() {

		Workflow workflow = new Workflow();

		workflow.setName("Simple Workflow");
		workflow.setDescription("Some people might say I am simple, "
				+ "but I am happy being who I am. Look at my 3 wonderful "
				+ "(simple) childrem.");

		Component component1 = new MockComponent();
		Component component2 = new MockComponent();
		Component component3 = new MockComponent();

		component1.setName("One");
		component2.setName("Two");
		component3.setName("Three");

		component1.setDescription("Im the first and therefore the best, "
				+ "my siblings don't realise this simple logic.");
		component2.setDescription("Honestly, I don't know who I hate more.");
		component2.setDescription("Lucky number 3! wait what? 7? "
				+ "Thats not even a number!");

		component1.addInputType(new AnnotationTypeOrFeature("input1"));
		component1.addInputType(new AnnotationTypeOrFeature("input2"));
		component1.addInputType(new AnnotationTypeOrFeature("input3"));

		component1.addOutputType(new AnnotationTypeOrFeature("output1"));

		workflow.addSubComponent(component1);
		workflow.addSubComponent(component2);
		workflow.addSubComponent(component3);

		return workflow;
	}

	/**
	 * Returns a workflow comprised of 3 non-aggregate components with
	 * parameters
	 * 
	 * @return returns a simple workflow.
	 */
	public static Workflow simpleWithParameters() {

		Workflow workflow = new Workflow();

		workflow.setName("Simple Workflow");
		workflow.setDescription("Some people might say I am simple, "
				+ "but I am happy being who I am. "
				+ "Look at my 3 wonderful (simple) children.");

		ArrayList<Parameter> c1params = new ArrayList<Parameter>();
		c1params.add(new BooleanParameter("bool1", "A true boolean", true, true));
		c1params.add(new BooleanParameter("bool2", "A false boolean "
				+ "with a really really long description "
				+ "that will get in the way", false, false));

		ArrayList<Parameter> c2params = new ArrayList<Parameter>();
		c2params.add(new StringParameter("String1", "A String to configure",
				false, "default value"));
		c2params.add(new StringParameter("String_file",
				"A String to configure", false, "/Users"));
		c2params.add(new IntegerParameter("Integer1",
				"An Integer to configure", true, 5));

		ArrayList<Parameter> c3params = new ArrayList<Parameter>();
		c3params.add(new BooleanParameter("bool1", "A true boolean", false,
				true));
		c3params.add(new BooleanParameter("bool2", "A false boolean", false,
				false));
		c3params.add(new StringParameter("Strin1", "A String to configure",
				false, "default value"));

		// Add a constrained parameter
		StringParameter constrainedParameter = new StringParameter("parama",
				"Four character string", false, "four");
		StringConstraint cons = new StringConstraint();
		cons.setLengthRange(4, 4);
		constrainedParameter.addConstraint(cons);
		c1params.add(constrainedParameter);

		IntegerParameter constrainedParameter1 = new IntegerParameter("<500",
				"less than 500", false, 500);
		IntegerConstraint cons1 = new IntegerConstraint();
		cons1.setMax(500);
		constrainedParameter1.addConstraint(cons1);
		c1params.add(constrainedParameter1);

		FloatParameter floatParameter = new FloatParameter("Float 0 - 10",
				"Float between 0 and 10", false, (float) 4);
		FloatConstraint consF = new FloatConstraint(0, 10);
		floatParameter.addConstraint(consF);
		c1params.add(floatParameter);
		
		FloatParameter floatParameter1 = new FloatParameter("Float Unconstrained",
				"Float anything", false, (float) 4);
		floatParameter.addConstraint(consF);
		c1params.add(floatParameter1);

		Component component1 = new MockComponent(c1params);
		Component component2 = new MockComponent(c2params);
		Component component3 = new MockComponent(c3params);

		component1.setName("One");
		component2.setName("Two");
		component3.setName("Three");

		component1.setDescription("Im the first and therefore the best, "
				+ "my siblings don't realise this simple logic.");
		component2.setDescription("Honestly, I don't know who I hate more.");
		component2
				.setDescription("Lucky number 3! wait what? 7? Thats not even a number!");

		component1.addInputType(new AnnotationTypeOrFeature("input1"));
		component1.addInputType(new AnnotationTypeOrFeature("input2"));
		component1.addInputType(new AnnotationTypeOrFeature("input3"));

		component1.addOutputType(new AnnotationTypeOrFeature("output1"));

		workflow.addSubComponent(component1);
		workflow.addSubComponent(component2);
		workflow.addSubComponent(component3);

		return workflow;
	}

	/**
	 * Returns a workflow comprised of two toplevel components, the first of
	 * which is an aggregate of two simple components.
	 * 
	 * @return
	 */
	public static Workflow aggregate() {

		Workflow workflow = new Workflow();

		ArrayList<Parameter> workflowParams = new ArrayList<Parameter>();
		workflowParams.add(new BooleanParameter("falseParam", "Report errors",
				false, false));
		workflowParams.add(new StringParameter("Settings File",
				"Error log location", true, "/home/"));

		ArrayList<Parameter> workflowParams1 = new ArrayList<Parameter>();
		workflowParams1.add(new BooleanParameter("trueParam",
				"Generate POS annotations", false, true));
		workflowParams1.add(new IntegerParameter("falseParam", "Timeout (ms)",
				false, 100));

		ArrayList<Parameter> workflowParams2 = new ArrayList<Parameter>();
		workflowParams2.add(new FloatParameter("trueParam",
				"Acceptance threshold", false, new Float(0.4)));
		workflowParams2.add(new StringParameter("falseParam",
				"Description prefix", false, "+"));

		ArrayList<Parameter> workflowParams3 = new ArrayList<Parameter>();
		workflowParams3.add(new BooleanParameter("trueParam", "A true boolean",
				false, true));
		workflowParams3.add(new BooleanParameter("falseParam",
				"A false boolean", false, false));

		Component simplea = new MockComponent(workflowParams1);
		Component simpleb = new MockComponent(workflowParams2);
		Component simplec = new MockComponent(workflowParams3);

		simplea.setName("First Subcomponent");
		simplea.setDescription("This component performs some basic task.");

		simpleb.setName("Second Subcomponent");
		simpleb.setDescription("This component also performs some basic task, and is run on a document once the execution of the first subcomponent is complete.");

		simplec.setName("Simple Component c");
		simplec.setDescription("This is a component. For some reason it is called c");

		AggregateComponent aggregate = new MockAggregateComponent(
				workflowParams);

		aggregate.setName("An Example Aggregate Component");
		aggregate
				.setDescription("This is a serial aggregate component. It has two subcomponents, \"First Subcomponent\" and \"Second Subcomponent\". These are executed in sequence.");

		aggregate.addSubComponent(simplea);
		aggregate.addSubComponent(simpleb);

		workflow.addSubComponent(aggregate);
		workflow.addSubComponent(simplec);

		return workflow;

	}

	public static Workflow deepAggregate(int depth, int branchingfactor) {

		Workflow workflow = new Workflow();
		workflow.setDescription("W");

		ArrayList<AggregateComponent> bottomLevel = new ArrayList<AggregateComponent>();
		bottomLevel.add(workflow);
		ArrayList<AggregateComponent> nextLevel = new ArrayList<AggregateComponent>();

		for (int i = 0; i < depth; i++) {
			for (AggregateComponent current : bottomLevel) {
				String currentDesc = current.getDescription();
				for (int j = 0; j < branchingfactor; j++) {
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

	public static Workflow realComponents() {

		Workflow workflow = new Workflow();

		workflow.setName("Real UIMA Components");
		workflow.setDescription("The components in this workflow "
				+ "are constructed from UIMA decriptor files, "
				+ "the workflow itself however is still assembled manually");
		try {
			Component real1 = AbstractComponent
					.constructComponentFromXML("src/org/u_compare/gui/model/uima/debugging/"
							+ "BasicAEwithSingleValuedParametersAndValues.xml");
			workflow.addSubComponent(real1);
		} catch (Exception e) {
			System.out.println("Error while constructing workflow"
					+ e.getMessage());
			e.printStackTrace();
		}

		return workflow;

	}

	public static Workflow realComponents1() {

		Workflow workflow = new Workflow();

		workflow.setName("Real UIMA Components");
		workflow.setDescription("The components in this workflow "
				+ "are constructed from UIMA decriptor files, "
				+ "the workflow itself however is still assembled manually");
		try {
			Component real1 = AbstractComponent
					.constructComponentFromXML("src/org/u_compare/gui/model/uima/debugging/"
							+ "BasicAEwithSimpleInputsAndOutputsIncludingFeatures.xml");
			workflow.addSubComponent(real1);
		} catch (Exception e) {
			System.out.println("Error while constructing workflow"
					+ e.getMessage());
			e.printStackTrace();
		}

		return workflow;

	}

	public static Workflow realComponents2() {

		Workflow workflow = new Workflow();

		workflow.setName("Real UIMA Aggregate Components");
		workflow.setDescription("The components in this workflow "
				+ "are constructed from UIMA decriptor files, "
				+ "the workflow itself however is still assembled manually");
		try {
			Component real1 = AbstractComponent
					.constructComponentFromXML("src/org/u_compare/gui/model/uima/debugging/"
							+ "AggregateAEWithChildren.xml");
			workflow.addSubComponent(real1);
		} catch (Exception e) {
			System.out.println("Error while constructing workflow"
					+ e.getMessage());
			e.printStackTrace();
		}

		return workflow;

	}

	public static Workflow cpeWorkflow() {
		return Workflow
				.constructWorkflowFromXML("src/org/u_compare/gui/model/uima/debugging/CPEimport.xml");
	}

	public static Workflow cpeWorkflowRecursive() {
		return Workflow
				.constructWorkflowFromXML("src/org/u_compare/gui/model/uima/debugging/CPEimportrecursive.xml");
	}

	public static Workflow cpeWorkflowParams() {
		return Workflow
				.constructWorkflowFromXML("src/org/u_compare/gui/model/uima/debugging/CPEparams.xml");
	}
}
