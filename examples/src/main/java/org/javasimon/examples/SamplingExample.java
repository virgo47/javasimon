package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.Split;

/**
 * SamplingExample uses one stopwatch to measure one method (random duration) and this stopwatch is sampled
 * with 10s period. Try it with reset/no-reset.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
@SuppressWarnings({"InfiniteLoopStatement"})
public final class SamplingExample {
	private SamplingExample() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// Starts the sampler
		Sampler sampler = new Sampler();
		sampler.setDaemon(true);
		sampler.start();

		// Starts the measuring
		while (true) {
			Split split = SimonManager.getStopwatch("sampled-stopwatch").start();
			ExampleUtils.waitRandomly(50);
			split.stop();
		}
	}

	/**
	 * Prints sampled values from the stopwatch every 10 seconds + resets the Simon.
	 */
	static class Sampler extends Thread {
		/**
		 * Method implementing the code of the thread.
		 */
		public void run() {
			while (true) {
				Stopwatch stopwatch = SimonManager.getStopwatch("sampled-stopwatch");
				System.out.println("\nstopwatch = " + stopwatch);
				System.out.println("Stopwatch sample: " + stopwatch.sample());
				// uncomment this if you want reset - of course comment the line above
//				System.out.println("Stopwatch sample: " + stopwatch.sampleAndReset());
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
