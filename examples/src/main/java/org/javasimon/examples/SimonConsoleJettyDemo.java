package org.javasimon.examples;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.javasimon.SimonManager;
import org.javasimon.callback.CompositeCallback;
import org.javasimon.callback.CompositeCallbackImpl;
import org.javasimon.callback.async.AsyncCallbackProxyFactory;
import org.javasimon.callback.calltree.CallTreeCallback;
import org.javasimon.callback.quantiles.AutoQuantilesCallback;
import org.javasimon.callback.timeline.TimelineCallback;
import org.javasimon.console.SimonConsoleServlet;
import org.javasimon.console.plugin.CallTreeDetailPlugin;
import org.javasimon.console.plugin.QuantilesDetailPlugin;
import org.javasimon.console.plugin.TimelineDetailPlugin;

/**
 * Example of using Jetty to run Simon Console.
 *
 * @author gquintana
 */
public class SimonConsoleJettyDemo {

	/** Jetty Server. */
	private Server server;
	/** Initialize Jetty Server. */
	private final SimonDataGenerator dataGenerator = new SimonDataGenerator();

	/** Main method. */
	public static void main(String[] args) {
		try {
			SimonConsoleJettyDemo main = new SimonConsoleJettyDemo();
			main.initServer();
			main.initData();
			main.runAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initServer() {
		// Server
		server = new Server(8080);
		// Context
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		// Callbacks
		CompositeCallback compositeCallback = new CompositeCallbackImpl();
		// QuantilesCallback automatically configured after 5 splits (5 buckets)
		compositeCallback.addCallback(new AutoQuantilesCallback(5, 5));
		// QuantilesCallback manually configured 5 duration buckets 200ms wide each
		// compositeCallback.addCallback(new FixedQuantilesCallback(0L, 200L, 5));
		// TimelineCallback 10 time range buckets of 1 minute each
		compositeCallback.addCallback(new TimelineCallback(10, 60000L));
		SimonManager.callback().addCallback(new AsyncCallbackProxyFactory(compositeCallback).newProxy());
		// CallTreeCallback doesn't support asynchronous operation
		SimonManager.callback().addCallback(new CallTreeCallback(50));
		// Simon Servlet
		final SimonConsoleServlet simonConsoleServlet = new SimonConsoleServlet();
		ServletHolder servletHolder = new ServletHolder(simonConsoleServlet);
		servletHolder.setInitParameter("console-path", "");
		servletHolder.setInitParameter("plugin-classes",
			QuantilesDetailPlugin.class.getName()
				+ "," + CallTreeDetailPlugin.class.getName()
				+ "," + TimelineDetailPlugin.class.getName());
		context.addServlet(servletHolder, "/*");
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
		server.join();
	}
}
