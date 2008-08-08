package org.javasimon;

/**
 * SimonUtils.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class SimonUtils {
	private static final int UNIT_PREFIX_FACTOR = 1000;
	private static final int ROUNDING_OFFSET = 500;
	private static final int CONVERSION_TRESHOLD = 10000;

	private SimonUtils() {
	}

	public static String presentTime(long time) {
		if (time == Long.MAX_VALUE) {
			return "undef";
		}
		if (time < CONVERSION_TRESHOLD) {
			return time + " ns";
		}
		time += ROUNDING_OFFSET;
		time /= UNIT_PREFIX_FACTOR;

		if (time < CONVERSION_TRESHOLD) {
			return time + " us";
		}
		time += ROUNDING_OFFSET;
		time /= UNIT_PREFIX_FACTOR;

		if (time < CONVERSION_TRESHOLD) {
			return time + " ms";
		}
		time += ROUNDING_OFFSET;
		time /= UNIT_PREFIX_FACTOR;

		return time + " s";
	}
}
