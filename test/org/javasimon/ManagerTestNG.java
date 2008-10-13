package org.javasimon;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;

/**
 * Tests SimonManager behavior.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 8, 2008
 */
public final class ManagerTestNG {
	private static final int FRESH_MANAGER_SIMON_LIST_SIZE = 1;
	private static final int SIMON_COUNT_AFTER_COUNTER_ADDED = 5;

	private static final String ORG_JAVASIMON_TEST_COUNTER = "org.javasimon.test.counter";
	private static final String ORG_JAVASIMON_INHERIT_SW1 = "org.javasimon.inherit.sw1";
	private static final String ORG_JAVASIMON_ENABLED_SW1 = "org.javasimon.enabled.sw1";
	private static final String ORG_JAVASIMON_DISABLED_SW1 = "org.javasimon.disabled.sw1";

	@BeforeMethod
	public void resetAndEnable() {
		SimonManager.enable();
		SimonManager.reset();
	}

	@Test
	public void testSimonCreation() {
		Assert.assertEquals(SimonManager.simonNames().size(), FRESH_MANAGER_SIMON_LIST_SIZE);
		SimonManager.getCounter(ORG_JAVASIMON_TEST_COUNTER).increment();
		Assert.assertEquals(SimonManager.simonNames().size(), SIMON_COUNT_AFTER_COUNTER_ADDED);

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
		Assert.assertEquals(SimonManager.getRootSimon().getName(), SimonManager.ROOT_SIMON_NAME);

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
		Assert.assertEquals(SimonManager.generateName("-stopwatch", true), getClass().getName() + ".testGeneratedNames-stopwatch");

		SimonManager.disable();
		Assert.assertNull(SimonManager.generateName("-stopwatch", true));
	}

	@Test(expectedExceptions = SimonException.class)
	public void testCantCreateRoot() {
		SimonManager.getUnknown(SimonManager.ROOT_SIMON_NAME);
	}

	@Test(expectedExceptions = SimonException.class)
	public void testCantDestroyRoot() {
		SimonManager.destroySimon(SimonManager.ROOT_SIMON_NAME);
	}
}
