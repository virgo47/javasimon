package org.javasimon.callback.quantiles;

import org.javasimon.Stopwatch;

/** Enumeration of buckets types, used for configuration purposes. */
public enum BucketsType {
	LINEAR() {
		public Buckets createBuckets(Stopwatch stopwatch, long min, long max, int bucketNb) {
			return new LinearBuckets(min, max, bucketNb);
		}
	},
	EXPONENTIAL() {
		public Buckets createBuckets(Stopwatch stopwatch, long min, long max, int bucketNb) {
			return new ExponentialBuckets(min, max, bucketNb);
		}
	};

	/** Factory method to create {@link Buckets}. */
	public abstract Buckets createBuckets(Stopwatch stopwatch, long min, long max, int bucketNb);
}
