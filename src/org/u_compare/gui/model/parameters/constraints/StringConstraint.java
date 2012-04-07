package org.u_compare.gui.model.parameters.constraints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class implementing common types of constraints on String parameters.
 * 
 * @author Luke McCrohon
 * 
 */
public class StringConstraint extends
		AbstractWhitelistBlacklistConstraint<String> {

	private int minlength = 0;
	private int maxLength = Integer.MAX_VALUE;

	// Null corresponds to no regex checking
	private Pattern regex;
	private Pattern characters;// Null corresponds to no character checking

	public StringConstraint() {
	}

	public StringConstraint(int minLength, int maxLength) {
		this.minlength = minLength;
		this.maxLength = maxLength;
	}

	public void setMaxLength(int max) {
		this.maxLength = max;
	}

	public void setMinLength(int min) {
		this.minlength = min;
	}

	public void setLengthRange(int min, int max) {
		this.minlength = min;
		this.maxLength = max;
	}

	public void setCharacterSet(String characters) {

		String charactersEscaped = "";
		for (int i = 0; i < characters.length(); i++) {
			char c = characters.charAt(i);
			if (c == '\\' || c == '[' || c == ']' || c == '-') {
				charactersEscaped += '\\';
			}
			charactersEscaped += c;
		}
		this.characters = Pattern.compile("[" + charactersEscaped + "]*");
	}

	public void setRegex(String regex) {
		this.regex = Pattern.compile(regex);
	}

	@Override
	public void validate(String value) throws ConstraintFailedException {

		if (value.length() < minlength) {
			throw new ConstraintFailedException("Input is too short. "
					+ "Please input a string of at least " + minlength
					+ " characters.");
		}

		if (value.length() > maxLength) {
			throw new ConstraintFailedException("Input is too long. "
					+ "Please input a string of less than " + maxLength
					+ " characters.");
		}

		super.validateBlacklistWhitelistConstraints(value);

		if (regex != null) {
			Matcher matcher = regex.matcher(value);

			if (!matcher.matches()) {
				throw new ConstraintFailedException(
						"Input string does not match the specified "
								+ "regex constraint: " + regex.pattern());
			}
		}

		if (characters != null) {
			Matcher matcher = characters.matcher(value);

			if (!matcher.matches()) {
				throw new ConstraintFailedException(
						"Input string does not match the specified "
								+ "character set constraint: "
								+ characters.pattern().substring(1,
										characters.pattern().length() - 2));
			}
		}
	}
}
