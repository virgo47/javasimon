package org.javasimon.clock;

/**
 * Interface for getting current time. By default {@link SimonClock#SYSTEM} is used to match System timers,
 * but different implementation can be used to provide different time for {@link org.javasimon.Manager}.
 * This also allows easier testing without sleeping.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 * @since 3.5
 * @noinspection UnusedDeclaration
 */
public interface SimonClock {

	/** Number of milliseconds in one second. */
	long MILLIS_IN_SECOND = 1000;

	/** Number of nanoseconds in one millisecond. */
	long NANOS_IN_MILLIS = SimonUnit.MILLISECOND.getDivisor();

	/** Number of nanoseconds in one second. */
	long NANOS_IN_SECOND = SimonUnit.SECOND.getDivisor();

	/**
	 * Gets current time in nanoseconds.
	 *
	 * @return current time in nanoseconds
	 */
	long nanoTime();

	/**
	 * Gets current time in milliseconds.
	 *
	 * @return current time in milliseconds
	 */
	long milliTime();

	/**
	 * Converts nano timer value into millis timestamp compatible with {@link #milliTime()} without actually
	 * using the timer. Method does not just divide nanos by one million, but also works with remembered
	 * values for milli- and nano-timers at one particular moment.
	 *
	 * @param nanos nano timer value
	 * @return ms timestamp
	 * @since 3.5 (before in SimonUtils since 3.3, before in SimonManager since 3.1)
	 */
	long millisForNano(long nanos);

	/** Default implementation using {@link java.lang.System#currentTimeMillis()} and {@link java.lang.System#nanoTime()}. */
	SimonClock SYSTEM = new SimonClock() {
		@Override
		public long nanoTime() {
			return System.nanoTime();
		}

		@Override
		public long milliTime() {
			return System.currentTimeMillis();
		}

		@Override
		public long millisForNano(long nanos) {
			return SimonClockUtils.millisForNano(nanos);
		}
	};

	/** Clock implementation measuring CPU time. */
	SimonClock CPU = new CpuClock();
}