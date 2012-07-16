package org.u_compare.gui.debugging;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;

/**
 * Demonstration class to show adding components to a panel in isolation.
 * 
 * @author Luke McCrohon
 *
 */
@SuppressWarnings("serial")
public class ComponentTester extends JFrame {

	public ComponentTester(){
		
		setPreferredSize(new Dimension(600, 400));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Boxlayout for proper vertical layout
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		
		Component component1 = null;
		Component component2 = null;
		try {
			component1 = AbstractComponent
					.constructComponentFromXML("src/org/u_compare/gui/model/uima/debugging/"
							+ "BasicAEwithSimpleInputsAndOutputsIncludingFeatures.xml");
			component2 = AbstractComponent
					.constructComponentFromXML("src/org/u_compare/gui/model/uima/debugging/"
							+ "AggregateAEWithChildren.xml");
			inner.add(new ComponentController(component1, true).getView());
			inner.add(new ComponentController(component2, true).getView());
		} catch (Exception e) {
			System.out.println("Error");
		}
		
		//An extra component for good measure
		inner.add(new JLabel("label"));

		scrollPane.setViewportView(inner);

		scrollPane.getVerticalScrollBar().setUnitIncrement(8);
		scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		
		setContentPane(scrollPane);
		pack();
	}
	
	public static void main(String[] args){
		ComponentTester tester = new ComponentTester();
		tester.setVisible(true);
	}
	
}
