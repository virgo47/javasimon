package org.javasimon;

/**
 * Sample produced by {@link UnknownSimon}s.
 *
 * @author gquintana
 * @since 3.2
 */
public class UnknownSample extends Sample {

	/**
	 * Returns readable representation of the sample object.
	 *
	 * @return string with readable representation of the sample
	 */
	@Override
	public String toString() {
		return "UnknownSample" + "{name=" + getName() + ", note=" + getNote() + '}';
	}

	@Override
	public String simonToString() {
		return null;
	}
}
