package org.javasimon;

import java.util.Collections;
import java.util.List;

/**
 * EmptyCallback implements Callback interface so that it does nothing.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 22, 2009
 */
public class CallbackSkeleton implements Callback {
	/**
	 * Returns empty list.
	 *
	 * @return empty list
	 */
	public List<Callback> callbacks() {
		return Collections.emptyList();
	}

	/**
	 * Throws UnsupportedOperationException.
	 *
	 * @param callback ignored
	 */
	public void addCallback(Callback callback) {
		throw new UnsupportedOperationException("Only CompositeCallback implements this method.");
	}

	/**
	 * Throws UnsupportedOperationException.
	 *
	 * @param callback ignored
	 */
	public void removeCallback(Callback callback) {
		throw new UnsupportedOperationException("Only CompositeCallback implements this method.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void deactivate() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchAdd(Stopwatch stopwatch, long ns) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStart(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterDecrease(Counter counter, long dec) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterIncrease(Counter counter, long inc) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterSet(Counter counter, long val) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void simonCreated(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void simonDestroyed(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
	}

	/**
	 * Prints the message to the standard output.
	 *
	 * @param message message text
	 */
	public void message(String message) {
		System.out.println("Simon message: " + message);
	}

	/**
	 * Warning and stack trace are print out to the error output.
	 *
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		System.err.println("Simon warning: " + warning);
		if (cause != null) {
			cause.printStackTrace();
		}
	}
}
