package org.javasimon.callback.async;

import java.util.concurrent.Callable;

/**
 * Executor is similar to {@link java.util.concurrent.Executor}
 * or {@link java.util.concurrent.ExecutorService} but simpler (only one method to implement).
 *
 * @param <T>
 * @author gerald
 * @see Executors Implementations
 */
public interface Executor<T> {

	/**
	 * Main method of the executor.
	 *
	 * @param callable Piece of code to execute
	 * @return Result of the execution
	 * @throws Throwable Raised when execution failed
	 */
	T execute(Callable<T> callable) throws Throwable;
}
