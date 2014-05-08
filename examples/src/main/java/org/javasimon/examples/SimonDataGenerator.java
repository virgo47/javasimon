package org.javasimon.examples;

import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.clock.ClockUtils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fills Simon manager with random data for demo purpose
 */
public class SimonDataGenerator {

	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private final Random random = new Random();
	private Lock lock = new ReentrantLock();
	/** Time for changing Simons. */
	private Timer timer = new Timer();

	/** Generate random int between min and max. */
	public int randomInt(int min, int max) {
		return min + random.nextInt(max - min);
	}

	/** Generate random long between min and max. */
	public long randomLong(long min, long max) {
		return randomInt((int) min, (int) max);
	}

	/**
	 * Wait random time.
	 *
	 * @return Waited random time
	 */
	public long randomWait(long min, long max) {
		final long waitTime = randomLong(min, max);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException interruptedException) {
			// ignored
		}
		return waitTime;
	}

	private static void addTime(String name, long sleep) {
		Stopwatch stopwatch = SimonManager.manager().getStopwatch(name);
		stopwatch.addSplit(Split.create(sleep * ClockUtils.NANOS_IN_MILLIS));
	}


	private static void addCounter(String name, long value) {
		Counter counter = SimonManager.manager().getCounter(name);
		counter.set(value);
	}

	/**
	 * Add basic simons A, B, C and X.
	 * X is used to test Counter rendering.
	 */
	public void addDefaultSimons() {
		addTime("A", 100);
		addTime("B", 200);
		addTime("C", 300);
		addTime("A", 200);
		addTime("A", 300);
		addTime("B", 100);
		addCounter("X", 1L);
		addCounter("X", 4L);
		addCounter("X", 2L);
		addStopwatchSplits(SimonManager.getStopwatch("$Proxy"), 5);
	}

	/**
	 * Starts a timer which changes Simons values.
	 * TL is used to test Timeline and Quantiles plugins rendering.
	 */
	public void addChangingSimons() {
		timer.schedule(new TimerTask() {
			final Stopwatch tlStopwatch = SimonManager.getStopwatch("TL");

			@Override
			public void run() {
				try {
					lock.lock();
					System.out.println("TL " + addStopwatchSplit(tlStopwatch));
				} finally {
					lock.unlock();
				}
			}
		}, 0, 10000L);
	}

	/**
	 * Add many Simons for performances testing.
	 * Z.* Simons are used for performance testing.
	 */
	public void addManySimons() {
		new Thread() {
			@Override
			public void run() {
				addManySimons("Z", 4, 3, 3, 6);
			}
		}.start();
	}


	/** Recursively Add many Simons for performances testing. */
	private void addManySimons(String prefix, int depth, int groupWidth, int leafWidth, int splitWidth) {
		if (depth == 0) {
			// Generate Simons of type Stopwatch
			final int sibblings = randomInt(Math.min(1, groupWidth / 2), leafWidth);
			for (int i = 0; i < sibblings; i++) {
				String name = prefix + "." + ALPHABET.charAt(i);
				addStopwatchSplits(SimonManager.getStopwatch(name), splitWidth);
			}
		} else {
			// Generate Simons of type Unknown
			final int sibblings = randomInt(Math.min(1, groupWidth / 2), groupWidth);
			for (int i = 0; i < sibblings; i++) {
				String name = prefix + "." + ALPHABET.charAt(i);
				addManySimons(name, depth - 1, groupWidth, leafWidth, splitWidth);
			}
		}
	}

	/** Generate a split for a Stopwatch. */
	private long addStopwatchSplit(Stopwatch stopwatch) {
		Split split = stopwatch.start();
		try {
			randomWait(50, 150);
		} finally {
			split.stop();
		}
		return split.runningFor() / ClockUtils.NANOS_IN_MILLIS;
	}

	/**
	 * Generate a Simon of type "Stopwatch" and fill it with some Splits.
	 *
	 * @param splitWidth Max number of splits per Stopwatch
	 */
	private void addStopwatchSplits(Stopwatch stopwatch, int splitWidth) {
		final int splits = randomInt(splitWidth / 2, splitWidth);
		try {
			lock.lock();
			System.out.print(stopwatch.getName() + " " + splits + ": ");
			for (int i = 0; i < splits; i++) {
				System.out.print(addStopwatchSplit(stopwatch) + ",");
			}
			System.out.println();
		} finally {
			lock.unlock();
		}
	}

	/** Stacked stopwatches to test call tree. */
	public void addStackedSimons() {
		Split splitA = SimonManager.getStopwatch("Y.A").start();
		Split splitB = SimonManager.getStopwatch("Y.B").start();
		addStopwatchSplits(SimonManager.getStopwatch("Y.C"), 6);
		splitB.stop();
		Split splitD = SimonManager.getStopwatch("Y.D").start();
		randomWait(100, 250);
		randomWait(100, 250);
		splitD.stop();
		splitA.stop();
	}

}
