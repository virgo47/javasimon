package org.javasimon.aggregation;

import org.javasimon.jmx.StopwatchSample;

import java.util.List;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface SamplesAggregator {
	void addStopwatchSamples(String managerId, List<StopwatchSample> samples);

	List<StopwatchSample> getSamples(String managerId);
}
