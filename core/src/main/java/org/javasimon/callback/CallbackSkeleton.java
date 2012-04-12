package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

/**
 * Implements {@link Callback} interface so that it does nothing - intended for extension by simple (non-composite)
 * callbacks. This class is to be subclassed when just a few methods need to be implemented instead of the whole Callback interface.
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
	public void onStopwatchAdd(Stopwatch stopwatch, long ns, StopwatchSample sample) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
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
	public void onStopwatchStop(Split split, StopwatchSample sample) {
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
	public void onCounterDecrease(Counter counter, long dec, CounterSample sample) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCounterSet(Counter counter, long val, CounterSample sample) {
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
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerMessage(String message) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onManagerWarning(String warning, Exception cause) {
	}
}
