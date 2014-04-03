package org.javasimon.callback.async;

import org.javasimon.SimonUnitTest;
import org.javasimon.callback.Callback;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.proxy.Delegating;
import org.javasimon.proxy.DelegatingMethodInvocation;
import org.javasimon.utils.SimonUtils;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertSame;

/**
 * Unit test for {@link org.javasimon.callback.async.AsyncCallbackProxyFactory}.
 *
 * @author gerald
 */
public class AsyncCallbackProxyTest extends SimonUnitTest {

	private Callback callbackMock;
	private Callback callbackProxy;
	private Executor executor;

	@BeforeMethod
	public void setUpMethod() throws Exception {
		callbackMock = mock(Callback.class);
		executor = mock(Executor.class);
		callbackProxy = new AsyncCallbackProxyFactory(callbackMock, executor).newProxy();
	}

	@AfterClass
	public void tearDown() {
		Executors.shutdownAsync();
	}

	/**
	 * Check that calling any method on proxy call the method on the delegate.
	 * We are not checking interaction with actual Executor to avoid possible race conditions.
	 * Instead we just check that proper method invocation was submitted to the specified Executor.
	 */
	@Test
	public void testOnMessage() throws Throwable {
		final String message = "Hello";
		callbackProxy.onManagerMessage(message);
		ArgumentCaptor<Callable> captor = ArgumentCaptor.forClass(Callable.class);
		verify(executor).execute(captor.capture());

		DelegatingMethodInvocation methodInvocation = (DelegatingMethodInvocation) captor.getValue();
		Assert.assertSame(methodInvocation.getDelegate(), callbackMock);
		Assert.assertEquals(methodInvocation.getMethod().getName(), "onManagerMessage");
		Assert.assertEquals(methodInvocation.getArgs(), new Object[] {message});
	}

	/** Checks that calling getDelegate method on proxy returns delegate implementation. */
	@Test
	public void testGetDelegate() throws Exception {
		@SuppressWarnings("unchecked")
		Callback callbackDelegate = ((Delegating<Callback>) callbackProxy).getDelegate();
		assertSame(callbackDelegate, callbackMock);
	}

	private long runPerformance(final int threadNb, ExecutorService executorService, final int iterationNb, Callback callback) throws ExecutionException, InterruptedException {
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

	/**
	 * Try to evaluate when AsyncCallback is profitable.
	 * You can change the number of threads and the duration of the callback.
	 * You can remove the synchronized keyword.
	 */
	@Test
	public void testPerformance() throws Exception {
		final int threadNb = 3, iterationNb = 100;
		final long threadSleep = 1L;
		// Prepare callbacks
		final Callback callbackReal = new CallbackSkeleton() {
			@Override
			public synchronized void onManagerMessage(String message) {
				try {
					Thread.sleep(threadSleep);
				} catch (InterruptedException ignored) {
				}
			}
		};
		final Callback callbackAsync = new AsyncCallbackProxyFactory(callbackReal).newProxy();
		// Prepare injectors
		ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(threadNb, new ThreadFactory() {
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
			timeAsync += runPerformance(threadNb, executorService, iterationNb, callbackAsync);
			timeNormal += runPerformance(threadNb, executorService, iterationNb, callbackReal);
		}
		System.out.println("Performance, async=" + SimonUtils.presentNanoTime(timeAsync) + ", normal=" + SimonUtils.presentNanoTime(timeNormal));
		executorService.shutdown();
	}
}
