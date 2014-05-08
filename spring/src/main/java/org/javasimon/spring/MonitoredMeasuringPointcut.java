package org.javasimon.spring;

import java.lang.reflect.Method;

import org.javasimon.aop.Monitored;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

/**
 * Pointcut that identifies methods/classes with the {@link Monitored} annotation.
 *
 * @author Erik van Oosten
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class MonitoredMeasuringPointcut implements Pointcut {
	/**
	 * Returns a class filter that lets all class through.
	 *
	 * @return a class filter that lets all class through
	 */
	@Override
	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	/**
	 * Returns a method matcher that matches any method that has the {@link Monitored} annotation,
	 * or is in a class with the {@link Monitored} annotation or is in a subclass of such a class or interface.
	 *
	 * @return method matcher matching {@link Monitored} methods
	 */
	@Override
	public MethodMatcher getMethodMatcher() {
		return MonitoredMethodMatcher.INSTANCE;
	}

	private enum MonitoredMethodMatcher implements MethodMatcher {
		INSTANCE;

		@Override
		public boolean matches(Method method, Class targetClass) {
			return !ClassUtils.isCglibProxyClass(targetClass) && isMonitoredAnnotationOnClassOrMethod(method, targetClass);
		}

		private boolean isMonitoredAnnotationOnClassOrMethod(Method method, Class targetClass) {
			return AnnotationUtils.findAnnotation(targetClass, Monitored.class) != null
				|| AnnotationUtils.findAnnotation(AopUtils.getMostSpecificMethod(method, targetClass), Monitored.class) != null;
		}

		@Override
		public boolean isRuntime() {
			return false;
		}

		@Override
		public boolean matches(Method method, Class targetClass, Object[] args) {
			throw new UnsupportedOperationException("This is not a runtime method matcher");
		}
	}
}
