package org.javasimon.spring;

import java.io.Serializable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * Method interceptor that measures the duration of the intercepted call with a Stopwatch.
 *
 * @author Erik van Oosten
 */
public final class MonitoringInterceptor implements MethodInterceptor, Serializable {
	/**
	 * Simon manager used for producing simons
	 */
	private Manager manager=SimonManager.manager();
	/**
	 * Performs method invocation and wraps it with Stopwatch.
	 *
	 * @param invocation method invocation
	 * @return return object from the method
	 * @throws Throwable anything thrown by the method
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String monitorName = MonitoredHelper.getMonitorName(invocation);

		Split split = manager.getStopwatch(monitorName).start();
		try {
			return invocation.proceed();
		} finally {
			split.stop();
		}
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
}