package org.u_compare.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * View for any aggregate component. Unlike a basic components an aggregate
 * component supports other workflow components being added to it.
 * 
 * @author pontus
 * @version 2009-08-26
 */
//XXX: Unused for now, only single class for view right now.
public class AggregateWorkflowComponent extends AbstractWorkflowComponent {
	@Override
	public boolean isAggregateComponent() {
		return true;
	}

	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDataFlavorSupported(DataFlavor arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}