package org.javasimon.callback.timeline;

import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;

/**
 * Timeline callback aims at keeping simon data for the last N minutes.
 * A "timeline" object is stored in each Simon, it's fixed size ring buffer
 * of "time ranges". A "time range" aggregates Simon data for a fixed duration.
 * <p/>
 * Example: a timeline containing 6 time ranges of 10 minutes each can be used to
 * see evolution for an hour.
 *
 * @author gerald
 */
public class TimelineCallback extends CallbackSkeleton {

	/** Default attribute name for storing timelines. */
	public static final String TIMELINE_ATTRIBUTE_NAME = "timeline";

	/** Attribute name for storing timeline in Simons. */
	private final String timelineAttributeName;
	/** Number of time ranges to keep in the timeline. */
	private final int timelineCapacity;
	/** Width in milliseconds of the time ranges. */
	private final long timeRangeWidth;

	/**
	 * Main constructor.
	 *
	 * @param timelineAttributeName Simon attribute name used for storing Timeline
	 * @param timelineCapacity Timeline capacity (number of time ranges)
	 * @param timeRangeWidth Time range width (in milliseconds)
	 */
	public TimelineCallback(String timelineAttributeName, int timelineCapacity, long timeRangeWidth) {
		this.timelineAttributeName = timelineAttributeName;
		this.timelineCapacity = timelineCapacity;
		this.timeRangeWidth = timeRangeWidth;
	}

	/**
	 * Constructor using default attribute name.
	 *
	 * @param timelineCapacity Timeline capacity (number of time ranges)
	 * @param timeRangeWidth Time range width (in milliseconds)
	 */
	public TimelineCallback(int timelineCapacity, long timeRangeWidth) {
		this(TIMELINE_ATTRIBUTE_NAME, timelineCapacity, timeRangeWidth);
	}

	/**
	 * Constructor using default attribute name, default timeline capacity of 6
	 * and default timeline width of 10 minutes. Timeline stores an hour of data.
	 */
	public TimelineCallback() {
		this(6, 1000L * 60L * 6L);
	}

	/**
	 * Returns timeline for given Stopwatch.
	 *
	 * @param stopwatch Stopwatch
	 * @return Stopwatch timeline
	 */
	private StopwatchTimeline getStopwatchTimeline(Stopwatch stopwatch) {
		return (StopwatchTimeline) stopwatch.getAttribute(timelineAttributeName);
	}

	/**
	 * On simon creation a timeline attribute is added (for Stopwatches only).
	 *
	 * @param simon created simon
	 */
	@Override
	public void onSimonCreated(Simon simon) {
		if (simon instanceof Stopwatch) {
			simon.setAttribute(timelineAttributeName, new StopwatchTimeline(timelineCapacity, timeRangeWidth));
		}
	}

	@Override
	public void onStopwatchAdd(Stopwatch stopwatch, Split split, StopwatchSample sample) {
		StopwatchTimeline timeline = getStopwatchTimeline(stopwatch);
		if (timeline != null) {
			timeline.addSplit(split);
		}
	}

	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		StopwatchTimeline timeline = getStopwatchTimeline(split.getStopwatch());
		if (timeline != null) {
			timeline.addSplit(split);
		}
	}
}
