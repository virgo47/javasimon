package org.javasimon;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.javasimon.utils.DebugCallback;
import org.javasimon.utils.LoggingCallback;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;

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
		Assert.assertEquals(callback.callbacks().get(0).getClass(), CompositeFilterCallback.class);
		Assert.assertEquals(callback.callbacks().get(1).getClass(), DebugCallback.class);
		Callback loggingCallback = callback.callbacks().get(0).callbacks().get(0);
		Assert.assertEquals(loggingCallback.getClass(), LoggingCallback.class);
		Assert.assertEquals(((LoggingCallback) loggingCallback).getLevel(), Level.INFO);
		Assert.assertEquals(((LoggingCallback) loggingCallback).getLogger().getName(), "org.javasimon.test");
		Assert.assertTrue(SimonManager.getStopwatch("whatever").isEnabled());
		Assert.assertFalse(SimonManager.getStopwatch("org.javasimon.test.whatever").isEnabled());
	}

	@Test
	public void testConfig() throws IOException {
		Manager manager = new EnabledManager();
		manager.configuration().readConfig(new StringReader("<simon-configuration>\n" +
			"  <simon pattern='org.javasimon.test.stopwatch'/>\n" +
			"  <simon pattern='org.javasimon.*' stats='basic'/>\n" +
			"  <simon pattern='*.debug' state='disabled'/>\n" +
			"  <simon pattern='*no-stats*' stats='null'/>\n" +
			"</simon-configuration>"));
		Assert.assertNull(manager.configuration().getConfig("org.javasimon.bubu").getState());
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.stopwatch").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getStatProcessorType().equals(StatProcessorType.BASIC));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getState().equals(SimonState.DISABLED));
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug.no-stats").getStatProcessorType().equals(StatProcessorType.NULL));
	}

	@Test
	public void testConditions() {
		Split split = new EnabledManager().getStopwatch(null).start();
		split.stop();
		Assert.assertTrue(new FilterCallback.Rule(null, "split > 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterCallback.Rule(null, "split < 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterCallback.Rule(null, "split < 5 || split > 1000000000000", null).checkCondition(split.getStopwatch(), split));
		Assert.assertTrue(new FilterCallback.Rule(null, "split > 100 && split < 10000000000", null).checkCondition(split.getStopwatch(), split));
	}

	@Test
	public void testStopwatchRules() {
		EnabledManager manager = new EnabledManager();
		CompositeFilterCallback filter = new CompositeFilterCallback();
		filter.addRule(FilterCallback.Rule.Type.MUST, "active == 2", "*.sw1", Callback.Event.STOPWATCH_START);
		class MyCallback extends CallbackSkeleton {
			private boolean triggered;

			public void stopwatchStart(Split split) {
				triggered = true;
			}

			boolean isTriggered() {
				boolean val = triggered;
				triggered = false;
				return val;
			}
		}
		MyCallback callback = new MyCallback();
		filter.addCallback(callback);
		manager.installCallback(filter);

		Stopwatch sw1 = manager.getStopwatch("whatever.sw1");
		Stopwatch sw2 = manager.getStopwatch("whatever.sw2");

		sw1.start().stop();
		Assert.assertFalse(callback.isTriggered());
		sw2.start().stop();
		Assert.assertFalse(callback.isTriggered());

		Split split1 = sw1.start();
		Assert.assertFalse(callback.isTriggered());
		Split split2 = sw1.start();
		// this is the only place when the callback should trigger - active==2 after start and it's *.sq1
		Assert.assertTrue(callback.isTriggered());
		sw1.start().stop(); // active went to three for a while, but wasn't at 2 after start
		Assert.assertFalse(callback.isTriggered());
		split1.stop();
		split2.stop();
		Assert.assertFalse(callback.isTriggered());

		split1 = sw2.start();
		Assert.assertFalse(callback.isTriggered());
		split2 = sw2.start();
		Assert.assertFalse(callback.isTriggered());
		split1.stop();
		split2.stop();
		Assert.assertFalse(callback.isTriggered());
	}
}
