package org.javasimon.aggregation;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface MetricsDaoFactory {
	MetricsDao createMetricsDao(String metricsDaoClass);
}
