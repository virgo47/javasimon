package org.javasimon.utils;

import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.Split;

/**
 * DebugCallback just prints start and stop operations on Stopwatches/Splits.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class DebugCallback extends CallbackSkeleton {
	/**
	 * Prints split at the start of the split.
	 *
	 * @param split started split
	 */
	public void onStopwatchStart(Split split) {
		System.out.println("Start split: " + split);
	}

	/**
	 * Prints split at the stop of the split.
	 *
	 * @param split stopped split
	 * @param sample
	 */
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		System.out.println("Stop split: " + split);
	}
}
