package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * TestClock allows setting any arbitrary values for millis and nanos.
 */
public class TestClock implements org.javasimon.Clock {

	private long millis;
	private long nanos;

	@Override
	public long nanoTime() {
		return nanos;
	}

	@Override
	public long milliTime() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public void setNanos(long nanos) {
		this.nanos = nanos;
	}

	public void setMillisNanosFollow(long millis) {
		this.millis = millis;
		this.nanos = millis * SimonUtils.NANOS_IN_MILLIS;
	}
}
