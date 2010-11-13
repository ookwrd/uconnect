package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TypeListPanel extends JPanel {

	private JPanel buttons;
	private JButton deleteButton;
	private JButton addButton;
	private JList list;
	
	public TypeListPanel(String[] options){
		
		//Options extracted more directly from model
		
		setOpaque(false);
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		
		
		

		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (evt.getValueIsAdjusting())
					return;
				System.out.println("Selected from " + evt.getFirstIndex() + " to " + evt.getLastIndex());
			}
		};
		
		FocusListener listFocusListener = new FocusListener() {
			
			public void focusGained(FocusEvent e) {
				buttons.setVisible(true);
			}

			public void focusLost(FocusEvent e) {
				
				Object source = e.getOppositeComponent();
				if(source.equals(list) || source.equals(addButton) || source.equals(deleteButton)){
					return;
				}
				buttons.setVisible(false);
				list.clearSelection();//TODO make it so the buttons dont clear this??
			}
		};
		
		list = new JList(options);
		list.setFixedCellWidth(150);
		list.setBackground(getBackground());
		
		list.addListSelectionListener(listSelectionListener);
		list.addFocusListener(listFocusListener);
		
		this.add(list);
		
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		deleteButton = new JButton("Delete Type");
		addButton = new JButton("Add Type");
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		
		buttons.setVisible(false);
		
		this.add(buttons);
		
		
		
		//TODO selectable only on editable
		//TODO deselection
		//TODO deletion
	}
	
	
}
