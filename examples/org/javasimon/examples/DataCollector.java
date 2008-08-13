package org.javasimon.examples;

import org.javasimon.Simon;

import java.util.*;

/**
 * DataCollector collects data. :-)
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 13, 2008
 */
public abstract class DataCollector {
	private Map<Simon, List<Double>> data = new LinkedHashMap<Simon, List<Double>>();
	private int samples;

	public DataCollector(Simon... simons) {
		for (Simon simon : simons) {
			data.put(simon, new ArrayList<Double>());
		}
	}

	public void collect() {
		for (Simon simon : data.keySet()) {
			data.get(simon).add(obtainValue(simon));
		}
		samples++;
	}

	public int sampleCount() {
		return samples;
	}

	public Set<Simon> getSimons() {
		return Collections.unmodifiableSet(data.keySet());
	}

	public List<Double> valuesFor(Simon simon) {
		return Collections.unmodifiableList(data.get(simon));
	}

	public abstract double obtainValue(Simon simon);
}
