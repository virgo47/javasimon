package org.javasimon.spring;

import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.aop.MonitorNameHelper;
import org.javasimon.aop.Monitored;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Utility class that helps to determine {@link org.javasimon.Stopwatch} name from the {@link Monitored} annotation.
 * Stopwatch name always
 *
 * @author Erik van Oosten
 */
final class MonitoredHelper {
	private MonitoredHelper() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Determine monitor name for a method invocation.
	 *
	 * @param invocation the method invocation (not null)
	 * @return the monitor name for this invocation
	 */
	public static String getMonitorName(MethodInvocation invocation) {
		Class targetClass = AopUtils.getTargetClass(invocation.getThis());
		Method method = invocation.getMethod();
		return new MonitorNameHelper(targetClass, AnnotationUtils.findAnnotation(targetClass, Monitored.class),
			method, AnnotationUtils.findAnnotation(method, Monitored.class)).getStopwatchName();
	}
}
