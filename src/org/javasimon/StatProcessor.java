package org.javasimon;

/**
 * StatProcessor processes observations - measured values - and provides additional statistical
 * information for them. StatProcessor interface gives you various methods but not all of them
 * are implemented in every implementation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public interface StatProcessor {
	StatProcessorType getType();

	void process(double value);

	double getAverage();
}
