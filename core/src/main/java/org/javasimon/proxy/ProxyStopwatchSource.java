package org.javasimon.proxy;

import org.javasimon.Manager;
import org.javasimon.source.AbstractMethodStopwatchSource;

import java.lang.reflect.Method;

/**
 * Stopwatch source for use with proxy
 * @author gquintana
 */
public class ProxyStopwatchSource<T> extends AbstractMethodStopwatchSource<ProxyMethodInvocation<T>> {
	/**
	 * Prefix used for simon name
	 */
	private String prefix="org.javasimon.proxy";

	public ProxyStopwatchSource() {
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	protected String getMonitorName(ProxyMethodInvocation<T> location) {
		final String className=getTargetClass(location).getSimpleName();
		final String methodName=location.getMethod().getName();
		return prefix+ Manager.HIERARCHY_DELIMITER+className+ Manager.HIERARCHY_DELIMITER+methodName;
	}

	@Override
	protected final Class<?> getTargetClass(ProxyMethodInvocation<T> location) {
		return location.getTarget().getClass();
	}

	@Override
	protected final Method getTargetMethod(ProxyMethodInvocation<T> location) {
		return location.getMethod();
	}
}
