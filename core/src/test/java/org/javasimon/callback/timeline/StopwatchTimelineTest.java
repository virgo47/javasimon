package org.javasimon.callback.timeline;

import org.javasimon.EnabledManager;
import org.javasimon.Manager;
import org.javasimon.SimonUnitTest;
import org.javasimon.Stopwatch;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.javasimon.callback.timeline.TimeUtil.createSplit;
import static org.javasimon.callback.timeline.TimeUtil.createTimestamp;
import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link StopwatchTimeline} and {@link Timeline}
 *
 * @author gerald
 */
public class StopwatchTimelineTest extends SimonUnitTest {

	@Test
	public void testAddSplit() {
		// 10 ranges of 5 minutes
		StopwatchTimeline timeline = new StopwatchTimeline(10, 5 * 60 * 1000);
		// First range 21:50 to 21:55
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 51, 12), 100));
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 52, 13), 200));
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 53, 14), 300));
		// Next range 21:55 to 22:00
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 56, 15), 100));
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 57, 16), 200));
		// First range again
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 54, 17), 200));
		// Forgotten range (too late)
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 44, 18), 400));
		// Next range 22:00 to 22:05
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 22, 1, 19), 100));
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 22, 2, 20), 200));
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 22, 3, 21), 200));
		// First range again
		timeline.addSplit(createSplit(createTimestamp(2012, 7, 12, 21, 54, 22), 250));
		// Date is ready, let's check the result
		StopwatchTimeRange[] timeRanges = timeline.sample().getTimeRanges();
		for (StopwatchTimeRange timeRange : timeRanges) {
			System.out.println(timeRange.toString());
		}
		assertEquals(timeRanges.length, 3);
		assertEquals(timeRanges[0].getCounter(), 5);
		assertEquals(timeRanges[1].getCounter(), 2);
		assertEquals(timeRanges[2].getCounter(), 3);
	}

	@Test
	public void issue113() {
		Manager manager = new EnabledManager();
		Stopwatch before = manager.getStopwatch("before");
		manager.callback().addCallback(new TimelineCallback());
		Stopwatch after = manager.getStopwatch("after");
		Assert.assertNotNull(after.getAttribute(TimelineCallback.TIMELINE_ATTRIBUTE_NAME));
		Assert.assertNull(before.getAttribute(TimelineCallback.TIMELINE_ATTRIBUTE_NAME));
		before.start().stop(); // caused NPE before the fix
	}
}
