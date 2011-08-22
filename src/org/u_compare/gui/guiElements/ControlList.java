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
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ControlList extends JPanel {

	private static final String EMPTY_LIST_MESSAGE = "(Empty. Click to edit)";
	private static final String DELETE_MSG = "Delete";
	private static final String ADD_MSG = "Add";
	
	private JPanel buttons;
	private HighlightButton deleteButton;
	private HighlightButton addButton;
	private JList list;
	private DefaultListModel listModel;
	
	public ControlList(Color background){
		super();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		
		list.setBackground(background);
		list.setFixedCellWidth(140);//TODO make this expand and contract
		
		buttons = new JPanel();
		buttons.setOpaque(false);
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		

		deleteButton = new HighlightButton(DELETE_MSG);
		addButton = new HighlightButton(ADD_MSG);
		
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
		
		rebuildListContents(new ArrayList<String>());
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
	
	@Override
	public void setBorder(Border bord){
		if(list != null){
			list.setBorder(bord);
		}
	} 
	
	@Override
	public void setEnabled(boolean enabled){
		list.setEnabled(enabled);
		//TODO Make sure the buttons are removed
		//TODO Change the empty message to remove "click to edit"
	}
}
