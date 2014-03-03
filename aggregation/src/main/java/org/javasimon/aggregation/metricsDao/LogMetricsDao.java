package org.javasimon.aggregation.metricsDao;

import org.javasimon.aggregation.MetricsDao;
import org.javasimon.jmx.StopwatchSample;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class LogMetricsDao implements MetricsDao {

	//private static final Logger logger = LoggerFactory.getLogger(LogMetricsDao.class);

	@Override
	public void init() throws DaoException {

	}

	@Override
	public void storeStopwatchSamples(String managerId, List<StopwatchSample> samples) {
        String samplesStr = concatSamples(samples);
		System.out.println(String.format("Manager: %s. %s", managerId, samplesStr));
	}

    private String concatSamples(List<StopwatchSample> samples) {
        String res = "";
        for (StopwatchSample sample : samples) {
            res += sample + "\n";
        }

        return res;
    }

    @Override
	public List<StopwatchSample> getStopwatchSamples(String managerId) {
		return Collections.emptyList();
	}
}
