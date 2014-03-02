package org.javasimon.aggregation;

import org.javasimon.jmx.StopwatchSample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ReplaceSamplesAggregator implements SamplesAggregator {

	Map<String, Map<String, StopwatchSample>> stopwatchSamples = new HashMap<String, Map<String, StopwatchSample>>();

	@Override
	public void addStopwatchSamples(String managerId, List<StopwatchSample> samples) {
		Map<String, StopwatchSample> managerSamples = stopwatchSamples.get(managerId);
		if (managerSamples == null) {
			managerSamples = new HashMap<String, StopwatchSample>();
			stopwatchSamples.put(managerId, managerSamples);
		}

		for (StopwatchSample sample : samples) {
			managerSamples.put(sample.getName(), sample);
		}
	}

	@Override
	public List<StopwatchSample> getSamples(String managerId) {
		Map<String, StopwatchSample> managerSamples = stopwatchSamples.get(managerId);
		return new ArrayList<StopwatchSample>(managerSamples.values());
	}
}
