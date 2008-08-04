package org.javasimon;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public class SimonCounterImpl extends AbstractSimon implements SimonCounter {
	private long counter;

	public SimonCounterImpl(String name) {
		super(name);
	}

	public void increment() {
		counter++;
	}

	public void increment(long inc) {
		counter += inc;
	}

	public void reset() {
		counter = 0;
	}

	public String toString() {
		return "Simon Counter: " + super.toString() + " counter=" + counter;
	}
}
