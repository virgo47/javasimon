package org.javasimon.examples;

import org.javasimon.SimonFactory;
import org.javasimon.Stopwatch;
import org.javasimon.StatProcessorType;

/**
 * SamplingExample uses one stopwatch to measure one method (random duration) and this stopwatch is sampled
 * with 10s period. Try it with reset/no-reset.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 4, 2008
 */
public final class SamplingExample {
	private static final boolean RESET = true;

	public static void main(String[] args) {
		// Setting richer stat processor
		SimonFactory.getStopwatch("sampled-stopwatch").setStatProcessor(StatProcessorType.BASIC.create());

		// Starts the sampler
		Sampler sampler = new Sampler();
		sampler.setDaemon(true);
		sampler.start();

		// Starts the measuring
		while (true) {
			callStrangeMethod();
		}
	}

	/**
	 * Method that lasts randomly from ~0 to ~2500 ms.
	 */
	private static void callStrangeMethod() {
		Stopwatch stopwatch = SimonFactory.getStopwatch("sampled-stopwatch").start();
		long random = (long) (Math.random() * 50);
		try {
			Thread.sleep(random * random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopwatch.stop();
	}

	/**
	 * Prints sampled values from the stopwatch (and its stat processor) every 10 seconds + resets the Simon.
	 */
	static class Sampler extends Thread {
		public void run() {
			while (true) {
				Stopwatch stopwatch = SimonFactory.getStopwatch("sampled-stopwatch");
				System.out.println("\nstopwatch = " + stopwatch);
				System.out.println("Stopwatch sample: " + stopwatch.sample(RESET));
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
