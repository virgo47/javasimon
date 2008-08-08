package org.javasimon.customsimons;

import org.javasimon.AbstractSimon;
import org.javasimon.Simon;
import org.javasimon.AbstractDisabledSimon;

/**
 * MySimon - demonstration of the custom simon. MySimon only measures time and it's not threadsafe.
 * It has one start method and one stop method that returns the delta. No cumulative state is stored.
 * <p/>
 * You may ask why there must be an interface - actually there need not to be any interface (see
 * MySimonSingleClass for an alternative) but interface allows to provide special class for disabled
 * version that can eliminate most of the overhead for some more complex simons (this one is not
 * complex). Otherwise there are checks if simon is enabled in every action method.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
 */
public final class MySimonImpl extends AbstractSimon implements MySimon {
	private long start;

	public MySimonImpl(String name) {
		super(name);
	}

	public void start() {
		start = System.nanoTime();
	}

	public long stop() {
		return System.nanoTime() - start;
	}

	public void reset() {
	}

	public Simon getDisabledDecorator() {
		return null;
	}
}

class DisabledCounter extends AbstractDisabledSimon implements MySimon {
	public DisabledCounter(Simon simon) {
		super(simon);
	}

	public void start() {
	}

	public long stop() {
		return 0;
	}
}

final class MySimonImplTest {
	private static final int LOOP = 100;
	private static final int SLEEP = 10;

	private MySimonImplTest() {
	}

	public static void main(String[] args) throws InterruptedException {
		long min = Long.MAX_VALUE;
		long max = 0;
		long total = 0;

		MySimonImpl simon = new MySimonImpl(null);
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
