package org.javasimon;

/**
 * NullObservationProcessor.
*
* @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
* @created Aug 12, 2008
*/
class NullStatProcessor implements StatProcessor {
	public static final NullStatProcessor INSTANCE = new NullStatProcessor();

	public StatProcessorType getType() {
		return StatProcessorType.NULL;
	}

	public void process(double value) {
	}

	public double getAverage() {
		return 0;
	}
}
