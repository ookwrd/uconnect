package org.u_compare.gui.component.gui_elements;

import javax.swing.text.*;

/**
 * 
 * @author olaf {@link http://www.java2s.com/}
 * 
 */
@SuppressWarnings("serial")
public class JTextFieldLimit extends PlainDocument {
	private int limit;
	// optional uppercase conversion
	private boolean toUppercase = false;

	public JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	public JTextFieldLimit(int limit, boolean upper) {
		super();
		this.limit = limit;
		toUppercase = upper;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit) {
			if (toUppercase)
				str = str.toUpperCase();
			super.insertString(offset, str, attr);
		} else {
			str = str.substring(0, limit - 1);
			if ((getLength() + str.length()) <= limit) {
				if (toUppercase)
					str = str.toUpperCase();
				super.insertString(offset, str, attr);
			}
		}
	}

}