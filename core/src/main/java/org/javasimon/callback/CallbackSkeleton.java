package org.javasimon.callback;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Manager;
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
	@Override
	public void initialize(Manager manager) {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
	}

	@Override
	public void onStopwatchStart(Split split) {
	}

	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
	}

	@Override
	public void onCounterDecrease(Counter counter, long dec, CounterSample sample) {
	}

	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
	}

	@Override
	public void onCounterSet(Counter counter, long val, CounterSample sample) {
	}

	@Override
	public void onSimonCreated(Simon simon) {
	}

	@Override
	public void onSimonDestroyed(Simon simon) {
	}

	@Override
	public void onManagerClear() {
	}

	@Override
	public void onManagerMessage(String message) {
	}

	@Override
	public void onManagerWarning(String warning, Exception cause) {
	}
}
