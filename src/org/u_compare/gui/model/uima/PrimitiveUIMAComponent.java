package org.u_compare.gui.model.uima;

import java.io.IOException;
import java.util.ArrayList;

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
import org.apache.uima.resource.metadata.FsIndexCollection;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.ResourceManagerConfiguration;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.AbstractParameter;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterGroup;
import org.u_compare.gui.model.parameters.StringParameter;
import org.xml.sax.SAXException;

public class PrimitiveUIMAComponent extends AbstractComponent {

	
	//TODO move these to the model
	private TypeSystemDescription typeSystemDescription;
	private TypePriorities typePriorities;
	private FsIndexCollection fsIndexCollection;
	private Capability[] capabilities;
	private OperationalProperties operationalProperties;
	private ResourceManagerConfiguration resourceManagerConfiguration;
	
	/**
	 * Constructor just for testing purposes.
	 */
	public PrimitiveUIMAComponent(){
		super();
		
		try {
			
			XMLInputSource xmlIn = new XMLInputSource(
					"src/org/u_compare/gui/model/uima/debugging/"
					//+ "BasicAEwithSingleValuedParametersAndValues.xml");
					+ "BasicAEwithSingleValuedParameterGroupsAndValues.xml");
			
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
				
				//TODO type system
				
				System.out.println("\nInputs & Outputs:\n");
				 //What exactly are the capability sets for? based on languages?
				for(Capability capability: metaData.getCapabilities() ){
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
				ConfigurationParameterSettings settings =
					metaData.getConfigurationParameterSettings();
				
				ConfigurationParameterDeclarations declarations =
					metaData.getConfigurationParameterDeclarations();
				
				System.out.println("Deafult Group name: " + declarations.getDefaultGroupName());
				System.out.println("Search Stratergy: " + declarations.getSearchStrategy());
				ConfigurationGroup group = declarations.getConfigurationGroups()[0];
				if(group != null){
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
				
				//Build description object
				//TODO
				//Metadata fields are all setable...
				
				
				//metaData.buildFrom....
				
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
		
		AnalysisEngineDescription description =
			UIMAFramework.getResourceSpecifierFactory()
			.createAnalysisEngineDescription();
		AnalysisEngineMetaData metadata =
			description.getAnalysisEngineMetaData();
		
		setupProcessingResourceMetaData(metadata);
		setupAnalysisEngineDescription(description);
		
		return description;
	}
	
	protected void setupAnalysisEngineDescription(
			AnalysisEngineDescription description) {
		
		description.setImplementationName(getImplementationName());
		description.setResourceManagerConfiguration(
				resourceManagerConfiguration);
		description.setPrimitive(!isAggregate());
		
	}
	
	protected void setupProcessingResourceMetaData(
			ProcessingResourceMetaData metaData) {
		
		setupResourceMetaData(metaData);
		
		//TODO actually save this stuff in the model
		metaData.setTypeSystem(typeSystemDescription);
		metaData.setTypePriorities(typePriorities);
		metaData.setFsIndexCollection(fsIndexCollection);
		metaData.setCapabilities(capabilities);
		metaData.setOperationalProperties(operationalProperties);
		//TODO
		
	}
	
	protected void setupResourceMetaData(ResourceMetaData metaData) {
		
		//Basic MetaData
		metaData.setName(getTitle());
		metaData.setDescription(getDescription());
		metaData.setVendor(getVendor());
		metaData.setVersion(getVersion());
		metaData.setCopyright(getCopyright());
		
		//Parameters
		ConfigurationParameterDeclarations settings = 
			metaData.getConfigurationParameterDeclarations();
		ConfigurationParameterSettings values = 
			metaData.getConfigurationParameterSettings();
		setupConfigurationParameterDeclarations(settings, values);
		
		//TODO
	}
	
	protected void setupConfigurationParameterDeclarations(
			ConfigurationParameterDeclarations declarations,
			ConfigurationParameterSettings settings){
		
		declarations.setSearchStrategy(getParameterSearchStratergy());
		declarations.setDefaultGroupName(getDefaultParameterGroup());
		
		//Ungrouped Parameters
		for(Parameter param : getConfigurationParameters()){
		
			ConfigurationParameter newParam = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
			Object value = constructConfigurationParameter(param, newParam);
			
			if(value != null){
				settings.setParameterValue(param.getName(), value);
			}

			declarations.addConfigurationParameter(newParam);
		}
		
		//Configuration Groups
		ArrayList<ConfigurationGroup> groups = new ArrayList<ConfigurationGroup>();
		for(ParameterGroup group : getParameterGroups()){
			
			ConfigurationGroup configGroup = UIMAFramework.getResourceSpecifierFactory().createConfigurationGroup();
			configGroup.setNames(group.getNames());
			
			ArrayList<ConfigurationParameter> parameters = new ArrayList<ConfigurationParameter>();
			for(Parameter param : group.getConfigurationParameters()){
				
				ConfigurationParameter newParam = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
				Object value = constructConfigurationParameter(param, newParam);
				
				if(value != null){
					//TODO do i need to do this for all group names??
					settings.setParameterValue(group.getNames()[0]/*just the first*/,param.getName(), value);
				}
				
				parameters.add(newParam);
			}
			configGroup.setConfigurationParameters(parameters.toArray(new ConfigurationParameter[parameters.size()]));
			
			groups.add(configGroup);
		}
		declarations.setConfigurationGroups(groups.toArray(new ConfigurationGroup[groups.size()]));
		
		
	}
	
	public Object constructConfigurationParameter(Parameter param,
			ConfigurationParameter newParameter){
		
		Object value = null;	
		
		newParameter.setName(param.getName());
		newParameter.setDescription(param.getDescription());
		newParameter.setMandatory(param.isMandatory());
		newParameter.setMultiValued(param.isMultivalued());
		
		if(!param.isMultivalued()){
			//Single valued parameters
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
		}else {
			//Multi-valued parameters
			if(param instanceof BooleanParameter){
				newParameter.setType(ConfigurationParameter.TYPE_BOOLEAN);
				value = ((BooleanParameter)param).getParameters();
			} else if (param instanceof StringParameter){
				newParameter.setType(ConfigurationParameter.TYPE_STRING);
				value = ((StringParameter)param).getParameters();
			} else if (param instanceof IntegerParameter){
				newParameter.setType(ConfigurationParameter.TYPE_INTEGER);
				value = ((IntegerParameter)param).getParameters();
			} else if (param instanceof FloatParameter){
				newParameter.setType(ConfigurationParameter.TYPE_FLOAT);
				value = ((FloatParameter)param).getParameters();
			} else {
				assert(false);
			}
		}
		
		//TODO Overrides?
		//TODO do we need to set the MetadataObject source?
		
		return value;
	}
	
	//TODO Need factory here? Yes, how do i handle primitive vs non-primitive
	
	public PrimitiveUIMAComponent(String descriptorLocation)
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
			
			//TODO extract to a helper method
			setImplementationName(desc.getImplementationName());
			resourceManagerConfiguration =
				desc.getResourceManagerConfiguration();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	protected void extractFromProcessingResouceMetaData(
			ProcessingResourceMetaData metaData){
		extractFromResourceMetaData(metaData);
		
		//TODO actually construct these
		typeSystemDescription = metaData.getTypeSystem();
		typePriorities = metaData.getTypePriorities();
		fsIndexCollection = metaData.getFsIndexCollection();
		capabilities = metaData.getCapabilities();
		operationalProperties = metaData.getOperationalProperties();
		//TODO inputs/outputs
	}
	
	protected void extractFromResourceMetaData(ResourceMetaData metaData){
		
		//Basic metaData
		setTitle(metaData.getName());
		setDescription(metaData.getDescription());
		setVendor(metaData.getVendor());
		setVersion(metaData.getVersion());
		setCopyright(metaData.getCopyright());
		
		//Parameters TODO extract to method
		ConfigurationParameterSettings settings =
			metaData.getConfigurationParameterSettings();
		ConfigurationParameterDeclarations declarations =
			metaData.getConfigurationParameterDeclarations();
		
		setParameterSearchStratergy(declarations.getSearchStrategy());
		setDefaultParameterGroup(declarations.getDefaultGroupName());
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(); 
		for(ConfigurationParameter param :
			declarations.getConfigurationParameters()){

			Parameter newParameter = AbstractParameter.constructParameter(
					param, settings.getParameterValue(param.getName()));
			parameters.add(newParameter);
		}
		setConfigurationParameters(parameters);
		
		//Parameter Groups
		ArrayList<ParameterGroup> groups = new ArrayList<ParameterGroup>();
		for(ConfigurationGroup group : declarations.getConfigurationGroups()){
			ParameterGroup parameterGroup = new ParameterGroup(this);
			parameterGroup.setNames(group.getNames());
			
			ArrayList<Parameter> groupParameters = new ArrayList<Parameter>();
			for(ConfigurationParameter param : group.getConfigurationParameters()){
				//TODO how do I handle multiple group names??
				Object paramValue = settings.getParameterValue(group.getNames()[0], param.getName());
				Parameter newParameter = AbstractParameter.constructParameter(param, paramValue);
				groupParameters.add(newParameter);
			}
			parameterGroup.setConfigurationParameters(groupParameters);
			
			groups.add(parameterGroup);
		}
		setParameterGroups(groups);
		
	}
	
	
	
	public static Component constructUIMAComponent(){
		//TODO needed as there are multiple kinds
		return null;
	}
	
	public static void main(String[] args){
		new PrimitiveUIMAComponent();
	}
	
}
