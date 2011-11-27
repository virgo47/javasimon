package org.javasimon.aop;

import org.javasimon.Manager;

import java.lang.reflect.Method;

/**
 * Helper to determine the monitor name for the aspect execution. Class, method, class annotation and method annotation
 * is provided from the aspect implementation (AspectJ/AOP), this resolution part is common. For name resolution rules
 * see {@link Monitored}.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 */
public class MonitorNameHelper {
	private Class targetClass;
	private Monitored classAnnotation;
	private Method method;
	private Monitored methodAnnotation;

	@SuppressWarnings({"unchecked"})
	public MonitorNameHelper(Class targetClass, Method method) {
		this.targetClass = targetClass;
		this.method = method;
		classAnnotation = (Monitored) targetClass.getAnnotation(Monitored.class);
		methodAnnotation = method.getAnnotation(Monitored.class);
	}

	public String getStopwatchName() {
		if (methodAnnotation != null && methodAnnotation.name() != null && methodAnnotation.name().length() > 0) {
			return methodAnnotation.name();
		}

		StringBuilder nameBuilder = new StringBuilder();
		if (classAnnotation != null && classAnnotation.name() != null && classAnnotation.name().length() > 0) {
			nameBuilder.append(classAnnotation.name());
		} else {
			nameBuilder.append(targetClass.getName());
		}
		nameBuilder.append(Manager.HIERARCHY_DELIMITER);

		// TODO does this find the first annotation up the hierarchy?
		String suffix = method.getName();
		if (methodAnnotation != null && methodAnnotation.suffix() != null && methodAnnotation.suffix().length() > 0) {
			suffix = methodAnnotation.suffix();
		}
		return nameBuilder.append(suffix).toString();
	}
}
