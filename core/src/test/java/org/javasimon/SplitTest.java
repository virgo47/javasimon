package org.javasimon;

import org.javasimon.clock.ClockUtils;
import org.javasimon.clock.TestClock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link Split}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SplitTest extends SimonUnitTest {

	private static final String STOPWATCH_NAME = "org.javasimon.test-stopwatch";

	@Test
	public void issue10NPEInSplitToString() {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		SimonManager.getStopwatch("org").setState(SimonState.DISABLED, true);
		Split split = stopwatch.start();
		Assert.assertFalse(split.isEnabled());
		Assert.assertFalse(split.isRunning());
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
		TestClock clock = new TestClock();
		clock.setMillisNanosFollow(10);
		Split split = Split.start(clock);

		Assert.assertNull(split.getStopwatch());
		Assert.assertTrue(split.isEnabled());
		Assert.assertTrue(split.isRunning());
		Assert.assertEquals(split.getStart(), 10000000);
		Assert.assertEquals(split.getStartMillis(), 10);
		Assert.assertEquals(split.runningFor(), 0);

		clock.setMillisNanosFollow(20);
		long runningFor = split.runningFor();
		Assert.assertTrue(runningFor == 10 * ClockUtils.NANOS_IN_MILLIS);

		Assert.assertEquals(split.stop(), split);
		runningFor = split.runningFor();
		Assert.assertTrue(runningFor == 10 * ClockUtils.NANOS_IN_MILLIS);

		clock.setMillisNanosFollow(30);
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

	@Test
	public void stopWithSubSimonOnAnonymousIsHarmless() {
		Split split = Split.start();
		Assert.assertNull(split.getStopwatch());
		Split splitAfterStop = split.stop("subsimon");
		Assert.assertSame(split, splitAfterStop);
	}

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Simon name must match following pattern.*")
	public void stopWithSubSimonWithWrongName() {
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		Split split = stopwatch.start();
		split.stop("subs; asd imon");
	}

	@Test
	public void stopWithSubSimon() {
		String tag = "error";
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);

		Split split = stopwatch.start();
		String effectiveStopwatchName = STOPWATCH_NAME + Manager.HIERARCHY_DELIMITER + tag;
		Assert.assertNull(SimonManager.getSimon(effectiveStopwatchName));
		Assert.assertEquals(stopwatch.getActive(), 1);
		split.stop(tag);

		Stopwatch effectiveStopwatch = split.getAttribute(Split.ATTR_EFFECTIVE_STOPWATCH, Stopwatch.class);
		Assert.assertEquals(effectiveStopwatch.getName(), effectiveStopwatchName);
		Assert.assertSame(SimonManager.getStopwatch(effectiveStopwatchName), effectiveStopwatch);
		Assert.assertEquals(stopwatch.getCounter(), 0);
		Assert.assertEquals(effectiveStopwatch.getCounter(), 1);
	}

	@Test
	public void stopWithSubSimonDisbledMain() {
		String tag = "error";
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		stopwatch.setState(SimonState.DISABLED, true);
		Assert.assertEquals(stopwatch.getActive(), 0);

		Split split = stopwatch.start();
		Assert.assertEquals(stopwatch.getActive(), 0);
		String effectiveStopwatchName = STOPWATCH_NAME + Manager.HIERARCHY_DELIMITER + tag;
		Assert.assertNull(SimonManager.getSimon(effectiveStopwatchName));
		split.stop(tag);

		Assert.assertNull(SimonManager.getSimon(effectiveStopwatchName));
		Assert.assertNull(split.getAttribute(Split.ATTR_EFFECTIVE_STOPWATCH, Stopwatch.class));
		Assert.assertEquals(stopwatch.getCounter(), 0);
	}

	@Test
	public void stopWithDisbledSubSimon() {
		String tag = "error";
		Stopwatch stopwatch = SimonManager.getStopwatch(STOPWATCH_NAME);
		String effectiveStopwatchName = STOPWATCH_NAME + Manager.HIERARCHY_DELIMITER + tag;
		Stopwatch effectiveStopwatch = SimonManager.getStopwatch(effectiveStopwatchName);
		effectiveStopwatch.setState(SimonState.DISABLED, true);
		Assert.assertEquals(effectiveStopwatch.getCounter(), 0);
		Assert.assertEquals(stopwatch.getActive(), 0);

		Split split = stopwatch.start();
		Assert.assertEquals(stopwatch.getActive(), 1);
		Assert.assertEquals(effectiveStopwatch.getActive(), 0);
		split.stop(tag);

		Assert.assertEquals(stopwatch.getActive(), 0);
		Stopwatch effectiveStopwatchFromSplit = split.getAttribute(Split.ATTR_EFFECTIVE_STOPWATCH, Stopwatch.class);
		Assert.assertEquals(effectiveStopwatchFromSplit.getName(), effectiveStopwatchName);
		Assert.assertSame(SimonManager.getStopwatch(effectiveStopwatchName), effectiveStopwatchFromSplit);
		Assert.assertEquals(stopwatch.getCounter(), 0);
		Assert.assertEquals(effectiveStopwatch.getCounter(), 0);
	}
}
