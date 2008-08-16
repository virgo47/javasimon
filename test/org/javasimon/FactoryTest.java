package org.javasimon;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;

/**
 * FactoryTest.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 8, 2008
 */
public final class FactoryTest {
	private static final int FRESH_FACTORY_SIMON_LIST_SIZE = 1;
	private static final int SIMON_COUNT_AFTER_COUNTER_ADDED = 5;

	private static final String ORG_JAVASIMON_TEST_COUNTER = "org.javasimon.test.counter";
	private static final String ORG_JAVASIMON_INHERIT_SW1 = "org.javasimon.inherit.sw1";
	private static final String ORG_JAVASIMON_ENABLED_SW1 = "org.javasimon.enabled.sw1";
	private static final String ORG_JAVASIMON_DISABLED_SW1 = "org.javasimon.disabled.sw1";

	@BeforeMethod
	public void resetAndEnable() {
		SimonFactory.enable();
		SimonFactory.reset();
	}

	@Test
	public void testSimonCreation() {
		Assert.assertEquals(SimonFactory.simonNames().size(), FRESH_FACTORY_SIMON_LIST_SIZE);
		SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).increment();
		Assert.assertEquals(SimonFactory.simonNames().size(), SIMON_COUNT_AFTER_COUNTER_ADDED);

		Assert.assertTrue(SimonFactory.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent() instanceof UnknownSimon);
		Assert.assertEquals(SimonFactory.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent().getChildren().size(), 1);
		String parentName = SimonFactory.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent().getName();
		SimonFactory.getCounter(parentName);
		Assert.assertTrue(SimonFactory.getSimon(parentName) instanceof Counter);
		Assert.assertEquals(SimonFactory.getSimon(parentName).getChildren().size(), 1);
	}

	@Test(expectedExceptions = SimonException.class)
	public void testSimonCreationProblem() {
		SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER);
		SimonFactory.getStopwatch(ORG_JAVASIMON_TEST_COUNTER);
	}

	@Test
	public void testDisabledSimons() {
		SimonFactory.getRootSimon().setState(SimonState.DISABLED, true);
		Assert.assertFalse(SimonFactory.getRootSimon().isEnabled());
		Assert.assertFalse(SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());

		SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).setState(SimonState.ENABLED, false);
		Assert.assertTrue(SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());
		Assert.assertFalse(SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).getParent().isEnabled());

		SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).setState(SimonState.INHERIT, false);
		Assert.assertFalse(SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());
		Assert.assertFalse(SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).getParent().isEnabled());

		SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).setState(SimonState.DISABLED, false);
		Assert.assertEquals(SimonFactory.getRootSimon().getName(), SimonFactory.ROOT_SIMON_NAME);

		SimonFactory.disable();
		Assert.assertNull(SimonFactory.getSimon(ORG_JAVASIMON_TEST_COUNTER).getParent());
		Assert.assertTrue(SimonFactory.getRootSimon() instanceof NullSimon);
		Assert.assertNull(SimonFactory.getRootSimon().getName());
	}

	@Test
	public void testStatePropagation() {
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());

		SimonFactory.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).getParent().setState(SimonState.ENABLED, true);
		SimonFactory.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).getParent().setState(SimonState.DISABLED, true);
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertFalse(SimonFactory.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());

		SimonFactory.getRootSimon().setState(SimonState.DISABLED, false);
		Assert.assertFalse(SimonFactory.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertFalse(SimonFactory.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());

		SimonFactory.getRootSimon().setState(SimonState.ENABLED, true);
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_INHERIT_SW1).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_ENABLED_SW1).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_DISABLED_SW1).isEnabled());
	}

	@Test
	public void testUnknownSimonReplacement() {
		Counter counter = SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER);
		Assert.assertTrue(counter.isEnabled());
		String parentName = counter.getParent().getName();
		Assert.assertTrue(SimonFactory.getSimon(parentName).isEnabled());
		Assert.assertTrue(SimonFactory.getStopwatch(parentName).isEnabled());
		Assert.assertTrue(SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER).isEnabled());
	}

	@Test
	public void testDestroySimon() {
		SimonFactory.getStopwatch(ORG_JAVASIMON_TEST_COUNTER);
		SimonFactory.destroySimon(ORG_JAVASIMON_TEST_COUNTER);
		Assert.assertNull(SimonFactory.getSimon(ORG_JAVASIMON_TEST_COUNTER));
		SimonFactory.getCounter(ORG_JAVASIMON_TEST_COUNTER);

		String counterChildName = ORG_JAVASIMON_TEST_COUNTER + ".child";
		Stopwatch child = SimonFactory.getStopwatch(counterChildName);
		SimonFactory.destroySimon(ORG_JAVASIMON_TEST_COUNTER);
		Assert.assertTrue(SimonFactory.getSimon(ORG_JAVASIMON_TEST_COUNTER) instanceof UnknownSimon);
		Assert.assertTrue(SimonFactory.getStopwatch(ORG_JAVASIMON_TEST_COUNTER).getChildren().contains(child));
	}

	@Test
	public void testGeneratedNames() {
		Assert.assertEquals(SimonFactory.generateName("-stopwatch", true), getClass().getName() + ".testGeneratedNames-stopwatch");

		SimonFactory.disable();
		Assert.assertNull(SimonFactory.generateName("-stopwatch", true));
	}
}
