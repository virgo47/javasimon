package org.javasimon;

import java.util.Collection;
import java.util.Collections;

/**
 * DisabledManager implements methods called from SimonManager to do nothing or return NullSimon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 16, 2008
 */
class DisabledManager implements Manager {
	static final Manager INSTANCE = new DisabledManager();

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

	public Stopwatch getStopwatch(String name, Stopwatch.Type impl) {
		return NullSimon.INSTANCE;
	}

	public UnknownSimon getUnknown(String name) {
		throw new UnsupportedOperationException("Disabled manager does not support creation of the UnknownSimon.");
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
