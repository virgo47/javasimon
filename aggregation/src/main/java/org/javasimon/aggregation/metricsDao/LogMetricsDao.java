package org.javasimon.aggregation.metricsDao;

import org.javasimon.aggregation.MetricsDao;
import org.javasimon.jmx.StopwatchSample;

import java.util.List;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class LogMetricsDao implements MetricsDao {

	//private static final Logger logger = LoggerFactory.getLogger(LogMetricsDao.class);

	@Override
	public void storeStopwatchSamples(String managerId, List<StopwatchSample> samples) {
		System.out.println(String.format("Manager: %s. %s", managerId, samples.toString()));
	}
}
