package org.javasimon;

import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.clock.TestClock;
import org.javasimon.utils.SimonUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tests SimonManager behavior.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class ManagerTest extends SimonUnitTest {

	private static final int FRESH_MANAGER_SIMON_LIST_SIZE = 1;
	private static final int SIMON_COUNT_AFTER_COUNTER_ADDED = 5;

	private static final String ORG_JAVASIMON_TEST_COUNTER = "org.javasimon.test.counter";
	private static final String ORG_JAVASIMON_INHERIT_SW1 = "org.javasimon.inherit.sw1";
	private static final String ORG_JAVASIMON_ENABLED_SW1 = "org.javasimon.enabled.sw1";
	private static final String ORG_JAVASIMON_DISABLED_SW1 = "org.javasimon.disabled.sw1";

	@Test
	public void testSimonCreation() {
		Assert.assertEquals(SimonManager.getSimonNames().size(), FRESH_MANAGER_SIMON_LIST_SIZE);
		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).increase();
		Assert.assertEquals(SimonManager.getSimonNames().size(), SIMON_COUNT_AFTER_COUNTER_ADDED);

		Assert.assertTrue(SimonManager.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent() instanceof UnknownSimon);
		Assert.assertEquals(SimonManager.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent().getChildren().size(), 1);
		String parentName = SimonManager.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent().getName();
		SimonManager.getCounter(parentName);
		Assert.assertTrue(SimonManager.getSimon(parentName) instanceof Counter);
		Assert.assertEquals(SimonManager.getSimon(parentName).getChildren().size(), 1);
	}

	@Test(expectedExceptions = SimonException.class)
	public void testSimonCreationProblem() {
		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER);
		SimonManager.getStopwatch(ORG_JAVASIMON_TEST_COUNTER);
	}

	@Test
	public void testDisabledSimons() {
		SimonManager.getRootSimon().setState(SimonState.DISABLED, true);
		Assert.assertFalse(SimonManager.getRootSimon().isEnabled());
		Assert.assertFalse(SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());

		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).setState(SimonState.ENABLED, false);
		Assert.assertTrue(SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());
		Assert.assertFalse(SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).getParent().isEnabled());

		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).setState(SimonState.INHERIT, false);
		Assert.assertFalse(SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());
		Assert.assertFalse(SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).getParent().isEnabled());

		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).setState(SimonState.DISABLED, false);
		Assert.assertEquals(SimonManager.getRootSimon().getName(), Manager.ROOT_SIMON_NAME);

		SimonManager.disable();
		Assert.assertNull(SimonManager.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent());
		Assert.assertTrue(SimonManager.getRootSimon() instanceof NullSimon);
		Assert.assertNull(SimonManager.getRootSimon().getName());
	}

	@Test
	public void testStatePropagation() {
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());

		SimonManager.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).getParent().setState(SimonState.ENABLED, true);
		SimonManager.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).getParent().setState(SimonState.DISABLED, true);
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertFalse(SimonManager.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());

		SimonManager.getRootSimon().setState(SimonState.DISABLED, false);
		Assert.assertFalse(SimonManager.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertFalse(SimonManager.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());

		SimonManager.getRootSimon().setState(SimonState.ENABLED, true);
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());
	}

	@Test
	public void testUnknownSimonReplacement() {
		Counter counter = SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER);
		Assert.assertTrue(counter.isEnabled());
		String parentName = counter.getParent().getName();
		Assert.assertTrue(SimonManager.getSimon(parentName).isEnabled());
		Assert.assertTrue(SimonManager.getStopwatch(parentName).isEnabled());
		Assert.assertTrue(SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());
	}

	@Test
	public void testDestroySimon() {
		SimonManager.getStopwatch(ORG_JAVASIMON_TEST_COUNTER);
		SimonManager.destroySimon(ORG_JAVASIMON_TEST_COUNTER);
		Assert.assertNull(SimonManager.getSimon(ORG_JAVASIMON_TEST_COUNTER));
		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER);

		String counterChildName = ORG_JAVASIMON_TEST_COUNTER + ".child";
		Stopwatch child = SimonManager.getStopwatch(counterChildName);
		SimonManager.destroySimon(ORG_JAVASIMON_TEST_COUNTER);
		Assert.assertTrue(SimonManager.getSimon(ORG_JAVASIMON_TEST_COUNTER) instanceof UnknownSimon);
		Assert.assertTrue(SimonManager.getStopwatch(ORG_JAVASIMON_TEST_COUNTER).getChildren().contains(child));
	}

	@Test
	public void testGeneratedNames() {
		Assert.assertEquals(SimonUtils.generateNameForClassAndMethod("-stopwatch"), getClass().getName() + ".testGeneratedNames-stopwatch");
	}

	@Test(expectedExceptions = SimonException.class)
	public void testCantCreateRoot() {
		SimonManager.getStopwatch(Manager.ROOT_SIMON_NAME);
	}

	@Test(expectedExceptions = SimonException.class)
	public void testCantDestroyRoot() {
		SimonManager.destroySimon(Manager.ROOT_SIMON_NAME);
	}

	@Test
	public void testAnonymousSimon() {
		Stopwatch stopwatch = SimonManager.getStopwatch(null);
		Assert.assertTrue(stopwatch.isEnabled());
		Assert.assertEquals(stopwatch.getState(), SimonState.ENABLED);
		stopwatch.start().stop();
		Assert.assertNotNull(((AbstractSimon) stopwatch).manager);

		final Queue<String> messages = new LinkedList<>();
		SimonManager.callback().removeAllCallbacks();
		SimonManager.callback().addCallback(new CallbackSkeleton() {
			public void onStopwatchStart(Split split) {
				messages.add("start");
			}

			public void onStopwatchStop(Split split, StopwatchSample sample) {
				messages.add("stop");
			}
		});
		stopwatch.start().stop();
		Assert.assertEquals(messages.size(), 2);
	}

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Simon name must match following pattern.*")
	public void testInvalidName() {
		SimonManager.getStopwatch("Inv@lid name!@#$%");
	}

	@Test
	public void testValidNames() {
		SimonManager.getStopwatch("A-Za-z0-9.,@$%()<>_-");
		SimonManager.getStopwatch("bubu[1]");
	}

	@Test
	public void simonTreeOnDisabledManager() {
		SimonManager.disable();
		Assert.assertNull(SimonUtils.simonTreeString(SimonManager.getRootSimon()));
	}

	@Test
	public void failedInitialization() {
		final Queue<String> messages = new LinkedList<>();
		SimonManager.callback().removeAllCallbacks();
		SimonManager.callback().addCallback(new CallbackSkeleton() {
			public void onManagerWarning(String warning, Exception cause) {
				messages.add(warning);
			}
		});

		System.setProperty(SimonManager.PROPERTY_CONFIG_RESOURCE_NAME, "whateverNonexistent");
		SimonManager.init();
		Assert.assertEquals(messages.poll(), "SimonManager initialization error");
		System.getProperties().remove(SimonManager.PROPERTY_CONFIG_RESOURCE_NAME);
	}

	@Test
	public void testRemoveIncrementalSimons() {
		TestClock testClock = new TestClock();
		EnabledManager enabledManager = new EnabledManager(testClock);
		Stopwatch stopwatch = enabledManager.getStopwatch("stopwatch");
		String key = "key";
		stopwatch.sampleIncrement(key);

		long timeInTheFuture = 1000;
		enabledManager.purgeIncrementalSimonsOlderThan(timeInTheFuture);
		boolean incrementalSimonExisted = stopwatch.stopIncrementalSampling(key);
		Assert.assertFalse(incrementalSimonExisted);
	}
}
