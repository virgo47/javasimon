package gwimon.client;

import java.io.Serializable;

/**
 * Simon universal value object (counter/stopwatch).
 *
 * @author virgo47@gmail.com
 */
public class SimonValue implements Serializable {
	public static final String TYPE_STOPWATCH = "stopwatch";
	public static final String TYPE_COUNTER = "counter";

	// non-simon
	public String type;

	// common
	public String name;
	public String note;
	public long firstUsage;
	public long lastUsage;
	public long lastReset;

	// shared names/types
	public long total;
	public long counter;
	public long min;
	public long max;
	public long minTimestamp;
	public long maxTimestamp;

	// stopwatch
	public long active;
	public long maxActive;
	public long maxActiveTimestamp;
	public long last;
	public double mean;
	public double standardDeviation;
	public double variance;
	public double varianceN;

	// counter
	public long incrementSum;
	public long decrementSum;
}
