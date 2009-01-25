package org.javasimon;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.io.StringReader;

/**
 * ConfigurationTestNG.
 * TODO: Not used/active yet. Configuration is not working yet.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 7, 2008
 */
public final class ConfigurationTestNG {
	@Test
	public void testConfigResource() throws IOException {
		System.setProperty(SimonManager.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/test.config");
	}

	@Test
	public void testConfig() throws IOException {
		SimonManager.clear();
		SimonManager.configuration().readConfig(new StringReader("strict\n" +
			"org.javasimon.test.stopwatch=stopwatch\n" +
			"org.javasimon.*=basicstats\n" +
			"*.debug=disabled\n" +
			"*no-stats*=nullstats\n" +
			""));
		Assert.assertNull(SimonManager.configuration().getConfig("org.javasimon.bubu").getState());
		Assert.assertTrue(SimonManager.configuration().getConfig("org.javasimon.test.stopwatch").getType().equals("stopwatch"));
		Assert.assertTrue(SimonManager.configuration().getConfig("org.javasimon.test.stopwatch").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(SimonManager.configuration().getConfig("org.javasimon.test.debug").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(SimonManager.configuration().getConfig("org.javasimon.test.debug").getState().equals(SimonState.DISABLED));
		Assert.assertTrue(SimonManager.configuration().getConfig("org.javasimon.test.debug.no-stats").getStatProcessorType().equals(StatProcessorType.NULL));
	}
}
