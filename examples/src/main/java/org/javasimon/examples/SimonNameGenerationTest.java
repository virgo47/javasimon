package org.javasimon.examples;

import org.javasimon.SimonManager;
import org.javasimon.StopwatchSample;
import org.javasimon.clock.ClockUtils;
import org.javasimon.utils.BenchmarkUtils;
import org.javasimon.utils.GoogleChartImageGenerator;
import org.javasimon.utils.SimonUtils;

/**
 * Compares get/start/stop with a static name and the same cycle with a name generated every time.
 * Typical result (Windows 7, JDK 6u31-32b, Intel Core i5) is:
 * http://chart.apis.google.com/chart?chs=420x320&cht=bvg&chbh=a,3,40&chco=2d69f9,a6c9fd,d0eeff&chxt=y,x,x&chtt=200k-loop+duration&chxr=2,0,4000&chds=0,4000&chxl=0:|0|1000|2000|3000|4000|1:|get-start-stop|gen-get-s-s|generate|get-stacktrace|2:|91.84+ms|3308.45+ms|3098.25+ms|2589.97+ms&chd=t:91.84,3308.45,3098.25,2589.97&chdl=avg&.png
 * <p/>
 * Morale: Generate your name once, not every time - most of the overhead is stacktrace construction (Java SE/native code).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class SimonNameGenerationTest {
	private static final int LOOP = 200000;

	private static final String NAME = SimonUtils.generateNameForClass("-stopwatch");

	private SimonNameGenerationTest() {
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		StopwatchSample[] results = BenchmarkUtils.run(2, 5,
			new BenchmarkUtils.Task("get-start-stop") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(NAME).start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("gen-get-s-s") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonManager.getStopwatch(SimonUtils.generateNameForClass("-stopwatch")).start().stop();
					}
				}
			},
			new BenchmarkUtils.Task("generate") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						SimonUtils.generateName();
					}
				}
			},
			new BenchmarkUtils.Task("get-stacktrace") {
				@Override
				public void perform() throws Exception {
					for (int i = 0; i < LOOP; i++) {
						Thread.currentThread().getStackTrace();
					}
				}
			}
		);

		System.out.println("\nGoogle Chart avg:\n" + GoogleChartImageGenerator.barChart(
			results, "200k-loop duration", ClockUtils.NANOS_IN_MILLIS, "ms", false));
		System.out.println("\nGoogle Chart avg/max/min:\n" + GoogleChartImageGenerator.barChart(
			results, "200k-loop duration", ClockUtils.NANOS_IN_MILLIS, "ms", true));
	}
}