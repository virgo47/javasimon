package org.javasimon.utils;

import org.javasimon.CallbackSkeleton;
import org.javasimon.Split;

/**
 * DebugCallback just prints start and stop operations on Stopwatches/Splits.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Feb 7, 2009
 */
public final class DebugCallback extends CallbackSkeleton {
	/**
	 * Prints split at the start of the split.
	 *
	 * @param split started split
	 */
	public void stopwatchStart(Split split) {
		System.out.println("Start split: " + split);
	}

	/**
	 * Prints split at the stop of the split.
	 *
	 * @param split stopped split
	 */
	public void stopwatchStop(Split split) {
		System.out.println("Stop split: " + split);
	}
}
