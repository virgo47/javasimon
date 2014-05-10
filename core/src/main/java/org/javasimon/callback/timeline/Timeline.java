package org.javasimon.callback.timeline;

import org.javasimon.callback.lastsplits.CircularList;

/**
 * Collection of values sorted on a time line.
 *
 * @author gerald
 */
public abstract class Timeline<TR extends TimeRange> {

	/** List of time ranges. */
	protected final CircularList<TR> timeRanges;
	/** Time range width in milliseconds. */
	protected final long timeRangeWidth;

	/** Last used time range. */
	private TR lastTimeRange;

	/**
	 * Main constructor.
	 *
	 * @param capacity Number of time ranges
	 * @param timeRangeWidth Width of each time range
	 */
	protected Timeline(int capacity, long timeRangeWidth) {
		this.timeRanges = new CircularList<>(capacity);
		this.timeRangeWidth = timeRangeWidth;
	}

	/**
	 * Creates time range (factory method).
	 *
	 * @param startTimestamp Time range start
	 * @param endTimestamp Time range end
	 * @return created time range
	 */
	protected abstract TR createTimeRange(long startTimestamp, long endTimestamp);

	/**
	 * Returns existing time range if it already exists or create a new one.
	 */
	protected final TR getOrCreateTimeRange(long timestamp) {
		TR timeRange;
		if (lastTimeRange == null || timestamp > lastTimeRange.getEndTimestamp()) {
			// New time range
			long roundedTimestamp = timestamp - timestamp % timeRangeWidth;
			timeRange = createTimeRange(roundedTimestamp, roundedTimestamp + timeRangeWidth);
			timeRanges.add(timeRange);
			lastTimeRange = timeRange;
		} else if (timestamp >= lastTimeRange.getStartTimestamp()) {
			// Current time range
			timeRange = lastTimeRange;
		} else {
			// Old time range
			timeRange = null;
			// Not very good from performance point of view
			// iterating from end till start would be a better solution
			for (TR oldTimeRange : timeRanges) {
				if (oldTimeRange.containsTimestamp(timestamp)) {
					timeRange = oldTimeRange;
					break;
				}
			}
		}

		return timeRange;
	}

	public abstract TimelineSample<TR> sample();
}
