package org.u_compare.gui.guiElements;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ControlList extends JPanel {

	public static final String EMPTY_LIST_MESSAGE = "(Empty. Click to edit)";
	
	private JPanel buttons;
	private HighlightButton deleteButton;
	private HighlightButton addButton;
	private JList list;
	private DefaultListModel listModel;
	
	public ControlList(Color background){
		super();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		
		setOpaque(false);
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		
		list.setBackground(background);
		list.setFixedCellWidth(150);
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));
		

		deleteButton = new HighlightButton("Delete Type");
		addButton = new HighlightButton("Add Type");
		
		FocusListener listFocusListener = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e){
				buttons.setVisible(true);
			}
			@Override
			public void focusLost(FocusEvent e) {
				Object source = e.getOppositeComponent();
				if(source==null
						|| source.equals(this) 
						|| source.equals(addButton) 
						|| source.equals(deleteButton)){
					return;
				}
				list.clearSelection();
				buttons.setVisible(false);
			}
		};
		list.addFocusListener(listFocusListener);
		deleteButton.addFocusListener(listFocusListener);
		addButton.addFocusListener(listFocusListener);
		
		ActionListener reactive = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.requestFocusInWindow(); //To reopen the button panel.
			}
		};
		addButton.addActionListener(reactive);
		//Makes reselection possible in a single click.
		deleteButton.addActionListener(reactive);
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		buttons.setVisible(false);
		
		add(list);
		add(buttons);
	}
	
	public Object[] getSelectedValues(){
		return list.getSelectedValues();
	}
	
	public void registerAddActionListener(ActionListener al){
		addButton.addActionListener(al);
	}
	
	public void registerRemoveActionListener(ActionListener al){
		deleteButton.addActionListener(al);
	}
	
	public void rebuildListContents(ArrayList<String> values){
		assert(list!=null);
		listModel.clear();	
		
		deleteButton.setEnabled(true);
		
		for(String str : values){
			listModel.addElement(str);
		}

		if(listModel.isEmpty()){
			listModel.addElement(EMPTY_LIST_MESSAGE);
			deleteButton.setEnabled(false);
		}
	}
}
