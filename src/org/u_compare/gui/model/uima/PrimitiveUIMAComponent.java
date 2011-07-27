package org.u_compare.gui.model.uima;

import java.io.IOException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationGroup;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.u_compare.gui.model.AbstractComponent;
import org.xml.sax.SAXException;

public class PrimitiveUIMAComponent extends AbstractComponent {
	
	/**
	 * Constructor just for testing purposes.
	 */
	public PrimitiveUIMAComponent(){
		super();
		
		try {
			
			XMLInputSource xmlIn = new XMLInputSource(
					"src/org/u_compare/gui/model/uima/debugging/"
					//+ "BasicAEwithSingleValuedParametersAndValues.xml");
					//+ "BasicAEwithSingleValuedParameterGroupsAndValues.xml");
					+ "BasicAEwithSimpleInputsAndOutputsIncludingFeatures.xml");
			
					
			/*AnalysisEngineDescription desc = 
			 		UIMAFramework.getXMLParser()
			 		.parseAnalysisEngineDescription(xmlIn);
			casConsumerDesc = UIMAFramework.getXMLParser()
					.parseCasConsumerDescription(xmlIn);
			*/
			//etc
			
			
			/*//Can i use this instead?
			  ResourceSpecifier specifier = 
			    UIMAFramework.getXMLParser().parseResourceSpecifier(xmlIn);
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
						
				//Decompose description object
				//ResourceMetaData metaData = desc.getMetaData();//Doesn't get us all the methods we need.
				AnalysisEngineMetaData metaData = desc.getAnalysisEngineMetaData();//Also exists Processing Resource metaData, maybe the level we want
				
				System.out.println(metaData.getName());
				System.out.println(metaData.getDescription());
				System.out.println(metaData.getVendor());
				System.out.println(metaData.getVersion());
				System.out.println(metaData.getCopyright());
				
				System.out.println("Primitive: " + desc.isPrimitive());
				System.out.println("Implementation Name: "
						+ desc.getImplementationName());
				
				
				System.out.println("Subcomponents:");
				for(String key :
						desc.getDelegateAnalysisEngineSpecifiers().keySet()){
					System.out.println("Key: " + key);
					ResourceSpecifier specifier =
						desc.getDelegateAnalysisEngineSpecifiers().get(key);
					
					if(specifier instanceof AnalysisEngineDescription){
						
						System.out.println("In the child");
						
						AnalysisEngineDescription child =
							(AnalysisEngineDescription)specifier;
						System.out.println("Child Name: "
								+ child.getAnalysisEngineMetaData().getName());
					}
				}
				
				System.out.println();
				System.out.println("Type system");
				
				TypeSystemDescription typeSystemDescription =
					metaData.getTypeSystem();
				
				for(TypeDescription type : typeSystemDescription.getTypes()){
					System.out.println(type.getName());
					System.out.println(type.getDescription());
					System.out.println(type.getSupertypeName());
					
					for(FeatureDescription feature : type.getFeatures()){
						System.out.println(feature.getName());
					}
				}
				
				System.out.println("\nInputs & Outputs:\n");
				 //What exactly are the capability sets for? based on languages?
				for(Capability capability: metaData.getCapabilities() ){
					System.out.println(capability.getDescription());//TODO how the hell do I set a capability in the XML editor description?
					System.out.println("Inputs:");
					
					for(TypeOrFeature torf : capability.getInputs()){
						System.out.println(torf.getName());
						System.out.println(torf.isType());
						System.out.println();
					}
					
					System.out.println("Outputs:");
					for(TypeOrFeature torf : capability.getOutputs()){
						System.out.println(torf.getName());
						System.out.println(torf.isType());
					}
					
					UIMAFramework.getResourceSpecifierFactory().createTypeOrFeature();
					
					//TODO what are the preconditions and how are they set??
					//capability.getPreconditions()
					//http://uima.apache.org/d/uimaj-2.3.1/api/org/apache/uima/resource/metadata/Precondition.html
					//Not fully supported by the framework, so I will ignore.
				}
				
				
				System.out.println("\nParameters:\n");
				ConfigurationParameterSettings settings =
					metaData.getConfigurationParameterSettings();
				
				ConfigurationParameterDeclarations declarations =
					metaData.getConfigurationParameterDeclarations();
				
				System.out.println("Deafult Group name: " + declarations.getDefaultGroupName());
				System.out.println("Search Stratergy: " + declarations.getSearchStrategy());
				ConfigurationGroup[] groups = declarations.getConfigurationGroups();
				if(groups != null && groups.length != 0){
					ConfigurationGroup group = groups[0];
					System.out.println("Group names: " + group.getNames());
					for(ConfigurationParameter parameter : group.getConfigurationParameters()){
						System.out.println("Name: " + parameter.getName());
						System.out.println("Value: " + settings.getParameterValue(group.getNames()[0], parameter.getName()));
					}
				}
				System.out.println();
				
				for(ConfigurationParameter param :
						metaData.getConfigurationParameterDeclarations()
						//metaData.getConfigurationParameterDeclarations().getConfigurationGroups()[0]
						.getConfigurationParameters()){
					System.out.println(param.getName());
					System.out.println(param.getDescription());
					System.out.println(param.getType()); 
					if(param.getType().equals(
							ConfigurationParameter.TYPE_BOOLEAN)){
						System.out.println("Yay its a BOOLEAN!");
					}
					System.out.println("Multivalued: " + param.isMultiValued());
					System.out.println("Mandatory: " + param.isMandatory());
					System.out.println("Value: "
							+ settings.getParameterValue(param.getName()));
					System.out.println();		
				}
				
				//Alternatively
				/*for(org.apache.uima.resource.metadata.NameValuePair pair :
				  		settings.getParameterSettings()){
					System.out.println("Name: " + pair.getName());
					System.out.println("Value: " + pair.getValue());
					System.out.println();
				}*/
				
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
	
	//TODO Need factory here? Yes, how do i handle primitive vs non-primitive
	
	/*public PrimitiveUIMAComponent(String descriptorLocation)
			throws IOException, InvalidXMLException {
		this(new XMLInputSource(descriptorLocation));
	}
	
	public PrimitiveUIMAComponent(XMLInputSource inputSource)
			throws InvalidXMLException{
		try {
		Object resourceSpecifier =
			UIMAFramework.getXMLParser().parse(inputSource);
		
		if(resourceSpecifier instanceof AnalysisEngineDescription){
			
			AnalysisEngineDescription desc =
				(AnalysisEngineDescription)resourceSpecifier;			
			
			extractFromProcessingResouceMetaData(
					desc.getAnalysisEngineMetaData());
			
			setImplementationName(desc.getImplementationName());
			resourceManagerConfiguration =
				desc.getResourceManagerConfiguration();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}*/
	
	public PrimitiveUIMAComponent(AnalysisEngineDescription desc){
		extractFromProcessingResouceMetaData(
				desc.getAnalysisEngineMetaData());
		
		setImplementationName(desc.getImplementationName());
		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}
	
	public static void main(String[] args){
		new PrimitiveUIMAComponent();
	}
	
}
