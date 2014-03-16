package org.javasimon.callback.quantiles;

import java.util.List;

import org.javasimon.SimonUnitTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Alexej Vlasov
 */
public class ExponentialBucketsTest extends SimonUnitTest {

	@Test
	public void testAdd() {
		Buckets buckets = new ExponentialBuckets(1L, 10000L, 4);
		buckets.addValue(1);
		buckets.addValue(5);
		buckets.addValue(10);
		buckets.addValue(200);
		buckets.addValue(1000);
		buckets.addValue(1001);
		buckets.addValue(5000);
		buckets.addValue(10000);
		List<Bucket> bucketList = buckets.getBuckets();
		assertEquals(bucketList.size(), 6);
		assertEquals(bucketList.get(0).getCount(), 0);// 0-1
		assertEquals(bucketList.get(1).getCount(), 2);// 1-10
		assertEquals(bucketList.get(2).getCount(), 1);// 10-100
		assertEquals(bucketList.get(3).getCount(), 1);// 100-1000
		assertEquals(bucketList.get(4).getCount(), 3);// 1000-10000
		assertEquals(bucketList.get(5).getCount(), 1);// 10000-
	}

	@Test
	public void testQuantiles1() {
		Buckets buckets = new ExponentialBuckets(1L, 64L, 6);
		buckets.addValue(1);
		buckets.addValue(2);
		buckets.addValue(25);
		buckets.addValue(33);
		buckets.addValue(34);
		assertEquals(buckets.getMedian(), 16D, 0.1D);
		buckets.clear();
		// 2 values in each bucket
		buckets.addValue(1);
		buckets.addValue(2);
		buckets.addValue(4);
		buckets.addValue(33);
		buckets.addValue(35);
		buckets.addValue(65);
		assertEquals(buckets.getMedian(), 32D, 0.1D); // End of second bucket
	}

	@Test
	public void testQuantiles2() {
		Buckets buckets = new ExponentialBuckets(1L, 10000L, 4);
		// 9 Values in 2st and 3nd buckets
		buckets.addValue(5);
		buckets.addValue(10);
		buckets.addValue(20);
		buckets.addValue(30);
		buckets.addValue(40);
		buckets.addValue(50);
		buckets.addValue(110);
		buckets.addValue(120);
		buckets.addValue(130);
		// 1 value in another bucket
		buckets.addValue(1050);
		assertEquals(buckets.getQuantile(0.9D), 1000D, 0.1D);// End of second bucket
	}
}
