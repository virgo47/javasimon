package org.javasimon.callback.quantiles;

import java.io.IOException;
import java.util.Properties;

import org.javasimon.SimonManager;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link PropertiesQuantilesCallback}
 */
public class PropertiesQuantilesCallbackTest {
	private static class LocalPropertiesQuantilesCallback extends PropertiesQuantilesCallback {
		private LocalPropertiesQuantilesCallback(Properties properties) {
			super(properties);
		}

		public Buckets createBuckets(String name) {
			return super.createBuckets(SimonManager.getStopwatch(name));
		}
	}

	@Test
	public void testCreateBuckets() throws IOException {
		Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("quantiles.properties"));
		LocalPropertiesQuantilesCallback callback = new LocalPropertiesQuantilesCallback(properties);

		Buckets buckets = callback.createBuckets("org.test.Test");
		assertEquals(buckets.getMin(), 0);
		assertEquals(buckets.getMax(), 60000);
		assertEquals(buckets.getBucketNb(), 5);
		assertEquals(buckets.getClass(), LinearBuckets.class);

		buckets = callback.createBuckets("org.javasimon.Test");
		assertEquals(buckets.getMin(), 0);
		assertEquals(buckets.getMax(), 60000);
		assertEquals(buckets.getBucketNb(), 10);
		assertEquals(buckets.getClass(), LinearBuckets.class);

		buckets = callback.createBuckets("org.javasimon.slow.SlowClass");
		assertEquals(buckets.getMin(), 0);
		assertEquals(buckets.getMax(), 300000);
		assertEquals(buckets.getBucketNb(), 10);
		assertEquals(buckets.getClass(), LinearBuckets.class);

		buckets = callback.createBuckets("org.javasimon.special.Test");
		assertEquals(buckets.getMin(), 0);
		assertEquals(buckets.getMax(), 60000);
		assertEquals(buckets.getBucketNb(), 10);
		assertEquals(buckets.getClass(), ExponentialBuckets.class);
	}
}
