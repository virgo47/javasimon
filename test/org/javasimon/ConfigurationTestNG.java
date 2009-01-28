package org.javasimon;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.util.Queue;
import java.util.LinkedList;

/**
 * ConfigurationTestNG tests the configuration facility.
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
		final Queue<String> messages = new LinkedList<String>();

		Manager manager = new EnabledManager();
		manager.installCallback(new CallbackSkeleton() {
			public void warning(String warning, Exception cause) {
				messages.add(warning);
			}
		});
		manager.configuration().readConfig(new StringReader("ugly line!\n" +
			"org.javasimon.test.stopwatch=stopwatch\n" +
			"org.javasimon.*=basicstats\n" +
			"*.debug=disabled\n" +
			"*no-stats*=nullstats\n" +
			""));
		Assert.assertEquals(messages.poll(), "Config error: Unknown config value 'line!' for name 'ugly' on line 1.");
		Assert.assertNull(messages.poll());
		Assert.assertNull(manager.configuration().getConfig("org.javasimon.bubu").getState());
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.stopwatch").getType().equals("stopwatch"));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.stopwatch").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getState().equals(SimonState.DISABLED));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug.no-stats").getStatProcessorType().equals(StatProcessorType.NULL));

	}
}
