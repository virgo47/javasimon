package org.javasimon;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * BasicStatProcessor provides following stats: sum, average, means, variances.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
class BasicStatProcessor extends AbstractStatProcessor {
	private double sum;
	private double mean;
	private double mean2;
	private int count;

	@Override
	public StatProcessorType getType() {
		return StatProcessorType.BASIC;
	}

	@Override
	public synchronized void process(double value) {
		sum += value;
		count++;
		double delta = value - mean;
		mean = sum / count;
		mean2 += delta * (value - mean);
	}

	@Override
	public synchronized double getSum() {
		return sum;
	}

	@Override
	public synchronized double getMean() {
		return mean;
	}

	@Override
	public synchronized double getVarianceN() {
		if (count == 0) {
			return 0;
		}
		return mean2 / count;
	}

	@Override
	public synchronized double getVariance() {
		if (count == 0) {
			return 0;
		}
		int countMinusOne = count - 1;
		if (count < 2) {
			countMinusOne = 1;
		}
		return mean2 / countMinusOne;
	}

	@Override
	public synchronized double getStandardDeviation() {
		return Math.sqrt(getVarianceN());
	}

	@Override
	public synchronized int getCount() {
		return count;
	}

	@Override
	public synchronized void reset() {
		sum = 0;
		mean = 0;
		mean2 = 0;
		count = 0;
	}

	@Override
	public synchronized Map<String, String> sample(boolean reset) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("spCount", String.valueOf(count));
		map.put("spSum", String.valueOf(sum));
		map.put("spMean", String.valueOf(mean));
		map.put("stdDev", String.valueOf(getStandardDeviation()));
//		map.put("spVariance", String.valueOf(getVariance()));
//		map.put("spVarianceN", String.valueOf(getVarianceN()));
		if (reset) {
			reset();
		}
		return map;
	}

	@Override
	public String toString() {
		return "Basic Stats: " +
			"sum=" + interpret(sum) +
			", count=" + count +
			", mean=" + interpret(mean) +
			", stdDev=" + interpret(getStandardDeviation());
	}
}
