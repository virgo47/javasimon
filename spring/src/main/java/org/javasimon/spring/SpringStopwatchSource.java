package org.javasimon.spring;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.aop.Monitored;
import org.javasimon.source.AbstractMethodStopwatchSource;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Monitor source providing stopwatches from Spring AOP method invocation.
 *
 * @author gquintana
 */
public class SpringStopwatchSource extends AbstractMethodStopwatchSource<MethodInvocation> {

	/**
	 * Constructor with specified {@link Manager}.
	 *
	 * @param manager Simon manager used for producing Stopwatches
	 */
	public SpringStopwatchSource(Manager manager) {
		super(manager);
	}

	/** Get target class. */
	protected final Class<?> getTargetClass(MethodInvocation methodInvocation) {
		return AopUtils.getTargetClass(methodInvocation.getThis());
	}

	/**
	 * Get method being invoked.
	 *
	 * @param methodInvocation Method invocation
	 * @return Method being invoked
	 */
	@Override
	protected Method getTargetMethod(MethodInvocation methodInvocation) {
		return getTargetMethod(methodInvocation, getTargetClass(methodInvocation));
	}

	private Method getTargetMethod(MethodInvocation methodInvocation, Class<?> targetClass) {
		return AopUtils.getMostSpecificMethod(methodInvocation.getMethod(), targetClass);
	}

	/**
	 * By default returns {@code true} because it is expected to be called from {@link MonitoringInterceptor} which means that the method call
	 * should be monitored. Pointcuts provided enough mechanism to decide whether the method is monitored or not, but this method can be overridden
	 * if needed.
	 *
	 * @param methodInvocation current method invocation
	 * @return true, if the method invocation should be monitored
	 */
	@Override
	public boolean isMonitored(MethodInvocation methodInvocation) {
		return true;
	}

	/**
	 * Returns monitor name for the given method invocation with {@link org.javasimon.aop.Monitored#name()}
	 * and {@link org.javasimon.aop.Monitored#suffix()} applied as expected.
	 *
	 * @param methodInvocation current method invocation
	 * @return name of the Stopwatch for the invocation
	 */
	protected String getMonitorName(MethodInvocation methodInvocation) {
		Class<?> targetClass = getTargetClass(methodInvocation);
		Method targetMethod = getTargetMethod(methodInvocation, targetClass);

		Monitored methodAnnotation = AnnotationUtils.findAnnotation(targetMethod, Monitored.class);
		if (methodAnnotation != null && methodAnnotation.name() != null && methodAnnotation.name().length() > 0) {
			return methodAnnotation.name();
		}

		StringBuilder nameBuilder = new StringBuilder();
		Monitored classAnnotation = AnnotationUtils.findAnnotation(targetClass, Monitored.class);
		if (classAnnotation != null && classAnnotation.name() != null && classAnnotation.name().length() > 0) {
			nameBuilder.append(classAnnotation.name());
		} else {
			nameBuilder.append(getMeaningfulClassName(targetClass));
		}
		nameBuilder.append(Manager.HIERARCHY_DELIMITER);

		String suffix = targetMethod.getName();
		if (methodAnnotation != null && methodAnnotation.suffix() != null && methodAnnotation.suffix().length() > 0) {
			suffix = methodAnnotation.suffix();
		}
		return nameBuilder.append(suffix).toString();
	}

	protected String getMeaningfulClassName(Class<?> targetClass) {
		if (java.lang.reflect.Proxy.isProxyClass(targetClass)) {
			for (Class<?> iface : targetClass.getInterfaces()) {
				if (iface != SpringProxy.class && iface != Advised.class) {
					return iface.getName();
				}
			}
		}
		return targetClass.getName();
	}
}
