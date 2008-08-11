package org.javasimon;

/**
 * NullObservationProcessor.
*
* @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
* @created Aug 12, 2008
*/
class NullObservationProcessor implements ObservationProcessor {
	public static final NullObservationProcessor INSTANCE = new NullObservationProcessor();

	public ObservationProcessorType getType() {
		return ObservationProcessorType.NULL;
	}
}
