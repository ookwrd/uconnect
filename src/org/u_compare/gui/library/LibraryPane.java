package org.u_compare.gui.library;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

//This class is intended to be an abstract base for the interchangeable library classes
public class LibraryPane extends JScrollPane {

	// Configuration
	private static final int HORIZONTAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private static final int VERTICAL_SCROLLBAR_POLICY =
		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
	private static final String BORDER_TITLE = "Component Library";
	private static final String TOOLTIP_TEXT =
		"Find components for use in the workflow.";	
	
	public LibraryPane(){
		
		this.setHorizontalScrollBarPolicy(
				LibraryPane.HORIZONTAL_SCROLLBAR_POLICY);
		this.setVerticalScrollBarPolicy(
				LibraryPane.VERTICAL_SCROLLBAR_POLICY);
		this.setBorder(new TitledBorder(new EtchedBorder(),
				LibraryPane.BORDER_TITLE));
		this.setToolTipText(LibraryPane.TOOLTIP_TEXT);
		
	}
	
}
