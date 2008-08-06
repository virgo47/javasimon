package org.javasimon;

/**
 * OverheadSimonOverhead .
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 6, 2008
 */
public final class OverheadSimonOverhead {
	private static final int LOOP = 10000000;
	private static String name;

	public static void main(String[] args) {
		SimonFactory.disbleOverheadSimon();
		name = SimonFactory.generateName("-stopwatch", false);

		SimonFactory.reset();
		// warmup
		theMethod();

		long ns = System.nanoTime();
		theMethod();
		System.out.println("Disabled overhead: " + SimonUtils.presentTime(System.nanoTime() - ns));
		System.out.println("SimonFactory.getOverheadSimon() = " + SimonFactory.getOverheadSimon());

		SimonFactory.reset();
		SimonFactory.enableOverheadSimon();
		// warmup
		theMethod();

		ns = System.nanoTime();
		theMethod();
		System.out.println("Enabled overhead: " + SimonUtils.presentTime(System.nanoTime() - ns));
		System.out.println("SimonFactory.getOverheadSimon() = " + SimonFactory.getOverheadSimon());
	}

	private static void theMethod() {
		for (int i = 0; i < LOOP; i++) {
			SimonStopwatch simon = SimonFactory.getStopwatch(name);
			simon.start();
			simon.stop();
		}
	}
}
