package org.javasimon.proxy;

import org.javasimon.Split;
import org.javasimon.source.StopwatchSource;

/**
 * Stopwatch proxy factory can generate a proxy to wrap an existing class and monitor its performance.
 * Sample usage:
 * <pre>
 * MonitoredInterface monitoredProxy=new StopwatchProxyFactory(monitoredImplementation).newProxy(MonitoredInterface.class);</pre>
 * or
 * <pre>
 * MonitoredInterface monitoredProxy=new StopwatchProxyFactory(monitoredImplementation, new CustomProxyStopwatchSource<MonitoredInterface>()).newProxy(MonitoredInterface.class);</pre>
 */
public final class StopwatchProxyFactory<T> extends DelegatingProxyFactory<T> {
	/**
	 * Stopwatch template.
	 */
	private final StopwatchSource<DelegatingMethodInvocation<T>> stopwatchSource;

	/**
	 * Constructor
	 *
	 * @param delegate Wrapped object
	 * @param stopwatchSource Stopwatch source (to configure Stopwatch naming)
	 */
	public StopwatchProxyFactory(T delegate, StopwatchSource<DelegatingMethodInvocation<T>> stopwatchSource) {
		super(delegate);
		this.stopwatchSource = stopwatchSource;
	}

	/**
	 * Constructor.
	 *
	 * @param delegate Wrapped object
	 */
	public StopwatchProxyFactory(T delegate) {
		this(delegate, new ProxyStopwatchSource<T>());
	}

	/**
	 * Invocation handler main method.
	 */
	@Override
	protected Object invoke(DelegatingMethodInvocation<T> methodInvocation) throws Throwable {
		final Split split = stopwatchSource.start(methodInvocation);
		try {
			return methodInvocation.proceed();
		} finally {
			split.stop();
		}
	}
}
