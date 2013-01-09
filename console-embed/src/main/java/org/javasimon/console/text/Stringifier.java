package org.javasimon.console.text;

/**
 * Value formatter for given type.
 * @param <T> Input type
 * @author gquintana
 */
public interface Stringifier<T> {
	/**
	 * Converts/formats a value into string.
	 * @param value Input value
	 * @return Ouput string
	 */
	String toString(T value);
}
