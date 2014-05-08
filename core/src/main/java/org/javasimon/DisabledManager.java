package org.javasimon;

import org.javasimon.callback.CompositeCallback;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link Manager} implementation that does nothing or returns {@code null} or {@link NullSimon} as expected
 * from manager in disabled state. Does not support {@link #enable()}/{@link #disable()} - for this
 * use {@link SwitchingManager}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class DisabledManager implements Manager {

	/**
	 * Returns "Null Simon" that always returns empty/null values and cannot measure anything.
	 * Null Simon returned by this method is neither {@link Stopwatch} nor {@link Counter}.
	 *
	 * @param name ignored
	 * @return null Simon
	 */
	@Override
	public Simon getSimon(String name) {
		return NullSimon.INSTANCE;
	}

	@Override
	public void destroySimon(String name) {
	}

	@Override
	public void clear() {
	}

	@Override
	public CompositeCallback callback() {
		return null;
	}

	@Override
	public ManagerConfiguration configuration() {
		return null;
	}

	/**
	 * Returns "Null Counter" that always returns empty/null values and cannot measure anything.
	 *
	 * @param name ignored
	 * @return null Counter
	 */
	@Override
	public Counter getCounter(String name) {
		return NullCounter.INSTANCE;
	}

	/**
	 * Returns "Null Stopwatch" that always returns empty/null values and cannot measure anything.
	 *
	 * @param name ignored
	 * @return null Stopwatch
	 */
	@Override
	public Stopwatch getStopwatch(String name) {
		return NullStopwatch.INSTANCE;
	}

	/**
	 * Returns "Null Simon" that always returns empty/null values and cannot measure anything.
	 * Null Simon returned by this method is neither {@link Stopwatch} nor {@link Counter}.
	 *
	 * @return null Simon
	 */
	@Override
	public Simon getRootSimon() {
		return NullSimon.INSTANCE;
	}

	@Override
	public Collection<String> getSimonNames() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Simon> getSimons(SimonFilter simonFilter) {
		return Collections.emptyList();
	}

	/** Throws {@link UnsupportedOperationException}. */
	@Override
	public void enable() {
		throw new UnsupportedOperationException("Only SwitchingManager supports this operation.");
	}

	/** Throws {@link UnsupportedOperationException}. */
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

	@Override
	public long nanoTime() {
		return 0;
	}

	@Override
	public long milliTime() {
		return 0;
	}

	@Override
	public long millisForNano(long nanos) {
		return 0;
	}
}
