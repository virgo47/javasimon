package org.javasimon;

/**
 * DisabledEnabledComparison.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 5, 2008
 */
public final class DisabledEnabledComparison {
	private static final String TEST1_SIMON_NAME = SimonFactory.generateName("-stopwatch", true);

	private static final int OUTER_LOOP = 100;
	private static final int INNER_LOOP = 1000;

	public static void main(String[] args) {
		long disbledSimonFactory = 0;
		long enabledSimonFactory = 0;
		long disbledTestSimon = 0;
		long disbledTopSimon = 0;
		long enabledTestSimon = 0;
		long pureTest = 0;
		long simonMeasurement = 0;
		long simonCounter = 0;

		for (int round = 0; round < 10; round++) {
			SimonFactory.reset();
			SimonFactory.disable();
			SimonFactory.enableOverheadSimon();

			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			long ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			disbledSimonFactory += System.nanoTime() - ns;

			SimonFactory.enable();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			enabledSimonFactory += System.nanoTime() - ns;

			SimonFactory.getSimon(TEST1_SIMON_NAME).disable(false);
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			disbledTestSimon += System.nanoTime() - ns;

			SimonFactory.getRootSimon().disable(true);
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			disbledTopSimon += System.nanoTime() - ns;

			SimonFactory.getSimon(TEST1_SIMON_NAME).enable(false);
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1();
			}
			enabledTestSimon += System.nanoTime() - ns;

			for (int i = 0; i < OUTER_LOOP; i++) {
				test1pure();
			}
			ns = System.nanoTime();
			for (int i = 0; i < OUTER_LOOP; i++) {
				test1pure();
			}
			pureTest += System.nanoTime() - ns;
			simonMeasurement += SimonFactory.getStopwatch(TEST1_SIMON_NAME).getElapsedNanos();
			simonCounter += SimonFactory.getStopwatch(TEST1_SIMON_NAME).getCounter();
			System.out.println("round " + round + " with cumulative Simon overhead: " + SimonUtils.presentTime(SimonFactory.getOverheadSimon().getElapsedNanos()));
		}
		System.out.println("Disabled Simon Factory: " + SimonUtils.presentTime(disbledSimonFactory));
		System.out.println("Enabled Simon Factory: " + SimonUtils.presentTime(enabledSimonFactory));
		System.out.println("Disabled test Simon: " + SimonUtils.presentTime(disbledTestSimon));
		System.out.println("Disabled top Simon: " + SimonUtils.presentTime(disbledTopSimon));
		System.out.println("Explicitly enbaled test Simon: " + SimonUtils.presentTime(enabledTestSimon));
		System.out.println("Pure test method without Simon: " + SimonUtils.presentTime(pureTest));
		System.out.println("Simon measurement: " + SimonUtils.presentTime(simonMeasurement));
		System.out.println("Simon counter: " + simonCounter);
		System.out.println("Simon overhead: " + SimonUtils.presentTime(SimonFactory.getOverheadSimon().getElapsedNanos()));
		System.out.println("Simon overhead counter: " + SimonFactory.getOverheadSimon().getCounter());
	}

	public static final String[] strings = {"AB", "ZD", "ZK", "SE", "AM", "whatever"};

	private static void test1() {
		SimonStopwatch simon = SimonFactory.getStopwatch(TEST1_SIMON_NAME);
		simon.start();
		StringBuilder builder = null;
		for (int i = 0; i < INNER_LOOP; i++) {
			builder = new StringBuilder();
			for (String s : strings) {
				builder.append(s);
			}
			builder.reverse();
		}
		builder.setLength(0);
		Runtime.getRuntime().gc();
		simon.stop();
	}

	private static void test1pure() {
		StringBuilder builder = null;
		for (int i = 0; i < INNER_LOOP; i++) {
			builder = new StringBuilder();
			for (String s : strings) {
				builder.append(s);
			}
			builder.reverse();
		}
		builder.setLength(0);
		Runtime.getRuntime().gc();
	}
}
