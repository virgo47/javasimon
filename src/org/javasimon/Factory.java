package org.javasimon;

import java.util.Collection;

/**
 * Factory.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 11, 2008
 */
interface Factory {
	Simon getRootSimon();

	Simon getSimon(String name);

	Counter getCounter(String name);

	Stopwatch getStopwatch(String name);

	Simon getUnknown(String name);

	String generateName(String suffix, boolean includeMethodName);

	Collection<String> simonNames();

	/**
	 * Removes Simon from the factory. If Simon has some children it will be replaced
	 * by UnknownSimon.
	 *
	 * @param name name of the Simon
	 */
	void destroySimon(String name);

	void reset();
}
