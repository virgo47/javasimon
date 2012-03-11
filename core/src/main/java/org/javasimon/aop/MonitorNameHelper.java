package org.javasimon.aop;

import org.javasimon.Manager;

import java.lang.reflect.Method;

/**
 * Helper to determine the monitor name for the aspect execution. Class, method, class annotation and method annotation
 * is provided from the aspect implementation (AOP), this resolution part is common. For name resolution rules
 * see {@link Monitored}. It is up to the user of this helper to provide proper annotation be it from the target class
 * or from the superclass, the same goes for method annotation. This helper does not check "inherited" annotations.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 */
public class MonitorNameHelper {
	private Class targetClass;
	private Monitored classAnnotation;
	private Method method;
	private Monitored methodAnnotation;

	/**
	 * Creates the helper with all the necessary parameters - used from aspect implementations.
	 *
	 * @param targetClass real class of the target object
	 * @param classAnnotation annotation on the class (or found on any superclass/superinterface)
	 * @param method method being called
	 * @param methodAnnotation annotation found on the method (or declared for the method in the superclass/superinterface)
	 */
	@SuppressWarnings({"unchecked"})
	public MonitorNameHelper(Class targetClass, Monitored classAnnotation, Method method, Monitored methodAnnotation) {
		this.targetClass = targetClass;
		this.classAnnotation = classAnnotation;
		this.method = method;
		this.methodAnnotation = methodAnnotation;
	}

	/**
	 * Determines the name of the stopwatch as specified in the class javadoc for {@link Monitored}.
	 *
	 * @return name of the stopwatch based on the call context and its annotations
	 */
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
