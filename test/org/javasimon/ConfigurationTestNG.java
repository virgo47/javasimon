package org.javasimon;

import org.testng.annotations.Test;

/**
 * ConfigurationTestNG.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 7, 2008
 */
public final class ConfigurationTestNG {
	@Test
	public void testConfig() {
		System.setProperty(SimonConfiguration.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/test.config");
		SimonFactory.reset();
		SimonConfiguration.init();
	}
}
