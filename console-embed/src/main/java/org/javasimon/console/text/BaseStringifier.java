package org.javasimon.console.text;

/**
 * Basic value formatter. 
 * Handles null value delegating to a null Stringifier.
 * @param <T> Input type
 * @author gquintana
 */
public class BaseStringifier<T> implements Stringifier<T> {
	/**
	 * Stringifier to handle null values.
	 */
	private final Stringifier nullStringifier;
	/**
	 * Constructor
	 * @param nullStringifier Stringifier to handle null values
	 */
	public BaseStringifier(Stringifier nullStringifier) {
		this.nullStringifier = nullStringifier;
	}
	/**
	 * Check whether input value should be considered as null
	 * @param object Input value
	 * @return Null-style
	 */
	protected boolean isValid(T object) {
		return object!=null;
	}
	@Override
	public final String toString(T object) {
		return isValid(object)?doToString(object):nullToString();
	}
	/**
	 * Converts null input value to string
	 */
	@SuppressWarnings("unchecked")
	protected final String nullToString() {
		return nullStringifier.toString(null);
	}
	/**
	 * Converts not null input value to string
	 */
	protected String doToString(T object) {
		return object.toString();
	}
}
