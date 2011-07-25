package org.u_compare.gui.model.uima.debugging;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.u_compare.gui.model.uima.PrimitiveUIMAComponent;
import org.xml.sax.SAXException;

public class UIMAComponentTester {

	@Before
	public void setUp(){
	}

	@After
	public void tearDown(){
	}
	
	/**
	 * Check parsing/deparsing of basic UIMAComponent description metadata.
	 * 
	 * @throws IOException
	 * @throws InvalidXMLException
	 */
	@Test
	public void inOutTestBasic() throws IOException, InvalidXMLException{
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAE.xml");
	}
	
	@Test
	public void inOutTestBasicSingleValuedParameters() throws InvalidXMLException, IOException{
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSingleValuedParameters.xml");
	}
	
	@Test
	public void inOutTestBasicSingleValuedParametersAndValues() throws InvalidXMLException, IOException{
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSingleValuedParametersAndValues.xml");
	}
	
	@Test
	public void inOutTestBasicMultivaluedParameters() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithMultivaluedParameters.xml");
	}
	
	@Test
	public void inOutTestBasicMultivaluedParametersAndValues() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithMultivaluedParametersAndValues.xml");
	}
	
	@Test
	public void inOutTestBasicParameterGroups() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSingleValuedParameterGroups.xml");
	}
	
	@Test
	public void inOutTestBasicParameterGroupsAndValues() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSingleValuedParameterGroupsAndValues.xml");
	}
	
	/**
	 * Performs a test to ensure the XML produced from loading a XMLDescriptor into the model and then
	 * extracting it again matches the original XML.
	 * 
	 * @param location The location of the XMLDescriptor to test.
	 * @throws IOException
	 * @throws InvalidXMLException
	 */
	private void inOutTest(String location) throws IOException, InvalidXMLException{
		XMLInputSource xmlIn = new XMLInputSource(location);
		AnalysisEngineDescription desc = (AnalysisEngineDescription) UIMAFramework.getXMLParser().parse(xmlIn);
		
		PrimitiveUIMAComponent component = new PrimitiveUIMAComponent(location);
		
		assertTrue(compare(descriptorToString(desc), descriptorToString(component.getUIMADescription())));
	}
	
	private static String descriptorToString(ResourceSpecifier in){
		StringWriter writeInput = new StringWriter();
		try {
			in.toXML(writeInput);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writeInput.toString();
	}
	
	public static boolean compare(String target, String result){
		
		if(target.equals(result)){
			return true;
		}else{
			System.out.println("Input = Output: " + result.equals(target));
			System.out.println();
			
			int i = 0;//TODO find a better way of doing this
			while(i<result.length() && result.regionMatches(0, target, 0, i)){
				i++;
			}
			i--;
			
			System.out.println("Matches the first " + i + " characters (of "+ target.length() + "):");
			System.out.println(result.substring(0, i));
			System.out.println("\nTarget:");
			System.out.println(target.substring(i - 10 > 0?i -10 : 0, i + 100<target.length()?i+100:target.length()));
			System.out.println("\nProduced:");
			System.out.println(result.substring(i - 10 > 0?i -10 : 0, (i + 100<result.length()?i+100:result.length())));
			
			return false;
		}
	}
}
