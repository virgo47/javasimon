package org.javasimon;

import org.javasimon.callback.Callback;

import java.util.Collection;
import java.util.List;
import java.util.Collections;

/**
 * DisabledManager implements methods called from SimonManager to do nothing or return NullSimon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class DisabledManager implements Manager {
	/**
	 * Returns "Null Simon" that always returns empty/null values and cannot measure anything.
	 * Null Simon returned by this method is neither {@link Stopwatch} nor {@link Counter}.
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
	 * Returns "Null Counter" that always returns empty/null values and cannot measure anything.
	 */
	@Override
	public Counter getCounter(String name) {
		return NullCounter.INSTANCE;
	}

	/**
	 * Returns "Null Stopwatch" that always returns empty/null values and cannot measure anything.
	 */
	@Override
	public Stopwatch getStopwatch(String name) {
		return NullStopwatch.INSTANCE;
	}

	/**
	 * Returns "Null Simon" that always returns empty/null values and cannot measure anything.
	 * Null Simon returned by this method is neither {@link Stopwatch} nor {@link Counter}.
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
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getSimonNames() {
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Simon> getSimons(String pattern) {
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
