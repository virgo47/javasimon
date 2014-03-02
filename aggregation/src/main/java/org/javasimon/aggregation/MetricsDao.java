package org.javasimon.aggregation;

import org.javasimon.aggregation.metricsDao.DaoException;
import org.javasimon.jmx.StopwatchSample;

import java.util.List;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface MetricsDao {

	void init() throws DaoException;

	void storeStopwatchSamples(String managerId, List<StopwatchSample> samples) throws DaoException, DaoException;

	List<StopwatchSample> getStopwatchSamples(String managerId);
}
