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
	
	private org.u_compare.gui.model.Component component;
	private TypeListPanelController controller;
	
	private LIST_TYPES listType;
	
	public TypeListPanel(final org.u_compare.gui.model.Component component,
			LIST_TYPES listType, final TypeListPanelController controller){
		
		this.component = component;
		this.listType = listType;
		this.controller = controller;
		
		setOpaque(false);
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		
		FocusListener listFocusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(!component.getLockedStatus()){
					buttons.setVisible(true);
				}
			}
			@Override
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
		
		list.addFocusListener(listFocusListener);
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));
 
		ActionListener addListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addAnnotation();
				list.requestFocusInWindow(); //To reopen the button panel.
			}
		};
		
		ActionListener removeListener = new ActionListener() {
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
		list.setEnabled(!component.getLockedStatus());
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
