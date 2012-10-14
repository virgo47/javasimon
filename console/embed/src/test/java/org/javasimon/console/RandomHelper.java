package org.javasimon.console;

import java.util.Random;

/**
 * Random utils
 */
public class RandomHelper {
    private final Random random=new Random();
    /**
     * Generate random int between min and max
     */
	public int randomInt(int min, int max) {
		return min+random.nextInt(max-min);
	}
    /**
     * Generate random long between min and max
     */
	public long randomLong(long min, long max) {
		return randomInt((int) min, (int) max);
	}
	/**
	 * Wait random time
	 * @return Waited random time
	 */
	public long randomWait(long min, long max) {
		final long waitTime=randomLong(min, max);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException interruptedException) {
		}
		return waitTime;
	}

}
