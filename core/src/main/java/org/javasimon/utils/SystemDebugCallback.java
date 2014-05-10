package org.javasimon.utils;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;

/**
 * SystemDebugCallback just prints operations on the standard output, warning is sent to error output.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SystemDebugCallback extends CallbackSkeleton {
	public static final String DEBUG_PREFIX = "SIMON DEBUG: ";

	@Override
	public void onStopwatchStart(Split split) {
		out("Start split: " + split);
	}

	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		out("Stopwatch stop (" + split + "): " + sample.simonToString());
	}

	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
		out("Stopwatch add (" + split + "): " + sample.simonToString());
	}

	@Override
	public void onCounterDecrease(Counter counter, long dec, CounterSample sample) {
		out("Counter decrease: " + sample.simonToString());
	}

	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
		out("Counter increase: " + sample.simonToString());
	}

	@Override
	public void onCounterSet(Counter counter, long val, CounterSample sample) {
		out("Counter set: " + sample.simonToString());
	}

	@Override
	public void onSimonCreated(Simon simon) {
		out("Simon created: " + simon);
	}

	@Override
	public void onSimonDestroyed(Simon simon) {
		out("Simon destroyed: " + simon);
	}

	@Override
	public void onManagerClear() {
		out("Manager clear");
	}

	@Override
	public void onManagerMessage(String message) {
		out("Simon message: " + message);
	}

	private void out(String message) {
		System.out.println(DEBUG_PREFIX + message);
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
			System.err.println(DEBUG_PREFIX + "Simon warning: " + warning);
		}
		if (cause != null) {
			System.err.print(DEBUG_PREFIX);
			cause.printStackTrace();
		}
	}
}
