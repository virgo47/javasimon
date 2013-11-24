package org.javasimon.callback.logging;

import java.util.logging.Level;

import org.javasimon.utils.SimonUtils;

import org.testng.annotations.Test;

import static org.javasimon.callback.logging.LogTemplates.everyNMilliseconds;
import static org.javasimon.callback.logging.LogTemplates.everyNSplits;
import static org.javasimon.callback.logging.LogTemplates.toJUL;
import static org.javasimon.callback.logging.LogTemplates.toSLF4J;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for all {@link LogTemplate} implementations.
 *
 * @author gquintana
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "UnusedAssignment"})
public class LogTemplateTest implements LogMessageSource<Object> {
	private String logMessage;

	public String getLogMessage(Object context) {
		return logMessage;
	}

	/**
	 * Test for {@link JULLogTemplate}
	 */
	@Test
	public void testJUL() {
		JULLogTemplate logTemplate = toJUL(getClass().getName(), Level.INFO);
		logMessage = "Test JUL";
		logTemplate.log(null, this);
	}

	/**
	 * Test for {@link SLF4JLogTemplate}
	 */
	@Test
	public void testSLF4J() {
		SLF4JLogTemplate logTemplate = toSLF4J(getClass().getName(), "info", null);
		logMessage = "Test SLF4J without marker";
		logTemplate.log(null, this);
		logTemplate = toSLF4J(getClass().getName(), "info", "marker");
		logMessage = "Test SLF4J with marker";
		logTemplate.log(null, this);
	}

	private static class TestLogTemplate extends LogTemplate<Object> {
		private boolean enabled = true;
		private String message;

		public boolean isEnabled(Object context) {
			return enabled;
		}

		public void setEnabled(boolean loggingEnabled) {
			this.enabled = loggingEnabled;
		}

		public void log(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void clear() {
			this.message = null;
		}
	}

	/**
	 * Test for {@link CounterLogTemplate}
	 */
	@Test
	public void testCounter() {
		TestLogTemplate logTemplate1 = new TestLogTemplate();
		logMessage = "Test Counter";
		CounterLogTemplate<Object> logTemplate2 = everyNSplits(logTemplate1, 3);
		// One
		assertFalse(logTemplate2.log(null, this));
		assertNull(logTemplate1.getMessage());
		// Two
		assertFalse(logTemplate2.log(null, this));
		assertNull(logTemplate1.getMessage());
		// Three
		assertTrue(logTemplate2.log(null, this));
		assertEquals(logTemplate1.getMessage(), logMessage);
		logTemplate1.clear();
		// One
		assertFalse(logTemplate2.log(null, this));
		assertNull(logTemplate1.getMessage());
		// Two
		assertFalse(logTemplate2.log(null, this));
		assertNull(logTemplate1.getMessage());
		// Three
		assertTrue(logTemplate2.log(null, this));
		assertEquals(logTemplate1.getMessage(), logMessage);
		logTemplate1.clear();
	}

	/**
	 * Test for {@link PeriodicLogTemplate}
	 */
	@Test
	public void testPeriodic() throws InterruptedException {
		TestLogTemplate logTemplate1 = new TestLogTemplate();
		logMessage = "Test Periodic";
		PeriodicLogTemplate logTemplate2 = everyNMilliseconds(logTemplate1, 500L);
		String nextTime = SimonUtils.presentTimestamp(logTemplate2.getNextTime());
		// Before
		assertFalse(logTemplate2.log(null, this));
		assertNull(logTemplate1.getMessage());
		// Wait
		Thread.sleep(550L);
		// After
		assertTrue(logTemplate2.log(null, this));
		assertEquals(logTemplate1.getMessage(), logMessage);
		logTemplate1.clear();
		nextTime = SimonUtils.presentTimestamp(logTemplate2.getNextTime());
		// Before
		assertFalse(logTemplate2.log(null, this));
		assertNull(logTemplate1.getMessage());
		// Wait
		Thread.sleep(550L);
		// After
		assertTrue(logTemplate2.log(null, this));
		assertEquals(logTemplate1.getMessage(), logMessage);
		logTemplate1.clear();
	}
}
