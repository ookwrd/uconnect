package org.u_compare.gui.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.metadata.NameValuePair;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.tools.components.InlineXmlCasConsumer;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.StringParameter;
import org.xml.sax.SAXException;

public class PrimitiveUIMAComponent extends AbstractComponent {

	public PrimitiveUIMAComponent(){
		super();
		
		CasConsumerDescription casConsumerDesc = null;
		try {
			
			
			//Analysis engine in
			//XMLInputSource xmlIn = new XMLInputSource("test_descriptors/True.xml");
			XMLInputSource xmlIn = new XMLInputSource("src/org/evolutionarylinguistics/uima/testers/TESTERTrueNot.xml");

			
			//AnalysisEngineDescription desc = UIMAFramework.getXMLParser().parseAnalysisEngineDescription(xmlIn);
			//casConsumerDesc = UIMAFramework.getXMLParser().parseCasConsumerDescription(xmlIn);
			//etc
			
			
			/*ResourceSpecifier specifier = 
			    UIMAFramework.getXMLParser().parseResourceSpecifier(xmlIn);//Can i use this instead?
			 */
			
			/** 	this is a nasty nasty way of doing this, but it is what I found done in 
			*	AnnotationViewerMain.viewDocuments() as shipped with UIMA. The problem is
			*	the input xml could represent multiple types, analysisEngine, casConsumer, etc
			*	but we either need to specify a specific parse method on the XMLParser or a specific
			*	method on the UIMAFramework when we construct an actual instance from it. There 
			*	should be a nicer way than this... TODO find it or consult the UIMA mailing list
			*/
			Object resourceSpecifier = UIMAFramework.getXMLParser().parse(xmlIn);
			
			if(resourceSpecifier instanceof AnalysisEngineDescription){
				AnalysisEngineDescription desc = (AnalysisEngineDescription)resourceSpecifier;			
			
				//Analysis engine out
				desc.toXML(System.out);
	
				System.out.println();
				
				//Alter Description object
				//not needed if we can decompose and then rebuild objects
				//TODO
				
				//Decompose description object
				//ResourceMetaData metaData = desc.getMetaData();//Doesn't get us all the methods we need.
				AnalysisEngineMetaData metaData = desc.getAnalysisEngineMetaData();//Also exists Processing Resource metaData, maybe the level we want
				
				System.out.println(metaData.getName());
				System.out.println(metaData.getDescription());
				System.out.println(metaData.getVendor());
				System.out.println(metaData.getVersion());
				System.out.println(metaData.getCopyright());//wow, didnt know about this
				
				System.out.println("Primitive: " + desc.isPrimitive());
				System.out.println("Implementation Name: " + desc.getImplementationName());
				
				
				System.out.println("Subcomponents:");
				for(String key : desc.getDelegateAnalysisEngineSpecifiers().keySet()){
					System.out.println("Key: " + key);
					ResourceSpecifier specifier = desc.getDelegateAnalysisEngineSpecifiers().get(key);
					
					if(specifier instanceof AnalysisEngineDescription){
						
						System.out.println("In the child");
						
						AnalysisEngineDescription child = (AnalysisEngineDescription)specifier;
						System.out.println("Child Name: " + child.getAnalysisEngineMetaData().getName());
					}
				}
				
				//TODO type system
				
				System.out.println("\nInputs & Outputs:\n");
				for(Capability capability: metaData.getCapabilities()){//What exactly are the capability sets for? based on languages?
					System.out.println(capability.getDescription());
					System.out.println("Inputs:");
					for(TypeOrFeature torf : capability.getInputs()){
						System.out.println(torf.getName());
						System.out.println(torf.isType());
					}
					System.out.println("Outputs:");
					for(TypeOrFeature torf : capability.getOutputs()){
						System.out.println(torf.getName());
						System.out.println(torf.isType());
					}
				}
				
				
				System.out.println("\nParameters:\n");
				ConfigurationParameterSettings settings = metaData.getConfigurationParameterSettings();
				for(ConfigurationParameter param : metaData.getConfigurationParameterDeclarations().getConfigurationParameters()){
					System.out.println(param.getName());
					System.out.println(param.getDescription());
					System.out.println(param.getType()); 
					if(param.getType().equals(ConfigurationParameter.TYPE_BOOLEAN)){
						System.out.println("Yay its a BOOLEAN!");
					}
					System.out.println("Multivalued: " + param.isMultiValued());
					System.out.println("Mandatory: " + param.isMandatory());
					System.out.println("Value: " +settings.getParameterValue(param.getName()));
					System.out.println();		
				}
				
				//Alternatively
				/*for(org.apache.uima.resource.metadata.NameValuePair pair : settings.getParameterSettings()){
					System.out.println("Name: " + pair.getName());
					System.out.println("Value: " + pair.getValue());
					System.out.println();
				}*/
				
				//Build description object
				//TODO
				//Metadata fields are all setable...
				
				
				//metaData.buildFrom....
			}
			
		} catch (InvalidXMLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}
	
	//TODO do I need a factory here?
	
	public PrimitiveUIMAComponent(String descriptorLocation) throws IOException, InvalidXMLException{
		this(new XMLInputSource(descriptorLocation));
	}
	
	public PrimitiveUIMAComponent(XMLInputSource inputSource) throws InvalidXMLException{
		try{
		Object resourceSpecifier = UIMAFramework.getXMLParser().parse(inputSource);
		
		if(resourceSpecifier instanceof AnalysisEngineDescription){
			
			AnalysisEngineDescription desc = (AnalysisEngineDescription)resourceSpecifier;			
		
			processProcessingResouceMetaData(desc.getAnalysisEngineMetaData());
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	protected void processProcessingResouceMetaData(ProcessingResourceMetaData metaData){
		processResourceMetaData(metaData);
		
		//TODO inputs/outputs
	}
	
	protected void processResourceMetaData(ResourceMetaData metaData){
		
		setTitle(metaData.getName());
		setDescription(metaData.getDescription());
		//TODO vendor
		//TODO version
		//TODO copyright
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(); 
		
		ConfigurationParameterSettings settings = metaData.getConfigurationParameterSettings();
		for(ConfigurationParameter param : metaData.getConfigurationParameterDeclarations().getConfigurationParameters()){

			Parameter newParameter = constructParameter(param, settings.getParameterValue(param.getName()));
			parameters.add(newParameter);
			
		}
		
		//TODO make the parameter constructors work first
		setConfigurationParameters(parameters);
		
	}
	
	//TODO move to parameter interface as static factory
	protected Parameter constructParameter(ConfigurationParameter param, Object value){
		
		Parameter retVal;
		
		String name = param.getName();
		String description = param.getDescription();
		boolean multivalued = param.isMultiValued();
		boolean mandatory = param.isMandatory();
		
		String type = param.getType();//Why are the types Strings? I cant do a switch statement...
		if(type.equals(ConfigurationParameter.TYPE_BOOLEAN)) {
			if(multivalued){
				retVal = null; //TODO
			}else{
				retVal = new BooleanParameter(name, description, mandatory, value!=null?(Boolean)value:null);
			}
		} else if (type.equals(ConfigurationParameter.TYPE_FLOAT)) {
			if(multivalued){
				retVal = null; //TODO
			}else{
				retVal = null; //TODO
			}
		} else if (type.equals(ConfigurationParameter.TYPE_INTEGER)) {
			if(multivalued){
				retVal = null; //TODO
			}else{
				retVal = new IntegerParameter(name, description, mandatory, value!=null?(Integer)value:null);
			}
		} else if (type.equals(ConfigurationParameter.TYPE_STRING)) {
			if(multivalued){
				retVal = null; //TODO
			}else{
				retVal = new StringParameter(name, description, mandatory, value!=null?(String)value:null);
			}
		} else {
			retVal = null; //TODO throw an error here. 
		}
		
		return retVal;
	}
	
	
	public static Component constructUIMAComponent(){
		//TODO need as there are multiple kinds
		return null;
	}
	
	public static void main(String[] args){
		PrimitiveUIMAComponent comp = new PrimitiveUIMAComponent();
	}
	
}
