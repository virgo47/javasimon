package org.javasimon.spring;

import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.source.AbstractMethodStopwatchSource;
import org.javasimon.aop.Monitored;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Monitor source providing stopwatches from Spring AOP method invocation
 *
 * @author gquintana
 */
public class SpringStopwatchSource extends AbstractMethodStopwatchSource<MethodInvocation> {
	/**
	 * Constructor
	 *
	 * @param manager Simon manager used for producing Stopwatches
	 */
	public SpringStopwatchSource(Manager manager) {
		super(manager);
	}

	/**
	 * Constructor using default Simon manager
	 */
	public SpringStopwatchSource() {
		super();
	}

	/**
	 * Get target class
	 */
	protected final Class<?> getTargetClass(MethodInvocation methodInvocation) {
		return AopUtils.getTargetClass(methodInvocation.getThis());
	}

	/**
	 * Get method being invoked
	 *
	 * @param methodInvocation Method invocation
	 * @return Method being invoked
	 */
	@Override
	protected Method getTargetMethod(MethodInvocation methodInvocation) {
		return methodInvocation.getMethod();
	}

	/**
	 * Tests whether invoked method or the class (or any superclass) is annotated with {@link org.javasimon.aop.Monitored}.
	 *
	 * @param methodInvocation current method invocation
	 * @return true, if the method invocation should be monitored
	 */
	@Override
	public boolean isMonitored(MethodInvocation methodInvocation) {
		return AnnotationUtils.findAnnotation(methodInvocation.getMethod(), Monitored.class) != null
			|| AnnotationUtils.findAnnotation(getTargetClass(methodInvocation), Monitored.class) != null;
	}

	/**
	 * Get monitor name for given method invocation
	 */
	protected String getMonitorName(MethodInvocation methodInvocation) {
		Monitored methodAnnotation = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), Monitored.class);
		if (methodAnnotation != null && methodAnnotation.name() != null && methodAnnotation.name().length() > 0) {
			return methodAnnotation.name();
		}

		StringBuilder nameBuilder = new StringBuilder();
		Monitored classAnnotation = AnnotationUtils.findAnnotation(getTargetClass(methodInvocation), Monitored.class);
		if (classAnnotation != null && classAnnotation.name() != null && classAnnotation.name().length() > 0) {
			nameBuilder.append(classAnnotation.name());
		} else {
			nameBuilder.append(methodInvocation.getMethod().getDeclaringClass().getName());
		}
		nameBuilder.append(Manager.HIERARCHY_DELIMITER);

		String suffix = methodInvocation.getMethod().getName();
		if (methodAnnotation != null && methodAnnotation.suffix() != null && methodAnnotation.suffix().length() > 0) {
			suffix = methodAnnotation.suffix();
		}
		return nameBuilder.append(suffix).toString();
	}
}
