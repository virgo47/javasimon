package org.javasimon.spring;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.javasimon.SimonManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** https://github.com/virgo47/javasimon/issues/14 */
@ContextConfiguration(classes = MonitoredPointcutStopwatchNameConfig.class)
public class MonitoredPointcutStopwatchNameTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private MonitoredService serviceOne;

	@Autowired
	private MonitoredService serviceTwo;

	@BeforeMethod
	public void cleanup() {
		SimonManager.clear();
	}

	@Test
	public void bothMethodsShouldBeMonitoredByDifferentSimons() {
		assertEquals(SimonManager.getSimonNames().size(), 1); // root simon is always there
		serviceOne.run();
		String simonOneName = ServiceOne.class.getName() + ".run";
		String simonTwoName = ServiceTwo.class.getName() + ".run";
		assertTrue(SimonManager.getSimonNames().contains(simonOneName));
		assertFalse(SimonManager.getSimonNames().contains(simonTwoName));
		assertEquals(SimonManager.getStopwatch(simonOneName).getCounter(), 1);

		serviceTwo.run();
		assertTrue(SimonManager.getSimonNames().contains(simonTwoName));
		assertEquals(SimonManager.getStopwatch(simonOneName).getCounter(), 1);
		assertEquals(SimonManager.getStopwatch(simonTwoName).getCounter(), 1);
	}

	@Test
	public void bothSimonsAreCreatedInReverseCallOrder() {
		assertEquals(SimonManager.getSimonNames().size(), 1); // root simon is always there
		serviceTwo.run();
		serviceOne.run();
		String simonOneName = ServiceOne.class.getName() + ".run";
		String simonTwoName = ServiceTwo.class.getName() + ".run";
		assertTrue(SimonManager.getSimonNames().contains(simonOneName));
		assertTrue(SimonManager.getSimonNames().contains(simonTwoName));
		assertEquals(SimonManager.getStopwatch(simonOneName).getCounter(), 1);
		assertEquals(SimonManager.getStopwatch(simonTwoName).getCounter(), 1);
	}
}