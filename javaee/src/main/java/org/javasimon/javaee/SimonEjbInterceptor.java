package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Simon EJB Interceptor.
 *
 * @author <a href="mailto:richard.richter@siemens-enterprise.com">Richard "Virgo" Richter</a>
 * @version $Revision$ $Date$
 * @created 22.1.2010 10:30:51
 */
public class SimonEjbInterceptor {
	/**
	 * Default prefix for EJB interceptor Simons if no "prefix" init parameter is used.
	 */
	public static final String DEFAULT_EJB_INTERCEPTOR_PREFIX = "org.javasimon.ejb";

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
