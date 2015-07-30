package org.javasimon.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Proxy method invocation.
 *
 * @author gquintana
 */
public class DelegatingMethodInvocation<T> implements Delegating<T>, Runnable, Callable<Object> {

	/** Target (real) object. */
	private final T delegate;

	/** Proxy. */
	private final Object proxy;

	/** Method. */
	private final Method method;

	/** Invocation arguments. */
	private final Object[] args;

	public DelegatingMethodInvocation(T target, Object proxy, Method method, Object... args) {
		this.delegate = target;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public Method getMethod() {
		return method;
	}

	public Object getProxy() {
		return proxy;
	}

	public T getDelegate() {
		return delegate;
	}

	public Method getTargetMethod() throws NoSuchMethodException {
		return delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
	}

	public Object proceed() throws Throwable {
		return method.invoke(delegate, args);
	}

	public void run() {
		try {
			proceed();
		} catch (Throwable throwable) {
			// Forget exception
		}
	}

	public Object call() throws Exception {
		try {
			return proceed();
		} catch (Exception exception) {
			throw exception;
		} catch (Throwable throwable) {
			throw new IllegalStateException(throwable);
		}
	}
}
