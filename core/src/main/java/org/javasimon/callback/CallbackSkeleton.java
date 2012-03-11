package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Implements {@link Callback} interface so that it does nothing - intended for extension by simple (non-composite)
 * callbacks. Message prints to stdout and warning prints to stderr, other event methods do nothing. This class is
 * to be subclassed when just a few methods need to be implemented instead of the whole Callback interface.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class CallbackSkeleton implements Callback {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cleanup() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, long ns) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchStart(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchStop(Split split) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimonReset(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterDecrease(Counter counter, long dec) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterIncrease(Counter counter, long inc) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterSet(Counter counter, long val) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimonCreated(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimonDestroyed(Simon simon) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerClear() {
	}

	/**
	 * Prints the message to the standard output.
	 *
	 * @param message message text
	 */
	@Override
	public void onManagerMessage(String message) {
		System.out.println("Simon message: " + message);
	}

	/**
	 * Warning and stack trace are print out to the error output. Either cause or warning
	 * (or both) should be provided otherwise the method does nothing.
	 * <p/>
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerWarning(String warning, Exception cause) {
		if (warning != null) {
			System.err.println("Simon warning: " + warning);
		}
		if (cause != null) {
			cause.printStackTrace();
		}
	}
}
