package org.u_compare.gui.component;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconFactory {

	//WorkflowViewer Icons
	public static final String STOPPED_ICON = 
		"../gfx/workflow_stopped.png";
	public static final String RUNNING_ICON =
		"../gfx/workflow_running.gif";
	public static final String FINISHED_ICON =
		"../gfx/workflow_finished.png";
	public static final String PAUSED_ICON =
		"../gfx/workflow_paused.png";
	public static final String ERROR_ICON =
		"../gfx/workflow_error.png";
	
	private static final String STOPPED_DESCRIPTION =
		"The workflow is currently stopped";
	private static final String RUNNING_DESCRIPTION =
		"The workflow is currently running";
	private static final String FINISHED_DESCRIPTION =
		"The workflow has finished executioning";
	private static final String PAUSED_DESCRIPTION =
		"The workflow is currently paused";
	private static final String ERROR_DESCRIPTION =
		"An error has occured with this workflow";
	
	//WorkflowControlPanel Icons
	public static final String RUN_ICON = 
		"../gfx/icon_start.png";
	public static final String STOP_ICON = 
		"../gfx/icon_stop.png";
	public static final String PAUSE_ICON = 
		"../gfx/icon_pause.png";
	
	private static final String RUN_DESCRIPTION = 
		"Run the workflow";
	private static final String STOP_DESCRIPTION =
		"Stop the workflow";
	private static final String PAUSE_DESCRIPTION =
		"Pause the workflow";
	
	//TitleButtonPanel Icons
	public final static String CLOSE_ICON = 
		"../gfx/close_icon_big.png";
	public final static String MAX_ICON = 
		"../gfx/icon_plus.gif";
	public final static String EXP_ICON = 
		"../gfx/icon_maximize1.png";
	public final static String MIN_ICON = 
		"../gfx/icon_minimize1.png";
	public final static String LOCKED_ICON = 
		"../gfx/icon_locked.png";
	public final static String UNLOCKED_ICON = 
		"../gfx/icon_unlocked.png";
	
	private static final String CLOSE_DESCRIPTION =
		"Remove";
	private static final String MAX_DESCRIPTION =
		"Maximize";
	private static final String EXP_DESCRIPTION =
		"Expand";
	private static final String MIN_DESCRIPTION =
		"Minimize";
	private static final String LOCK_DESCRIPTION =
		"Lock Component";
	private static final String UNLOCK_DESCRIPTION =
		"Unlock Component";
	
	public static boolean iconsLoaded = false;
	private static HashMap<String, ImageIcon> iconsHashMap = new HashMap<String, ImageIcon>();
	
	public static synchronized void loadIcons() {
		if (iconsLoaded == true) {
			return;
		}
		loadIcon(STOPPED_ICON, STOPPED_DESCRIPTION);
		loadIcon(RUNNING_ICON, RUNNING_DESCRIPTION);
		loadIcon(FINISHED_ICON, FINISHED_DESCRIPTION);
		loadIcon(PAUSED_ICON, PAUSED_DESCRIPTION);
		loadIcon(ERROR_ICON, ERROR_DESCRIPTION);
		
		loadIcon(RUN_ICON, RUN_DESCRIPTION);
		loadIcon(STOP_ICON, STOP_DESCRIPTION);
		loadIcon(PAUSE_ICON, PAUSE_DESCRIPTION);
		
		loadIcon(MIN_ICON, MIN_DESCRIPTION);
		loadIcon(MAX_ICON, MAX_DESCRIPTION);
		loadIcon(EXP_ICON, EXP_DESCRIPTION);
		loadIcon(LOCKED_ICON, LOCK_DESCRIPTION);
		loadIcon(UNLOCKED_ICON, UNLOCK_DESCRIPTION);
		loadIcon(CLOSE_ICON, CLOSE_DESCRIPTION);

		iconsLoaded = true;
	}
	
	private static void loadIcon(String path, String description){
		URL image_url = IconFactory.class.getResource(
				path);
		assert image_url != null;
		ImageIcon icon = new ImageIcon(image_url, description);
		iconsHashMap.put(path, icon);
	}
	
	public static ImageIcon getIcon(String key){
		assert(iconsLoaded);
		return iconsHashMap.get(key);
	}
}
