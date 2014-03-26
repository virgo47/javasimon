package org.javasimon.jmx;

import org.javasimon.Manager;
import org.javasimon.SimonException;
import org.javasimon.SimonManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Utility class for registering JMX beans that can be used to access Simon manager and Simons.
 * To create JmxReporter one need to first to acquire {@link org.javasimon.jmx.JmxReporter.Builder} instance
 * using one of {@link org.javasimon.jmx.JmxReporter#forManager(org.javasimon.Manager)}
 * or {@link JmxReporter#forDefaultManager()} methods. Using the builder instance a JmxReporter instance can be configured.
 * Then JmxReporter can be built and started using methods {@link org.javasimon.jmx.JmxReporter.Builder#build()} and
 * {@link JmxReporter#start()} respectively.
 * When JmxReporter should be stopped one need to call {@link JmxReporter#stop()} method.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public final class JmxReporter {

	public static final String DEFAULT_BEAN_NAME = "org.javasimon.jmx.JmxReporter:type=Simon";
	public static final String DEFAULT_SIMON_DOMAIN = "org.javasimon.jmx.simons";

	private static final Logger logger = LoggerFactory.getLogger(JmxReporter.class);

	/** Simon manager that will be accesed through registred JMX bean */
	private final Manager manager;

	/** JMX bean server used by JmxReporter */
	private MBeanServer beanServer;

	/** Name of a new JMX bean for Simon manager */
	private String beanName;

	/** Should already registered bean with the specified name be replaced */
	private boolean toReplace;

	/** Should JMX beans for separate Simons be registered */
	private boolean registerSimons;

	/** Domain of JMX beans for separate Simons */
	private String simonDomain;
	private JmxRegisterCallback jmxRegisterCallback;

	/**
	 * Start building JmxReporter for the specified manager.
	 *
	 * @param manager manager that will be used by JmxReporter
	 * @return builder for JmxReporter
	 */
	static public Builder forManager(Manager manager) {
		JmxReporter reporter = new JmxReporter(manager);

		reporter.setBeanName(DEFAULT_BEAN_NAME);
		reporter.setRegisterSimons(false);
		reporter.setReplaceExisting(false);
		reporter.setSimonDomain(DEFAULT_SIMON_DOMAIN);
		reporter.setBeanServer(ManagementFactory.getPlatformMBeanServer());

		return new Builder(reporter);
	}

	/**
	 * Start building JmxReporting for default manager.
	 *
	 * @return builder for JmxReporter
	 */
	public static Builder forDefaultManager() {
		return forManager(SimonManager.manager());
	}

	/**
	 * Class for configuring JmxReporter. When configuration is sepcified,
	 * {@link org.javasimon.jmx.JmxReporter.Builder#build()}
	 * method should be used to create JmxReporter
	 */
	public static final class Builder {
		private final JmxReporter reporter;

		private Builder(JmxReporter reporter) {
			this.reporter = reporter;
		}

		/**
		 * Specify either a separate JMX beans should be registered for each Simon in current manager.
		 *
		 * @param toRegister register all Simons if true, none otherwise
		 * @return builder for JmxReporter
		 */
		public Builder registerSimons(boolean toRegister) {
			reporter.setRegisterSimons(toRegister);
			return this;
		}

		/**
		 * Specify domain for registered JMX for separate Simons. Is used only if {@link #registerSimons} set to true.
		 *
		 * @param simonDomain domain for separate JMX beans for separate Simons
		 * @return builder for JmxReporter
		 */
		public Builder simonDomain(String simonDomain) {
			reporter.setSimonDomain(simonDomain);
			return this;
		}

		/**
		 * Specify if previously registered bean with the same name should be replaced.
		 *
		 * @param toReplace if true previously registred bean with the same name will be replaced,
		 *                  false otherwise
		 * @return builder for JmxReporter
		 */
		public Builder replaceExisting(boolean toReplace) {
			reporter.setReplaceExisting(toReplace);
			return this;
		}

		/**
		 * Bean name that will be used to register JMX bean for Simon manager.
		 *
		 * @param beanName bean name that will be used to register JMX bean for Simon manager
		 * @return builder for JmxReporter
		 */
		public Builder beanName(String beanName) {
			reporter.setBeanName(beanName);
			return this;
		}

		Builder beanServer(MBeanServer beanServer) {
			reporter.setBeanServer(beanServer);
			return this;
		}

		/**
		 * Finish configuration of JmxReporter and return configured instance.
		 * @return configured JmxReporter
		 */
		public JmxReporter build() {
			return reporter;
		}
	}

	private void setRegisterSimons(boolean registerSimons) {
		this.registerSimons = registerSimons;
	}

	public void setSimonDomain(String simonDomain) {
		if (simonDomain == null || simonDomain.isEmpty()) {
			throw new IllegalArgumentException("Simon domain is null or empty");
		}
		this.simonDomain = simonDomain;
	}

	private void setReplaceExisting(boolean toReplace) {
		this.toReplace = toReplace;
	}

	private JmxReporter(Manager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("Manager is null");
		}

		this.manager = manager;
	}

	private void setBeanServer(MBeanServer beanServer) {
		this.beanServer = beanServer;
	}

	private void setBeanName(String beanName) {
		if (beanName == null || beanName.isEmpty()) {
			throw new IllegalArgumentException("Bean name is null or empty");
		}

		this.beanName = beanName;
	}

	/**
	 * Start JmxReporter. Registers all required beans in JMX bean server.
	 */
	public void start() {
		SimonManagerMXBean simonManagerMXBean = new SimonManagerMXBeanImpl(manager);
		registerMXBean(simonManagerMXBean, beanName);

		if (registerSimons) {
			jmxRegisterCallback = new JmxRegisterCallback(beanServer, simonDomain);
			manager.callback().addCallback(jmxRegisterCallback);
		}
	}

	private void registerMXBean(Object bean, String newBeanName) {
		try {
			ObjectName beanObjectName = new ObjectName(newBeanName);
			if (toReplace && beanServer.isRegistered(beanObjectName)) {
				logger.warn("Replacing existing SimonManager JMX bean");
				beanServer.unregisterMBean(beanObjectName);
			}

			logger.info("Registering new SimonManager JMX bean with name {}", newBeanName);
			beanServer.registerMBean(bean, beanObjectName);
		} catch (JMException e) {
			throw new SimonException("Failed to register Jmx reporter", e);
		}
	}

	/**
	 * Stop JMX reporter. Unregister all previously registered beans.
	 */
	public void stop() {
		unregisterManagerBean();
		unregisterSimonBeans();
	}

	private void unregisterSimonBeans() {
		if (jmxRegisterCallback != null) {
			manager.callback().removeCallback(jmxRegisterCallback);
			jmxRegisterCallback.stop();
		}
	}

	private void unregisterManagerBean() {
		try {
			ObjectName beanObjectName = new ObjectName(beanName);
			if (beanServer.isRegistered(beanObjectName)) {
				beanServer.unregisterMBean(beanObjectName);
			} else {
				logger.warn("SimonManager JMX bean with name {} was not registered", beanName);
			}
		} catch (JMException e) {
			throw new SimonException("Failed to unregister SimonManager MX bean", e);
		}
	}
}
