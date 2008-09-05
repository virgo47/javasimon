package org.javasimon.utils;

import org.javasimon.Simon;
import org.javasimon.SimonFactory;

/**
 * SimonUtils class holds static utility methods.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class SimonUtils {
	private static final int UNIT_PREFIX_FACTOR = 1000;
	private static final int ROUNDING_OFFSET = 500;
	private static final int CONVERSION_TRESHOLD = 10000;

	private SimonUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns nano-time in human readable form with unit. Number is always from 10 to 9999
	 * except for seconds that are biggest unit used.
	 *
	 * @param time time in nanoseconds
	 * @return human readable time string
	 */
	public static String presentNanoTime(long time) {
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

	public static void printSimonTree(Simon simon) {
		StringBuilder sb = new StringBuilder();
		printSimonTree(0, simon, sb);
		System.out.println(sb);
	}

	private static void printSimonTree(int level, Simon simon, StringBuilder sb) {
		printSimon(level, simon, sb);
		for (Simon child : simon.getChildren()) {
			printSimonTree(level + 1, child, sb);
		}
	}

	private static void printSimon(int level, Simon simon, StringBuilder sb) {
		for (int i = 0; i < level; i++) {
			sb.append("  ");
		}
		sb.append(localName(simon.getName()))
			.append('(')
			.append(simon.isEnabled() ? '+' : '-')
			.append("): ")
			.append(simon.toString())
			.append('\n');
	}

	private static String localName(String name) {
		int ix = name.lastIndexOf(SimonFactory.PROPERTY_CONFIG_FILE_NAME);
		if (ix == -1) {
			return name;
		}
		return name.substring(ix + 1);
	}
}
