package org.javasimon.examples;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;

/**
 * Shows how to get outputs similar to Perf4J with Simon. This solution does not support "message" part.
 * Result may look like this:
 * <pre>INFO: start[1321403641356] time[527] tag[codeBlock2.failure]
INFO: start[1321403641884] time[528] tag[codeBlock2.failure]
INFO: start[1321403642413] time[174] tag[codeBlock2.success]
INFO: start[1321403642588] time[791] tag[codeBlock2.failure]
INFO: start[1321403643379] time[386] tag[codeBlock2.success]
INFO: start[1321403643766] time[610] tag[codeBlock2.failure]
INFO: start[1321403644377] time[140] tag[codeBlock2.success]
INFO: start[1321403644518] time[436] tag[codeBlock2.success]
INFO: start[1321403644955] time[225] tag[codeBlock2.success]
INFO: start[1321403645181] time[978] tag[codeBlock2.failure]
INFO: start[1321403646160] time[808] tag[codeBlock2.failure]
INFO: start[1321403646969] time[47] tag[codeBlock2.success]
INFO: start[1321403647017] time[705] tag[codeBlock2.failure]
INFO: start[1321403647723] time[10] tag[codeBlock2.success]
INFO: start[1321403647734] time[712] tag[codeBlock2.failure]
INFO: start[1321403648447] time[868] tag[codeBlock2.failure]
INFO: start[1321403649316] time[57] tag[codeBlock2.success]
INFO: start[1321403649374] time[839] tag[codeBlock2.failure]
INFO: start[1321403650214] time[710] tag[codeBlock2.failure]
INFO: start[1321403650925] time[90] tag[codeBlock2.success]</pre>
 * And running {@code java -jar perf4j-0.9.16.jar times.log} (put output to times.log) shows following:
 * <pre>Performance Statistics   2011-11-16 01:34:00 - 2011-11-16 01:34:30
Tag                                                  Avg(ms)         Min         Max     Std Dev       Count
codeBlock2.failure                                     734,2         527         978       134,8          11
codeBlock2.success                                     173,9          10         436       141,9           9</pre>
 *
 * @author virgo47@gmail.com
 * @since 3.1
 */
public class Perf4JLikeExample {
	public static void main(String[] args) {
		SimonManager.callback().addCallback(new CallbackSkeleton() {
			@Override
			public void stopwatchAdd(Stopwatch stopwatch, Split split) {
				System.out.println("INFO: start[" + SimonManager.millisForNano(split.getStart()) + "] time[" + (split.runningFor() / SimonUtils.NANOS_IN_MILLIS ) + "] tag[" + stopwatch.getName() + "]");
			}
		});
		for (int i = 0; i < 20; i++) {
			method();
		}
	}

	// original lines from Perf4J commented out
	// example from http://perf4j.codehaus.org/devguide.html
	private static void method() {
//		StopWatch stopWatch = new LoggingStopWatch();
		Split split = new Split();

		try {
			// the code block being timed - this is just a dummy example
			long sleepTime = (long) (Math.random() * 1000L);
			Thread.sleep(sleepTime);
			if (sleepTime > 500L) {
				throw new Exception("Throwing exception");
			}

			SimonManager.getStopwatch("codeBlock2.success").addSplit(split.stop()); //, "Sleep time was < 500 ms");
//			stopWatch.stop("codeBlock2.success", "Sleep time was < 500 ms");
		} catch (Exception e) {
			// Technically, this is NOT correct usage - however it will work if you forget the split and
			// don't use it for something else again
			SimonManager.getStopwatch("codeBlock2.failure").addSplit(split); //, "Exception was: " + e);
//			stopWatch.stop("codeBlock2.failure", "Exception was: " + e);
		}
	}
}
