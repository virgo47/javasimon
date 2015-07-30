package org.javasimon.spring;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.source.StopwatchSource;

/**
 * Basic method interceptor that measures the duration of the intercepted call with a Stopwatch.
 * Class can be overridden in case more sophisticated measuring needs to be provided - this all should
 * happen in {@link #processInvoke(org.aopalliance.intercept.MethodInvocation, org.javasimon.Split)} method.
 *
 * @author Erik van Oosten
 */
public class BasicMonitoringInterceptor implements MethodInterceptor, Serializable {
	private final StopwatchSource<MethodInvocation> stopwatchSource;

	/**
	 * Constructor with specified {@link org.javasimon.source.MonitorSource}.
	 *
	 * @param stopwatchSource stopwatch provider for method invocation
	 */
	public BasicMonitoringInterceptor(StopwatchSource<MethodInvocation> stopwatchSource) {
		this.stopwatchSource = stopwatchSource;
	}

	/** Constructor with specified {@link org.javasimon.Manager}. */
	public BasicMonitoringInterceptor(Manager manager) {
		this(new SpringStopwatchSource(manager));
	}

	/** Default constructor using {@link org.javasimon.SimonManager#manager}. */
	public BasicMonitoringInterceptor() {
		this(new SpringStopwatchSource(SimonManager.manager()));
	}

	/**
	 * Performs method invocation and wraps it with Stopwatch.
	 *
	 * @param invocation method invocation
	 * @return return object from the method
	 * @throws Throwable anything thrown by the method
	 */
	public final Object invoke(MethodInvocation invocation) throws Throwable {
		final Split split = stopwatchSource.start(invocation);
		try {
			return processInvoke(invocation, split);
		} finally {
			split.stop();
		}
	}

	/**
	 * Method with default invoke (just calls proceed). It can be overridden and overriding method can stop
	 * the provided split. If split is not stopped it will be stopped right after this method finishes.
	 * Any caught throwable should be rethrown again. Method should call {@code invocation.proceed()}.
	 *
	 * @param invocation method invocation
	 * @param split running split for this monitored action
	 * @return return object from the method
	 */
	protected Object processInvoke(MethodInvocation invocation, @SuppressWarnings("UnusedParameters") Split split) throws Throwable {
		return invocation.proceed();
	}
}