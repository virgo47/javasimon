package org.javasimon.utils;

import org.javasimon.EnabledManager;
import org.javasimon.Manager;
import org.javasimon.Split;
import org.javasimon.StopwatchSample;

/**
 * Utility class for benchmark execution.
 *
 * @author virgo47@gmail.com
 * @since 3.1
 */
public class BenchmarkUtils {
	private static final Manager MANAGER = new EnabledManager();

	/**
	 * Runs the list of tasks to be benchmarked and returns {@link StopwatchSample} array with measured results.
	 * Number of warmup runs (without measuring) and measured runs must be specified.
	 * {@link Task} provides the name of the {@link org.javasimon.Stopwatch} that will store results.
	 * <p/>
	 * Tasks should not be extremely short - see {@link Task} javadoc for more.
	 *
	 * @param warmupRuns number of runs before the measuring starts
	 * @param measuredRuns number of measured runs
	 * @param tasks list of tasks to measure
	 * @return result of the measured runs as an array of stopwatch objects in the order of the tasks
	 */
	public static StopwatchSample[] run(int warmupRuns, int measuredRuns, Task... tasks) {
		warmup(warmupRuns, tasks);
		measure(measuredRuns, tasks);
		presentSummary(tasks);
		StopwatchSample[] result = new StopwatchSample[tasks.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = MANAGER.getStopwatch(tasks[i].stopwatchName).sample();
		}
		return result;
	}

	private static void warmup(int warmupRuns, Task[] tasks) {
		for (int i = 1; i <= warmupRuns; i++) {
			System.out.println("\nWarmup run #" + i);
			for (Task task : tasks) {
				warmupTask(task);
			}
		}
	}

	private static void warmupTask(Task task) {
		try {
			task.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void measure(int measuredRuns, Task[] tasks) {
		for (int i = 1; i <= measuredRuns; i++) {
			System.out.println("\nMeasured run #" + i);
			for (Task task : tasks) {
				task.run();
			}
		}
	}

	private static void presentSummary(Task[] tasks) {
		System.out.println("\nSUMMARY:");
		for (Task task : tasks) {
			System.out.println(task.stopwatchName + ": " +
				MANAGER.getStopwatch(task.stopwatchName));
		}
	}

	/**
	 * Helper object that requires implementing the {@link #perform()} method with benchmarked block of code.
	 * Calling {@link #run()} executes the code and measures statistics using the stopwatch named in the
	 * constructor. Calling {@link #perform()} executes the code without the stopwatch being used.
	 * <p/>
	 * It is not recommended to implement too short Task repeated for many runs (thousands or millions)
	 * but rather to impelement loop in the task to measure short operations and run the Task for units
	 * of times (tens, hundreds). Otherwise Simon overhead (mostly {@link System#nanoTime()} call) may
	 * distort the results. If the measured operation is extremely short even the for loop can distort the results.
	 */
	public static abstract class Task implements Runnable {
		private String stopwatchName;

		/**
		 * Protected constructor intended for extension.
		 *
		 * @param stopwatchName name of the stopwatch (measuring name)
		 */
		protected Task(String stopwatchName) {
			this.stopwatchName = stopwatchName;
		}

		/**
		 * Executes {@link #perform()} and measures the run using the Stopwatch named in the constructor.
		 */
		@Override
		public void run() {
			System.out.print(stopwatchName + ": ");
			Split split = Split.start();
			try {
				perform();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				split.stop();
				System.out.println(split.presentRunningFor());
				MANAGER.getStopwatch(stopwatchName).addSplit(split);
			}
		}

		/**
		 * Performes the measured block of code without actual measuring.
		 *
		 * @throws Exception any exception can be thrown here
		 */
		public abstract void perform() throws Exception;
	}
}
