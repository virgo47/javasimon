package org.javasimon.examples;

import org.javasimon.*;

/**
 * CallbackFilteringExample shows how filter works and how it can be set up programmaticaly.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Feb 12, 2009
 */
public final class CallbackFilteringExample {
	public static void main(String[] args) {
		// manager with two stopwatches is created
		Manager manager = new EnabledManager();
		Stopwatch sw1 = manager.getStopwatch("org.javasimon.examples.stopwatch1");
		Stopwatch sw2 = manager.getStopwatch("other.stopwatch2");

		// simple callback printing actions to the stdout is created and installed
		Callback stdoutCallback = new CallbackSkeleton() {
			public void stopwatchStart(Split split) {
				System.out.println("Starting " + split.getStopwatch().getName());
			}

			public void stopwatchStop(Split split) {
				System.out.println("Stopped " + split.getStopwatch().getName() + " (" + split.runningFor() + " ns)");
			}
		};
		manager.callback().addCallback(stdoutCallback);
		// prints start/stop for both stopwatches
		sw1.start().stop();
		sw2.start().stop();
		System.out.println();

		// filter callback is created
		CompositeFilterCallback filter = new CompositeFilterCallback();
		// rule to filter out all simons matching pattern "other.*" is added
		filter.addRule(FilterCallback.Rule.Type.MUST_NOT, null, "other.*");
		// original callback is added after this callback
		filter.addCallback(stdoutCallback);

		// filter callback is installed to the manager (with printing callback behind)
		manager.callback().addCallback(filter);

		// start/stop is printed only for sw1 because sw2 matches other.* pattern that is excluded (MUST_NOT)
		sw1.start().stop();
		sw2.start().stop();
	}
}