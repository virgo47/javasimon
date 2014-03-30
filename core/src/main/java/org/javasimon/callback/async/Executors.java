package org.javasimon.callback.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * This class contains some basic {@link Executor}s.
 * <ul>
 * <li>Disabled: does nothing, callable is not called at all.</li>
 * <li>Sync: callable is called synchronously, using the same thread as caller, result is returned.</li>
 * <li>Async: callable is called asynchronously, using the different thread as caller, no result is returned.</li>
 * </ul>
 *
 * @author gerald
 */
public final class Executors {

	/** Disabled Executor. */
	public static Executor DISABLED = new Executor() {
		public Object execute(Callable callable) throws Throwable {
			return null;
		}
	};
	/** Synchronous, same thread Executor. */
	public static Executor SYNC = new Executor() {
		public Object execute(Callable callable) throws Throwable {
			return callable.call();
		}
	};
	/** Single threaded executor service used by default async. */
	private static ExecutorService ASYNC_EXECUTOR_SERVICE;

	/** Initializes default single threaded executor service. */
	private static synchronized ExecutorService initAsyncExecutorService() {
		if (ASYNC_EXECUTOR_SERVICE == null) {
			ASYNC_EXECUTOR_SERVICE = java.util.concurrent.Executors.newSingleThreadExecutor(
				new ThreadFactory() {
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r, "javasimon-async");
						thread.setDaemon(true);
						return thread;
					}
				});
		}
		return ASYNC_EXECUTOR_SERVICE;
	}

	/** Asynchronous, different thread executor. */
	private static class AsyncCallbackExecutor<T> implements Executor<T> {
		/** Used executor service. */
		private final ExecutorService executorService;

		public AsyncCallbackExecutor(ExecutorService executorService) {
			this.executorService = executorService;
		}

		public T execute(Callable<T> callable) throws Throwable {
			executorService.submit(callable);
			return null;
		}

	}

	/** Returns disabled executor. */
	public static <T> Executor<T> disabled() {
		return DISABLED;
	}

	/** Returns synchronous, same thread executor. */
	public static <T> Executor<T> sync() {
		return SYNC;
	}

	/** Returns asynchronous, different thread executor. */
	public static <T> Executor<T> async(ExecutorService executorService) {
		return new AsyncCallbackExecutor(executorService);
	}

	/** Returns asynchronous, different but unique thread executor. */
	public static <T> Executor<T> async() {
		return async(initAsyncExecutorService());
	}

	/** Stops thread used by default async executor. */
	public static synchronized void shutdownAsync() {
		if (ASYNC_EXECUTOR_SERVICE != null) {
			ASYNC_EXECUTOR_SERVICE.shutdown();
		}
	}
}
