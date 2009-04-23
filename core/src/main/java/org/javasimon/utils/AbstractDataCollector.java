package org.javasimon.utils;

import org.javasimon.Simon;

import java.util.*;

/**
 * DataCollector collects data. :-) Data are stored as doubles whatever the original type was.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 13, 2008
 */
public abstract class AbstractDataCollector {
	private final Map<Simon, List<Double>> data = new LinkedHashMap<Simon, List<Double>>();
	private int samples;

	/**
	 * Initializes the new data collector with the set of Simons.
	 *
	 * @param simons Simon set for this data collector
	 */
	public AbstractDataCollector(Simon... simons) {
		for (Simon simon : simons) {
			data.put(simon, new ArrayList<Double>());
		}
	}

	/**
	 * Samples Simon values using the {@link #obtainValue(org.javasimon.Simon)} that must be
	 * implemented by the concrete data collector.
	 */
	public final void collect() {
		for (Simon simon : data.keySet()) {
			data.get(simon).add(obtainValue(simon));
		}
		samples++;
	}

	/**
	 * Returns number of samples.
	 *
	 * @return number of samples
	 */
	public final int sampleCount() {
		return samples;
	}

	/**
	 * Returns set of sampled Simons.
	 *
	 * @return set of sampled Simons
	 */
	public final Set<Simon> getSimons() {
		return Collections.unmodifiableSet(data.keySet());
	}

	/**
	 * Returns the whole dataset for one Simon.
	 *
	 * @param simon Simon
	 * @return list of all values for the Simon
	 */
	public final List<Double> valuesFor(Simon simon) {
		return Collections.unmodifiableList(data.get(simon));
	}

	/**
	 * Returns the value for a Simon.
	 *
	 * @param simon Simon which the value is obtained from
	 * @return value for that Simon
	 */
	public abstract double obtainValue(Simon simon);
}
