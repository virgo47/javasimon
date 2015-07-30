package org.javasimon.proxy;

import java.lang.reflect.Method;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.source.AbstractMethodStopwatchSource;

/**
 * Stopwatch source for use with proxy.
 *
 * @author gquintana
 */
public class ProxyStopwatchSource<T> extends AbstractMethodStopwatchSource<DelegatingMethodInvocation<T>> {

	/** Prefix used for simon name. */
	private String prefix = "org.javasimon.proxy";

	public ProxyStopwatchSource() {
		super(SimonManager.manager());
	}

	public ProxyStopwatchSource(Manager manager) {
		super(manager);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	protected String getMonitorName(DelegatingMethodInvocation<T> location) {
		final String className = getTargetClass(location).getSimpleName();
		final String methodName = location.getMethod().getName();
		return prefix + Manager.HIERARCHY_DELIMITER + className + Manager.HIERARCHY_DELIMITER + methodName;
	}

	@Override
	protected final Class<?> getTargetClass(DelegatingMethodInvocation<T> location) {
		return location.getDelegate().getClass();
	}

	@Override
	protected final Method getTargetMethod(DelegatingMethodInvocation<T> location) {
		return location.getMethod();
	}
}
