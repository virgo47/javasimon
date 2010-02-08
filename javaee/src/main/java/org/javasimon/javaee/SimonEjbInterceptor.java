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
 * @version $Revision$ $Date$
 * @since 2.3
 */
public class SimonEjbInterceptor {
	/**
	 * Default prefix for EJB interceptor Simons if no "prefix" init parameter is used.
	 */
	public static final String DEFAULT_EJB_INTERCEPTOR_PREFIX = "org.javasimon.ejb";

	/**
	 * Around invoke method that measures the split for one method invocation.
	 *
	 * @param context EJB invocation context
	 * @return return value from the invocation
	 * @throws Exception exception thrown from the invocation
	 */
	@AroundInvoke
	public Object monitor(InvocationContext context) throws Exception {
		String simonName = context.getMethod().getName();
		Split split = SimonManager.getStopwatch(DEFAULT_EJB_INTERCEPTOR_PREFIX + Manager.HIERARCHY_DELIMITER + simonName).start();
		try {
			return context.proceed();
		} finally {
			split.stop();
		}
	}
}
