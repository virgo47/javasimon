package org.javasimon.spring;

import org.springframework.aop.Pointcut;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;

import java.lang.reflect.Method;

/**
 * Pointcut that identifies methods/classes with the {@link Monitored} annotation.
 *
 * @author Erik van Oosten
 */
public class MonitoredMeasuringPointcut implements Pointcut {
	/**
	 * @return a class filter that lets all class through.
	 */
	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	/**
	 * @return a method matcher that matches any method that has the {@link Monitored} annotation,
	 *         or is in a class with the {@link Monitored} annotation
	 */
	public MethodMatcher getMethodMatcher() {
		return MonitoredMethodMatcher.INSTANCE;
	}

	private enum MonitoredMethodMatcher implements MethodMatcher {
		INSTANCE;

		public boolean matches(Method method, Class targetClass) {
			return targetClass.isAnnotationPresent(Monitored.class) || method.isAnnotationPresent(Monitored.class);
		}

		public boolean isRuntime() {
			return false;
		}

		public boolean matches(Method method, Class targetClass, Object[] args) {
			throw new UnsupportedOperationException("This is not a runtime method matcher");
		}
	}
}
