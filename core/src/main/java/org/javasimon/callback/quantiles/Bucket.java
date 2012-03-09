package org.javasimon.callback.quantiles;

/**
 * Bucket count the number of samples in the range min-max.
 * @author gquintana
 */
public final class Bucket {
	/**
	 * Minimal value 
	 */
	private final long min;
	/**
	 * Maximal value
	 */
	private final long max;
	/**
	 * Number of values in the range min-max
	 */
	private int count;
	/**
	 * Constructor
	 * @param min Min value
	 * @param max Max value
	 */
	public Bucket(long min, long max) {
		this.min = min;
		this.max = max;
	}
	/**
	 * Get number of values in the range
	 * @return Number of value in the range
	 */
	public int getCount() {
		return count;
	}
	/**
	 * Get upper bound of the range
	 * @return Max value
	 */
	public long getMax() {
		return max;
	}
	/**
	 * Get lower bound of the range
	 * @return Min value
	 */
	public long getMin() {
		return min;
	}
	/**
	 * Check whether value is in the range
	 * @param value Value
	 * @return true if in range
	 */
	public boolean contains(long value) {
		return (value>=min)&&(value<=max);
	}
	/**
	 * Increment value number
	 */
	public void incrementCount() {
		count++;
	}
	/**
	 * Check if value is in range and increment value number
	 * @param value Value
	 * @return true if value number increased
	 */
	public boolean addValue(long value) {
		if (contains(value)) {
			incrementCount();
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Reset value number
	 */
	public void clear() {
		count=0;
	}
}
