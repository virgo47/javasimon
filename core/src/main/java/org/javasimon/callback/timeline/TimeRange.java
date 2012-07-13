package org.javasimon.callback.timeline;

import org.javasimon.utils.SimonUtils;

/**
 * Time range and associated data.
 *
 * @author gerald
 */
public class TimeRange {
	/**
	 * Beginning of the time range
	 */
	private final long startTimestamp;

	/**
	 * Ending of the time range
	 */
	private final long endTimestamp;

	/**
	 * Timestamp of last change
	 */
	protected long lastTimestamp;

	/**
	 * Main constructor.
	 *
	 * @param startTimestamp Beginning of the time range
	 * @param endTimestamp Ending of the time range
	 */
	protected TimeRange(long startTimestamp, long endTimestamp) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	/**
	 * Indicates whether this time range contains given timestamp.
	 *
	 * @param timestampInMs Timestamp express in milliseconds
	 */
	public boolean containsTimestamp(long timestampInMs) {
		return timestampInMs >= startTimestamp && timestampInMs <= endTimestamp;
	}

	protected StringBuilder toStringBuilder(StringBuilder stringBuilder) {
		return stringBuilder.append(getClass().getSimpleName())
			.append(" start=").append(SimonUtils.presentTimestamp(startTimestamp))
			.append(" end=").append(SimonUtils.presentTimestamp(endTimestamp));
	}

	@Override
	public String toString() {
		return toStringBuilder(new StringBuilder()).toString();
	}

}
