package org.javasimon.utils;

import org.javasimon.Simon;
import org.javasimon.Manager;

import java.text.*;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SimonUtils provides static utility methods.
 * <p/>
 * <h3>Human readable outputs</h3>
 * Both {@link org.javasimon.Stopwatch} and {@link org.javasimon.Counter} provide human readable
 * {@code toString} outputs. All nanosecond values are converted into few valid digits with
 * proper unit (ns, us, ms, s) - this is done via method {@link #presentNanoTime(long)}.
 * Max/min counter values are checked for undefined state (max/min long value is converted
 * to string "undef") - via method {@link #presentMinMax(long)}.
 * <p/>
 * <h3>Simon tree operations</h3>
 * Method for recursive reset of the {@link org.javasimon.Simon} and all its children is provided -
 * {@link #recursiveReset(org.javasimon.Simon)}. For various debug purposes there is a method
 * that creates string displaying the whole Simon sub-tree. Here is example code that initializes
 * two random Simons and prints the whole Simon hierarchy (note that the method can be used to
 * obtain any sub-tree of the hierarchy):
 * <pre>
 * Stopwatch stopwatch = SimonManager.getStopwatch("com.my.other.stopwatch").start();
 * SimonManager.getCounter("com.my.counter").setState(SimonState.DISABLED, false);
 * stopwatch.stop();
 * System.out.println(SimonUtils.simonTreeString(SimonManager.getRootSimon()));</pre>
 * And the output is:
 * <pre>
 * (+): Unknown Simon: [ ENABLED/stats=NULL]
 * com(+): Unknown Simon: [com INHERIT/stats=NULL]
 * my(+): Unknown Simon: [com.my INHERIT/stats=NULL]
 * counter(-): Simon Counter: [com.my.counter DISABLED/stats=NULL] counter=0, max=undef, min=undef
 * other(+): Unknown Simon: [com.my.other INHERIT/stats=NULL]
 * stopwatch(+): Simon Stopwatch: [com.my.other.stopwatch INHERIT/stats=NULL] total 1.51 ms, counter 1, max 1.51 ms, min 1.51 ms</pre>
 * Notice +/- signs in parenthesis that displays effective Simon state (enabled/disabled), further
 * details are printed via each Simon's {@code toString} method.
 * <p/>
 * <h3>Other utilities</h3>
 * It is possible to obtain "local name" of the Simon (behind the last dot) via {@link #localName(String)},
 * check if the name is valid Simon name via {@link #checkName(String)} and finally there is method mostly
 * for internal use - {@link #warning(String)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @author Radovan Sninsky
 * @created Aug 6, 2008
 * @since 1.0
 */
public final class SimonUtils {
	/**
	 * Number of nanoseconds in one millisecond.
	 */
	public static final long NANOS_IN_MILLIS = 1000000;

	/**
	 * Regex pattern for Simon names.
	 */
	public static final Pattern NAME_PATTERN = Pattern.compile("[-A-Za-z0-9.@$%()]+");

	private static final int UNIT_PREFIX_FACTOR = 1000;

	private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);

	private static final int TEN = 10;
	private static final DecimalFormat UNDER_TEN_FORMAT = new DecimalFormat("0.00", DECIMAL_FORMAT_SYMBOLS);

	private static final int HUNDRED = 100;
	private static final DecimalFormat UNDER_HUNDRED_FORMAT = new DecimalFormat("00.0", DECIMAL_FORMAT_SYMBOLS);

	private static final DecimalFormat DEFAULT_FORMAT = new DecimalFormat("000", DECIMAL_FORMAT_SYMBOLS);

	private static final String UNDEF_STRING = "undef";

	private static final int CLIENT_CODE_STACK_INDEX;

	static {
		// Finds out the index of "this code" in the returned stack trace - funny but it differs in JDK 1.5 and 1.6
		int i = 0;
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			i++;
			if (ste.getClassName().equals(SimonUtils.class.getName())) {
				break;
			}
		}
		CLIENT_CODE_STACK_INDEX = i;
	}

	private SimonUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns nano-time in human readable form with unit. Number is always from 10 to 9999
	 * except for seconds that are the biggest unit used.
	 *
	 * @param nanos time in nanoseconds
	 * @return human readable time string
	 */
	public static String presentNanoTime(long nanos) {
		if (nanos == Long.MAX_VALUE) {
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

	/**
	 * Returns min/max counter values in human readable form - if the value is max or min long value
	 * it is considered unused and string "undef" is returned.
	 *
	 * @param minmax counter value
	 * @return counter value or "undef" if counter contains {@code Long.MIN_VALUE} or {@code Long.MAX_VALUE}
	 */
	public static String presentMinMax(long minmax) {
		if (minmax == Long.MAX_VALUE || minmax == Long.MIN_VALUE) {
			return UNDEF_STRING;
		}
		return String.valueOf(minmax);
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
	 * Returns multi-line string containing Simon tree starting with the specified Simon.
	 * Root Simon can be used to obtain tree with all Simons.
	 *
	 * @param simon root Simon of the output tree
	 * @return string containing the tree
	 */
	public static String simonTreeString(Simon simon) {
		StringBuilder sb = new StringBuilder();
		printSimonTree(0, simon, sb);
		return sb.toString();
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

	/**
	 * Returns last part of simon hierarchycal name - local name.
	 *
	 * @param name full simon name
	 * @return string containing local name
	 */
	public static String localName(String name) {
		int ix = name.lastIndexOf(Manager.HIERARCHY_DELIMITER);
		if (ix == -1) {
			return name;
		}
		return name.substring(ix + 1);
	}

	/**
	 * Resets the whole Simon subtree - calls {@link org.javasimon.Simon#reset()} on the
	 * Simon and recursively on all its children.
	 *
	 * @param simon subtree root
	 */
	public static void recursiveReset(Simon simon) {
		simon.reset();
		for (Simon child : simon.getChildren()) {
			recursiveReset(child);
		}
	}

	/**
	 * Checks if the input string is correct Simon name. Simon name is checked against
	 * public {@link #NAME_PATTERN}.
	 *
	 * @param name checked string
	 * @return true if the string is proper Simon name
	 */
	public static boolean checkName(String name) {
		return NAME_PATTERN.matcher(name).matches();
	}

	/**
	 * Reports the warning.
	 * JDK14 logging is used but this can change in the future.
	 *
	 * @param warning warning message
	 */
	public static void warning(String warning) {
		Logger.getLogger("org.javasimon").warning(warning);
	}

	/**
	 * Autogenerates name for the Simon using the class name and (optionaly) the method name.
	 *
	 * @param suffix name suffix for eventual Simon discrimination
	 * @param includeMethodName if true, method name will be included in the name thus effectively adding another level
	 * of hierarchy
	 * @return autogenerated name for Simon
	 */
	public static String generateName(String suffix, boolean includeMethodName) {
		StackTraceElement stackElement = Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX];
		StringBuilder nameBuilder = new StringBuilder(stackElement.getClassName());
		if (includeMethodName) {
			nameBuilder.append('.').append(stackElement.getMethodName());
		}
		if (suffix != null) {
			nameBuilder.append(suffix);
		}
		return nameBuilder.toString();
	}
}
