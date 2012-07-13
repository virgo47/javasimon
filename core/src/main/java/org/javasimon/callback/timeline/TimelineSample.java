package org.javasimon.callback.timeline;

/**
 * Snapshot of timeline state
 *
 * @author gerald
 */
public class TimelineSample<TR extends TimeRange> {
	private final int capacity;
	private final long width;
	private final TR[] timeRanges;

	public TimelineSample(int capacity, long width, TR[] timeRanges) {
		this.capacity = capacity;
		this.width = width;
		this.timeRanges = timeRanges;
	}

	public int getCapacity() {
		return capacity;
	}

	public long getWidth() {
		return width;
	}

	public TR[] getTimeRanges() {
		return timeRanges;
	}

}
