package org.u_compare.gui.model.uima;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataHandler;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.u_compare.gui.model.AbstractAggregateComponent;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;

public class AggregateAnalysisEngine extends AbstractAggregateComponent {

	public AggregateAnalysisEngine(AnalysisEngineDescription desc){
		extractFromProcessingResouceMetaData(
				desc.getAnalysisEngineMetaData());
		
		setImplementationName(desc.getImplementationName());
		flowController = desc.getFlowControllerDeclaration();
		
	    Map<String,MetaDataObject> map = desc.getDelegateAnalysisEngineSpecifiersWithImports();
        for (String key : map.keySet()) {
	      Import imp = (Import) map.get(key);
	      String name = imp.getName();
	      
	      // example: <import name="some.classpath.based.path.without.extension.and.dot.delimted">
	      // <import name> field is without .xml, and separated with dot but not slash
	      String classPathBasedLocation = name.replace('.', '/') + ".xml";

	      // TODO for kano replaced by actual class loader for each workflow
	      ClassLoader classLoader = AggregateAnalysisEngine.class.getClassLoader();
	      URL resource = classLoader.getResource(classPathBasedLocation);
	      try {
            XMLInputSource xmlInputSource = new XMLInputSource(resource);

            Component subComponent = AbstractComponent.constructComponentFromXML(xmlInputSource);
            subComponent.setFlowControllerIdentifier(key);
            addSubComponent(subComponent);
	      } catch (IOException e) {
	        System.err.println("IOException in " + AggregateAnalysisEngine.class.getName() + 
	            " due to reading import name field : " + resource );
            e.printStackTrace();
          }
	    }
		
		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}
	
}
