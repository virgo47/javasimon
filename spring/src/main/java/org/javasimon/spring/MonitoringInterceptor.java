package org.javasimon.spring;

import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.source.MonitorSource;
import org.javasimon.source.StopwatchSource;

/**
 * Method interceptor that measures the duration of the intercepted call with a Stopwatch and treats failure
 * cases (exceptions) separately. By default exceptional flow reports to "stopwatch.failed" ({@link #EXCEPTION_TAG}),
 * but setting
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class MonitoringInterceptor extends BasicMonitoringInterceptor {

	public static final String EXCEPTION_TAG = "failed";

	private boolean tagByExceptionType;

	/**
	 * Constructor with specified {@link MonitorSource}.
	 *
	 * @param stopwatchSource stopwatch provider for method invocation
	 */
	public MonitoringInterceptor(StopwatchSource<MethodInvocation> stopwatchSource) {
		super(stopwatchSource);
	}

	/** Constructor with specified {@link Manager}. */
	public MonitoringInterceptor(Manager manager) {
		super(new SpringStopwatchSource(manager));
	}

	/** Default constructor using {@link SimonManager#manager}. */
	public MonitoringInterceptor() {
		super(new SpringStopwatchSource(SimonManager.manager()));
	}

	/**
	 * Sets whether all exceptions should report to {@link #EXCEPTION_TAG} sub-simon or sub-simon for each
	 * exception type should be introduced (based on exception's simple name).
	 *
	 * @param tagByExceptionType {@code true} for fine grained exception-class-name-based sub-simons
	 */
	public void setTagByExceptionType(boolean tagByExceptionType) {
		this.tagByExceptionType = tagByExceptionType;
	}

	/**
	 * Method stops the split
	 *
	 * @param invocation method invocation
	 * @param split running split for this monitored action
	 * @return return object from the method
	 */
	protected Object processInvoke(MethodInvocation invocation, Split split) throws Throwable {
		try {
			return invocation.proceed();
		} catch (Throwable t) {
			split.stop(tagByExceptionType ? t.getClass().getSimpleName() : EXCEPTION_TAG);
			throw t;
		}
	}
}