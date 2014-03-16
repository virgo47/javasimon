package org.javasimon.callback.quantiles;

import java.util.List;

import org.javasimon.SimonUnitTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author gquintana
 */
public class LinearBucketsTest extends SimonUnitTest {

	@Test
	public void testAdd() {
		Buckets buckets = new LinearBuckets(0L, 500L, 5);
		buckets.addValue(50);
		buckets.addValue(60);
		buckets.addValue(125);
		buckets.addValue(340);
		buckets.addValue(620);
		List<Bucket> bucketList = buckets.getBuckets();
		assertEquals(7, bucketList.size());
		assertEquals(0, bucketList.get(0).getCount());// 0-
		assertEquals(2, bucketList.get(1).getCount());//   0-100
		assertEquals(1, bucketList.get(2).getCount());// 100-200
		assertEquals(0, bucketList.get(3).getCount());// 200-300
		assertEquals(1, bucketList.get(4).getCount());// 300-400
		assertEquals(0, bucketList.get(5).getCount());// 400-500
		assertEquals(1, bucketList.get(6).getCount());// 500+
	}

	@Test
	public void testQuantiles1() {
		Buckets buckets = new LinearBuckets(0L, 500L, 5);
		buckets.addValue(50);
		buckets.addValue(150);
		buckets.addValue(250);
		buckets.addValue(350);
		buckets.addValue(450);
		assertEquals(250D, buckets.getMedian(), 0.1D);
		buckets.clear();
		// 2 values in each bucket
		buckets.addValue(10);
		buckets.addValue(20);
		buckets.addValue(110);
		buckets.addValue(120);
		buckets.addValue(210);
		buckets.addValue(220);
		buckets.addValue(310);
		buckets.addValue(320);
		assertEquals(200D, buckets.getMedian(), 0.1D); // End of second bucket
	}

	@Test
	public void testQuantiles2() {
		Buckets buckets = new LinearBuckets(0L, 500L, 5);
		// 9 Values in 1st and 2nd buckets
		buckets.addValue(10);
		buckets.addValue(20);
		buckets.addValue(30);
		buckets.addValue(40);
		buckets.addValue(50);
		buckets.addValue(110);
		buckets.addValue(120);
		buckets.addValue(130);
		buckets.addValue(140);
		// 1 value in another bucket
		buckets.addValue(300);
		assertEquals(200D, buckets.getQuantile(0.9D), 0.1D);// End of second bucket
	}
}
