package org.javasimon.javaee;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.javasimon.Manager;
import org.javasimon.source.AbstractMethodStopwatchSource;

/**
 * Provide stopwatch source for EJB and CDI invocation context.
 * Used by {@link SimonInterceptor} as default stopwatch source.
 * Can be overridden to customize monitored EJB methods and their
 * related Simon name.
 *
 * @author gquintana
 */
public class MethodStopwatchSource extends AbstractMethodStopwatchSource<InvocationContext> {

	/** Default prefix for Simon names. */
	public static final String DEFAULT_PREFIX = "org.javasimon.business";

	/** Simon name prefix - can be overridden in subclasses. */
	protected String prefix = DEFAULT_PREFIX;

	public MethodStopwatchSource(Manager manager) {
		super(manager);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	protected final Class<?> getTargetClass(InvocationContext context) {
		return context.getTarget().getClass();
	}

	@Override
	protected final Method getTargetMethod(InvocationContext context) {
		return context.getMethod();
	}

	/**
	 * Returns Simon name for the specified Invocation context.
	 * By default it contains the prefix + method name.
	 * This method can be overridden.
	 *
	 * @param context Invocation context
	 * @return fully qualified name of the Simon
	 * @since 3.1
	 */
	protected String getMonitorName(InvocationContext context) {
		String className = context.getMethod().getDeclaringClass().getSimpleName();
		String methodName = context.getMethod().getName();
		return prefix + Manager.HIERARCHY_DELIMITER + className + Manager.HIERARCHY_DELIMITER + methodName;
	}
}
