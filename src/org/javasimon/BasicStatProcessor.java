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

	public double getSum() {
		return sum;
	}

	@Override
	public double getMean() {
		return mean;
	}

	@Override
	public double getVarianceN() {
		if (count == 0) {
			return 0;
		}
		return mean2 / count;
	}

	@Override
	public double getVariance() {
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
	public int getCount() {
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
	public Map<String, String> sample(boolean reset) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("spCount", String.valueOf(count));
		map.put("spSum", String.valueOf(sum));
		map.put("spMean", String.valueOf(mean));
		map.put("spMean2", String.valueOf(mean2));
		map.put("spVariance", String.valueOf(getVariance()));
		map.put("spVarianceN", String.valueOf(getVarianceN()));
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
			", std.dev=" + interpret(Math.sqrt(getVarianceN()));
	}
}
