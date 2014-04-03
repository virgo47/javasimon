package org.javasimon;

/**
 * Interface for getting current time. By default {@link org.javasimon.Clock#SYSTEM} is used to match System timers,
 * but different implementation can be used to provide different time for {@link org.javasimon.Manager}.
 * This also allows easier testing without sleeping.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 * @since 3.5
 */
public interface Clock {

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

	/*
	 * TODO currently org.javasimon.utils.SimonUtils.millisForNano() works based on System timers, it should be moved here
	 *
	 * Converts nano timer value into millis timestamp compatible with {@link #milliTime()} without actually
	 * using the timer. Method does not just divide nanos by one million, but also works with remembered
	 * values for milli- and nano-timers at one particular moment.
	 *
	 * @param nanos nano timer value
	 * @return ms timestamp
	 * @since 3.5 (before in SimonUtils since 3.3, before in SimonManager since 3.1)
	 */
	// long millisForNano(long nanos);

	/** Default implementation using {@link java.lang.System#currentTimeMillis()} and {@link java.lang.System#nanoTime()}. */
	Clock SYSTEM = new Clock() {
		@Override
		public long nanoTime() {
			return System.nanoTime();
		}

		@Override
		public long milliTime() {
			return System.currentTimeMillis();
		}
	};
}