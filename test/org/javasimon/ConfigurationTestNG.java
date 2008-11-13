package org.javasimon;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;

/**
 * ConfigurationTestNG.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 7, 2008
 */
public final class ConfigurationTestNG {
	@Test
	public void testConfigResource() throws IOException {
		System.setProperty(SimonConfigManager.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/test.config");
		SimonManager.clear();
		SimonConfigManager.init();
	}

	@Test
	public void testConfig() throws IOException {
		SimonManager.clear();
		BufferedReader br = new BufferedReader(
			new StringReader("strict\n" +
				"org.javasimon.test.stopwatch=stopwatch\n" +
				"org.javasimon.*=basicstats\n" +
				"*.debug=disabled\n" +
				"*no-stats*=nullstats\n" +
				"")
		);
		SimonConfigManager.initFromReader(br);
		Assert.assertNull(SimonConfigManager.getConfig("org.javasimon.bubu").getState());
		Assert.assertTrue(SimonConfigManager.getConfig("org.javasimon.test.stopwatch").getType().equals("stopwatch"));
		Assert.assertTrue(SimonConfigManager.getConfig("org.javasimon.test.stopwatch").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(SimonConfigManager.getConfig("org.javasimon.test.debug").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(SimonConfigManager.getConfig("org.javasimon.test.debug").getState().equals(SimonState.DISABLED));
		Assert.assertTrue(SimonConfigManager.getConfig("org.javasimon.test.debug.no-stats").getStatProcessorType().equals(StatProcessorType.NULL));
	}
}
