package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

public class TypeListPanel extends JPanel {

	private JPanel buttons;
	
	public TypeListPanel(String[] options){
		
		//Options extracted more directly from model
		
		setOpaque(false);
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		
		JList list = new JList(options);
		list.setFixedCellWidth(150);
		list.setBackground(getBackground());
		
		this.add(list);
		
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		JButton deleteButton = new JButton("Delete Type");
		JButton addButton = new JButton("Add Type");
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		
		buttons.setVisible(false);
		
		this.add(buttons);
		
		
		
		//TODO selectable only on editable
		//TODO deselection
		//TODO deletion
	}
	
	
}
