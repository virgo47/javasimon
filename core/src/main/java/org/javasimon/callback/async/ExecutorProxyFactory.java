package org.javasimon.callback.async;

import org.javasimon.proxy.DelegatingMethodInvocation;
import org.javasimon.proxy.DelegatingProxyFactory;

/**
 * Proxy factory which can be used make any class asynchronous.
 *
 * @param <T>
 * @author gerald
 */
public class ExecutorProxyFactory<T> extends DelegatingProxyFactory<T> {

	/** Executor used for invoking methods on delegate object. */
	private Executor executor;

	/**
	 * Constructor.
	 *
	 * @param delegate Delegate object
	 */
	public ExecutorProxyFactory(T delegate) {
		this(delegate, Executors.async());
	}

	/**
	 * Constructor.
	 *
	 * @param delegate Delegate object
	 * @param executor Executor used, see {@link Executors}
	 */
	public ExecutorProxyFactory(T delegate, Executor executor) {
		super(delegate);
		this.executor = executor;
	}

	/**
	 * Returns used executor.
	 *
	 * @return Executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * Sets used executor.
	 *
	 * @param executor Executor
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	@Override
	protected Object invoke(DelegatingMethodInvocation<T> delegatingMethodInvocation) throws Throwable {
		return executor.execute(delegatingMethodInvocation);
	}
}
