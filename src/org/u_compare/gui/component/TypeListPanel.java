package org.u_compare.gui.component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.u_compare.gui.control.TypeListPanelController;
import org.u_compare.gui.guiElements.ControlList;
import org.u_compare.gui.model.AnnotationTypeOrFeature;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.InputOutputChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;

/**
 * View element which alternatively displays the Inputs or the Outputs of a component.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class TypeListPanel extends JPanel implements LockedStatusChangeListener, InputOutputChangeListener {

	public static enum LIST_TYPES {INPUTS, OUTPUTS};
	private Component component;
	private LIST_TYPES listType;
	private ControlList list;
	
	public TypeListPanel(final Component component,
			LIST_TYPES listType, final TypeListPanelController controller){
		
		this.component = component;
		this.listType = listType;
		
		setOpaque(false);
		
		list = new ControlList(getBackground());
 
		list.registerAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addAnnotation();
			}
		});		
		
		list.registerDeleteActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*JList and DefaultListModel provide no way for accessing
				 * contents as anything other than an Object[]. Hence having
				 * to go through the array element by element and casting each
				 * selected name individually.
				 */
				for(Object name : list.getSelectedValues()){
					controller
						.removeAnnotation((String)name);
				}	
			}
		});
		
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
			strings.add(toF.getDisplayName());
		}
		list.rebuildListContents(strings);
	}
	
	@Override
	public void lockStatusChanged(Component component) {
		configureLockStatus();
	}

	@Override
	public void inputOutputChanged(
			Component component) {
		rebuildListContents();
	}
	
}
