package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import java.util.Collections;
import java.util.List;

/**
 * EmptyCallback implements Callback interface so that it does nothing. Message prints
 * to stdout and warning prints to stderr. Methods for composite callbacks are final and
 * throws unsupported operation, {@link #callbacks()} returns empty list. Should be used
 * only for implementing simple callbacks, most useful when few methods need to be
 * implemented.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class CallbackSkeleton implements Callback {
	/**
	 * Returns empty list.
	 *
	 * @return empty list
	 */
	public final List<Callback> callbacks() {
		return Collections.emptyList();
	}

	/**
	 * Throws UnsupportedOperationException.
	 *
	 * @param callback ignored
	 */
	public final void addCallback(Callback callback) {
		throw new UnsupportedOperationException("Only CompositeCallback implements this method.");
	}

	/**
	 * Throws UnsupportedOperationException.
	 *
	 * @param callback ignored
	 */
	public final void removeCallback(Callback callback) {
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
	public void cleanup() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchAdd(Stopwatch stopwatch, long ns) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchAdd(Stopwatch stopwatch, Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchStart(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStopwatchStop(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSimonReset(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCounterDecrease(Counter counter, long dec) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCounterIncrease(Counter counter, long inc) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCounterSet(Counter counter, long val) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSimonCreated(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSimonDestroyed(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onManagerClear() {
	}

	/**
	 * Prints the message to the standard output.
	 *
	 * @param message message text
	 */
	public void onManagerMessage(String message) {
		System.out.println("Simon message: " + message);
	}

	/**
	 * Warning and stack trace are print out to the error output. Either cause or warning
	 * (or both) should be provided otherwise the method does nothing.
	 * <p/>
	 * {@inheritDoc}
	 */
	public void onManagerWarning(String warning, Exception cause) {
		if (warning != null) {
			System.err.println("Simon warning: " + warning);
		}
		if (cause != null) {
			cause.printStackTrace();
		}
	}
}
