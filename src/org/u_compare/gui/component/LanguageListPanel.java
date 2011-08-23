package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.u_compare.gui.control.LanguageListPanelController;
import org.u_compare.gui.guiElements.ControlList;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LanguageChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;

@SuppressWarnings("serial")
public class LanguageListPanel extends JPanel implements LockedStatusChangeListener, LanguageChangeListener {

	private ControlList list;
	private Component component;
	
	public LanguageListPanel(Component component, final LanguageListPanelController controller){
		
		this.component = component;
		
		setOpaque(false);
		
		list = new ControlList(getBackground());
		list.registerAddActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.addLanguage();
			}
		});
		
		list.registerRemoveActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Object name : list.getSelectedValues()){
					controller
						.removeLanguage((String)name);
				}		
			}
		});
		
		rebuildListContents();
		this.add(list);
		
		configureLockStatus();
		
		component.registerLockedStatusChangeListener(this);
		component.registerLanguagesChangeListener(this);
	}
	
	private void configureLockStatus(){
		list.setEnabled(!component.getLockedStatus());
	}
	
	private void rebuildListContents(){
		list.rebuildListContents(component.getLanguageTypes());
	}

	@Override
	public void languagesChanged(Component component) {
		rebuildListContents();
	}

	@Override
	public void lockStatusChanged(Component component) {
		configureLockStatus();
	}
	
}
