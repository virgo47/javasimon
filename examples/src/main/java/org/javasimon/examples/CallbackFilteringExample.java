package org.javasimon.examples;

import org.javasimon.EnabledManager;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.Callback;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.CompositeFilterCallback;
import org.javasimon.callback.FilterRule;
import org.javasimon.utils.SimonUtils;

/**
 * CallbackFilteringExample shows how filter works and how it can be set up programmaticaly.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class CallbackFilteringExample {
	/**
	 * Entry point to the Callback Filtering Example.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		// manager with two stopwatches is created
		Manager manager = new EnabledManager();
		Stopwatch sw1 = manager.getStopwatch("org.javasimon.examples.stopwatch1");
		Stopwatch sw2 = manager.getStopwatch("other.stopwatch2");

		// simple callback printing actions to the stdout is created and installed
		Callback stdoutCallback = new CallbackSkeleton() {
			@Override
			public void onStopwatchStart(Split split) {
				System.out.println("Starting " + split.getStopwatch().getName());
			}

			@Override
			public void onStopwatchStop(Split split, StopwatchSample sample) {
				System.out.println("Stopped " + split.getStopwatch().getName() + " (" + SimonUtils.presentNanoTime(split.runningFor()) + ")");
			}
		};
		manager.callback().addCallback(stdoutCallback);

		// prints start/stop for both stopwatches
		sw1.start().stop();
		sw2.start().stop();
		System.out.println();

		// we need to remove old callback
		manager.callback().removeCallback(stdoutCallback);
		// alternatively you can call this if you want to remove all callbacks
		SimonManager.callback().removeAllCallbacks();

		// filter callback is created
		CompositeFilterCallback filter = new CompositeFilterCallback();
		// rule to filter out all Simons matching pattern "other.*" is added
		filter.addRule(FilterRule.Type.MUST_NOT, null, "other.*");
		// original callback is added after this callback
		filter.addCallback(stdoutCallback);

		// filter callback is installed to the manager (with printing callback behind)
		manager.callback().addCallback(filter);

		// start/stop is printed only for sw1 because sw2 matches other.* pattern that is excluded (MUST_NOT)
		sw1.start().stop();
		sw2.start().stop();
	}
}