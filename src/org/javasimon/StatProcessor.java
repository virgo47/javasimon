package org.javasimon;

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
	StatProcessorType getType();

	void process(double value);

	double getAverage();

	/**
	 * Samples stat processor values and returns them as a map (propertyName -> value). Resets
	 * the stat processor if needed. Values may not all be strings, but string is used as
	 * the best universal type to represent the value. Compound values must be split into more values.
	 *
	 * @param reset if true, stat processor is reset after sampling
	 * @return sampled values in a map
	 */
	Map<String, String> sample(boolean reset);

	double getSum();

	double getMean();

	double getVarianceN();

	double getVariance();

	int getCount();

	void reset();
}
