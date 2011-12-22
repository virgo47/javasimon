package org.javasimon.examples.jmx.custom;

import javax.management.MBeanServer;

import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.jmx.JmxRegisterCallback;
import org.javasimon.jmx.SimonSuperMXBean;

/**
 * Customized JMX register callback.
 * Register CustomStopwatchMXBeanImpl instead of usual StopwatchMXBeanImpl.
 *
 * @author <a href="mailto:gerald.quintana@gmail.com">Gerald Quintana</a>
 */
public class CustomJmxRegisterCallback extends JmxRegisterCallback {
	private static final String DOMAIN = "com.mycompany.myapp";

	public CustomJmxRegisterCallback(MBeanServer mBeanServer) {
		super(mBeanServer, "com.mycompany.myapp");
	}

	public CustomJmxRegisterCallback() {
		super(DOMAIN);
	}

	@Override
	protected SimonSuperMXBean constructObject(Simon simon) {
		if (simon instanceof Stopwatch) {
			return new CustomStopwatchMXBeanImpl((Stopwatch) simon);
		} else {
			return super.constructObject(simon);
		}
	}

	@Override
	protected String constructObjectName(Simon simon) {
		return domain + ":type=Simon,kind=" + simonType(simon) + ",name=" + simon.getName();
	}
}
