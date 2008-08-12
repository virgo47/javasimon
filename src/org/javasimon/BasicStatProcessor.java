package org.javasimon;

/**
 * BasicObservationProcessor.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
class BasicStatProcessor implements StatProcessor {
	private double sum;
	private int count;

	public StatProcessorType getType() {
		return StatProcessorType.BASIC;
	}

	public synchronized void process(double value) {
		sum += value;
		count++;
	}

	public synchronized double getAverage() {
		if (count == 0) {
			return 0;
		}
		return sum / count;
	}
}
