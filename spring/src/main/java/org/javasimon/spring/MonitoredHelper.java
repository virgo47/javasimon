package org.javasimon.spring;

import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.aop.Monitored;
import org.springframework.aop.support.AopUtils;

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
		String simonName = nameFromClassAnnotation(invocation);
		simonName = nameFromMethodAnnotation(invocation, simonName);
		return simonName;
	}

	private static String nameFromClassAnnotation(MethodInvocation invocation) {
		Class targetClass = AopUtils.getTargetClass(invocation.getThis());
		Monitored classAnnotation = (Monitored) targetClass.getAnnotation(Monitored.class);
		if (classAnnotation == null || classAnnotation.name() == null || classAnnotation.name().length() == 0) {
			return targetClass.getName();
		} else {
			return classAnnotation.name();
		}
	}

	private static String nameFromMethodAnnotation(MethodInvocation invocation, String simonName) {
		Monitored methodAnnotation = invocation.getMethod().getAnnotation(Monitored.class);
		if (methodAnnotation == null || methodAnnotation.name() == null || methodAnnotation.name().length() == 0) {
			return invocation.getMethod().getName();
		}

		return methodAnnotation.name();
	}
}
