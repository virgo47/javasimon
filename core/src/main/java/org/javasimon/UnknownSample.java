package org.javasimon;

/**
 * Sample produced by {@link UnknownSimon}s.
 *
 * @author gquintana
 */
public class UnknownSample extends Sample {

	/**
	 * Returns readable representation of the sample object.
	 *
	 * @return string with readable representation of the sample
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("UnknownSample");
		sb.append("{name=").append(getName());
		sb.append(", note=").append(getNote()).append('}');
		return sb.toString();
	}
}
