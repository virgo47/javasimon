package org.javasimon.examples.jmx.custom;

import org.javasimon.jmx.StopwatchMXBean;

/**
 * Custom extension to Stopwatch MX bean interface to provide values in millis.
 *
 * @author gquintana
 */
public interface CustomStopwatchMXBean extends StopwatchMXBean {

	long getCounter();

	long getLastInMillis();

	long getMaxInMillis();

	double getMeanInMillis();

	long getMinInMillis();

	double getStandardDeviationInMillis();

	long getTotalInMillis();
}
