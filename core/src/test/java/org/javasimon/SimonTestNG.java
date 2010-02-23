package org.javasimon;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * StopwatchTest.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 11, 2008
 */
public final class SimonTestNG {
	private static final String STOPWATCH_NAME = "org.javasimon.test-stopwatch";

	@BeforeMethod
	public void resetAndEnable() {
		SimonManager.clear();
		SimonManager.enable();
	}

	@Test
	public void attributesTest() {
		Simon simon = SimonManager.getStopwatch(STOPWATCH_NAME);
		Assert.assertFalse(simon.getAttributeNames().hasNext());
		Assert.assertNull(simon.getAttribute("key"));
		simon.setAttribute("key", "value");
		Iterator<String> attributeNamesIterator = simon.getAttributeNames();
		Assert.assertTrue(attributeNamesIterator.hasNext());
		Assert.assertEquals(attributeNamesIterator.next(), "key");
		Assert.assertFalse(attributeNamesIterator.hasNext());
		Assert.assertEquals(simon.getAttribute("key"), "value");

		// with existing attributes, null simon should return nothing
		SimonManager.disable();
		Simon nullSimon = SimonManager.getStopwatch(STOPWATCH_NAME);
		Assert.assertFalse(nullSimon.getAttributeNames().hasNext());
		Assert.assertNull(nullSimon.getAttribute("key"));
		SimonManager.enable();

		// after manager re-enable, it should be ok again
		simon = SimonManager.getStopwatch(STOPWATCH_NAME);
		Assert.assertTrue(simon.getAttributeNames().hasNext());
		Assert.assertEquals(simon.getAttribute(simon.getAttributeNames().next()), "value");

		// after removal...
		simon.removeAttribute("key");
		Assert.assertFalse(simon.getAttributeNames().hasNext());
		Assert.assertNull(simon.getAttribute("key"));
	}
}