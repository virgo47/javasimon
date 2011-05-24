package gwimon.client;

import java.io.Serializable;

/**
 * Simon universal value object (counter/stopwatch).
 *
 * @author virgo47@gmail.com
 */
public class SimonValue implements Serializable {
	public String name;
	public String note;
	public long firstUsage;
	public long lastUsage;
	public long lastReset;
	public long total;
	public long counter;
	public long min;
	public long max;
	public long minTimestamp;
	public long maxTimestamp;
	public long active;
	public long maxActive;
	public long maxActiveTimestamp;
	public long last;
	public double mean;
	public double standardDeviation;
	public double variance;
	public double varianceN;

}
