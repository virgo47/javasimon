package org.javasimon.examples;

import java.util.Map;
import java.util.TreeMap;

/**
 * SystemTimersPrecision checks the granularity of both System timers. It counts how many times
 * returned values for ms/ns changes and prints the results. Loop is repeated 5 times. After that
 * it executes another loop that contains five rapid System.nanoTime() calls assigned to five
 * different longs. This is repeated many times (see LOOP constant), although with nanoTime being
 * native method it's probably completely pointless. However results are printed 5 times during the
 * loop and you can check how well nanoTime call works.
 * <p/>
 * On my testing environment (Intel Q6600 Core 2 Quad @2.4GHz, 2GB RAM, Windows XP, JDK 1.6.0_05)
 * it currently provides following output (fifth rounds only):
 * <pre>
 * msChanges: 188 during 2938 ms
 * nsChanges: 10000000 during 2930945951 ns
 * deltaCount = {15=70, 16=118}
 * ...
 * ns1 = 12506467484797
 * ns2 = 12506467485045 (diff: 248)
 * ns2 = 12506467485289 (diff: 244)
 * ns2 = 12506467485536 (diff: 247)
 * ns2 = 12506467485784 (diff: 248)
 * </pre>
 * As you can see, ms timer is changed quite rarely while ns timer is changed on every call. This promises much
 * better precision with nanoTime instead of currentTimeMillis. However you may observe completely different
 * results on different platform. That's why you should always find out how these calls behave in your target
 * environment.
 * <p/>
 * Linux test shows roughly 1ms precision of 1ms timer, which is much better if you measure something that
 * lasts more than 1 ms (Intel Pentium 4@2.8 GHz, 2.5GB RAM, Ubuntu 8.04, kernel 2.6.24, JDK 1.6.0_02):
 * <pre>
 * msChanges: 9132 during 9249 ms
 * nsChanges: 10000000 during 9249735411 ns
 * </pre>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 13, 2008
 */
public final class SystemTimersPrecision {
	private static final long LOOP = 10000000;

	private SystemTimersPrecision() {
		super();
	}

	public static void main(String[] args) {
		for (int round = 1; round <= 5; round++) {
			Map<Long, Integer> deltaCount = new TreeMap<Long, Integer>();
			System.out.println("\nRound: " + round);
			long msChanges = 0;
			long nsChanges = 0;

			long initMs = System.currentTimeMillis();
			long initNs = System.nanoTime();

			long ms = initMs;
			long ns = initNs;
			for (int i = 0; i < LOOP; i++) {
				long newMs = System.currentTimeMillis();
				long newNs = System.nanoTime();
				if (newMs != ms) {
					long delta = newMs - ms;
					Integer count = deltaCount.get(delta);
					if (count == null) {
						count = 0;
					}
					deltaCount.put(delta, ++count);
					msChanges++;
				}
				if (newNs != ns) {
					nsChanges++;
				}
				ms = newMs;
				ns = newNs;
			}
			System.out.println("msChanges: " + msChanges + " during " + (System.currentTimeMillis() - initMs) + " ms");
			System.out.println("nsChanges: " + nsChanges + " during " + (System.nanoTime() - initNs) + " ns");
			System.out.println("deltaCount = " + deltaCount);
		}

		for (int round = 1; round <= LOOP; round++) {
			long ns1 = System.nanoTime();
			long ns2 = System.nanoTime();
			long ns3 = System.nanoTime();
			long ns4 = System.nanoTime();
			long ns5 = System.nanoTime();
			if (round % (LOOP / 5) == 0) {
				System.out.println("\nns1 = " + ns1);
				System.out.println("ns2 = " + ns2 + " (diff: " + (ns2 - ns1) + ")");
				System.out.println("ns2 = " + ns3 + " (diff: " + (ns3 - ns2) + ")");
				System.out.println("ns2 = " + ns4 + " (diff: " + (ns4 - ns3) + ")");
				System.out.println("ns2 = " + ns5 + " (diff: " + (ns5 - ns4) + ")");
			}
		}
	}
}
