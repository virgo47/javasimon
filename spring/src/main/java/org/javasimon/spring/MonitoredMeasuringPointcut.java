package org.javasimon.spring;

import java.lang.reflect.Method;

import org.javasimon.aop.Monitored;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Pointcut that identifies methods/classes with the {@link Monitored} annotation.
 *
 * @author Erik van Oosten
 */
public final class MonitoredMeasuringPointcut implements Pointcut {
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
			if (AnnotationUtils.findAnnotation(targetClass, Monitored.class) != null) {
				return true;
			} else if (AnnotationUtils.findAnnotation(method, Monitored.class) != null) {
				return true;
			}

			// if nothing matches return false
			return false;
		}

		public boolean isRuntime() {
			return false;
		}

		public boolean matches(Method method, Class targetClass, Object[] args) {
			throw new UnsupportedOperationException("This is not a runtime method matcher");
		}
	}
}
