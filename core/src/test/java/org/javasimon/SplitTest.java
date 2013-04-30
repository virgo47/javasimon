package org.javasimon;

import org.javasimon.utils.SimonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link Split}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SplitTest {
	private static final String STOPWATCH_NAME = "org.javasimon.test-stopwatch";

	@BeforeMethod
	public void resetAndEnable() {
		SimonManager.clear();
		SimonManager.enable();
	}

	@Test
	public void issue10NPEInSplitToString() {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		SimonManager.getStopwatch("org").setState(SimonState.DISABLED, true);
		Split split = stopwatch.start();
		split.stop();
		Assert.assertTrue(split.toString().startsWith("Split created from disabled Stopwatch"));
	}

	@Test
	public void toStringForAnonymousSplit() {
		Split split = Split.start();
		Assert.assertTrue(split.toString().startsWith("Running split"));
		split.stop();
		Assert.assertTrue(split.toString().startsWith("Stopped split"));
	}

	@Test
	public void anonymousSplitTest() throws InterruptedException {
		Split split = Split.start();
		Assert.assertNull(split.getStopwatch());
		Assert.assertTrue(split.isEnabled());
		Assert.assertTrue(split.getStart() > 0);
		Assert.assertTrue(split.runningFor() >= 0);

		Thread.sleep(10);
		long runningFor = split.runningFor();
		Assert.assertTrue(runningFor >= 9 * SimonUtils.NANOS_IN_MILLIS, "Unexpectedly short running for: " + runningFor);

		Assert.assertEquals(split.stop(), split);
		runningFor = split.runningFor();
		Assert.assertTrue(runningFor >= 9 * SimonUtils.NANOS_IN_MILLIS, "Unexpectedly short running for");
		Thread.sleep(10);
		Assert.assertEquals(runningFor, split.runningFor());
	}

	@Test
	public void disabledManagerTest() {
		SimonManager.disable();
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		Assert.assertEquals(stopwatch, NullStopwatch.INSTANCE);
		Split split = stopwatch.start();
		Assert.assertFalse(split.isEnabled());
		Assert.assertEquals(split.getStart(), 0);
		Assert.assertTrue(split.toString().startsWith("Split created from disabled Stopwatch"));
	}
}
