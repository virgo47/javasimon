package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Simon EJB Interceptor measuring EJB method execution time.
 *
 * @author <a href="mailto:richard.richter@siemens-enterprise.com">Richard "Virgo" Richter</a>
 * @since 2.3
 */
@SuppressWarnings("UnusedParameters")
public class SimonEjbInterceptor {
	/**
	 * Default prefix for EJB interceptor Simons if no "prefix" init parameter is used.
	 */
	public static final String DEFAULT_EJB_INTERCEPTOR_PREFIX = "org.javasimon.ejb";

	/**
	 * Simon name prefix - can be overriden in subclasses.
	 */
	protected String prefix = DEFAULT_EJB_INTERCEPTOR_PREFIX;

	/**
	 * Returns Simon name for the specified Invocation context.
	 * By default it contains the prefix + method name.
	 * This method can be overriden.
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
	 * Default: always returns true.
	 * This method can be overriden
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
	 * @param context EJB invocation context
	 * @return return value from the invocation
	 * @throws Exception exception thrown from the invocation
	 */
	@AroundInvoke
	public Object monitor(InvocationContext context) throws Exception {
		if (isMonitored(context)) {
			String simonName = getSimonName(context);
			Split split = SimonManager.getStopwatch(simonName).start();
			try {
				return context.proceed();
			} finally {
				split.stop();
			}
		} else {
			return context.proceed();
		}
	}
}
