package org.u_compare.gui.model;

import java.io.IOException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.metadata.NameValuePair;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.tools.components.InlineXmlCasConsumer;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;

public class PrimitiveUIMAComponent extends AbstractComponent {

	public PrimitiveUIMAComponent(){
		super();
		
		CasConsumerDescription casConsumerDesc = null;
		try {
			
			
			//Analysis engine in
			XMLInputSource xmlIn = new XMLInputSource("test_descriptors/True.xml");
			
			//TODO different constructor based on xml type, 
			AnalysisEngineDescription desc = UIMAFramework.getXMLParser().parseAnalysisEngineDescription(xmlIn);
			//casConsumerDesc = UIMAFramework.getXMLParser().parseCasConsumerDescription(xmlIn);
			//etc
		
			//Analysis engine out
			desc.toXML(System.out);

			
			//Alter Description object
			//not needed if we can decompose and then rebuild objects
			//TODO
			
			//Decompose description object
			//TODO
			ResourceMetaData metaData = desc.getMetaData();
			System.out.println(metaData.getName());
			System.out.println(metaData.getDescription());
			System.out.println(metaData.getVendor());
			System.out.println(metaData.getVersion());
			
			System.out.println(metaData.getCopyright());//wow, didnt know about this
			
			System.out.println("Parameters:\n");
			

			//Why the hell are the settings seperated from the paramaters like this?
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
				System.out.println("Value: " +settings.getParameterValue(param.getName())); //TODO not working
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
			
		} catch (InvalidXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			// get the settings from the metadata
			/*ConfigurationParameterSettings consumerParamSettings =
			    casConsumerDesc.getMetaData().getConfigurationParameterSettings();
			*/
		
			// Set a parameter value
			/*consumerParamSettings.setParameterValue(
			  InlineXmlCasConsumer.PARAM_OUTPUTDIR,
			  outputDir.getAbsolutePath());*/
	}
	
	public static void main(String[] args){
		PrimitiveUIMAComponent comp = new PrimitiveUIMAComponent();
	}
	
}
