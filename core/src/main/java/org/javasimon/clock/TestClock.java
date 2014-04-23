package org.javasimon.clock;

/**
 * TestClock allows setting any arbitrary values for millis and nanos.
 * Available for non-test code too, if for nothing else then it's easier to use in tests in other modules too.
 *
 * @since 3.5
 */
public final class TestClock implements Clock {

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

	/** Here millis are simply nanos divided by {@link ClockUtils#NANOS_IN_MILLIS}. */
	@Override
	public long millisForNano(long nanos) {
		return nanos / ClockUtils.NANOS_IN_MILLIS;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public void setNanos(long nanos) {
		this.nanos = nanos;
	}

	public void setMillisNanosFollow(long millis) {
		this.millis = millis;
		this.nanos = millis * ClockUtils.NANOS_IN_MILLIS;
	}
}
