package org.javasimon.examples;

import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.utils.SimonUtils;

/**
 * HelloUniverse is just a bit more than {@link HelloWorld}. Example shows usage of an utility method
 * that can run a block of code inside the stopwatch. This time {@link org.javasimon.Counter} is introduced,
 * both sample methods are used and note is shown too.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.0
 */
public final class HelloUniverse {

	private HelloUniverse() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SimonUtils.doWithStopwatch("stopwatch", new Runnable() {
			@Override
			public void run() {
				Counter counter = SimonManager.getCounter("counter");
				counter.setNote("Just a poor counter");
				System.out.println("Let's count too: " + counter);
				counter.increase();
				System.out.println("After increase: " + counter);
				counter.decrease(5);
				System.out.println("After decrease 5: " + counter);
				counter.set(7);
				System.out.println("After set 7: " + counter);
				System.out.println("\nSample from the counter: " + counter.sample());
			}
		});

		// here you don't need specific stopwatch interface, so we may use getSimon instead of getStopwatch
		// mind though that getSimon will NOT create non-existent Simons, but returns null instead
		System.out.println("\nHello universe, here is your stopwatch: " + SimonManager.getSimon("stopwatch"));
		System.out.println("\nAnd here is its sample: " + SimonManager.getSimon("stopwatch").sample());

		SimonManager.getSimon("stopwatch").setNote("stopwatch wants some note too! ...after all to have a note is the basic right of any Simon!");
		System.out.println("\nStopwatch with note: " + SimonManager.getSimon("stopwatch"));
	}
}
