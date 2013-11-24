package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.utils.SimonUtils;

/**
 * CallbackExample shows how to implement callback that prints out some information on the specific events.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class CallbackExample {
	/**
	 * Entry point to the Callback Example.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		SimonManager.callback().addCallback(new CallbackSkeleton() {
			@Override
			public void onStopwatchStart(Split split) {
				System.out.println("\nStopwatch " + split.getStopwatch().getName() + " has just been started.");
			}

			@Override
			public void onStopwatchStop(Split split, StopwatchSample sample) {
				System.out.println("Stopwatch " + split.getStopwatch().getName()
					+ " has just been stopped (" + SimonUtils.presentNanoTime(split.runningFor()) + ").");
			}
		});

		Stopwatch sw = SimonManager.getStopwatch(SimonUtils.generateName());
		sw.start().stop();

		Split split = sw.start();
		//noinspection StatementWithEmptyBody
		for (int i = 0; i < 1000000; i++) {
			// what does JVM do with empty loop? :-)))
		}
		split.stop();

		sw.start().stop();

		System.out.println("\nAdditional stop() does nothing, Split state is preserved.");
		split.stop();
		System.out.println("split = " + split);
	}
}
