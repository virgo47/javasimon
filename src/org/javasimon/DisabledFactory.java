package org.javasimon;

import java.util.Collection;
import java.util.Collections;

/**
 * DisabledFactory implements methods called from SimonFactory to do nothing or return NullSimon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 16, 2008
 */
class DisabledFactory implements Factory {
	static final Factory INSTANCE = new DisabledFactory();

	public Simon getSimon(String name) {
		return NullSimon.INSTANCE;
	}

	public void destroySimon(String name) {
	}

	public void reset() {
	}

	public Counter getCounter(String name) {
		return NullSimon.INSTANCE;
	}

	public Stopwatch getStopwatch(String name) {
		return NullSimon.INSTANCE;
	}

	public String generateName(String suffix, boolean includeMethodName) {
		return null;
	}

	public Simon getRootSimon() {
		return NullSimon.INSTANCE;
	}

	public Collection<String> simonNames() {
		return Collections.emptySet();
	}
}
