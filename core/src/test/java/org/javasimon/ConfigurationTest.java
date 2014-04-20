package org.javasimon;

import org.javasimon.callback.Callback;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.CompositeCallback;
import org.javasimon.callback.CompositeFilterCallback;
import org.javasimon.callback.FilterRule;
import org.javasimon.clock.ClockUtils;
import org.javasimon.utils.LoggingCallback;
import org.javasimon.utils.SystemDebugCallback;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;

import javax.script.ScriptException;

/**
 * Tests for the configuration facility (callbacks, {@link ManagerConfiguration}).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class ConfigurationTest extends SimonUnitTest {

	@Test
	public void testConfigResource() throws IOException {
		SimonManager.callback().removeAllCallbacks();

		System.setProperty(SimonManager.PROPERTY_CONFIG_RESOURCE_NAME, "org/javasimon/test-config.xml");
		SimonManager.init(); // this really reads the config resource
		CompositeCallback callback = SimonManager.manager().callback();
		Assert.assertEquals(callback.callbacks().size(), 2);
		Assert.assertEquals(callback.callbacks().get(0).getClass(), CompositeFilterCallback.class);
		Assert.assertEquals(callback.callbacks().get(1).getClass(), SystemDebugCallback.class);
		Callback loggingCallback = ((CompositeCallback) callback.callbacks().get(0)).callbacks().get(0);
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
			"  <simon pattern='*.debug' state='disabled'/>\n" +
			"</simon-configuration>"));
		Assert.assertNull(manager.configuration().getConfig("org.javasimon.bubu").getState());
		Assert.assertTrue(manager.configuration().getConfig("org.javasimon.test.debug").getState().equals(SimonState.DISABLED));
	}

	@Test
	public void testConditions() throws ScriptException {
		Split split = new EnabledManager().getStopwatch(null).start();
		split.stop();
		Assert.assertTrue(new FilterRule(null, "split > 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertTrue(new FilterRule(null, "split gt 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertTrue(new FilterRule(null, "split ge 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterRule(null, "split < 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterRule(null, "split lt 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterRule(null, "split le 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterRule(null, "split eq 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertTrue(new FilterRule(null, "split ne 5", null).checkCondition(split.getStopwatch(), split));
		Assert.assertFalse(new FilterRule(null, "split < 5 || split > 1000000000us", null).checkCondition(split.getStopwatch(), split));
		Assert.assertTrue(new FilterRule(null, "split > 100 && split < 10000ms", null).checkCondition(split.getStopwatch(), split));
		Assert.assertTrue(new FilterRule(null, "value == 1s", null).checkCondition(split.getStopwatch(), 1000000000L));
		Assert.assertTrue(new FilterRule(null, "value == 1ms", null).checkCondition(split.getStopwatch(), ClockUtils.NANOS_IN_MILLIS));
		Assert.assertTrue(new FilterRule(null, "value == 1us", null).checkCondition(split.getStopwatch(), 1000L));
		Assert.assertTrue(new FilterRule(null, "value == 1", null).checkCondition(split.getStopwatch(), 1L));
	}

	// Callback helper class that does sets trigger on start/stop events
	class MyCallback extends CallbackSkeleton {
		private boolean triggered;

		@Override
		public void onStopwatchStart(Split split) {
			triggered = true;
		}

		@Override
		public void onStopwatchStop(Split split, StopwatchSample sample) {
			triggered = true;
		}

		// checks and resets the trigger flag
		boolean isTriggered() {
			boolean val = triggered;
			triggered = false;
			return val;
		}
	}

	@Test
	public void testMustRule() {
		EnabledManager manager = new EnabledManager();
		CompositeFilterCallback filter = new CompositeFilterCallback();
		filter.addRule(FilterRule.Type.MUST, "active == 2", "*.sw1", Callback.Event.STOPWATCH_START);
		MyCallback callback = new MyCallback();
		filter.addCallback(callback);
		manager.callback().addCallback(filter);

		Stopwatch sw1 = manager.getStopwatch("whatever.sw1");
		Stopwatch sw2 = manager.getStopwatch("whatever.sw2");

		sw1.start().stop();
		Assert.assertFalse(callback.isTriggered());
		sw2.start().stop();
		Assert.assertFalse(callback.isTriggered());

		Split split1 = sw1.start();
		Assert.assertFalse(callback.isTriggered());
		Split split2 = sw1.start();
		// this is the only place when the callback should trigger - active==2 after start and it's *.sw1
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
		manager.callback().removeCallback(filter);
	}

	@Test
	public void testSufficeRuleAllEvents() {
		EnabledManager manager = new EnabledManager();
		CompositeFilterCallback filter = new CompositeFilterCallback();
		filter.addRule(FilterRule.Type.SUFFICE, null, "*.sw1");
		MyCallback callback = new MyCallback();
		filter.addCallback(callback);
		manager.callback().addCallback(filter);

		Stopwatch sw1 = manager.getStopwatch("whatever.sw1");
		Stopwatch sw2 = manager.getStopwatch("whatever.sw2");

		sw1.start().stop();
		Assert.assertTrue(callback.isTriggered());
		sw2.start().stop();
		Assert.assertFalse(callback.isTriggered());

		Split split1 = sw1.start();
		Assert.assertTrue(callback.isTriggered());
		split1.stop();
		Assert.assertTrue(callback.isTriggered());

		split1 = sw2.start();
		Assert.assertFalse(callback.isTriggered());
		split1.stop();
		Assert.assertFalse(callback.isTriggered());
		manager.callback().removeCallback(filter);
	}

	@Test
	public void testSufficeRuleForEvent() {
		EnabledManager manager = new EnabledManager();
		CompositeFilterCallback filter = new CompositeFilterCallback();
		filter.addRule(FilterRule.Type.SUFFICE, null, "*.sw1", Callback.Event.STOPWATCH_START);
		MyCallback callback = new MyCallback();
		filter.addCallback(callback);
		manager.callback().addCallback(filter);

		Stopwatch sw1 = manager.getStopwatch("whatever.sw1");
		Stopwatch sw2 = manager.getStopwatch("whatever.sw2");

		sw1.start().stop();
		Assert.assertTrue(callback.isTriggered());
		sw2.start().stop();
		Assert.assertFalse(callback.isTriggered());

		Split split1 = sw1.start();
		Assert.assertTrue(callback.isTriggered());
		split1.stop();
		Assert.assertFalse(callback.isTriggered());

		split1 = sw2.start();
		Assert.assertFalse(callback.isTriggered());
		split1.stop();
		Assert.assertFalse(callback.isTriggered());
		manager.callback().removeCallback(filter);
	}
}
