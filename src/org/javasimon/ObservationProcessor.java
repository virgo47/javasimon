package org.javasimon;

/**
 * ObservationProcessor processes observations - measured values - and gives them an additional statistical
 * representation. Observation processor interface gives you various methods but not all of them are implemented
 * in every implementation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public interface ObservationProcessor {
	ObservationProcessorType getType();
}
