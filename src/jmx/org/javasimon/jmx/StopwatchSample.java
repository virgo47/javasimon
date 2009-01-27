package org.javasimon.jmx;

import java.beans.ConstructorProperties;

/**
 * Trieda StopwatchSample.
 *
 * @author <a href="mailto:radovan.sninsky@siemens.com">Radovan Sninsky</a>
 * @version $ Revision $ $ Date $
 * @created 26.1.2009 10:21:36
 * @since 2
 */
public final class StopwatchSample extends org.javasimon.StopwatchSample {

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

	public StopwatchSample(org.javasimon.StopwatchSample s) {
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
