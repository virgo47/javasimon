package org.javasimon.examples.perf;

import java.util.Map;
import java.util.TreeMap;

/**
 * SystemTimersPrecision checks the granularity of both System timers. It counts how many times
 * returned values for ms/ns changes and prints the results. Loop is repeated 5 times. After that
 * it executes another loop that contains five rapid System.nanoTime() calls assigned to five
 * different longs. This is repeated many times (see LOOP constant), although with nanoTime being
 * native method it's probably completely pointless. However results are printed 5 times during the
 * loop and you can check how well nanoTime call works.
 *
 * See http://code.google.com/p/javasimon/wiki/SystemTimersGranularity for more.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SystemTimersPrecision {
	private static final long LOOP = 10000000;
	private static final long MS_RUN = 500;

	private SystemTimersPrecision() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		for (int round = 1; round <= 5; round++) {
			Map<Long, Integer> deltaMsCount = new TreeMap<>();
			Map<Long, Integer> deltaNsCount = new TreeMap<>();
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
					Integer count = deltaMsCount.get(delta);
					if (count == null) {
						count = 0;
					}
					deltaMsCount.put(delta, ++count);
					msChanges++;
				}
				if (newNs != ns) {
					long delta = newNs - ns;
					Integer count = deltaNsCount.get(delta);
					if (count == null) {
						count = 0;
					}
					deltaNsCount.put(delta, ++count);
					nsChanges++;
				}
				ms = newMs;
				ns = newNs;
			}
			System.out.println("msChanges: " + msChanges + " during " + (System.currentTimeMillis() - initMs) + " ms");
			System.out.println("deltaMsCount = " + deltaMsCount);
			System.out.println("nsChanges: " + nsChanges + " during " + (System.nanoTime() - initNs) + " ns");
			System.out.println("deltaNsCount = " + deltaNsCount);

			// now something else...
			long msCount = 0;
			initMs = System.currentTimeMillis();
			while (true) {
				long newMs = System.currentTimeMillis();
				msCount++;
				if (newMs - initMs >= MS_RUN) {
					break;
				}
			}
			System.out.println("currentTimeMillis msCount = " + msCount);

			long nsCount = 0;
			initMs = System.nanoTime() / 1000000;
			while (true) {
				long newMs = System.nanoTime() / 1000000;
				nsCount++;
				if (newMs - initMs >= MS_RUN) {
					break;
				}
			}
			System.out.println("nanoTime msCount = " + nsCount);
			System.out.println("Ratio ms/ns: " + msCount / (double) nsCount);
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
				System.out.println("ns3 = " + ns3 + " (diff: " + (ns3 - ns2) + ")");
				System.out.println("ns4 = " + ns4 + " (diff: " + (ns4 - ns3) + ")");
				System.out.println("ns5 = " + ns5 + " (diff: " + (ns5 - ns4) + ")");
			}
		}

		System.out.println();

		// last thing - we will count how many nanoTimes in a row will have the same value
		for (int round = 1; round <= LOOP; round++) {
			long val = System.nanoTime();
			int count = 1;
			while (val == System.nanoTime()) {
				count++;
			}
			if (round % (LOOP / 5) == 0) {
				System.out.println("Change after " + count + " calls");
			}
		}
	}
}
