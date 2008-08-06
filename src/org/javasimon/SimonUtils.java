package org.javasimon;

/**
 * SimonUtils.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class SimonUtils {
	public static String presentTime(long time) {
		if (time < 10000) {
			return time + " ns";
		}
		time += 500;
		time /= 1000;

		if (time < 10000) {
			return time + " us";
		}
		time += 500;
		time /= 1000;

		if (time < 10000) {
			return time + " ms";
		}
		time += 500;
		time /= 1000;

		return time + " s";
	}
}
