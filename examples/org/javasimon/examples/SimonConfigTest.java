package org.javasimon.examples;

import org.javasimon.SimonFactory;

/**
 * SimonConfigTest.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public final class SimonConfigTest {
	public static void main(String[] args) {
		System.setProperty(SimonFactory.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/examples/simon.config");
		SimonFactory.getRootSimon();
	}
}
