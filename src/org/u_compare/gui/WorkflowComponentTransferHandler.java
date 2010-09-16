package org.u_compare.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import sun.awt.datatransfer.DataTransferer;

/**
 * 
 * @author olaf
 * @version 2010-05-29
 * @deprecated
 */

public class WorkflowComponentTransferHandler extends TransferHandler {

	private boolean debug = true; 
	
	public WorkflowComponentTransferHandler() {
		super();
		if (debug==true) System.out.println("new th instance");
	}

	public WorkflowComponentTransferHandler(String property) {
		super(property);
	}

	public boolean canImport(TransferHandler.TransferSupport info) {
		return true;
	}
	
	/*
	public boolean importData(TransferHandler.TransferSupport info) {
		System.out.println("importing data");
		if (!info.isDrop()) {
			return false;
		}

		// Check for String flavor
		if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			// displayDropLocation("Component doesn't accept a drop of this
			// type.");
			return false;
		}

		JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
		// DefaultListModel listModel = (DefaultListModel)list.getModel();
		int index = dl.getIndex();
		boolean insert = dl.isInsert();
		// Get the current string under the drop.
		// String value = (String)listModel.getElementAt(index);

		// Get the string that is being dropped.
		Transferable t = info.getTransferable();
		String data;
		try {
			data = (String) t.getTransferData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			return false;
		}

		// Display a dialog with the drop information.
		String dropValue = "\"" + data + "\" dropped ";
		if (dl.isInsert()) {
			if (dl.getIndex() == 0) {
				// displayDropLocation(dropValue + "at beginning of list");
			} // else if (dl.getIndex() >= list.getModel().getSize()) {
			// displayDropLocation(dropValue + "at end of list");
		} else {
			// String value1 =
			// (String)list.getModel().getElementAt(dl.getIndex() - 1);
			// String value2 =
			// (String)list.getModel().getElementAt(dl.getIndex());
			// displayDropLocation(dropValue + "between \"" + value1 + "\"
			// and \"" + value2 + "\"");
		}
		// } else {
		// displayDropLocation(dropValue + "on top of " + "\"" + value +
		// "\"");}
		return false;
	}

	*/
	
	//tricky part
	public boolean importData(TransferSupport info) {
		return false;
	}
	
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}
	
	protected Transferable createTransferable(WorkflowComponent wc) {
		if (debug) System.out.println("create transferable (wc)");
		String transferable = "testable transferable";//(WorkflowComponent) c;
		return new StringSelection(transferable);//c;//(Transferable) transferable; //new StringSelection(transferable);
	}
	
	protected void exportDone(JComponent source, Transferable data, int action) {
		if (debug) System.out.println("Export done.");
		super.exportDone(source, data, action);
	}
}
