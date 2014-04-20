package org.javasimon.utils;

import org.javasimon.Counter;
import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.SimonFilter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

/**
 * SimonUtils provides static utility methods.
 * <h3>Human readable outputs</h3>
 * Both {@link org.javasimon.Stopwatch} and {@link org.javasimon.Counter} provide human readable
 * {@code toString} outputs. All nanosecond values are converted into few valid digits with
 * proper unit (ns, us, ms, s) - this is done via method {@link #presentNanoTime(long)}.
 * Max/min counter values are checked for undefined state (max/min long value is converted
 * to string "undef") - via method {@link #presentMinMaxCount(long)}.
 * <h3>Simon tree operations</h3>
 * Method for recursive reset of the {@link org.javasimon.Simon} and all its children is provided -
 * {@link #recursiveReset(org.javasimon.Simon)}. For various debug purposes there is a method
 * that creates string displaying the whole Simon sub-tree. Here is example code that initializes
 * two random Simons and prints the whole Simon hierarchy (note that the method can be used to
 * obtain any sub-tree of the hierarchy):
 * <pre>
 * Split split = SimonManager.getStopwatch("com.my.other.stopwatch").start();
 * SimonManager.getCounter("com.my.counter").setState(SimonState.DISABLED, false);
 * split.stop();
 * System.out.println(SimonUtils.simonTreeString(SimonManager.getRootSimon()));</pre>
 * And the output is:
 * <pre>
 * (+): Unknown Simon:  [ ENABLED]
 *   com(+): Unknown Simon:  [com INHERIT]
 *     my(+): Unknown Simon:  [com.my INHERIT]
 *       other(+): Unknown Simon:  [com.my.other INHERIT]
 *         stopwatch(+): Simon Stopwatch: total 24.2 ms, counter 1, max 24.2 ms, min 24.2 ms, mean 24.2 ms [com.my.other.stopwatch INHERIT]
 *       counter(-): Simon Counter: counter=0, max=undef, min=undef [com.my.counter DISABLED]</pre>
 * Notice +/- signs in parenthesis that displays effective Simon state (enabled/disabled), further
 * details are printed via each Simon's {@code toString} method.
 * <h3>Other utilities</h3>
 * It is possible to obtain "local name" of the Simon (behind the last dot) via {@link #localName(String)}
 * or check if the name is valid Simon name via {@link #checkName(String)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @author Radovan Sninsky
 * @since 1.0
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class SimonUtils {

	/** Number of milliseconds in one second. */
	public static final long MILLIS_IN_SECOND = 1000;

	/** Number of nanoseconds in one millisecond. */
	public static final long NANOS_IN_MILLIS = 1000000;

	/** Number of nanoseconds in one second. */
	public static final long NANOS_IN_SECOND = NANOS_IN_MILLIS * MILLIS_IN_SECOND;

	/** Regex character class content for {@link #NAME_PATTERN}. */
	public static final String NAME_PATTERN_CHAR_CLASS_CONTENT = "-_\\[\\]A-Za-z0-9.,@$%)(<>";

	/** Regex pattern for Simon names. */
	public static final Pattern NAME_PATTERN = Pattern.compile("[" + NAME_PATTERN_CHAR_CLASS_CONTENT + "]+");

	/**
	 * Allowed Simon name characters.
	 *
	 * @since 2.3
	 */
	public static final String ALLOWED_CHARS = "-_[]ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,@$%()<>";

	/**
	 * Name of the attribute where manager is searched for in an appropriate context - used for Spring/JavaEE/console integration.
	 * While the name can be used in any context supporting named attributes, it is primarily aimed for ServletContext. Manager can
	 * be shared in ServletContext by {@code SimonWebConfigurationBean} (Spring module) and then picked up by {@code SimonServletFilter}
	 * (JavaEE module) and {@code SimonConsoleFilter} (Embeddable console). If no manager is found in the attribute of the context,
	 * it is expected that components will use default {@link org.javasimon.SimonManager} instead.
	 *
	 * @since 3.2
	 */
	public static final String MANAGER_SERVLET_CTX_ATTRIBUTE = "manager-servlet-ctx-attribute";

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

	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyMMdd-HHmmss.SSS");

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
		int i = 1;
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			i++;
			if (ste.getClassName().equals(SimonUtils.class.getName())) {
				break;
			}
		}
		CLIENT_CODE_STACK_INDEX = i;
	}

	private static final SimonFilter ACCEPT_ALL_FILTER = new SimonFilter() {
		@Override
		public boolean accept(Simon simon) {
			return true;
		}
	};

	private SimonUtils() {
		throw new AssertionError();
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
		return presentOverNanoTime((double) nanos);
	}

	/**
	 * Returns nano-time in human readable form with unit. Number is always from 10 to 9999
	 * except for seconds that are the biggest unit used.
	 *
	 * @param nanos time in nanoseconds
	 * @return human readable time string
	 */
	public static String presentNanoTime(double nanos) {
		if (nanos == Double.MAX_VALUE) {
			return UNDEF_STRING;
		}
		return presentOverNanoTime(nanos);
	}

	private static String presentOverNanoTime(double time) {
		if (Math.abs(time) < 1d) {
			return "0";
		}

		if (time < UNIT_PREFIX_FACTOR) {
			return ((long) time) + " ns";
		}

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

	private static synchronized String formatTime(double time, String unit) {
		if (time < TEN) {
			return UNDER_TEN_FORMAT.format(time) + unit;
		}
		if (time < HUNDRED) {
			return UNDER_HUNDRED_FORMAT.format(time) + unit;
		}
		return DEFAULT_FORMAT.format(time) + unit;
	}

	/**
	 * Returns timestamp in human readable (yet condensed) form "yyMMdd-HHmmss.SSS".
	 *
	 * @param timestamp timestamp in millis
	 * @return timestamp as a human readable string
	 */
	public static String presentTimestamp(long timestamp) {
		if (timestamp == 0) {
			return UNDEF_STRING;
		}
		synchronized (TIMESTAMP_FORMAT) {
			return TIMESTAMP_FORMAT.format(new Date(timestamp));
		}
	}

	/**
	 * Returns min/max counter values in human readable form - if the value is max or min long value
	 * it is considered unused and string "undef" is returned.
	 *
	 * @param minmax counter extreme value
	 * @return counter value or "undef" if counter contains {@code Long.MIN_VALUE} or {@code Long.MAX_VALUE}
	 */
	public static String presentMinMaxCount(long minmax) {
		if (minmax == Long.MAX_VALUE || minmax == Long.MIN_VALUE) {
			return UNDEF_STRING;
		}
		return String.valueOf(minmax);
	}

	/**
	 * Returns min/max split values in human readable form - if the value is max or min long value
	 * it is considered unused and string "undef" is returned.
	 *
	 * @param minmax split extreme value
	 * @return extreme value or "undef" if extreme contains {@code Long.MIN_VALUE} or {@code Long.MAX_VALUE}
	 */
	public static String presentMinMaxSplit(long minmax) {
		if (minmax == Long.MAX_VALUE || minmax == Long.MIN_VALUE) {
			return UNDEF_STRING;
		}
		return presentNanoTime(minmax);
	}

	/**
	 * Returns multi-line string containing Simon tree starting with the specified Simon.
	 * Root Simon can be used to obtain tree with all Simons. Returns {@code null} for
	 * input value of null or for NullSimon or any Simon with name equal to null (anonymous
	 * Simons) - this is also the case when the Manager is disabled and tree for its root
	 * Simon is requested.
	 *
	 * @param simon root Simon of the output tree
	 * @return string containing the tree or null if the Simon is null Simon
	 */
	public static String simonTreeString(Simon simon) {
		if (simon == null || simon.getName() == null) {
			return null;
		}
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
	 * Returns last part of Simon name - local name.
	 *
	 * @param name full Simon name
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
	 * Simon and recursively on all its children. Operation is not truly atomic as a whole,
	 * consistency on the Simon level depends on the implementation of {@link org.javasimon.Simon#reset()}
	 * (which is thread-safe in all current implementations).
	 *
	 * @param simon subtree root
	 * @deprecated will be removed in 4.0
	 */
	@Deprecated
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
	 * Autogenerates name for the Simon using the fully-qualified class name.
	 * <i>This method has inherent performance penalty (getting the stack-trace) so it is recommended to store
	 * pre-generated name. (Whole get-start-stop cycle is roughly 30-times slower with this method included.)</i>
	 *
	 * @param suffix name suffix for eventual Simon discrimination
	 * @return autogenerated name for Simon
	 * @since 3.1
	 */
	public static String generateNameForClass(String suffix) {
		return generatePrivate(suffix, false);
	}

	/**
	 * Autogenerates name for the Simon using the fully qualified class name and the method name.
	 * <i>This method has inherent performance penalty (getting the stack-trace) so it is recommended to store
	 * pre-generated name. (Whole get-start-stop cycle is roughly 30-times slower with this method included.)</i>
	 *
	 * @param suffix name suffix for eventual Simon discrimination
	 * @return autogenerated name for Simon
	 * @since 3.1
	 */
	public static String generateNameForClassAndMethod(String suffix) {
		return generatePrivate(suffix, true);
	}

	// method is extracted, so the stack trace index is always right
	private static String generatePrivate(String suffix, boolean includeMethodName) {
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

	/**
	 * Autogenerates name for the Simon using the class name and the method name.
	 *
	 * @return autogenerated name for Simon
	 * @see #generateNameForClassAndMethod(String)
	 */
	public static String generateName() {
		return generatePrivate(null, true);
	}

	/**
	 * Calls a block of code with stopwatch around and returns result.
	 *
	 * @param name name of the Stopwatch
	 * @param callable callable block of code
	 * @param <T> return type
	 * @return whatever block of code returns
	 * @throws Exception whatever block of code throws
	 * @since 3.0
	 */
	public static <T> T doWithStopwatch(String name, Callable<T> callable) throws Exception {
		Split split = SimonManager.getStopwatch(name).start();
		try {
			return callable.call();
		} finally {
			split.stop();
		}
	}

	/**
	 * Calls a block of code with stopwatch around, can not return any result or throw an exception
	 * (use {@link #doWithStopwatch(String, java.util.concurrent.Callable)} instead).
	 *
	 * @param name name of the Stopwatch
	 * @param runnable wrapped block of code
	 * @since 3.0
	 */
	public static void doWithStopwatch(String name, Runnable runnable) {
		Split split = SimonManager.getStopwatch(name).start();
		try {
			runnable.run();
		} finally {
			split.stop();
		}
	}

	private static final String SHRINKED_STRING = "...";

	/**
	 * Shrinks the middle of the input string if it is too long, so it does not exceed limitTo.
	 *
	 * @since 3.2
	 */
	public static String compact(String input, int limitTo) {
		if (input == null || input.length() <= limitTo) {
			return input;
		}

		int headLength = limitTo / 2;
		int tailLength = limitTo - SHRINKED_STRING.length() - headLength;
		if (tailLength < 0) {
			tailLength = 1;
		}

		return input.substring(0, headLength) + SHRINKED_STRING + input.substring(input.length() - tailLength);
	}

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

	/**
	 * Aggregate statistics from all stopwatches in hierarchy that pass specified filter. Filter is applied
	 * to all simons in the hierarchy of all types. If a simon is rejected by filter its children are not considered.
	 * Simons are aggregated in the top-bottom fashion, i.e. parent simons are aggregated before children.
	 *
	 * @param simon root of the hierarchy of simons for which statistics will be aggregated
	 * @param filter filter to select subsets of simons to aggregate
	 * @return aggregates statistics
	 * @since 3.5
	 */
	public static StopwatchAggregate calculateStopwatchAggregate(Simon simon, SimonFilter filter) {
		StopwatchAggregate stopwatchAggregate = new StopwatchAggregate();
		if (filter != null) {
			calculateAggregate(stopwatchAggregate, simon, filter);
		} else {
			calculateAggregate(stopwatchAggregate, simon, ACCEPT_ALL_FILTER);
		}


		return stopwatchAggregate;
	}

	/**
	 * Aggregate statistics from all stopwatches in hierarchy of simons.
	 *
	 * @param simon root of the hierarchy of simons for which statistics will be aggregated
	 * @return aggregated statistics
	 * @since 3.5
	 */
	public static StopwatchAggregate calculateStopwatchAggregate(Simon simon) {
		return calculateStopwatchAggregate(simon, null);
	}

	private static void calculateAggregate(StopwatchAggregate aggregate, Simon simon, SimonFilter filter) {
		if (filter.accept(simon)) {
			if (simon instanceof Stopwatch) {
				Stopwatch stopwatch = (Stopwatch) simon;
				aggregate.addSample(stopwatch.sample());
			}

			for (Simon child : simon.getChildren()) {
				calculateAggregate(aggregate, child, filter);
			}
		}
	}

	/**
	 * Aggregate statistics from all counters in hierarchy of simons.
	 *
	 * @param simon root of the hierarchy of simons for which statistics will be aggregated
	 * @return aggregated statistics
	 * @since 3.5
	 */
	public static CounterAggregate calculateCounterAggregate(Simon simon) {
		return calculateCounterAggregate(simon, null);
	}

	/**
	 * Aggregate statistics from all counters in hierarchy that pass specified filter. Filter is applied
	 * to all simons in the hierarchy of all types. If a simon is rejected by filter its children are not considered.
	 * Simons are aggregated in the top-bottom fashion, i.e. parent simons are aggregated before children.
	 *
	 * @param simon root of the hierarchy of simons for which statistics will be aggregated
	 * @param filter filter to select subsets of simons to aggregate
	 * @return aggregates statistics
	 * @since 3.5
	 */
	public static CounterAggregate calculateCounterAggregate(Simon simon, SimonFilter filter) {
		CounterAggregate aggregate = new CounterAggregate();
		if (filter != null) {
			calculateCounterAggregate(aggregate, simon, filter);
		} else {
			calculateCounterAggregate(aggregate, simon, ACCEPT_ALL_FILTER);
		}
		return aggregate;
	}

	private static void calculateCounterAggregate(CounterAggregate aggregate, Simon simon, SimonFilter filter) {
		if (filter.accept(simon)) {
			if (simon instanceof Counter) {
				Counter counter = (Counter) simon;
				aggregate.addSample(counter.sample());
			}

			for (Simon child : simon.getChildren()) {
				calculateCounterAggregate(aggregate, child, filter);
			}
		}
	}

	private static class Calibration {

		private static final int TENTH_OF_MILLIS = 1000000;
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
