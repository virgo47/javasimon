package org.javasimon;

/**
 * Sample contains all relevant values of the Simon that are obtained by the
 * {@link org.javasimon.Simon#sample()} and {@link org.javasimon.Simon#sampleAndReset()} methods.
 * Returned object contains consistent set of Simon values as both operations are synchronized.
 * However Sample is a Java Bean and it can be modified afterwards so no consistency is guaranteed
 * when the object is used in an inapropriate context. As a Java Bean object can be
 * used directly as the data transfer object without need to create another DTO with the same data.
 * Sample generally doesn't have any behavior.
 *
 * Common abstract part of the Sample object holds values related to statistics processor.
 * Some or all of them can be 0 depending on type of the processo.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 * @see org.javasimon.StatProcessor
 */
public abstract class Sample {
	private String note;
	private int count;
	private double mean;
	private double standardDeviation;
	private double sum;
	private double variance;
	private double varianceN;

	/**
	 * Initializes values related to stat processor. Used internally in sample methods, but
	 * can be also used explicitely when necessary.
	 *
	 * @param statProcessor statistics processor
	 */
	public final void setFromStatProcessor(StatProcessor statProcessor) {
		count = statProcessor.getCount();
		mean = statProcessor.getMean();
		standardDeviation = statProcessor.getStandardDeviation();
		sum = statProcessor.getSum();
		variance = statProcessor.getVariance();
		varianceN = statProcessor.getVarianceN();
	}

	/**
	 * Note from the sampled Simon.
	 *
	 * @return Simon's note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Sets the note for the sample, typically note from the sampled Simon.
	 *
	 * @param note Simon's note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Observation count, count of measured values.
	 *
	 * @return values count
	 */
	public final int getCount() {
		return count;
	}

	public final void setCount(int count) {
		this.count = count;
	}

	/**
	 * Returns mean value (average) of all measured values.
	 *
	 * @return mean value
	 */
	public final double getMean() {
		return mean;
	}

	public final void setMean(double mean) {
		this.mean = mean;
	}

	/**
	 * Returns standard deviation for all measured values.
	 *
	 * @return standard deviation
	 */
	public final double getStandardDeviation() {
		return standardDeviation;
	}

	public final void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	/**
	 * Returns sum of all measured values.
	 *
	 * @return sum of values
	 */
	public final double getSum() {
		return sum;
	}

	public final void setSum(double sum) {
		this.sum = sum;
	}

	/**
	 * Returns unbiased estimate of the population variance.
	 *
	 * @return unbiased estimated variance
	 */
	public final double getVariance() {
		return variance;
	}

	public final void setVariance(double variance) {
		this.variance = variance;
	}

	/**
	 * Returns variance value of all measured values (entire population).
	 *
	 * @return entire population variance
	 */
	public final double getVarianceN() {
		return varianceN;
	}

	public final void setVarianceN(double varianceN) {
		this.varianceN = varianceN;
	}

	/**
	 * Returns readable representation of object.
	 *
	 * @return string with readable representation of object
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Sample");
		sb.append("{count=").append(count);
		sb.append(", mean=").append(mean);
		sb.append(", standardDeviation=").append(standardDeviation);
		sb.append(", sum=").append(sum);
		sb.append(", variance=").append(variance);
		sb.append(", varianceN=").append(varianceN);
		sb.append('}');
		return sb.toString();
	}
}
