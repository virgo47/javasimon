package org.javasimon;

import java.util.Collection;

/**
 * Factory.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 11, 2008
 */
interface Factory {
	Simon getSimon(String name);

	Counter getCounter(String name);

	Simon getRootSimon();

	Stopwatch getStopwatch(String name);

	String generateName(String suffix, boolean includeMethodName);

	Collection<String> simonNames();

	Counter getCounter(String name, ObservationProcessorType observationProcessorType);

	Stopwatch getStopwatch(String name, ObservationProcessorType observationProcessorType);
}
