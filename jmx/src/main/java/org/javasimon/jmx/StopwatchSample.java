package org.javasimon.jmx;

import java.beans.ConstructorProperties;

/**
 * Value object for retrieving data from Stopwatch Simon. Basically, it's
 * {@link org.javasimon.StopwatchSample} with added JMX capabilities to be return as object via
 * MXBean method.
 * <p/>
 * Example:
 * <pre>
 * SimonMXBean simon = JMX.newMXBeanProxy(..., new ObjectName("domain:type=Simon"), SimonMXBean.class);
 * StopwatchSample = simon.getStopwatchSample("simon.stopwatch");
 * </pre>
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 26.1.2009 10:21:36
 * @since 2
 */
public final class StopwatchSample extends org.javasimon.StopwatchSample {

	/**
	 * JMX constructor. Constructor used by JMX client code to initialize all properties of object
	 * from composite data object.
	 *
	 * @param mean mean value (provided optionally)
	 * @param stdDev standard deviation (provided optionally)
	 * @param var variance (provided optionally)
	 * @param varN variance N (provided optionally)
	 * @param total sum of all measured times
	 * @param note note (provided optionally)
	 * @param counter count of measures
	 * @param min minimal measured time
	 * @param max maximal measured time
	 * @param minTimestamp time when minimal time was measured
	 * @param maxTimestamp time when maximal time was measured
	 * @param active count of actual running measures
	 * @param maxActive maximum paralel measures
	 * @param maxActiveTimestamp time when maximum paralel measures happend
	 * @param last last split value in ns
	 */
	@ConstructorProperties({"mean", "standardDeviation", "variance", "varianceN", "note", "firstUsage", "lastUsage",
		"lastReset", "total", "counter", "min", "max", "minTimestamp", "maxTimestamp", "active", "maxActive",
		"maxActiveTimestamp", "last"})
	public StopwatchSample(double mean, double stdDev, double var, double varN, String note, long firstUsage,
		long lastUsage, long lastReset, long total, long counter, long min, long max, long minTimestamp,
		long maxTimestamp, long active, long maxActive, long maxActiveTimestamp, long last) {
		setMean(mean);
		setStandardDeviation(stdDev);
		setVariance(var);
		setVarianceN(varN);
		setNote(note);
		setFirstUsage(firstUsage);
		setLastUsage(lastUsage);
		setLastReset(lastReset);

		setTotal(total);
		setCounter(counter);
		setMin(min);
		setMax(max);
		setMinTimestamp(minTimestamp);
		setMaxTimestamp(maxTimestamp);
		setActive(active);
		setMaxActive(maxActive);
		setMaxActiveTimestamp(maxActiveTimestamp);
		setLast(last);
	}

	/**
	 * Internall, framework constructor for Simon MBean implementation to initialize all properties
	 * by sample obtained from Simon.
	 *
	 * @param s sample object obtained from Stopwatch Simon
	 */
	StopwatchSample(org.javasimon.StopwatchSample s) {
		setMean(s.getMean());
		setStandardDeviation(s.getStandardDeviation());
		setVariance(s.getVariance());
		setVarianceN(s.getVarianceN());
		setNote(s.getNote());
		setFirstUsage(s.getFirstUsage());
		setLastUsage(s.getLastUsage());
		setLastReset(s.getLastReset());

		setCounter(s.getCounter());
		setTotal(s.getTotal());
		setMin(s.getMin());
		setMax(s.getMax());
		setMinTimestamp(s.getMinTimestamp());
		setMaxTimestamp(s.getMaxTimestamp());
		setActive(s.getActive());
		setMaxActive(s.getMaxActive());
		setMaxActiveTimestamp(s.getMaxActiveTimestamp());
		setLast(s.getLast());
	}
}
