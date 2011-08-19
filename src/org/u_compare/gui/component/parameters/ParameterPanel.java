package org.u_compare.gui.component.parameters;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.u_compare.gui.component.TypeListPanel;
import org.u_compare.gui.guiElements.HighlightButton;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;

/**
 * 
 * Abstract base class for common functionality of all ParameterPanel types.
 * 
 * @author Luke McCrohon
 *
 */
public abstract class ParameterPanel implements
		LockedStatusChangeListener {

	private static final int DESCRIPTION_LENGTH = 43;
	
	protected JComponent field;

	protected Parameter param;
	protected Component component;

	public ParameterPanel(Parameter param, Component component){
		this.param = param;
		this.component = component;
		
		component.registerLockedStatusChangeListener(this);
	}
	
	public JLabel getLabel() {
		String description = param.getDescription();
		if(description.length() > DESCRIPTION_LENGTH){
			description = description.substring(0,DESCRIPTION_LENGTH-3) + "...";
		}
		JLabel descriptionLabel = new JLabel(description);
		descriptionLabel.setToolTipText(param.getDescription());//Unabridged description
		descriptionLabel.setHorizontalAlignment(JLabel.TRAILING);
		
		return descriptionLabel;
	}
	
	public JLabel getMandatoryLabel(){
		JLabel mandatory;
		if(param.isMandatory()){
			mandatory = new JLabel("*");
			mandatory.setToolTipText("Mandatory Parameter");
		}else{
			mandatory = new JLabel("");
		}
		return mandatory;
	}
	
	public JComponent getField() {
		return field;
	}
	
	@Override
	public void lockStatusChanged(Component component) {
		updateLockedStatus();
	}
	
	protected void updateLockedStatus(){
		field.setEnabled(!component.getLockedStatus());
	}
	
	protected class MultivaluedParameterPanel extends JPanel{
		
		private JPanel buttons;
		private HighlightButton deleteButton;
		private HighlightButton addButton;
		private JList list;
		
		public MultivaluedParameterPanel(final Component component){

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
		}
		
		
	}
}
