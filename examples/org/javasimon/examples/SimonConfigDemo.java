package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.SimonConfigManager;
import org.javasimon.utils.SimonUtils;

/**
 * SimonConfigTest.
 * TODO: Ignore this example, it doesn't show anything now. 
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public final class SimonConfigDemo {
	private SimonConfigDemo() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		System.setProperty(SimonConfigManager.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/examples/simon.config");
		System.out.println(SimonUtils.simonTreeString(SimonManager.getRootSimon()));
	}
}
