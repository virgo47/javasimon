package org.javasimon.jmx;

import org.javasimon.utils.SimonUtils;

import java.beans.ConstructorProperties;
import java.util.Date;

/**
 * Value object for retrieving data from Counter Simon. Basically, it's
 * {@link org.javasimon.CounterSample} with added JMX capabilities to be return as object via
 * MXBean method.
 * <p/>
 * Example:
 * <pre>
 * SimonManagerMXBean simon = JMX.newMXBeanProxy(..., new ObjectName("domain:type=Simon"), SimonManagerMXBean.class);
 * CounterSample = simon.getCounterSample("simon.counter");
 * </pre>
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public final class CounterSample extends org.javasimon.CounterSample {

	/**
	 * JMX constructor. Constructor used by JMX client code to initialize all properties of object
	 * from composite data object.
	 *
	 * @param name Simon's name
	 * @param note note (provided optionally)
	 * @param firstUsage first usage ms timestamp
	 * @param lastUsage last usage ms timestamp
	 * @param counter actual counter value
	 * @param min minimal counter value
	 * @param max maximal counter value
	 * @param minTimestamp time when counter reached minimal value
	 * @param maxTimestamp time when counter reached maximal value
	 * @param incSum sum of all increments
	 * @param decSum sum of all decrements
	 */
	@ConstructorProperties({"name", "note", "firstUsage", "lastUsage", "counter", "min",
		"max", "minTimestamp", "maxTimestamp", "incrementSum", "decrementSum"})
	public CounterSample(String name, String note, long firstUsage, long lastUsage, long counter,
	                     long min, long max, long minTimestamp, long maxTimestamp, long incSum, long decSum) {
		setName(name);
		setNote(note);
		setFirstUsage(firstUsage);
		setLastUsage(lastUsage);

		setCounter(counter);
		setMin(min);
		setMax(max);
		setMinTimestamp(minTimestamp);
		setMaxTimestamp(maxTimestamp);
		setIncrementSum(incSum);
		setDecrementSum(decSum);
	}

	/**
	 * Internal, framework constructor for Simon MBean implementation to initialize all properties
	 * by sample obtained from Simon.
	 *
	 * @param sample sample object obtained from Counter Simon
	 */
	CounterSample(org.javasimon.CounterSample sample) {
		setName(sample.getName());
		setNote(sample.getNote());
		setFirstUsage(sample.getFirstUsage());
		setLastUsage(sample.getLastUsage());

		setCounter(sample.getCounter());
		setMin(sample.getMin());
		setMax(sample.getMax());
		setMinTimestamp(sample.getMinTimestamp());
		setMaxTimestamp(sample.getMaxTimestamp());
		setIncrementSum(sample.getIncrementSum());
		setDecrementSum(sample.getDecrementSum());
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
	 * Timestamp of the last usage from the sampled Simon as a date.
	 *
	 * @return Simon's last usage timestamp as date
	 */
	public Date getLastUsageAsDate() {
		return new Date(getLastUsage());
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
}
