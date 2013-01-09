package org.javasimon.console;

/**
 * Strategies used to to display times
 *
 * @author gquintana
 */
public enum TimeFormatType {
	NANOSECOND(1L),
	MICROSECOND(1000L),
	MILLISECOND(1000000L),
	SECOND(     1000000000L),
	AUTO(       0L);

	private final long longFactor;
	private final double doubleFactor;

	private TimeFormatType(long longFactor) {
		this.longFactor = longFactor;
		this.doubleFactor=(double) longFactor;
	}
	

	public double convert(double value) {
		return value / doubleFactor;
	}

	public long convert(long value) {
		return value / longFactor;
	}
}
