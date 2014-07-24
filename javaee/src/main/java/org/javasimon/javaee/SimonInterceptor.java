package org.javasimon.javaee;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * Simon Interceptor measuring method execution time - can be used in EJB, or CDI in general.
 *
 * @author <a href="mailto:richard.richter@siemens-enterprise.com">Richard "Virgo" Richter</a>
 * @since 2.3
 */
@SuppressWarnings("UnusedParameters")
public class SimonInterceptor {
	/**
	 * Default prefix for interceptor Simons if no "prefix" init parameter is used.
	 */
	public static final String DEFAULT_INTERCEPTOR_PREFIX = "org.javasimon.business";

	/**
	 * Simon name prefix - can be overridden in subclasses.
	 */
	protected String prefix = DEFAULT_INTERCEPTOR_PREFIX;

	/**
	 * Returns Simon name for the specified Invocation context.
	 * By default it contains the prefix + method name.
	 * This method can be overridden.
	 *
	 * @param context Invocation context
	 * @return fully qualified name of the Simon
	 * @since 3.1
	 */
	protected String getSimonName(InvocationContext context) {
		String className = context.getMethod().getDeclaringClass().getSimpleName();
		String methodName = context.getMethod().getName();
		return prefix + Manager.HIERARCHY_DELIMITER + className + Manager.HIERARCHY_DELIMITER + methodName;
	}

	/**
	 * Indicates whether the method invocation should be monitored.
	 * Default behavior always returns true.
	 * This method can be overridden
	 *
	 * @param context Method invocation context
	 * @return true to enable Simon, false either
	 */
	protected boolean isMonitored(InvocationContext context) {
		return true;
	}

	/**
	 * Around invoke method that measures the split for one method invocation.
	 *
	 * @param context invocation context
	 * @return return value from the invocation
	 * @throws Exception exception thrown from the invocation
	 */
	@AroundInvoke
	public Object monitor(InvocationContext context) throws Exception {
		if (isMonitored(context)) {
			String simonName = getSimonName(context);
			try (Split ignored = SimonManager.getStopwatch(simonName).start()) {
				return context.proceed();
			}
		} else {
			return context.proceed();
		}
	}
}
