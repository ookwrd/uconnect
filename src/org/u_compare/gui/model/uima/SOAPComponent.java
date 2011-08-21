package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.Parameter;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.URISpecifier;
import org.u_compare.gui.model.AbstractComponent;

public class SOAPComponent extends AbstractComponent {

	private Parameter[] params;
	private String protocol;
	private String resourceType;
	private Integer timeoutInteger;
	private String uriString;
	
	public SOAPComponent(URISpecifier spec){
		
		this.params = spec.getParameters();
		this.protocol = spec.getProtocol();
		this.resourceType = spec.getResourceType();
		this.timeoutInteger = spec.getTimeout();
		this.uriString = spec.getUri();
		
	}
	
	@Override
	public MinimizedStatusEnum getMinimizedStatus(){
		return MinimizedStatusEnum.MINIMIZED;
	}
	
	@Override
	public boolean getLockedStatus(){
		return true;
	}
	
	@Override
	public String getName(){
		return uriString;
	}
	
	@Override
	public ResourceSpecifier getResourceCreationSpecifier(){
		URISpecifier retVal = UIMAFramework.getResourceSpecifierFactory().createURISpecifier();
		
		retVal.setParameters(params);
		retVal.setProtocol(protocol);
		retVal.setResourceType(resourceType);
		retVal.setTimeout(timeoutInteger);
		retVal.setUri(uriString);
		
		return retVal;
	}
	
}
