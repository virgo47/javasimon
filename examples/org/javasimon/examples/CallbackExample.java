package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.CallbackSkeleton;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * CallbackExample shows how to implement callback that prints out some information
 * on the specific events.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 22, 2009
 */
public final class CallbackExample {
	public static void main(String[] args) {
		SimonManager.installCallback(new CallbackSkeleton() {
			public void stopwatchStart(Split split) {
				System.out.println("Stopwatch " + split.getStopwatch().getName()
					+ " has just been started.");
			}

			public void stopwatchStop(Split split) {
				System.out.println("Stopwatch " + split.getStopwatch().getName()
					+ " has just been stopped (" + SimonUtils.presentNanoTime(split.runningFor()) + ").");
			}
		});

		Stopwatch sw = SimonManager.getStopwatch(SimonUtils.generateName(null, false));
		sw.start().stop();

		Split split = sw.start();
		for (int i = 0; i < 1000000; i++) {
			// what does JVM do with empty loop? :-)))
		}
		split.stop();

		System.out.println("\"Illegal\" stop is ignored, hope you like it (and it returns 0).");
		split.stop();
	}
}
