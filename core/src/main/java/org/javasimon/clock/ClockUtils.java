package org.javasimon.clock;

/**
 * Utils related to timers and Clock class, especially default implementation based on system timers
 * ({@link Clock#SYSTEM}).
 *
 * @since 3.5
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ClockUtils {

	/** Number of milliseconds in one second. */
	public static final long MILLIS_IN_SECOND = 1000;
	/** Number of nanoseconds in one millisecond. */
	public static final long NANOS_IN_MILLIS = 1000000;
	/** Number of nanoseconds in one second. */
	public static final long NANOS_IN_SECOND = NANOS_IN_MILLIS * MILLIS_IN_SECOND;

	/**
	 * Value of {@link System#nanoTime()} at a particular time, when {@link #INIT_MILLIS} is initialized as well.
	 * Used in {@link #millisForNano(long)}.
	 *
	 * @since 3.3
	 */
	public static final long INIT_NANOS = Calibration.initNanos;

	/**
	 * Value of {@link System#currentTimeMillis()} at a particular time, when {@link #INIT_NANOS} is initialized as well.
	 * Used in {@link #millisForNano(long)}.
	 *
	 * @since 3.3
	 */
	public static final long INIT_MILLIS = Calibration.initMillis;

	/**
	 * Measured difference in {@link System#currentTimeMillis()} during calibration.
	 *
	 * @since 3.5
	 */
	public static final long MILLIS_GRANULARITY = Calibration.millisGranularity;

	/**
	 * Average difference in {@link System#nanoTime()} during calibration.
	 *
	 * @since 3.5
	 */
	public static final long NANOS_GRANULARITY = Calibration.nanosGranularity;

	/**
	 * Converts nano timer value into millis timestamp compatible with {@link System#currentTimeMillis()}. Method does not
	 * just divide nanos by one million, but also works with remembered values for milli- and nano-timers at one particular moment.
	 *
	 * @param nanos nano timer value
	 * @return ms timestamp
	 * @since 3.3 (moved from SimonManager where it was since 3.1)
	 */
	public static long millisForNano(long nanos) {
		return INIT_MILLIS + (nanos - INIT_NANOS) / NANOS_IN_MILLIS;
	}

	private static class Calibration {

		private static final int NANO_CHANGES = 100;

		private static long initNanos;
		private static long initMillis;
		private static long nanosGranularity;

		private static long millisGranularity;

		static {
			initMillis = System.currentTimeMillis();
			long oldNanos;
			while (true) {
				oldNanos = System.nanoTime();
				long nextMillis = System.currentTimeMillis();
				if (nextMillis > initMillis) {
					millisGranularity = nextMillis - initMillis;
					initMillis = nextMillis;
					break;
				} else {
					// this ensures that we should get the last possible nano value before initMillis
					initNanos = oldNanos;
				}
			}

			long sumOfNanoDiffs = 0;
			int nanoChanges = 0;
			int nanoMeasurements = 0;
			// we will reuse oldNanos from before
			while (nanoChanges < NANO_CHANGES) {
				long nextNanos = System.nanoTime();
				nanoMeasurements++;
				if (nextNanos > oldNanos) {
					nanoChanges++;
					sumOfNanoDiffs += nextNanos - oldNanos;
					oldNanos = nextNanos;
				}
			}
			nanosGranularity = sumOfNanoDiffs / nanoChanges;
			/*
			Produces funny results when repeated - granularity differences are striking even during a single Maven build:
            nanosGranularity = 460 (based on 100 changes and 327 measurements)
            nanosGranularity = 1198 (based on 100 changes and 324 measurements)
            nanosGranularity = 605 (based on 100 changes and 328 measurements)
			System.out.println("nanosGranularity = " + nanosGranularity + " (based on " + nanoChanges + " changes and " + nanoMeasurements + " measurements)");
			System.out.println("millisGranularity = " + millisGranularity);
			*/
		}
	}
}
