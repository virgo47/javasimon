package org.javasimon;

/**
 * Null stat processor ignores all measured values and provides no results. It is used
 * by default if no other stat processor is requested.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
final class NullStatProcessor implements StatProcessor {
	/**
	 * Singleton instance of this stat processor.
	 */
	static final NullStatProcessor INSTANCE = new NullStatProcessor();

	private NullStatProcessor() {
	}

	/**
	 * {@inheritDoc}
	 */
	public StatProcessorType getType() {
		return StatProcessorType.NULL;
	}

	/**
	 * {@inheritDoc}
	 */
	public void process(double value) {
	}

	/**
	 * {@inheritDoc}
	 */
	public double getSum() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getMean() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getVarianceN() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getVariance() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getStandardDeviation() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInterpreter(ResultInterpreter interpreter) {
	}

	/**
	 * Returns string {@code Null Stats}.
	 *
	 * @return string {@code Null Stats}
	 */
	@Override
	public String toString() {
		return "Null Stats";
	}
}
