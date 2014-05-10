package org.javasimon.jmx;

import org.javasimon.utils.SimonUtils;

import java.beans.ConstructorProperties;
import java.util.Date;

/**
 * Value object for retrieving data from Stopwatch Simon. Basically, it's
 * {@link org.javasimon.StopwatchSample} with added JMX capabilities to be return as object via
 * MXBean method.
 * <p/>
 * Example:
 * <pre>
 * SimonManagerMXBean simon = JMX.newMXBeanProxy(..., new ObjectName("domain:type=Simon"), SimonManagerMXBean.class);
 * StopwatchSample = simon.getStopwatchSample("simon.stopwatch");
 * </pre>
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public final class StopwatchSample extends org.javasimon.StopwatchSample {

	/**
	 * JMX constructor. Constructor used by JMX client code to initialize all properties of object
	 * from composite data object.
	 *
	 * @param name Simon's name
	 * @param mean mean value (provided optionally)
	 * @param stdDev standard deviation (provided optionally)
	 * @param var variance (provided optionally)
	 * @param varN variance N (provided optionally)
	 * @param firstUsage first usage ms timestamp
	 * @param lastUsage last usage ms timestamp
	 * @param total sum of all measured times
	 * @param note note (provided optionally)
	 * @param counter count of measures
	 * @param min minimal measured time
	 * @param max maximal measured time
	 * @param minTimestamp ms timestamp when minimal time was measured
	 * @param maxTimestamp ms timestamp when maximal time was measured
	 * @param active count of actual running measures
	 * @param maxActive maximum parallel measures
	 * @param maxActiveTimestamp ms timestamp time when maximum parallel measures happened
	 * @param last last split value in ns
	 */
	@ConstructorProperties({"name", "mean", "standardDeviation", "variance", "varianceN", "note", "firstUsage", "lastUsage",
		"total", "counter", "min", "max", "minTimestamp", "maxTimestamp", "active", "maxActive",
		"maxActiveTimestamp", "last"})
	public StopwatchSample(String name, double mean, double stdDev, double var, double varN, String note, long firstUsage,
		long lastUsage, long total, long counter, long min, long max, long minTimestamp,
		long maxTimestamp, long active, long maxActive, long maxActiveTimestamp, long last)
	{
		setName(name);
		setMean(mean);
		setStandardDeviation(stdDev);
		setVariance(var);
		setVarianceN(varN);
		setNote(note);
		setFirstUsage(firstUsage);
		setLastUsage(lastUsage);

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
	 * Framework constructor for Simon MBean implementation to initialize all properties
	 * by sample obtained from Simon.
	 *
	 * @param sample sample object obtained from Stopwatch Simon
	 */
	public StopwatchSample(org.javasimon.StopwatchSample sample) {
		setName(sample.getName());
		setMean(sample.getMean());
		setStandardDeviation(sample.getStandardDeviation());
		setVariance(sample.getVariance());
		setVarianceN(sample.getVarianceN());
		setNote(sample.getNote());
		setFirstUsage(sample.getFirstUsage());
		setLastUsage(sample.getLastUsage());

		setCounter(sample.getCounter());
		setTotal(sample.getTotal());
		setMin(sample.getMin());
		setMax(sample.getMax());
		setMinTimestamp(sample.getMinTimestamp());
		setMaxTimestamp(sample.getMaxTimestamp());
		setActive(sample.getActive());
		setMaxActive(sample.getMaxActive());
		setMaxActiveTimestamp(sample.getMaxActiveTimestamp());
		setLast(sample.getLast());
	}

	/**
	 * Returns the value of the last measured split in ns as a formatted string.
	 *
	 * @return last measured split in ns as string
	 */
	public final String getLastAsString() {
		return SimonUtils.presentNanoTime(getLast());
	}

	/**
	 * Returns the total sum of all split times in nanoseconds as a formatted string.
	 *
	 * @return total time of the stopwatch in nanoseconds as string
	 */
	public final String getTotalAsString() {
		return SimonUtils.presentNanoTime(getTotal());
	}

	/**
	 * Returns minimal time split value in nanoseconds as a formatted string.
	 *
	 * @return minimal time split in nanoseconds as string
	 */
	public final String getMinAsString() {
		return SimonUtils.presentNanoTime(getMin());
	}

	/**
	 * Returns maximal time split value in nanoseconds as a formatted string.
	 *
	 * @return maximal time split in nanoseconds as string
	 */
	public final String getMaxAsString() {
		return SimonUtils.presentNanoTime(getMax());
	}

	/**
	 * Returns ms timestamp when the min value was measured as a formatted string.
	 *
	 * @return ms timestamp of the min value measurement as string
	 */
	public final String getMinTimestampAsString() {
		return SimonUtils.presentTimestamp(getMinTimestamp());
	}

	/**
	 * Returns ms timestamp when the min value was measured as a formatted date.
	 *
	 * @return ms timestamp of the min value measurement as date
	 */
	public final Date getMinTimestampAsDate() {
		return new Date(getMinTimestamp());
	}

	/**
	 * Returns ms timestamp when the max value was measured as a formatted string.
	 *
	 * @return ms timestamp of the max value measurement as string
	 */
	public final String getMaxTimestampAsString() {
		return SimonUtils.presentTimestamp(getMaxTimestamp());
	}

	/**
	 * Returns ms timestamp when the max value was measured as a formatted date.
	 *
	 * @return ms timestamp of the max value measurement as date
	 */
	public final Date getMaxTimestampAsDate() {
		return new Date(getMaxTimestamp());
	}

	/**
	 * Returns ms timestamp when the last peek of the active split count occurred as a formatted string.
	 *
	 * @return ms timestamp of the last peek of the active split count as string
	 */
	public final String getMaxActiveTimestampAsString() {
		return SimonUtils.presentTimestamp(getMaxActiveTimestamp());
	}

	/**
	 * Returns ms timestamp when the last peek of the active split count occurred as a formatted date.
	 *
	 * @return ms timestamp of the last peek of the active split count as date
	 */
	public final Date getMaxActiveTimestampAsDate() {
		return new Date(getMaxActiveTimestamp());
	}

	/**
	 * Returns mean value (average) of all measured values as a formatted string (ns).
	 *
	 * @return mean value as string
	 */
	public final String getMeanAsString() {
		return SimonUtils.presentNanoTime((long) getMean());
	}

	/**
	 * Timestamp of the first usage from the sampled Simon as a formatted string.
	 *
	 * @return Simon's first usage timestamp as string
	 */
	public String getFirstUsageAsString() {
		return SimonUtils.presentTimestamp(getFirstUsage());
	}

	/**
	 * Timestamp of the first usage from the sampled Simon as a formatted date.
	 *
	 * @return Simon's first usage timestamp as date
	 */
	public Date getFirstUsageAsDate() {
		return new Date(getFirstUsage());
	}

	/**
	 * Timestamp of the last usage from the sampled Simon as a formatted string.
	 *
	 * @return Simon's last usage timestamp as string
	 */
	public String getLastUsageAsString() {
		return SimonUtils.presentTimestamp(getLastUsage());
	}

	/**
	 * Timestamp of the last usage from the sampled Simon as a formatted date.
	 *
	 * @return Simon's last usage timestamp as string
	 */
	public Date getLastUsageAsDate() {
		return new Date(getLastUsage());
	}
}
