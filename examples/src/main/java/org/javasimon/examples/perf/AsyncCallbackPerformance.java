package org.javasimon.examples.perf;

import org.javasimon.callback.Callback;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.async.AsyncCallbackProxyFactory;
import org.javasimon.callback.async.Executors;
import org.javasimon.utils.SimonUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Try to evaluate when AsyncCallback is profitable.
 * You can change the number of threads and the duration of the callback.
 * You can remove the synchronized keyword.
 *
 * @author gerald
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class AsyncCallbackPerformance {

	private static final int THREADS_NUMBER = 3;
	private static final int ITERATIONS_NUMBER = 100;
	private static final long THREAD_SLEEP = 1L;

	public static void main(String... args) throws Exception {
		// Prepare callbacks
		final Callback callbackReal = new CallbackSkeleton() {
			@Override
			public synchronized void onManagerMessage(String message) {
				try {
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException ignored) {
				}
			}
		};
		final Callback callbackAsync = new AsyncCallbackProxyFactory(callbackReal).newProxy();
		// Prepare injectors
		ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(THREADS_NUMBER, new ThreadFactory() {
			final String threadName = getClass().getName() + ".testPerformance";
			final ThreadGroup threadGroup = new ThreadGroup(threadName);
			int threadIndex = 0;

			private synchronized String nextThreadName() {
				return threadName + "#" + (threadIndex++);
			}

			public Thread newThread(Runnable r) {
				return new Thread(threadGroup, r, nextThreadName());
			}
		});
		// Run performance test
		long timeAsync = 0, timeNormal = 0;
		for (int i = 0; i < 3; i++) {
			timeAsync += runPerformance(THREADS_NUMBER, executorService, ITERATIONS_NUMBER, callbackAsync);
			timeNormal += runPerformance(THREADS_NUMBER, executorService, ITERATIONS_NUMBER, callbackReal);
		}
		System.out.println("Performance, async=" + SimonUtils.presentNanoTime(timeAsync) + ", normal=" + SimonUtils.presentNanoTime(timeNormal));
		executorService.shutdown();
		Executors.shutdownAsync();
	}

	private static long runPerformance(final int threadNb, ExecutorService executorService, final int iterationNb, Callback callback) throws ExecutionException, InterruptedException {
		long time0 = System.nanoTime();
		Future[] futures = new Future[threadNb];
		for (int i = 0; i < threadNb; i++) {
			futures[i] = executorService.submit(new PerformanceRunnable(iterationNb, callback));
		}
		for (int i = 0; i < threadNb; i++) {
			futures[i].get();
		}
		long time1 = System.nanoTime();
		return time1 - time0;
	}

	private static class PerformanceRunnable implements Runnable {
		final int iterationNb;
		final Callback callback;

		public PerformanceRunnable(int iterationNb, Callback callback) {
			this.iterationNb = iterationNb;
			this.callback = callback;
		}

		public void run() {
			for (int i = 0; i < iterationNb; i++) {
				callback.onManagerMessage("Performance test running");
			}
		}
	}
}
