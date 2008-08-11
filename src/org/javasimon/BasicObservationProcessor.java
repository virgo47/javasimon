package org.javasimon;

/**
 * BasicObservationProcessor.
*
* @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
* @created Aug 12, 2008
*/
class BasicObservationProcessor implements ObservationProcessor {
	public ObservationProcessorType getType() {
		return ObservationProcessorType.BASIC;
	}
}
