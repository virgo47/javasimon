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
	@Override
	public Simon getSimon(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroySimon(String name) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Callback callback() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagerConfiguration configuration() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Counter getCounter(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stopwatch getStopwatch(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Simon getRootSimon() {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> simonNames() {
		return Collections.emptyList();
	}

	/**
	 * Throws UnsupportedOperationException.
	 */
	@Override
	public void enable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/**
	 * Throws UnsupportedOperationException.
	 */
	@Override
	public void disable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/**
	 * Returns false.
	 *
	 * @return false
	 */
	@Override
	public boolean isEnabled() {
		return false;
	}

	/**
	 * Does nothing here.
	 *
	 * @param message not used
	 */
	@Override
	public void message(String message) {
	}

	/**
	 * Does nothing here.
	 *
	 * @param message not used
	 * @param cause not used
	 */
	@Override
	public void warning(String message, Exception cause) {
	}
}
