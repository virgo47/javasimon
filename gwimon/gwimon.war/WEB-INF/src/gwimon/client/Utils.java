package gwimon.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

import java.util.Date;

/**
 * Utils.
 *
 * @author virgo47@gmail.com
 */
public class Utils {
	private static final int UNIT_PREFIX_FACTOR = 1000;
	private static final DateTimeFormat TIMESTAMP_FORMAT = DateTimeFormat.getFormat("yyMMdd-HHmmss.SSS");

	private static final int TEN = 10;

	private static final NumberFormat UNDER_TEN_FORMAT = NumberFormat.getFormat("0.00");
	private static final int HUNDRED = 100;

	private static final NumberFormat UNDER_HUNDRED_FORMAT = NumberFormat.getFormat("00.0");
	private static final NumberFormat DEFAULT_FORMAT = NumberFormat.getFormat("000");

	private static final String UNDEF_STRING = "undef";

	/**
	 * Retrofitted to GWT from {@link org.javasimon.utils.SimonUtils#presentNanoTime(long)}.
	 *
	 * @param nanos time in nanoseconds
	 * @return human readable time string
	 */
	public static String presentNanoTime(long nanos) {
		if (nanos == Long.MAX_VALUE || nanos == 0) {
			return UNDEF_STRING;
		}
		if (nanos < UNIT_PREFIX_FACTOR) {
			return nanos + " ns";
		}

		double time = nanos;
		time /= UNIT_PREFIX_FACTOR;
		if (time < UNIT_PREFIX_FACTOR) {
			return formatTime(time, " us");
		}

		time /= UNIT_PREFIX_FACTOR;
		if (time < UNIT_PREFIX_FACTOR) {
			return formatTime(time, " ms");
		}

		time /= UNIT_PREFIX_FACTOR;
		return formatTime(time, " s");
	}

	private static String formatTime(double time, String unit) {
		if (time < TEN) {
			return UNDER_TEN_FORMAT.format(time) + unit;
		}
		if (time < HUNDRED) {
			return UNDER_HUNDRED_FORMAT.format(time) + unit;
		}
		return DEFAULT_FORMAT.format(time) + unit;
	}

	/**
	 * Retrofitted to GWT from {@link org.javasimon.utils.SimonUtils#presentTimestamp(long)}.
	 *
	 * @param timestamp timestamp in millis
	 * @return timestamp as a human readable string
	 */
	public static String presentTimestamp(long timestamp) {
		if (timestamp == 0) {
			return UNDEF_STRING;
		}
		return TIMESTAMP_FORMAT.format(new Date(timestamp));
	}
}
