package org.javasimon.callback.timeline;

import static org.javasimon.callback.timeline.TimeUtil.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
/**
 * Unit test for {@link StopwatchTimeRange} and {@link TimeRange}
 * @author gerald
 */
public class StopwatchTimeRangeTest {
    private StopwatchTimeRange createStopwatchTimeRange() {
        return new StopwatchTimeRange(createTimestamp(2012, 7, 12, 21, 5, 0), createTimestamp(2012, 7, 12, 21, 10, 0));
    }
    /**
     * Test {@link TimeRange#containsTimestamp(long) }
     */
    @Test
    public void testContainsTimestamp() {
        TimeRange timeRange=createStopwatchTimeRange();
        assertTrue(timeRange.containsTimestamp(createTimestamp(2012, 7, 12, 21, 7, 0)));
        assertFalse(timeRange.containsTimestamp(createTimestamp(2012, 7, 12, 21, 13, 0)));
        assertFalse(timeRange.containsTimestamp(createTimestamp(2012, 7, 12, 21, 4, 0)));
    }
    /**
     * Test statistics method
     */
    @Test
    public void testStatistics() {
        // Prepare date
        StopwatchTimeRange timeRange=createStopwatchTimeRange();
        addSplit(timeRange, createTimestamp(2012, 7, 12, 21, 11, 0), 100);
        addSplit(timeRange, createTimestamp(2012, 7, 12, 21, 12, 0), 200);
        addSplit(timeRange, createTimestamp(2012, 7, 12, 21, 13, 0), 300);
        // Check results
        assertEquals(timeRange.getCounter(), 3);
        assertEquals(timeRange.getTotal(), 600);
        assertEquals(timeRange.getMean(), 200.0D, 0.01D);
        assertEquals(timeRange.getMin(), 100);
        assertEquals(timeRange.getMax(), 300);
        assertEquals(timeRange.getLast(), 300);
        assertEquals(timeRange.getStandardDeviation(), 81.65D, 0.01D);
    }

}
