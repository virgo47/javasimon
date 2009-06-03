package org.javasimon;

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

	/**
	 * {@inheritDoc}
	 */
	public StatProcessorType getType() {
		return StatProcessorType.BASIC;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void process(double value) {
		sum += value;
		count++;
		double delta = value - mean;
		mean = sum / count;
		mean2 += delta * (value - mean);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized double getSum() {
		return sum;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized double getMean() {
		return mean;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized double getVarianceN() {
		if (count == 0) {
			return 0;
		}
		return mean2 / count;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public synchronized double getStandardDeviation() {
		return Math.sqrt(getVarianceN());
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized int getCount() {
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void reset() {
		sum = 0;
		mean = 0;
		mean2 = 0;
		count = 0;
	}

	/**
	 * Returns sum, count, mean and standard deviation as a human readable string.
	 *
	 * @return sum, count, mean and standard deviation as a string
	 */
	@Override
	public String toString() {
		return "Basic Stats: " +
			"sum=" + interpret(sum) +
			", count=" + count +
			", mean=" + interpret(mean) +
			", stdDev=" + interpret(getStandardDeviation());
	}
}
