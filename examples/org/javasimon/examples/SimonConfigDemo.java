package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.SimonConfiguration;
import org.javasimon.utils.SimonUtils;

/**
 * SimonConfigTest.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public final class SimonConfigDemo {
	public static void main(String[] args) {
		System.setProperty(SimonConfiguration.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/examples/simon.config");
		SimonUtils.printSimonTree(SimonManager.getRootSimon());
	}
}
