package org.javasimon.utils;

import org.javasimon.CallbackSkeleton;
import org.javasimon.Split;

/**
 * DebugCallback.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Feb 7, 2009
 */
public final class DebugCallback extends CallbackSkeleton {
	public void stopwatchStart(Split split) {
		System.out.println("Start split: " + split);
	}

	public void stopwatchStop(Split split) {
		System.out.println("Stop split: " + split);
	}
}
