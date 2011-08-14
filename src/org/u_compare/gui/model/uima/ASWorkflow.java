package org.u_compare.gui.model.uima;

import org.apache.uima.aae.client.UimaASStatusCallbackListener;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.u_compare.gui.model.Workflow;

public class ASWorkflow extends Workflow implements UimaASStatusCallbackListener {



	@Override
	public void collectionProcessComplete(EntityProcessStatus arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializationComplete(EntityProcessStatus arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
	}
}
