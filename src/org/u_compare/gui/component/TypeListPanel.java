package org.u_compare.gui.component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.u_compare.gui.control.TypeListPanelController;
import org.u_compare.gui.guiElements.HighlightButton;
import org.u_compare.gui.model.AnnotationTypeOrFeature;
import org.u_compare.gui.model.Component.InputOutputChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;

@SuppressWarnings("serial")
public class TypeListPanel extends JPanel implements LockedStatusChangeListener, InputOutputChangeListener {

	public static enum LIST_TYPES {INPUTS, OUTPUTS};
	
	private static final String EMPTY_LIST_MESSAGE = "(Empty. Click to edit)";
	
	private JPanel buttons;
	private HighlightButton deleteButton;
	private HighlightButton addButton;
	private JList list;
	private DefaultListModel listModel;
	
	private ActionListener addListener;
	private ActionListener removeListener;
	
	private org.u_compare.gui.model.Component component;
	private TypeListPanelController controller;
	
	private LIST_TYPES listType;
	
	public TypeListPanel(org.u_compare.gui.model.Component component,
			LIST_TYPES listType, TypeListPanelController controller){
		
		this.component = component;
		this.listType = listType;
		this.controller = controller;
		
		setOpaque(false);
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));

		ListSelectionListener listSelectionListener =
			new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (evt.getValueIsAdjusting()){
					return; //TODO why?
				}
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
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));
 
		addListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TypeListPanel.this.controller.addAnnotation();
				list.requestFocusInWindow(); //To reopen the button panel.
			}
		};
		
		removeListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				/*JList and DefaultListModel provide no way for accessing
				 * contents as anything other than an Object[]. Hence having
				 * to go through the array element by element and casting each
				 * selected name individually.
				 */
				for(Object name : list.getSelectedValues()){
					TypeListPanel.this.controller
						.removeAnnotation((String)name);
				}
				
			}
		};
		
		deleteButton = new HighlightButton("Delete Type");
		addButton = new HighlightButton("Add Type");
		
		deleteButton.addActionListener(removeListener);
		addButton.addActionListener(addListener);
		
		deleteButton.addFocusListener(listFocusListener);
		addButton.addFocusListener(listFocusListener);
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		
		buttons.setVisible(false);
		
		rebuildListContents();
		this.add(list);
		
		this.add(buttons);

		configureLockStatus();

		component.registerLockedStatusChangeListener(this);
		component.registerInputOutputChangeListener(this);

	}
	

	
	private void configureLockStatus(){
		
		if(component.getLockedStatus()){
			list.setEnabled(false);
			//Sets the list items unselect-able, without changing appearance
		    /*list.setCellRenderer(new DefaultListCellRenderer() {
		        public Component getListCellRendererComponent(
		            JList list,
		            Object value,
		            int index,
		            boolean isSelected,
		            boolean cellHasFocus) {

		            super.getListCellRendererComponent(list, value, index,
		            		isSelected, cellHasFocus);
		            this.setEnabled(true);
		            return this;
		        }
		    });*/
		}else{
			list.setEnabled(true);
		}
	}

	private void rebuildListContents(){
		assert(list!=null);
		listModel.clear();
		
		//Clear
		deleteButton.setEnabled(true);
		
		switch(listType){
		case INPUTS:
			for(AnnotationTypeOrFeature annotation : component.getInputTypes()){
				
				listModel.addElement(annotation.getTypeName());

			}
			break;
		case OUTPUTS:
			for(AnnotationTypeOrFeature annotation : component.getOutputTypes()){
				
				listModel.addElement(annotation.getTypeName());
				
			}
			break;
		default:
			throw new Error("TypeListPanel listType not set to a valid value: "
					+ listType);
		}
		
		if(listModel.isEmpty()){
			listModel.addElement(EMPTY_LIST_MESSAGE);
			deleteButton.setEnabled(false);
		}
	}
	
	@Override
	public void lockStatusChanged(org.u_compare.gui.model.Component component) {
		configureLockStatus();
	}

	@Override
	public void inputOutputChanged(
			org.u_compare.gui.model.Component component) {
		rebuildListContents();
	}
	
}
