package other;

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
 * @created Aug 13, 2008
 */
public final class SystemTimersPrecision {
	private static final long LOOP = 10000000;

	private SystemTimersPrecision() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
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
			System.out.println("deltaCount = " + deltaCount);
			System.out.println("nsChanges: " + nsChanges + " during " + (System.nanoTime() - initNs) + " ns");
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
	}
}
