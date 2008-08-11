package org.javasimon;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * StopwatchTest.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 11, 2008
 */
public final class StopwatchTest {
	public static final String STOPWATCH_NAME = "org.javasimon.test-stopwatch";

	@BeforeMethod
	public void resetAndEnable() {
		SimonFactory.reset();
		SimonFactory.enable();
	}

	@Test
	public void basicStopwatchTest() {
		Stopwatch stopwatch = SimonFactory.getStopwatch(STOPWATCH_NAME).start();
		stopwatch.stop();
		Assert.assertTrue(stopwatch.getTotal() >= 0);
		Assert.assertEquals(stopwatch.getCounter(), 1);
		Assert.assertEquals(stopwatch.getMax(), stopwatch.getTotal());
		Assert.assertEquals(stopwatch.getMin(), stopwatch.getTotal());
	}
}
