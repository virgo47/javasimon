package org.javasimon.spring;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.source.MonitorSource;
import org.javasimon.source.StopwatchTemplate;

/**
 * Method interceptor that measures the duration of the intercepted call with a Stopwatch.
 *
 * @author Erik van Oosten
 */
// TODO: Is it possible to support both AOP alliance and AspectJ style?
// maybe extending org.springframework.aop.interceptor.AbstractMonitoringInterceptor (see also JamonPMI in Spring)
@SuppressWarnings("UnusedDeclaration")
public final class MonitoringInterceptor implements MethodInterceptor, Serializable {
	/**
	 * Stopwatch template.
	 */
	private final StopwatchTemplate<MethodInvocation> stopwatchTemplate;

	/**
	 * Constructor with specified {@link MonitorSource}.
	 *
	 * @param stopwatchSource stopwtach provider for method invocation
	 */
	public MonitoringInterceptor(MonitorSource<MethodInvocation, Stopwatch> stopwatchSource) {
		this.stopwatchTemplate = new StopwatchTemplate<MethodInvocation>(stopwatchSource);
	}

	/**
	 * Constuctor with specified {@link Manager}.
	 */
	public MonitoringInterceptor(Manager manager) {
		this(new SpringStopwatchSource(manager).cache());
	}

	/**
	 * Default constuctor using {@link SimonManager#manager}.
	 */
	public MonitoringInterceptor() {
		this(new SpringStopwatchSource(SimonManager.manager()).cache());
	}

	/**
	 * Performs method invocation and wraps it with Stopwatch.
	 *
	 * @param invocation method invocation
	 * @return return object from the method
	 * @throws Throwable anything thrown by the method
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		final Split split = stopwatchTemplate.start(invocation);
		try {
			return invocation.proceed();
		} finally {
			stopwatchTemplate.stop(split);
		}
	}
}