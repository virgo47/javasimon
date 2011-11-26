package org.javasimon.utils;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

/**
 * Utility class for benchmark execution.
 *
 * @author virgo47@gmail.com
 * @since 3.1
 */
public class BenchmarkUtils {
	/**
	 * Suffix of the stopwatch that aggregates partial measurings.
	 */
	public static final String STOPWATCH_SUM_SUFFIX = "-sum";

	/**
	 * Runs the list of tasks to be benchmarked and returns {@link Stopwatch} arrah with measured results.
	 * Number of warmup runs (without measuring) and measured runs must be specified.
	 * {@link Task} provides the name of the {@link org.javasimon.Stopwatch} that will store results.
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
			Stopwatch stopwatch = SimonManager.getStopwatch(tasks[i].stopwatchName + STOPWATCH_SUM_SUFFIX);
			stopwatch.setNote(tasks[i].stopwatchName);
			result[i] = stopwatch.sample();
		}
		return result;
	}

	private static void warmup(int warmupRuns, Task[] tasks) {
		for (int i = 1; i <= warmupRuns; i++) {
			System.out.println("\nWarmup run #" + i);
			for (Task task : tasks) {
				try {
					task.perform();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
				SimonManager.getStopwatch(task.stopwatchName + STOPWATCH_SUM_SUFFIX));
		}
	}

	/**
	 * Helper object that requires implementing the {@link #perform()} method with benchmarked block of code.
	 * Calling {@link #run()} executes the code and measures statistics using the stopwatch named in the
	 * constructor. Calling {@link #perform()} executes the code without the stopwatch being used.
	 */
	public static abstract class Task implements Runnable {
		private String stopwatchName;

		protected Task(String stopwatchName) {
			this.stopwatchName = stopwatchName;
		}

		/**
		 * Executes {@link #perform()} and measures the run using the Stopwatch named in the constructor.
		 */
		@Override
		public void run() {
			System.out.print(stopwatchName + ": ");
			Split split = SimonManager.getStopwatch(stopwatchName).reset().start();
			try {
				perform();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				split.stop();
				System.out.println(split.presentRunningFor());
				SimonManager.getStopwatch(stopwatchName + STOPWATCH_SUM_SUFFIX).addTime(split.runningFor());
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
