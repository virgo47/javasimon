package org.javasimon.console;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Main using Jetty to test Simon Console.
 * @author gquintana
 */
public class JettyMain {
	public static void main(String[] args) {
		try {
			// Server
			Server server = new Server(8080);
			// Context
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
			// Servlet
			SimonData.initialize();
			ServletHolder servletHolder=new ServletHolder(new SimonConsoleServlet());
			servletHolder.setInitParameter("console-path", "");
			context.addServlet(servletHolder, "/*");
			// Start server thread
			server.start();
			server.join();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
