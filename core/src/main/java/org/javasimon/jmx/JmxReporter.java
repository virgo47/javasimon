package org.javasimon.jmx;

import org.javasimon.Manager;
import org.javasimon.SimonException;
import org.javasimon.SimonManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Utility class for registering JMX beans that can be used to access Simon manager and Simons.
 * JmxReporter is created by either {@link org.javasimon.jmx.JmxReporter#forManager(org.javasimon.Manager)}
 * or {@link JmxReporter#forDefaultManager()}. Additional configuration methods can be called using fluent
 * API ().
 * Finally {@link JmxReporter#start()} will set up all expected MX beans and system is ready for monitoring
 * through JMX. Method {@link JmxReporter#stop()} unregisters all related MX beans when monitoring is no
 * longer required.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class JmxReporter {

	public static final String DEFAULT_BEAN_NAME = "org.javasimon.jmx.JmxReporter:type=Simon";
	public static final String DEFAULT_SIMON_DOMAIN = "org.javasimon.jmx.simons";

	private static final Logger logger = LoggerFactory.getLogger(JmxReporter.class);

	/** Simon manager that will be accessed through registered JMX bean */
	private final Manager manager;

	/** JMX bean server used by JmxReporter */
	private MBeanServer beanServer;

	/** Name of a new JMX bean for Simon manager */
	private String beanName;

	/** Should already registered bean with the specified name be replaced. */
	private boolean replaceExistingMxBeans;

	/** Should JMX beans for separate Simons be registered. */
	private boolean registerSimons;

	/**
	 * Should JMX beans for already existing Simons be registered ({@link #registerSimons} must be true
	 * for this to have effect).
	 */
	private boolean registerExistingSimons;

	/** Domain of JMX beans for separate Simons. */
	private String simonDomain;

	private JmxRegisterCallback jmxRegisterCallback;

	private JmxReporter(Manager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("Manager is null");
		}

		this.manager = manager;
	}

	/**
	 * Creates new JmxReporter for the specified manager with default values.
	 *
	 * @param manager manager that will be used by JmxReporter
	 * @return new JmxReporter
	 */
	static public JmxReporter forManager(Manager manager) {
		JmxReporter reporter = new JmxReporter(manager);

		reporter.setBeanName(DEFAULT_BEAN_NAME);
		reporter.simonDomain = DEFAULT_SIMON_DOMAIN;
		reporter.beanServer = ManagementFactory.getPlatformMBeanServer();

		return reporter;
	}

	/**
	 * Creates new JmxReporter for the default manager with default values.
	 *
	 * @return new JmxReporter
	 */
	public static JmxReporter forDefaultManager() {
		return forManager(SimonManager.manager());
	}


	/**
	 * Specify domain for registered JMX for separate Simons. Is used only if {@link #registerSimons} set to true.
	 *
	 * @param simonDomain domain for separate JMX beans for separate Simons
	 * @return this
	 */
	public JmxReporter simonDomain(String simonDomain) {
		setSimonDomain(simonDomain);
		return this;
	}

	/**
	 * Specifies that previously registered bean with the same name should be replaced.
	 *
	 * @return this
	 */
	public JmxReporter replaceExisting() {
		replaceExistingMxBeans = true;
		return this;
	}

	/**
	 * Specifies that separate JMX beans should be registered for each Simon in current manager.
	 *
	 * @return this
	 */
	public JmxReporter registerSimons() {
		registerSimons = true;
		return this;
	}

	public JmxReporter registerExistingSimons() {
		this.registerExistingSimons = true;
		return this;
	}

	/**
	 * Bean name that will be used to register JMX bean for Simon manager.
	 *
	 * @param beanName bean name that will be used to register JMX bean for Simon manager
	 * @return this
	 */
	public JmxReporter beanName(String beanName) {
		setBeanName(beanName);
		return this;
	}

	JmxReporter beanServer(MBeanServer beanServer) {
		setBeanServer(beanServer);
		return this;
	}

	private void setBeanName(String beanName) {
		if (beanName == null || beanName.isEmpty()) {
			throw new IllegalArgumentException("Bean name is null or empty");
		}

		this.beanName = beanName;
	}

	public Manager getManager() {
		return manager;
	}

	public MBeanServer getBeanServer() {
		return beanServer;
	}

	public void setBeanServer(MBeanServer beanServer) {
		this.beanServer = beanServer;
	}

	public String getBeanName() {
		return beanName;
	}

	public String getSimonDomain() {
		return simonDomain;
	}

	public void setSimonDomain(String simonDomain) {
		if (simonDomain == null || simonDomain.isEmpty()) {
			throw new IllegalArgumentException("Simon domain is null or empty");
		}
		this.simonDomain = simonDomain;
	}

	public boolean isReplaceExistingMxBeans() {
		return replaceExistingMxBeans;
	}

	public void setReplaceExistingMxBeans(boolean replaceExistingMxBeans) {
		this.replaceExistingMxBeans = replaceExistingMxBeans;
	}

	public boolean isRegisterSimons() {
		return registerSimons;
	}

	public void setRegisterSimons(boolean registerSimons) {
		this.registerSimons = registerSimons;
	}

	public boolean isRegisterExistingSimons() {
		return registerExistingSimons;
	}

	public void setRegisterExistingSimons(boolean registerExistingSimons) {
		this.registerExistingSimons = registerExistingSimons;
	}

	/** Starts JmxReporter - registers all required beans in JMX bean server. */
	public JmxReporter start() {
		SimonManagerMXBean simonManagerMXBean = new SimonManagerMXBeanImpl(manager);
		registerMXBean(simonManagerMXBean, beanName);

		if (registerSimons) {
			jmxRegisterCallback = new JmxRegisterCallback(beanServer, simonDomain);
			if (registerExistingSimons) {
				jmxRegisterCallback.setRegisterExisting(true);
			}
			manager.callback().addCallback(jmxRegisterCallback);
		}
		return this;
	}

	private void registerMXBean(Object bean, String newBeanName) {
		try {
			ObjectName beanObjectName = new ObjectName(newBeanName);
			if (replaceExistingMxBeans && beanServer.isRegistered(beanObjectName)) {
				logger.warn("Replacing existing SimonManager JMX bean");
				beanServer.unregisterMBean(beanObjectName);
			}

			logger.info("Registering new SimonManager JMX bean with name {}", newBeanName);
			beanServer.registerMBean(bean, beanObjectName);
		} catch (JMException e) {
			throw new SimonException("Failed to register Jmx reporter", e);
		}
	}

	/** Stop JMX reporter. Unregister all previously registered beans. */
	public void stop() {
		unregisterManagerBean();
		unregisterSimonBeans();
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

	private void unregisterSimonBeans() {
		if (jmxRegisterCallback != null) {
			manager.callback().removeCallback(jmxRegisterCallback);
		}
	}
}
