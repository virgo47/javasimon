package org.javasimon.spring;

import java.lang.reflect.Method;

import org.javasimon.aop.Monitored;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Pointcut that identifies methods/classes with the {@link Monitored} annotation.
 *
 * @author Erik van Oosten
 */
public final class MonitoredMeasuringPointcut implements Pointcut {
	/**
	 * Returns a class filter that lets all class through.
	 *
	 * @return a class filter that lets all class through
	 */
	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	/**
	 * Returns a method matcher that matches any method that has the {@link Monitored} annotation,
	 * or is in a class with the {@link Monitored} annotation or is in a subclass of such a class or interface.
	 *
	 * @return method matcher matching {@link Monitored} methods
	 */
	public MethodMatcher getMethodMatcher() {
		return MonitoredMethodMatcher.INSTANCE;
	}

	private enum MonitoredMethodMatcher implements MethodMatcher {
		INSTANCE;

		public boolean matches(Method method, Class targetClass) {
			return AnnotationUtils.findAnnotation(targetClass, Monitored.class) != null
				|| AnnotationUtils.findAnnotation(AopUtils.getMostSpecificMethod(method, targetClass), Monitored.class) != null;
		}

		public boolean isRuntime() {
			return false;
		}

		public boolean matches(Method method, Class targetClass, Object[] args) {
			throw new UnsupportedOperationException("This is not a runtime method matcher");
		}
	}
}
