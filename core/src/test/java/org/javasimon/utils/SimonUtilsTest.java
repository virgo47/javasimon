package org.javasimon.utils;


import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * OtherTestNG.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SimonUtilsTest extends SimonUnitTest {

	@DataProvider(name = "test-data-utils")
	public Object[][] testDataUtilsProvider() {
		return new Object[][]{
			{1, "1 ns"},
			{47, "47 ns"},
			{999, "999 ns"},
			{1047, "1.05 us"},
			{11047, "11.0 us"},
			{11000, "11.0 us"},
			{141047, "141 us"},
			{942141047, "942 ms"},
			{942141047666L, "942 s"},
			{10942141047666L, "10942 s"},
		};
	}

	@Test(dataProvider = "test-data-utils")
	public void testUtilPresentNanoTime(long ns, String nsString) throws InterruptedException {
		Assert.assertEquals(SimonUtils.presentNanoTime(ns), nsString);
	}

	@Test
	public void testStringCompact() {
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 4), "as...w");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 7), "asd...w");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 12), "asdfad...5tw");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 17), "asdfadfa... 345tw");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 18), "asdfadfasd;a 345tw");
		Assert.assertEquals(SimonUtils.compact("asdfadfasd;a 345tw", 30), "asdfadfasd;a 345tw");
	}
}
