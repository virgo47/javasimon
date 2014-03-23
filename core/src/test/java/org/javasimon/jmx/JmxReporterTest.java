package org.javasimon.jmx;

import org.javasimon.Manager;
import org.javasimon.SimonException;
import org.javasimon.SimonManager;
import org.javasimon.callback.Callback;
import org.javasimon.callback.CompositeCallback;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.management.*;

import java.lang.management.ManagementFactory;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class JmxReporterTest {

	private Manager manager;
	private MBeanServer beanServer;
	public static final String CUSTOM_BEAN_NAME = "custom.bean.name:type=Custom";
	private ObjectName customObjectName;
	private CompositeCallback compositeCallback;

	@BeforeMethod
	public void beforeMethod() throws MalformedObjectNameException {
		manager = mock(Manager.class);
		beanServer = mock(MBeanServer.class);

		customObjectName = new ObjectName(CUSTOM_BEAN_NAME);

		compositeCallback = mock(CompositeCallback.class);
		when(manager.callback()).thenReturn(compositeCallback);
	}

	@Test
	public void testStartJmxReporter() throws Exception {
		JmxReporter reporter = JmxReporter.forManager(manager).beanServer(beanServer).build();
		reporter.start();

		verify(beanServer).registerMBean(argThat(new SimonManagerMXBeanVerifier(manager)), eq(new ObjectName(JmxReporter.DEFAULT_BEAN_NAME)));
	}

	@Test(expectedExceptions = SimonException.class)
	public void testStartJmxReporter_JMException() throws Exception {
		when(beanServer.registerMBean(any(Object.class), any(ObjectName.class)))
				.thenThrow(JMException.class);

		JmxReporter.forManager(manager).beanServer(beanServer).build().start();
	}

	@Test
	public void testStartJmxReporterForDefaultManager() throws Exception {
		JmxReporter reporter = JmxReporter.forDefaultManager().beanServer(beanServer).build();
		reporter.start();

		Manager defaultManager = SimonManager.manager();
		verify(beanServer).registerMBean(argThat(new SimonManagerMXBeanVerifier(defaultManager)), eq(new ObjectName(JmxReporter.DEFAULT_BEAN_NAME)));
	}

	@Test
	public void testStartJmxReporterWithCustomBeanName() throws Exception {
		JmxReporter reporter = JmxReporter.forManager(manager).beanServer(beanServer).beanName(CUSTOM_BEAN_NAME).build();
		reporter.start();

		verify(beanServer).registerMBean(argThat(new SimonManagerMXBeanVerifier(manager)), eq(customObjectName));
	}

	@Test
	public void testReplaceJmxReporterOnStart() throws Exception {
		when(beanServer.isRegistered(customObjectName)).thenReturn(true);

		JmxReporter reporter = JmxReporter.forManager(manager).beanServer(beanServer).beanName(CUSTOM_BEAN_NAME).replaceExisting(true).build();
		reporter.start();

		InOrder order = inOrder(beanServer);

		order.verify(beanServer).unregisterMBean(customObjectName);
		order.verify(beanServer).registerMBean(argThat(new SimonManagerMXBeanVerifier(manager)), eq(customObjectName));
	}

	@Test
	public void testReplaceJmxStop() throws Exception {
		when(beanServer.isRegistered(customObjectName)).thenReturn(true);

		JmxReporter reporter = JmxReporter.forManager(manager).beanServer(beanServer).beanName(CUSTOM_BEAN_NAME).build();
		reporter.start();
		reporter.stop();

		InOrder order = inOrder(beanServer);

		order.verify(beanServer).isRegistered(customObjectName);
		order.verify(beanServer).unregisterMBean(customObjectName);
	}

	@Test
	public void testReplaceJmxStopWhenBeanIsNotRegistered() throws Exception {
		when(beanServer.isRegistered(customObjectName)).thenReturn(false);

		JmxReporter reporter = JmxReporter.forManager(manager).beanServer(beanServer).beanName(CUSTOM_BEAN_NAME).build();
		reporter.start();
		reporter.stop();

		verify(beanServer).isRegistered(customObjectName);
		verify(beanServer, never()).unregisterMBean(customObjectName);
	}

	@Test(expectedExceptions = SimonException.class)
	public void testReplaceJmxStop_JMException() throws Exception {
		when(beanServer.isRegistered(customObjectName)).thenReturn(true);
		doThrow(InstanceNotFoundException.class).when(beanServer).unregisterMBean(customObjectName);

		JmxReporter reporter = JmxReporter.forManager(manager).beanServer(beanServer).beanName(CUSTOM_BEAN_NAME).build();
		reporter.start();
		reporter.stop();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetNullBeanName() {
		JmxReporter.forManager(manager).beanName(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetEmptyBeanName() {
		JmxReporter.forManager(manager).beanName("");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetNullManager() {
		JmxReporter.forManager(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetNullSimonDomain() {
		JmxReporter.forManager(manager).simonDomain(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetEmptySimonDomain() {
		JmxReporter.forManager(manager).simonDomain("");
	}

	@Test
	public void testRegisterSimons() throws Exception {
		JmxReporter reporter = JmxReporter.forManager(manager).beanName(CUSTOM_BEAN_NAME).beanServer(beanServer).registerSimons(true).build();
		reporter.start();

		verify(compositeCallback).addCallback(argThat(new JmxRegistrationCallbackVerifier(JmxReporter.DEFAULT_SIMON_DOMAIN, beanServer)));
	}

	@Test
	public void testNoInteractionWhenRegistrationIsNotRequired() {
		JmxReporter reporter = JmxReporter.forManager(manager).beanName(CUSTOM_BEAN_NAME).beanServer(beanServer).registerSimons(false).build();
		reporter.start();

		verify(compositeCallback, never()).addCallback(any(Callback.class));
	}

	@Test
	public void testRegisterSimonsWithCustomDomain() throws Exception {
		String simonDomain = "simonDomain";

		JmxReporter reporter = JmxReporter
				.forManager(manager)
				.beanName(CUSTOM_BEAN_NAME)
				.beanServer(beanServer)
				.registerSimons(true)
				.simonDomain(simonDomain)
				.build();
		reporter.start();

		verify(compositeCallback).addCallback(argThat(new JmxRegistrationCallbackVerifier(simonDomain, beanServer)));
	}

	@Test
	public void testRegisterManagerInGlobalBeanServer() {
		MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
		// Clean up in case of probable previous test failure
		try {
			platformMBeanServer.unregisterMBean(customObjectName);
		} catch (JMException e) {
			e.printStackTrace();
		}

		JmxReporter reporter = JmxReporter.forManager(manager).beanName(CUSTOM_BEAN_NAME).build();
		reporter.start();

		Assert.assertTrue(platformMBeanServer.isRegistered(customObjectName));

		reporter.stop();
	}

	private class SimonManagerMXBeanVerifier extends ArgumentMatcher<Object> {

		private final Manager expectedManager;

		public SimonManagerMXBeanVerifier(Manager expectedManager) {
			this.expectedManager = expectedManager;
		}

		@Override
		public boolean matches(Object manager) {
			if (!(manager instanceof SimonManagerMXBeanImpl)) {
				return false;
			}

			SimonManagerMXBeanImpl simonManagerMXBean = (SimonManagerMXBeanImpl) manager;
			Manager actualManager = simonManagerMXBean.getManager();
			return actualManager == expectedManager;
		}
	}

	private class JmxRegistrationCallbackVerifier extends ArgumentMatcher<Callback> {

		private final String domain;
		private final MBeanServer beanServer;

		public JmxRegistrationCallbackVerifier(String domain, MBeanServer beanServer) {
			this.domain = domain;
			this.beanServer = beanServer;
		}

		@Override
		public boolean matches(Object o) {
			if (!(o instanceof JmxRegisterCallback)) {
				return false;
			}

			JmxRegisterCallback registerCallback = (JmxRegisterCallback) o;
			if (!registerCallback.getDomain().equals(domain)) {
				return false;
			}

			if (registerCallback.getBeanServer() != beanServer) {
				return false;
			}

			return true;
		}
	}

}
