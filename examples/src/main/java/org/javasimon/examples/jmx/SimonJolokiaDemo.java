package org.javasimon.examples.jmx;

import org.javasimon.SimonManager;
import org.javasimon.callback.CompositeCallback;
import org.javasimon.callback.CompositeCallbackImpl;
import org.javasimon.callback.async.AsyncCallbackProxyFactory;
import org.javasimon.callback.calltree.CallTreeCallback;
import org.javasimon.callback.quantiles.AutoQuantilesCallback;
import org.javasimon.callback.timeline.TimelineCallback;
import org.javasimon.examples.SimonDataGenerator;
import org.javasimon.jmx.JmxRegisterCallback;
import org.javasimon.jmx.SimonManagerMXBeanImpl;
import org.jolokia.jvmagent.JolokiaServer;
import org.jolokia.jvmagent.JolokiaServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Example of using Jolokia with JavaSimon.
 * <p/>
 * Jolokia Agent starts on http://localhost:8778/jolokia/
 * Search http://localhost:8778/jolokia/search/org.javasimon:*
 * Manager http://localhost:8778/jolokia/read/org.javasimon:type=Manager
 * Stopwatch http://localhost:8778/jolokia/read/org.javasimon:name=A,type=Stopwatch
 * Counter http://localhost:8778/jolokia/read/org.javasimon:name=X,type=Counter
 *
 * @author G Quintana
 */
public class SimonJolokiaDemo {

	/** Jolokia Server. */
	private JolokiaServer server;
	/** Initialize Jetty Server. */
	private final SimonDataGenerator dataGenerator = new SimonDataGenerator();

	/** Main method. */
	public static void main(String[] args) {
		try {
			SimonJolokiaDemo main = new SimonJolokiaDemo();
			main.initServer();
			main.initData();
			main.runAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initServer() throws IOException, JMException {
		// Register JMX
		MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		ObjectName beanObjectName = new ObjectName("org.javasimon:type=Manager");
		SimonManagerMXBeanImpl bean = new SimonManagerMXBeanImpl(SimonManager.manager());
		beanServer.registerMBean(bean, beanObjectName);
		// Server
		Map<String, String> serverConfigMap = new HashMap<>();
		JolokiaServerConfig serverConfig = new JolokiaServerConfig(serverConfigMap);
		server = new JolokiaServer(serverConfig, true);
		// Callbacks
		CompositeCallback compositeCallback = new CompositeCallbackImpl();
		// QuantilesCallback automatically configured after 5 splits (5 buckets)
		compositeCallback.addCallback(new AutoQuantilesCallback(5, 5));
		// QuantilesCallback manually configured 5 duration buckets 200ms wide each
		// compositeCallback.addCallback(new FixedQuantilesCallback(0L, 200L, 5));
		// TimelineCallback 10 time range buckets of 1 minute each
		compositeCallback.addCallback(new TimelineCallback(10, 60000L));
		compositeCallback.addCallback(new JmxRegisterCallback(beanServer, "org.javasimon"));
		SimonManager.callback().addCallback(new AsyncCallbackProxyFactory(compositeCallback).newProxy());
		// CallTreeCallback doesn't support asynchronous operation
		SimonManager.callback().addCallback(new CallTreeCallback(50));
	}

	/** Add Simons. */
	private void initData() {
		dataGenerator.addDefaultSimons();
		dataGenerator.addChangingSimons();
		dataGenerator.addStackedSimons();
		dataGenerator.addManySimons();
	}

	/** Run Jetty and wait till it stops. */
	private void runAndWait() throws Exception {
		// Start server thread
		server.start();
		Logger logger = LoggerFactory.getLogger(SimonJolokiaDemo.class);
		logger.info("Jolokia Agent started http://localhost:8778/jolokia/");
		logger.info("Search http://localhost:8778/jolokia/search/org.javasimon:*");
		logger.info("Manager http://localhost:8778/jolokia/read/org.javasimon:type=Manager");
		logger.info("Stopwatch http://localhost:8778/jolokia/read/org.javasimon:name=A,type=Stopwatch");
		logger.info("Counter http://localhost:8778/jolokia/read/org.javasimon:name=X,type=Counter");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line;
		boolean stop = false;
		while (!stop && (line = reader.readLine()) != null) {
			stop = line.toLowerCase().contains("stop");
		}
	}
}
