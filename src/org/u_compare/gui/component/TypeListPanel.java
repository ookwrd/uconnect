package org.u_compare.gui.component;
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

import org.u_compare.gui.control.TypeListPanelController;
import org.u_compare.gui.guiElements.ControlList;
import org.u_compare.gui.guiElements.HighlightButton;
import org.u_compare.gui.model.AnnotationTypeOrFeature;
import org.u_compare.gui.model.Component.InputOutputChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;

@SuppressWarnings("serial")
public class TypeListPanel extends JPanel implements LockedStatusChangeListener, InputOutputChangeListener {

	public static enum LIST_TYPES {INPUTS, OUTPUTS};
	
	private org.u_compare.gui.model.Component component;
	private TypeListPanelController controller;
	
	private LIST_TYPES listType;
	
	private ControlList list;
	
	public TypeListPanel(final org.u_compare.gui.model.Component component,
			LIST_TYPES listType, final TypeListPanelController controller){
		
		this.component = component;
		this.listType = listType;
		this.controller = controller;
		
		setOpaque(false);
		
		list = new ControlList(getBackground());
 
		ActionListener addListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addAnnotation();
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
		
		list.registerAddActionListener(addListener);
		list.registerRemoveActionListener(removeListener);
		
		rebuildListContents();
		this.add(list);

		configureLockStatus();

		component.registerLockedStatusChangeListener(this);
		component.registerInputOutputChangeListener(this);
	}
	
	private void configureLockStatus(){
		list.setEnabled(!component.getLockedStatus());
	}

	private void rebuildListContents(){
	
		ArrayList<AnnotationTypeOrFeature> inList;
		switch(listType){
		case INPUTS:
			inList = component.getInputTypes();
			break;
		case OUTPUTS:
			inList = component.getOutputTypes();
			break;
		default:
			throw new Error("TypeListPanel listType not set to a valid value: "
					+ listType);
		}
		
		ArrayList<String> strings = new ArrayList<String>();
		for(AnnotationTypeOrFeature toF : inList){
			strings.add(toF.getTypeName());
		}
		list.rebuildListContents(strings);
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
