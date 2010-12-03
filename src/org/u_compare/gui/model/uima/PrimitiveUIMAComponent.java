package org.u_compare.gui.model.uima;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.uima.ResourceSpecifierFactory;
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
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.tools.components.InlineXmlCasConsumer;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.AbstractParameter;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.StringParameter;
import org.xml.sax.SAXException;

import com.sun.source.tree.NewClassTree;

public class PrimitiveUIMAComponent extends AbstractComponent {

	public PrimitiveUIMAComponent(){
		super();
		
		CasConsumerDescription casConsumerDesc = null;
		try {
			
			
			//Analysis engine in
			//XMLInputSource xmlIn = new XMLInputSource("src/org/evolutionarylinguistics/uima/logic/True.xml");
			XMLInputSource xmlIn = new XMLInputSource("src/org/evolutionarylinguistics/uima/logic/SuperSimpleFalse.xml");
			//XMLInputSource xmlIn = new XMLInputSource("src/org/evolutionarylinguistics/uima/testers/TESTERTrueNot.xml");

			
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
				
				
				System.out.println("^^^^^^^^^^^^^^^^^^^");
				
				extractFromProcessingResouceMetaData(metaData);
				
				AnalysisEngineDescription newDescription = UIMAFramework.getResourceSpecifierFactory().createAnalysisEngineDescription();
				
				AnalysisEngineMetaData newMetaData = newDescription.getAnalysisEngineMetaData();
				
				constructProcessingResourceMetaData(newMetaData);
				
				//newDescription.setMetaData(newMetaData);
				
				
				StringWriter writeInput = new StringWriter();
				desc.toXML(writeInput);
				String inputString = writeInput.toString();
				
				StringWriter writeConstructed = new StringWriter();
				newDescription.toXML(writeConstructed);
				String constructedString = writeConstructed.toString();
				
				
				
				//TODO
				
			}
			
		} catch (InvalidXMLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		
		

		
	}
	
	public AnalysisEngineDescription getUIMADescription(){
		
		AnalysisEngineDescription description = UIMAFramework.getResourceSpecifierFactory().createAnalysisEngineDescription();
		
		AnalysisEngineMetaData metadata = description.getAnalysisEngineMetaData();
		
		constructProcessingResourceMetaData(metadata);
		
		return description;
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
			extractFromProcessingResouceMetaData(desc.getAnalysisEngineMetaData());
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	protected void extractFromProcessingResouceMetaData(ProcessingResourceMetaData metaData){
		extractFromResourceMetaData(metaData);
		
		//TODO inputs/outputs
	}
	
	protected void constructProcessingResourceMetaData(ProcessingResourceMetaData metaData) {
		
		constructResourceMetaData(metaData);
		
		//TODO
	}
	
	protected void extractFromResourceMetaData(ResourceMetaData metaData){
		
		//Basic metaData
		setTitle(metaData.getName());
		setDescription(metaData.getDescription());
		setVendor(metaData.getVendor());
		setVersion(metaData.getVersion());
		setCopyright(metaData.getCopyright());
		
		//Parameters
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(); 
		ConfigurationParameterSettings settings = metaData.getConfigurationParameterSettings();
		for(ConfigurationParameter param : metaData.getConfigurationParameterDeclarations().getConfigurationParameters()){

			Parameter newParameter = AbstractParameter.constructParameter(param, settings.getParameterValue(param.getName()));
			parameters.add(newParameter);
			
		}
		//TODO make all the parameter constructors work
		setConfigurationParameters(parameters);
		
	}
	
	protected void constructResourceMetaData(ResourceMetaData metaData){
		
		//Basic MetaData
		metaData.setName(getTitle());
		metaData.setDescription(getDescription());
		metaData.setVendor(getVendor());
		metaData.setVersion(getVersion());
		metaData.setCopyright(getCopyright());
		
		//Parameters
		ConfigurationParameterDeclarations settings = metaData.getConfigurationParameterDeclarations();
		ConfigurationParameterSettings values = metaData.getConfigurationParameterSettings();
		
		for(Parameter param : getConfigurationParameters()){
			ConfigurationParameter newParameter = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
			
			newParameter.setName(param.getName());
			newParameter.setDescription(param.getDescription());
			newParameter.setMandatory(param.isMandatory());
			newParameter.setMultiValued(param.isMultivalued());
			
			Object value = null;
			
			if(param instanceof BooleanParameter){
				newParameter.setType(ConfigurationParameter.TYPE_BOOLEAN);
				value = ((BooleanParameter)param).getParameter();
			} else if (param instanceof StringParameter){
				newParameter.setType(ConfigurationParameter.TYPE_STRING);
				value = ((StringParameter)param).getParameter();
			} else if (param instanceof IntegerParameter){
				newParameter.setType(ConfigurationParameter.TYPE_INTEGER);
				value = ((IntegerParameter)param).getParameter();
			} else if (param instanceof FloatParameter){
				newParameter.setType(ConfigurationParameter.TYPE_FLOAT);
				value = ((FloatParameter)param).getParameter();
			} else {
				assert(false);
			}
			//newParameter.setSourceUrl(arg0) TODO
			
			if(value != null){
				values.setParameterValue(param.getName(), value);
			}
			
			settings.addConfigurationParameter(newParameter);
		}
		
		//TODO
		
	}
	
	public static Component constructUIMAComponent(){
		//TODO need as there are multiple kinds
		return null;
	}
	
	public static void main(String[] args){
		PrimitiveUIMAComponent comp = new PrimitiveUIMAComponent();
	}
	
}
