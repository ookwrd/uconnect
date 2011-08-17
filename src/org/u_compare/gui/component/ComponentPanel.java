package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.debugging.GUITestingHarness;
import org.u_compare.gui.guiElements.LukesDragAndDropImplementation;
import org.u_compare.gui.guiElements.RoundedBorder;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.AggregateComponent.SubComponentsChangedListener;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

/**
 * @author pontus
 * @author olaf
 * @author luke
 * @version 2009-08-26
 */
@SuppressWarnings("serial")
public class ComponentPanel extends JPanel implements
		SubComponentsChangedListener, FocusListener, MouseListener {

	public final static int PREFERRED_WIDTH = 300;
	public static final int BORDER_ROUNDING = 5;
	public static final int BORDER_WIDTH = 2;
	public static final Color BORDER_COLOR = Color.DARK_GRAY;
	public static Color HEADER_COLOR = Color.WHITE;
	public final static Color SELECTED_HEADER_COLOR = new Color(16448250);
	public final Color BODY_COLOR = getBackground();
	public static Color HIGHLIGHT_COLOR = new Color(16748574);
	public static Color HIGHLIGHT_COLOR_2 = new Color(15631900);

	protected JPanel innerPanel;
	protected ComponentTitleBar topPanel;
	protected JPanel outerPanel; // contains innerPanel et topPanel
	
	protected Component component;
	protected ComponentController controller;
	
	private ComponentDescriptionPanel descriptionPanel;
	private InputOutputPanel inputOutputPanel;
	private ConfigurationParametersPanel parametersPanel;
	private SubComponentsPanel subComponentsPanel;
	private JPanel subComponentsContainer;
	
	protected ComponentPanel(){
	}
	
	public ComponentPanel(Component component,
			ComponentController controller) {
		
		initialConfiguration(component, controller);
		
		// let the component have focus
		this.setFocusable(true);
		addFocusListener(this);
		addMouseListener(this);
		
		if (component.isAggregate()) {
			((AggregateComponent) component)
					.registerSubComponentsChangedListener(this);
		}
		
		//this.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR, BORDER_ROUNDING, BORDER_WIDTH, false));
		this.setBorder(new EmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));

		
		innerPanel.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR,
				BORDER_ROUNDING, BORDER_WIDTH, false));
		
		// TODO the outerPanel was supposed to implement the colored borders, but it turns out to ruin the layout 
		outerPanel = new JPanel();
		outerPanel.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR,
				BORDER_ROUNDING, BORDER_WIDTH, false));
		this.add(outerPanel);
		outerPanel.setOpaque(false);
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		
		
		setupTopPanel(outerPanel, false);
		setupInnerPanel();
		
		setupDescriptionPanel(innerPanel);
		setupInputOutputPanel(innerPanel);
		
		if(component.isAggregate()){
			
			JPanel subComponentsBorder = new JPanel();
			subComponentsBorder.setLayout(new GridLayout());
			subComponentsBorder.setOpaque(false);
			subComponentsBorder.setBorder(new TitledBorder("Subcomponents:"));
			setupSubComponentsPanel(subComponentsBorder);
			innerPanel.add(subComponentsBorder);
			
		}

		setupParametersPanel(innerPanel);
		
		outerPanel.add(innerPanel);
		this.add(outerPanel);
	}
	
	protected void initialConfiguration(Component component,
			ComponentController controller){
		
		this.controller = controller;
		this.component = component;
		
		//Set display properties
		this.setOpaque(false);
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
		innerPanel = new JPanel();
	}

	//TODO can this be integrated into the initialConfiguration Method?
	protected void setupInnerPanel(){
		// set up an inner panel and its layout
		BoxLayout innerLayout = new BoxLayout(innerPanel, BoxLayout.Y_AXIS);
		innerPanel.setLayout(innerLayout);
		innerPanel.setOpaque(false);
		innerPanel.setBorder(new EmptyBorder(BORDER_WIDTH, BORDER_WIDTH,
				BORDER_WIDTH, BORDER_WIDTH));
	}
	
	public void setBorderColored(boolean colored) {
		if(colored) {
			this.setBorder(new EtchedBorder(HIGHLIGHT_COLOR, HIGHLIGHT_COLOR_2));
			//this.setBorder(new RoundedBorder(null, Color.BLUE, BODY_COLOR, BORDER_ROUNDING, BORDER_WIDTH, false));
			//topPanel.setBorderColored(true);
		}
		else {
			this.setBorder(new EmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));
			//this.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR, BORDER_ROUNDING, BORDER_WIDTH, false));
			//topPanel.setBorderColored(false);
		}
					
	}
	
	protected void setupTopPanel(JPanel target, boolean isWorkflow){
		
		topPanel = new ComponentTitleBar(controller, component, innerPanel, isWorkflow);
		target.add(topPanel, BorderLayout.NORTH);
		
	}
	
	protected void setupDescriptionPanel(JPanel target){
		
		descriptionPanel = new ComponentDescriptionPanel(controller, component);
		target.add(descriptionPanel);
		
		
	}

	protected void setupInputOutputPanel(JPanel target){
		
		inputOutputPanel = new InputOutputPanel(component, controller);
		target.add(inputOutputPanel);

	}
	
	protected void setupParametersPanel(JPanel target){
		
		parametersPanel = new ConfigurationParametersPanel(component, controller);
		target.add(parametersPanel);
	
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
	 * the component to prevent stretching.
	 */
	@Override
	public Dimension getMaximumSize() {
		Dimension ret = super.getPreferredSize();
		Dimension max = super.getMaximumSize();
		ret.width = max != null ? max.width : 2000;
		return ret;
	}
	
	public static void main(String[] argv) {
		GUITestingHarness.main(argv);
	}
	
	public Workflow getWorkflow() {
		if (this.component.isWorkflow() == false) {
			//TODO: The proper exception here
			throw new IllegalArgumentException(
					"Can't get the workflow "
					+ "from a non-workflow ComponentPanel");
		}
		else {
			return (Workflow) this.component;
		}
	}

	@Override//TODO move all of this to an inner class extending mouse adaptor... olaf you really know how to make a mess.
	public void focusGained(FocusEvent e) {
		setBorderColored(true);
	}

	@Override
	public void focusLost(FocusEvent e) {
		setBorderColored(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//setVisible(false);
		//setVisible(true);
		requestFocusInWindow();
		System.out.println("focus detected on the component");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}