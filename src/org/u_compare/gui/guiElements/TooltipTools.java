package org.u_compare.gui.guiElements;

public final class TooltipTools {

	private TooltipTools(){}
	
	public static int LINES = 4;
	public static int LENGTH = 40;
	
	public static String formatTooltip(String in){
		// Need <html> to allow multiline tooltip
		return "<html>" + wrap(in, LENGTH, LINES);
	}
	
	/**
	 * Word wrap algorithm to insert line returns at word breaks. Used to create
	 * multiline tooltips.
	 * 
	 * Based on http://ramblingsrobert.wordpress.com/2011/04/13/java-word-wrap-
	 * algorithm/
	 */
	private static String wrap(String in, int len, int lines) {
		in = in.trim();

		if (in.length() < len) {
			return in;
		} else if (lines <= 1) {
			return in.substring(0, len - 3) + "...";
		}

		if (in.substring(0, len).contains("\n")) {
			return in.substring(0, in.indexOf("\n")).trim() + "<br>"
					+ wrap(in.substring(in.indexOf("\n") + 1), len, lines - 1);
		}

		int place = Math.max(
				Math.max(in.lastIndexOf(" ", len), in.lastIndexOf("\t", len)),
				in.lastIndexOf("-", len));

		return in.substring(0, place).trim() + "<br>"
				+ wrap(in.substring(place), len, lines - 1);
	}
	
}
