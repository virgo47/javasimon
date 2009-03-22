package org.javasimon.testapp;

import org.javasimon.testapp.test.Timer;

import java.util.Random;

/**
 * Class UniformRandomTimer.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $ Date: $
 * @created 19.3.2009 13:13:38
 * @since 2.0
 */
public class UniformRandomTimer implements Timer {

	private final Random random = new Random();

	private long range;
	private long delay;

	public UniformRandomTimer(long range, long delay) {
		this.range = range;
		this.delay = delay;
	}

	public long getRange() {
		return range;
	}

	public long getDelay() {
		return delay;
	}

	public long delay() {
		return (long) Math.abs((this.random.nextDouble() * getRange()) + getDelay());
	}
}
