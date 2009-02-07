package org.javasimon;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.io.StringReader;

/**
 * ConfigurationTestNG tests the configuration facility.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 7, 2008
 */
public final class ConfigurationTestNG {
	@Test
	public void testConfigResource() throws IOException {
		SimonManager.isEnabled();
		// Calling init causes configuration to be read twice because the init is called from
		// static initialization anyway. This just makes the static initialization do
		// empty initialization, because no configuration property is set.

		System.setProperty(SimonManager.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/test-config.xml");
		SimonManager.init(); // this really reads the config resource
		Callback callback = SimonManager.manager().callback();
		Assert.assertEquals(callback.getClass(), CompositeCallback.class);
		Assert.assertEquals(callback.callbacks().size(), 2);
	}

	@Test
	public void testConfig() throws IOException {
		Manager manager = new EnabledManager();
		manager.configuration().readConfig(new StringReader("<simon-configuration>\n" +
			"  <simon pattern='org.javasimon.test.stopwatch' type='stopwatch'/>\n" +
			"  <simon pattern='org.javasimon.*' stats='basic'/>\n" +
			"  <simon pattern='*.debug' state='disabled'/>\n" +
			"  <simon pattern='*no-stats*' stats='null'/>\n" +
			"</simon-configuration>"));
		Assert.assertNull(manager.configuration().getConfig("org.javasimon.bubu").getState());
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.stopwatch").getType().equals("stopwatch"));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.stopwatch").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getState().equals(SimonState.DISABLED));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug.no-stats").getStatProcessorType().equals(StatProcessorType.NULL));

	}
}
