package org.javasimon.jmx;

import java.beans.ConstructorProperties;

/**
 * Value object for retrieving data from Stopwatch simon. Basically, it's
 * {@link org.javasimon.StopwatchSample} with added JMX capabilities to be return as object via
 * MBean method.
 * <p>
 * Example:
 * <pre>
 * SimonMXBean simon = JMX.newMXBeanProxy(..., new ObjectName("domain:type=Simon"), SimonMXBean.class);
 * StopwatchSample = simon.getStopwatchSample("simon.stopwatch");
 * </pre>
 *
 * @author Radovan Sninsky
 * @version $ Revision $ $ Date $
 * @created 26.1.2009 10:21:36
 * @since 2
 */
public final class StopwatchSample extends org.javasimon.StopwatchSample {

	/**
	 * JMX constructor. Constructor used by JMX client code to initialize all properties of object
	 * from composite data object.
	 *
	 * @param count count value (provided optionally)
	 * @param mean mean value (provided optionally)
	 * @param stdDev standard deviation (provided optionally)
	 * @param sum sum value (provided optionally)
	 * @param var variance (provided optionally)
	 * @param varN variance N (provided optionally)
	 * @param total sum of all measured times
	 * @param counter count of measures
	 * @param min minimal measured time
	 * @param max maximal measured time
	 * @param minTimestamp time when minimal time was measured
	 * @param maxTimestamp time when maximal time was measured
	 * @param active count of actual running measures
	 * @param maxActive maximum paralel measures
	 * @param maxActiveTimestamp time when maximum paralel measures happend 
	 */
	@ConstructorProperties({"count", "mean", "standardDeviation", "sum", "variance", "varianceN",
			"total", "counter", "min", "max", "minTimestamp", "maxTimestamp", "active", "maxActive", "maxActiveTimestamp"})
	public StopwatchSample(int count, double mean, double stdDev, double sum, double var, double varN,
		long total, long counter, long min, long max, long minTimestamp, long maxTimestamp, long active, long maxActive, long maxActiveTimestamp) {
		setCount(count);
		setMean(mean);
		setStandardDeviation(stdDev);
		setSum(sum);
		setVariance(var);
		setVarianceN(varN);

		setTotal(total);
		setCounter(counter);
		setMin(min);
		setMax(max);
		setMinTimestamp(minTimestamp);
		setMaxTimestamp(maxTimestamp);
		setActive(active);
		setMaxActive(maxActive);
		setMaxActiveTimestamp(maxActiveTimestamp);
	}

	/**
	 * Internall, framework constructor for Simon MBean implementation to initialize all properties
	 * by sample obtained from simon.
	 *
	 * @param s sample object obtained from Stopwatch simon
	 */
	StopwatchSample(org.javasimon.StopwatchSample s) {
		setCount(s.getCount());
		setMean(s.getMean());
		setStandardDeviation(s.getStandardDeviation());
		setSum(s.getSum());
		setVariance(s.getVariance());
		setVarianceN(s.getVarianceN());

		setCounter(s.getCounter());
		setMin(s.getMin());
		setMax(s.getMax());
		setMinTimestamp(s.getMinTimestamp());
		setMaxTimestamp(s.getMaxTimestamp());
		setActive(s.getActive());
		setMaxActive(s.getMaxActive());
		setMaxActiveTimestamp(s.getMaxActiveTimestamp());
	}
}
