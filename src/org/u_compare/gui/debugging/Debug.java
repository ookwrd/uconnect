package org.u_compare.gui.debugging;

import java.io.PrintStream;

/**
 * 
 * Class aggregating Debugging variables
 * 
 * @author lukemccrohon
 *
 */
public class Debug {

	//Debuglevel values
	//Implemented via int Enum Pattern to allow DEBUGLEVEL > X statement (using an "enum" wouldn't allow this).
	public static final int	NONE = 0;
	public static final int ERROR = 1;
	public static final int WARNING = 2;
	public static final int TRACE = 3;
	
	//Current DebugLevel
	public static final int DEBUGLEVEL = WARNING;
	
	//PrintStream to use for debugging outputs
	public static final PrintStream out = System.out;
	
}
