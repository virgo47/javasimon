package org.javasimon.proxy;

import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.source.MonitorSource;
import org.javasimon.source.StopwatchTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Stopwatch proxy is an invocation handler which can generate proxies.
 * Sample usage:
 * <code>
 * MonitoredInterface monitoredProxy=StopwatchProxy.newProxy(monitoredImplementation, MonitoredInterface.class);
 * </code>
 * or
 * <code>
 * MonitoredInterface monitoredProxy=StopwatchProxy.newProxy(monitoredImplementation, MonitoredInterface.class,
 * new CustomProxyStopwatchSource<MonitoredInterface>()
 * );
 * </code>
 */
public class StopwatchProxy<T> implements InvocationHandler {
	/**
	 * Target (real) object.
	 */
	private final T delegate;

	/**
	 * Stopwatch template.
	 */
	private final StopwatchTemplate<ProxyMethodInvocation<T>> stopwatchTemplate;

	/**
	 * Private constructor - use {@link #newProxy} factory methods instead.
	 */
	private StopwatchProxy(T delegate, MonitorSource<ProxyMethodInvocation<T>, Stopwatch> stopwatchSource) {
		this.delegate = delegate;
		this.stopwatchTemplate = new StopwatchTemplate<ProxyMethodInvocation<T>>(stopwatchSource);
	}

	/**
	 * Invocation handler main method.
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final ProxyMethodInvocation<T> methodInvocation = new ProxyMethodInvocation<T>(delegate, proxy, method, args);
		final Split split = stopwatchTemplate.start(methodInvocation);
		try {
			return methodInvocation.proceed();
		} finally {
			stopwatchTemplate.stop(split);
		}
	}

	/**
	 * Create a new stopwatch proxy.
	 *
	 * @param <T> Target interface
	 * @param target Target instance
	 * @param interfaceClasses Interfaces inmplemented by proxy
	 * @param stopwatchSource Stopwatch source
	 * @param proxyClassLoader Class loader used by proxy
	 * @return Proxy
	 */
	public static <T> Object newProxy(T target, Class<?>[] interfaceClasses, MonitorSource<ProxyMethodInvocation<T>, Stopwatch> stopwatchSource, ClassLoader proxyClassLoader) {
		final StopwatchProxy<T> invocationHandler = new StopwatchProxy<T>(target, stopwatchSource);
		return Proxy.newProxyInstance(proxyClassLoader, interfaceClasses, invocationHandler);
	}

	/**
	 * Create a new stopwatch proxy.
	 *
	 * @param <T> Target interface
	 * @param target Target instance
	 * @param interfaceClass Interface implemented by proxy
	 * @param stopwatchSource Stopwatch source
	 * @return Proxy
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newProxy(T target, Class<T> interfaceClass, MonitorSource<ProxyMethodInvocation<T>, Stopwatch> stopwatchSource) {
		return (T) newProxy(target, new Class[]{interfaceClass}, stopwatchSource, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Create a new stopwatch proxy with default stopwatch source.
	 *
	 * @param <T> Target interface
	 * @param target Target instance
	 * @param interfaceClass Interface implemented by proxy
	 * @return Proxy
	 */
	public static <T> T newProxy(T target, Class<T> interfaceClass) {
		return newProxy(target, interfaceClass, new ProxyStopwatchSource<T>());
	}
}
