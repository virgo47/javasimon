package org.javasimon;

/**
 * AbstractSample.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
abstract class AbstractSample implements Sample {
	private final int count;
	private final double mean;
	private final double standardDeviation;
	private final double sum;
	private final double variance;
	private final double varianceN;

	AbstractSample(StatProcessor statProcessor) {
		count = statProcessor.getCount();
		mean = statProcessor.getMean();
		standardDeviation = statProcessor.getStandardDeviation();
		sum = statProcessor.getSum();
		variance = statProcessor.getVariance();
		varianceN = statProcessor.getVarianceN();
	}

	public int getCount() {
		return count;
	}

	public double getMean() {
		return mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public double getSum() {
		return sum;
	}

	public double getVariance() {
		return variance;
	}

	public double getVarianceN() {
		return varianceN;
	}
}
