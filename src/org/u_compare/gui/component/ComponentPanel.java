package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.DynamicCardLayout;
import org.u_compare.gui.guiElements.RoundedBorder;
import org.u_compare.gui.model.AbstractComponent.MinimizedStatusEnum;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.AggregateComponent.SubComponentsChangedListener;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

/**
 * Highest level view component representing an entire UIMA component. 
 * 
 * @author pontus
 * @author olaf
 * @author luke
 * @version 2009-08-26
 */
@SuppressWarnings("serial")
public class ComponentPanel extends JPanel implements
		SubComponentsChangedListener {

	public final static int PREFERRED_WIDTH = 300;
	public static final int BORDER_ROUNDING = 5;
	public static final int BORDER_WIDTH = 2;
	public static final Color BORDER_COLOR = Color.DARK_GRAY;
	public static Color HEADER_COLOR = Color.WHITE;
	public final static Color SELECTED_HEADER_COLOR = new Color(16448250);
	public final Color BODY_COLOR = getBackground();
	public static Color HIGHLIGHT_COLOR = new Color(16748574);
	public static Color HIGHLIGHT_COLOR_2 = new Color(15631900);

	protected ComponentTitleBar topPanel;
	protected JPanel outerPanel; // contains innerPanel et topPanel
	
	protected Component component;
	protected ComponentController controller;
	
	private SubComponentsPanel subComponentsPanel;
	private JPanel subComponentsContainer;
	
	JPanel innerPanel;
	
	protected ComponentPanel(){
	}
	
	public ComponentPanel(Component component,
			ComponentController controller) {
		
		// let the component have focus
		this.setFocusable(true);
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				setBorderColored(true);
			}
			@Override
			public void focusLost(FocusEvent e) {
				setBorderColored(false);
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocusInWindow();
			}
		});
		
		if (component.isAggregate()) {
			((AggregateComponent) component)
					.registerSubComponentsChangedListener(this);
		}
		
		initialConfiguration(component, controller);
		
		outerPanel = new JPanel();
		outerPanel.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR,
				BORDER_ROUNDING, BORDER_WIDTH, false));
		this.add(outerPanel);
		
		outerPanel.setOpaque(false);
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		
		innerPanel = new JPanel();
		innerPanel.setLayout(new DynamicCardLayout());
		innerPanel.setOpaque(false);
		
		outerPanel.add(getTitlePanel(false),BorderLayout.NORTH);
		
		JPanel card0 = setupCardPanel();
		innerPanel.add(card0,MinimizedStatusEnum.MINIMIZED.name());
		JPanel card1 = setupCardPanel();
		innerPanel.add(card1,MinimizedStatusEnum.PARTIAL.name());
		JPanel card2 = setupCardPanel();
		innerPanel.add(card2,MinimizedStatusEnum.MAXIMIZED.name());
		
		card0.setVisible(false);
		card0.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		card1.add(getDescriptionPanel());
		card1.add(getInputOutputPanel());
		
		card2.add(getDescriptionPanel());
		card2.add(getInputOutputPanel());
				
		if(component.isAggregate()){
			JPanel subComponentsBorder = new JPanel();
			subComponentsBorder.setLayout(new GridLayout());
			subComponentsBorder.setOpaque(false);
			subComponentsBorder.setBorder(new TitledBorder(new EtchedBorder(), "Subcomponents:"));
			setupSubComponentsPanel(subComponentsBorder);
			card2.add(subComponentsBorder);
			
		}

		card2.add(getParametersPanel());
		
		outerPanel.add(innerPanel);
		this.add(outerPanel);
		
		setMinimizeStatus(component.getMinimizedStatus());
	}
	
	protected void initialConfiguration(Component component,
			ComponentController controller){
		
		this.controller = controller;
		this.component = component;
		
		//Set display properties
		this.setOpaque(false);
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
		if(!component.isWorkflow()){
			this.setBorderColored(false);
		}
	}

	protected JPanel setupCardPanel(){
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setOpaque(false);
		card.setBorder(new EmptyBorder(BORDER_WIDTH, BORDER_WIDTH,
				BORDER_WIDTH, BORDER_WIDTH));
		return card;
	}
	
	public void setBorderColored(boolean colored) {
		if(colored) {
			this.setBorder(new EtchedBorder(HIGHLIGHT_COLOR, HIGHLIGHT_COLOR_2));
		}
		else {
			this.setBorder(new EmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));
		}				
	}
	
	protected JPanel getTitlePanel(boolean isWorkflow){		
		return new ComponentTitleBar(controller, component, this, isWorkflow);
	}
	
	protected JPanel getDescriptionPanel(){	
		return new ComponentDescriptionPanel(controller, component);
	}

	protected JPanel getInputOutputPanel(){
		return new LanguageInputOutputPanel(component, controller);
	}
	
	protected JPanel getParametersPanel(){
		return new ConfigurationParametersPanel(component, controller);
	}
	
	protected void setupSubComponentsPanel(JPanel target) {
		
		//TODO make sure it is only setup in one location
		
		subComponentsContainer = target;
		
		// set up the aggregate panel if necessary
		if (component.isAggregate()) {

			if(subComponentsPanel != null){
				target.remove(subComponentsPanel);
				target.validate();
			}
			
			subComponentsPanel = new SubComponentsPanel(component, controller);
			target.add(subComponentsPanel);
			
		}
	}
	
	private void resetSubComponents(){
		
		subComponentsContainer.remove(subComponentsPanel);
		setupSubComponentsPanel(subComponentsContainer);
		
	}
	
	//TODO this shouldn't be here. move to SubComponentsPanel
	@Override
	public void subComponentsChanged() {
		controller.resetSubComponents();
		resetSubComponents();
		controller.validateWorkflow();// TODO this needs to validate at a higher
										// level
	}

	public Component getComponent() {
		return this.component;
	}
	
	public ComponentController getComponentController(){
		return this.controller;
	}

	public void setDragOverHighlightingDroppableLight() {
		setBackground(Color.LIGHT_GRAY);

	}

	public void setDragOverHighlightingUndroppable() {
		setBackground(Color.RED);

	}

	public void clearDragOverHighlighting() {
		setBackground(BODY_COLOR);
	}

	/**
	 * Instead of setting the preferred size directly which would require
	 * specifying both horizontal and vertical dimensions, this method allows us
	 * to use the automatically calculated vertical dimension and override just
	 * the horizontal value.
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension ret = super.getPreferredSize();
		ret.width = PREFERRED_WIDTH;
		return ret;
	}

	/**
	 * Dynamically sets the maximum height to the current preferred height of
	 * the component to prevent stretching. //TODO use a layout manager
	 */
	@Override
	public Dimension getMaximumSize() {
		Dimension ret = super.getPreferredSize();
		Dimension max = super.getMaximumSize();
		ret.width = max != null ? max.width : 2000;
		return ret;
	}
	
	public Workflow getWorkflow() {
		if (this.component.isWorkflow() == false) {
			throw new UnsupportedOperationException(
					"Can't get the workflow "
					+ "from a non-workflow ComponentPanel");
		}
		else {
			return (Workflow) this.component;
		}
	}
	
	public void setMinimizeStatus(MinimizedStatusEnum status){
		CardLayout cl = (CardLayout)innerPanel.getLayout();
		cl.show(innerPanel, status.name());
	}
}