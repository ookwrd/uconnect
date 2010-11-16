package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.u_compare.gui.model.AnnotationType;
import org.u_compare.gui.model.InputOutputChangeListener;
import org.u_compare.gui.model.LockedStatusChangeListener;

@SuppressWarnings("serial")
public class TypeListPanel extends JPanel implements LockedStatusChangeListener, InputOutputChangeListener {

	public static final int INPUTS_LIST = 0;
	public static final int OUTPUTS_LIST = 1;
	
	private JPanel buttons;
	private JButton deleteButton;
	private JButton addButton;
	private JList list;
	private DefaultListModel listModel;
	
	private org.u_compare.gui.model.Component component;
	
	private int listType;
	
	public TypeListPanel(org.u_compare.gui.model.Component component, int listType/*String[] options*/){
		
		this.component = component;
		this.listType = listType;
		
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
				if(!TypeListPanel.this.component.getLockedStatus()){
					buttons.setVisible(true);
				}
			}

			public void focusLost(FocusEvent e) {
				
				Object source = e.getOppositeComponent();
				
				if(source==null
						|| source.equals(list) 
						|| source.equals(addButton) 
						|| source.equals(deleteButton)){
					return;
				}
				
				list.clearSelection();
				buttons.setVisible(false);
			}
		};
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setFixedCellWidth(150);
		list.setBackground(getBackground());
		
		list.addListSelectionListener(listSelectionListener);
		list.addFocusListener(listFocusListener);
	
		rebuildListContents();
		this.add(list);
		
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		deleteButton = new JButton("Delete Type");
		addButton = new JButton("Add Type");
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		
		buttons.setVisible(false);
		
		this.add(buttons);
		
		configureLockStatus();

		component.registerLockedStatusChangeListener(this);
		component.registerInputOutputChangeListener(this);
		
		//TODO deletion
	}
	
	private void configureLockStatus(){
		if(component.getLockedStatus()){
			//Sets the list items unselect-able, without changing their appearance
			list.setEnabled(false);
		    list.setCellRenderer(new DefaultListCellRenderer() {
		        public Component getListCellRendererComponent(
		            JList list,
		            Object value,
		            int index,
		            boolean isSelected,
		            boolean cellHasFocus) {

		            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		            this.setEnabled(true);
		            return this;
		        }
		    });
		}else{
			list.setEnabled(true);
		}
	}

	private void rebuildListContents(){
		assert(list!=null);
		list.removeAll();
		
		switch(listType){
		case INPUTS_LIST:
			for(AnnotationType annotation : component.getInputTypes()){
				
				listModel.addElement(annotation.getTypeName());

			}
			break;
		case OUTPUTS_LIST:
			for(AnnotationType annotation : component.getOutputTypes()){
				
				listModel.addElement(annotation.getTypeName());
				
			}
			break;
		default:
			throw new Error("TypeListPanel listType not set to a valid value: " + listType);
		}
	}
	
	@Override
	public void lockStatusChanged(org.u_compare.gui.model.Component component) {
		configureLockStatus();
	}

	@Override
	public void inputOutputChanged(org.u_compare.gui.model.Component component) {
		rebuildListContents();
	}
	
}
