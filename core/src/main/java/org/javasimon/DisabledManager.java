package org.javasimon;

import java.util.List;
import java.util.Collections;

/**
 * DisabledManager implements methods called from SimonManager to do nothing or return NullSimon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class DisabledManager implements Manager {
	/**
	 * {@inheritDoc}
	 */
	public Simon getSimon(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySimon(String name) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Callback callback() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagerConfiguration configuration() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter getCounter(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch getStopwatch(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getRootSimon() {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> simonNames() {
		return Collections.emptyList();
	}

	/**
	 * Throws UnsupportedOperationException.
	 */
	public void enable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/**
	 * Throws UnsupportedOperationException.
	 */
	public void disable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/**
	 * Returns false.
	 *
	 * @return false
	 */
	public boolean isEnabled() {
		return false;
	}
}
