package other;

import com.jamonapi.MonitorFactory;
import com.jamonapi.Monitor;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * JamonComparison shows Jamon and Simon monitors next to each other. Wait a few rounds for JVM
 * to warm up and then compare the performance of both. Notice though that you should (be already)
 * decide(d) on the features/needs basis. ;-)
 *
 * Both APIs are run with pure start/stop calls (if you use one monitor a lot and you keep the
 * reference) and then with get-monitor call followed by start/stop (as typical if you don't want
 * to hold the reference to the monitor).
 *
 * You may want to adjust both loops - inner loop count also affects the relative difference in
 * both APIs performance. You may want to comment out {@code stay()} calls. Notice also how precise
 * both APIs are. Typically Simon stopwatch total time is pretty close to half of the overal loop
 * time - which is correct. Jamon precision is strongly affected by usage of ms and if its results
 * are correct it's rather statistical effect of many repetitions in the loop (or luck :-)).
 *
 * Wrap up: You can compare performances of both APIs on your HW + verify Simon's precision.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 7, 2008
 */
public final class JamonComparison {
	private static final int OUTER_LOOP = 1000000;
	private static final int INNER_LOOP = 10;

	private JamonComparison() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		MonitorFactory.enable();

		int round = 1;
		while (true) {
			System.out.println("\nRound: " + round++);
			jamonTest();
			jamonTest2();
			simonTest();
			simonTest2();
		}
	}

	private static void simonTest() {
		Stopwatch stopwatch = SimonManager.getStopwatch("bu");
		stopwatch.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			Split split = stopwatch.start();
			stay();
			split.stop();
		}
		ns = System.nanoTime() - ns;

		printResults(ns, stopwatch, "Simon start/stop");
	}

	private static void simonTest2() {
		SimonManager.getStopwatch("org.javasimon.examples.stopwatch1").reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			Split split = SimonManager.getStopwatch("org.javasimon.examples.stopwatch1").start();
			stay();
			split.stop();
		}
		ns = System.nanoTime() - ns;

		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.stopwatch1");
		printResults(ns, stopwatch, "Simon get+start/stop");
	}

	private static void printResults(long ns, Stopwatch stopwatch, String title) {
		System.out.println(title + ": " + SimonUtils.presentNanoTime(stopwatch.getTotal()) +
			" max: " + SimonUtils.presentNanoTime(stopwatch.getMax()) + " min: " + SimonUtils.presentNanoTime(stopwatch.getMin()) +
			" real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void jamonTest() {
		Monitor monitor = MonitorFactory.getTimeMonitor("bu");
		monitor.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			monitor.start();
			stay();
			monitor.stop();
		}
		ns = System.nanoTime() - ns;

		System.out.println("Jamon start/stop: " + monitor.getTotal() + " max: " + monitor.getMax() + " min: " + monitor.getMin() + " real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void jamonTest2() {
		Monitor monitor = MonitorFactory.getTimeMonitor("bu");
		monitor.reset();

		long ns = System.nanoTime();
		for (int i = 0; i < OUTER_LOOP; i++) {
			stay();
			monitor = MonitorFactory.start("bu");
			stay();
			monitor.stop();
		}
		ns = System.nanoTime() - ns;

		System.out.println("Jamon get+start/stop: " + monitor.getTotal() + " max: " + monitor.getMax() + " min: " + monitor.getMin() + " real: " + SimonUtils.presentNanoTime(ns));
	}

	private static void stay() {
		for (int j = 0; j < INNER_LOOP; j++) {
			j++;
			j--;
		}
	}
}
