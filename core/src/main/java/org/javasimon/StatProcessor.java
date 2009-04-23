package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * StatProcessor processes observations - measured values - and provides additional statistical
 * information for them. StatProcessor interface provides various methods but not all of them
 * are implemented in every implementation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public interface StatProcessor {
	/**
	 * Returns type of the stat processor that determines how many statistical data
	 * this processor provides.
	 *
	 * @return stat processor type
	 */
	StatProcessorType getType();

	/**
	 * Processes one measured value - so called observation.
	 *
	 * @param value obseravation value (one measured value)
	 */
	void process(double value);

	/**
	 * Returns sum of all measured values.
	 *
	 * @return sum of values
	 */
	double getSum();

	/**
	 * Returns mean value (average) of all measured values.
	 *
	 * @return mean value
	 */
	double getMean();

	/**
	 * Returns variance value of all measured values (entire population).
	 *
	 * @return entire population variance
	 */
	double getVarianceN();

	/**
	 * Returns unbiased estimate of the population variance.
	 *
	 * @return unbiased estimated variance
	 */
	double getVariance();

	/**
	 * Observation count, count of measured values.
	 *
	 * @return values count
	 */
	int getCount();

	/** Resets the stat processor to the state like right after the creation. */
	void reset();

	/**
	 * Assings result interpreter for this stat processor to print results in {@code toString}.
	 *
	 * @param interpreter result interpeter
	 */
	void setInterpreter(StatProcessor.ResultInterpreter interpreter);

	/**
	 * Returns standard deviation for all measured values.
	 *
	 * @return standard deviation
	 */
	double getStandardDeviation();

	/**
	 * Interface that interprets the results and changes values to strings. This is
	 * used in {@code toString} method of stat processor. Not all values are interpreted,
	 * various square values does not represent the original value in the original unit
	 * hence they are difficult to interpret. On the other hand, average and deviation are
	 * typical values that are interpreted.
	 */
	interface ResultInterpreter {
		/**
		 * Interprets the double value to string. It is used in {@code toString} so it should
		 * be human readable string.
		 *
		 * @param value double value
		 * @return human readable string
		 */
		String interpret(double value);
	}

	/** Default result interpreter returns the value as a string without any changes. */
	class DefaultInterpreter implements ResultInterpreter {
		/** Reusable instance of the default result interpreter. */
		static final ResultInterpreter INSTANCE = new DefaultInterpreter();

		/**
		 * {@inheritDoc}
		 */
		public String interpret(double value) {
			return String.valueOf(value);
		}
	}

	/**
	 * Nano interpreter treats values as nanosecond times and returns it in a human
	 * readable format using {@link org.javasimon.utils.SimonUtils#presentNanoTime(long)}.
	 */
	class NanoInterpreter implements ResultInterpreter {
		/** Reusable instance of the default result interpreter. */
		static final ResultInterpreter INSTANCE = new NanoInterpreter();

		/**
		 * {@inheritDoc}
		 */
		public String interpret(double value) {
			return SimonUtils.presentNanoTime((long) value);
		}
	}
}
