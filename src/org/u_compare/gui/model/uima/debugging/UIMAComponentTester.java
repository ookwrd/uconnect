package org.u_compare.gui.model.uima.debugging;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLizable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.uima.CPE;
import org.xml.sax.SAXException;

public class UIMAComponentTester {

	public static boolean[] flags = new boolean[] {false,false};
	
	@Before
	public void setUp(){
		flags = new boolean[] {false,false};
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
	public void inOutTestBasic1() throws IOException, InvalidXMLException{
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAE1.xml");
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
	
	@Test
	public void inOutTestBasicParameterGroupsWithMultipleNames() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSingleValuedParameterGroupsWithMultipleNames.xml");
	}
	
	@Test
	public void inOutTestBasicParameterGroupsWithMultipleNamesAndCommonParams() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSingleValuedParameterGroupsWithMultipleNamesAndCommonParams.xml");
	}
	
	@Test
	public void inOutTestBasicSimpleInputsAndOutputs() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSimpleInputsAndOutputs.xml");
	}
	
	@Test
	public void inOutTestBasicSimpleInputsAndOutputs1() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSimpleInputsAndOutputs1.xml");
	}
	
	@Test
	public void inOutTestBasicSimpleInputsAndOutputsIncludingFeatures() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSimpleInputsAndOutputsIncludingFeatures.xml");
	}
	
	@Test
	public void inOutTestBasicSimpleInputsAndOutputsAndLanguage() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSimpleInputsAndOutputsLanguage.xml");
	}
	
	@Test
	public void inOutTestBasicSimpleInputsAndOutputsSofas() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSimpleInputsAndOutputsSofas.xml");
	}
	
	@Test
	public void inOutTestBasicSimpleInputsAndOutputsCapabilities() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithSimpleInputsAndOutputsMultipleCapabilities.xml");
	}
	
	@Test
	public void inOutTestBasicTypeSystem() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithTypeSystem.xml");
	}
	
	@Test
	public void inOutTestBasicTypeSystemInputsAndOutputs() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEwithTypeSystemInputsAndOutputs.xml");
	}
	
	@Test
	public void inOutTestEverythingMinusAggregation() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/BasicAEEverythingMinusAggregate.xml");
	}
	
	@Test
	public void inOutTestBasicAggregation() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/AggregateAE.xml");
	}
	
	@Test
	public void inOutTestBasicAggregationWithChildren() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/AggregateAEWithChildren.xml");
	}
	
	@Test
	public void inOutTestBasicCasConsumer() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/basicCasConsumerDescriptor.xml");
	}
	
	@Test
	public void inOutTestBasicCollectionReader() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/basicCollectionReaderDescriptor.xml");
	}
	
	@Test
	public void inOutTestBasicCollectionReader1() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/basicCollectionReaderDescriptor1.xml");
	}
	
	@Test
	public void inOutTestBasicCollectionReader2() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/basicCollectionReaderDescriptor2.xml");
	}
	
	@Test
	public void inOutTestlanguages() throws InvalidXMLException, IOException {
		inOutTest("src/org/u_compare/gui/model/uima/debugging/languages.xml");
	}
	
	@Test
	public void inOutTestCPEimport() throws InvalidXMLException, IOException, CpeDescriptorException {
		inOutTestCPE("src/org/u_compare/gui/model/uima/debugging/CPEimport.xml");
	}
	
	@Test
	public void inOutTestCPEdirect() throws InvalidXMLException, IOException, CpeDescriptorException {
		inOutTestCPE("src/org/u_compare/gui/model/uima/debugging/CPEdirect.xml");
	}
	
	/**
	 * Performs a test to ensure the XML produced from loading a XMLDescriptor into the model and then
	 * extracting it again matches the original XML.
	 * 
	 * Currently uses string matching to determine if a correct match is produced. This will sometimes 
	 * produced false negatives. Should be replaced with an XML comparator TODO
	 * 
	 * @param location The location of the XMLDescriptor to test.
	 * @throws IOException
	 * @throws InvalidXMLException
	 */
	private void inOutTest(String location) throws IOException, InvalidXMLException{
		XMLInputSource xmlIn = new XMLInputSource(location);
		ResourceSpecifier desc = (ResourceSpecifier) UIMAFramework.getXMLParser().parse(xmlIn);
		
		Component component = AbstractComponent.constructComponentFromXML(location);

		assertTrue(compare(descriptorToString(desc), descriptorToString(component.getResourceCreationSpecifier())));
	}
	
	private void inOutTestCPE(String location) throws IOException, InvalidXMLException, CpeDescriptorException {
		XMLInputSource xmlIn = new XMLInputSource(location);
		CpeDescription desc = (CpeDescription) UIMAFramework.getXMLParser().parse(xmlIn);
		
		CPE workflow = new CPE(desc);
		
		assertTrue(compare(descriptorToString(desc), descriptorToString(workflow.getWorkflowDescription())));
	}
	
	private static String descriptorToString(XMLizable in){
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
			System.out.print(result.substring(0, i)+ "\nXXXXXXXXXXXXXXXX\n" + result.substring(i,result.length())); 
			System.out.println("\nTarget:");
			System.out.println(target.substring(i - 10 > 0?i -10 : 0, i + 100<target.length()?i+100:target.length()));
			System.out.println("\nProduced:");
			System.out.println(result.substring(i - 10 > 0?i -10 : 0, (i + 100<result.length()?i+100:result.length())));
			
			return false;
		}
	}
}
