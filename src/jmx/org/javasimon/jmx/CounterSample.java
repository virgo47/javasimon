package org.javasimon.jmx;

import java.beans.ConstructorProperties;

/**
 * Trieda CounterSample.
 *
 * @author <a href="mailto:radovan.sninsky@siemens.com">Radovan Sninsky</a>
 * @version $ Revision $ $ Date $
 * @created 23.1.2009 12:37:40
 * @since 2
 */
public final class CounterSample extends org.javasimon.CounterSample {

	@ConstructorProperties({"count", "mean", "standardDeviation", "sum", "variance", "varianceN", "counter", "min", "max", "minTimestamp",
		"maxTimestamp", "incrementSum", "decrementSum"})
	public CounterSample(int count, double mean, double stdDev, double sum, double var, double varN,
		long counter, long min, long max, long minTimestamp, long maxTimestamp, long incSum, long decSum) {
		setCount(count);
		setMean(mean);
		setStandardDeviation(stdDev);
		setSum(sum);
		setVariance(var);
		setVarianceN(varN);

		setCounter(counter);
		setMin(min);
		setMax(max);
		setMinTimestamp(minTimestamp);
		setMaxTimestamp(maxTimestamp);
		setIncrementSum(incSum);
		setDecrementSum(decSum);
	}

	public CounterSample(org.javasimon.CounterSample s) {
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
		setIncrementSum(s.getIncrementSum());
		setDecrementSum(s.getDecrementSum());
	}
}
