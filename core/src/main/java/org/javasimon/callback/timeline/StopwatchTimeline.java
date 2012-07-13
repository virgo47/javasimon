package org.javasimon.callback.timeline;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

/**
 * Timeline for Stopwatches
 *
 * @author gerald
 */
public class StopwatchTimeline extends Timeline<StopwatchTimeRange> {

	public StopwatchTimeline(int capacity, long timeRangeWidth) {
		super(capacity, timeRangeWidth);
	}

	@Override
	protected StopwatchTimeRange createTimeRange(long startTimestamp, long endTimestamp) {
		return new StopwatchTimeRange(startTimestamp, endTimestamp);
	}

	public void addSplit(Split split) {
		final long timestamp = SimonManager.millisForNano(split.getStart());
		// TODO[gerald] review and remove if invalid
//		final long timestamp = split.getStart() / SimonUtils.NANOS_IN_MILLIS;
		StopwatchTimeRange timeRange;
		synchronized (this) {
			timeRange = getOrCreateTimeRange(timestamp);
		}
		if (timeRange != null) {
			timeRange.addSplit(timestamp, split.runningFor());
		}
	}

	@Override
	public TimelineSample<StopwatchTimeRange> sample() {
		StopwatchTimeRange[] timeRangesCopy;
		synchronized (this) {
			timeRangesCopy = timeRanges.toArray(new StopwatchTimeRange[timeRanges.size()]);
		}
		return new TimelineSample<StopwatchTimeRange>(timeRanges.size(), timeRangeWidth, timeRangesCopy);
	}

}
