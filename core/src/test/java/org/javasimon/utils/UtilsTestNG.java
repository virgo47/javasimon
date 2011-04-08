package org.javasimon.utils;

import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * OtherTestNG.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class UtilsTestNG {
	@Test
	public void testUtilPresentNanoTime() {
		Assert.assertEquals(SimonUtils.presentNanoTime(1), "1 ns");
		Assert.assertEquals(SimonUtils.presentNanoTime(47), "47 ns");
		Assert.assertEquals(SimonUtils.presentNanoTime(999), "999 ns");
		Assert.assertEquals(SimonUtils.presentNanoTime(1047), "1.05 us");
		Assert.assertEquals(SimonUtils.presentNanoTime(11047), "11.0 us");
		Assert.assertEquals(SimonUtils.presentNanoTime(141047), "141 us");
		Assert.assertEquals(SimonUtils.presentNanoTime(942141047), "942 ms");
		Assert.assertEquals(SimonUtils.presentNanoTime(942141047666L), "942 s");
		Assert.assertEquals(SimonUtils.presentNanoTime(10942141047666L), "10942 s");
	}
}
