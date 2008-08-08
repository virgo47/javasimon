package org.javasimon.customsimons;

import org.javasimon.AbstractSimonWithDisabledBehavior;

/**
 * MySimonSingleClass - version of MySimon without interface and disabled decorator. This
 * style utilizes observeDisabledSimonReturned and observeEnabledSimonReturned methods.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
 */
public final class MySimonSingleClass extends AbstractSimonWithDisabledBehavior {
	private long start;

	public MySimonSingleClass(String name) {
		super(name);
	}

	public void start() {
		if (enabled) {
			start = System.nanoTime();
		}
	}

	public long stop() {
		if (enabled) {
			return System.nanoTime() - start;
		}
		return 0;
	}

	public void reset() {
	}
}

final class MySimonSingleClassTest {
	private static final int LOOP = 100;
	private static final int SLEEP = 10;

	private MySimonSingleClassTest() {
	}

	public static void main(String[] args) throws InterruptedException {
		long min = Long.MAX_VALUE;
		long max = 0;
		long total = 0;

		MySimonSingleClass simon = new MySimonSingleClass(null);
		for (int i = 0; i < LOOP; i++) {
			simon.start();
			Thread.sleep(SLEEP);
			long val = simon.stop();
			if (val > max) {
				max = val;
			}
			if (val < min) {
				min = val;
			}
			total += val;
		}
		System.out.println("total = " + total);
		System.out.println("min = " + min);
		System.out.println("max = " + max);
	}
}
