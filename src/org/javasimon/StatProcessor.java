package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.util.Map;

/**
 * StatProcessor processes observations - measured values - and provides additional statistical
 * information for them. StatProcessor interface gives you various methods but not all of them
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
	 * Samples stat processor values and returns them as a map (propertyName -> value). Resets
	 * the stat processor if needed. Values may not all be strings, but string is used as
	 * the best universal type to represent the value. Compound values must be split into more values.
	 *
	 * @param reset if true, stat processor is reset after sampling
	 * @return sampled values in a map
	 */
	Map<String, String> sample(boolean reset);

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

	/**
	 * Resets the stat processor to the state like right after the creation.
	 */
	void reset();

	void setInterpreter(StatProcessor.ResultInterpreter interpreter);

	interface ResultInterpreter {
		String interpret(double value);
	}

	class DefaultInterpreter implements ResultInterpreter {
		public static final ResultInterpreter INSTANCE = new DefaultInterpreter();

		public String interpret(double value) {
			return String.valueOf(value);
		}
	}

	class TimeInterpreter implements ResultInterpreter {
		public static final ResultInterpreter INSTANCE = new TimeInterpreter();

		public String interpret(double value) {
			return SimonUtils.presentNanoTime((long) value);
		}
	}
}
