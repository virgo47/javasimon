package org.javasimon.console;

import java.util.Random;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.javasimon.SimonManager;
import org.javasimon.Split;

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
			addSimons("Z",5);
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
	private static final Random random=new Random();
	private static void addSimons(String prefix,int depth)  {
		for(int i=0;i<3+random.nextInt(4);i++) {
			if (depth==0) {
				long simonTime=random.nextInt(100*100000);
				System.out.println("Stopwatch "+prefix+" "+simonTime);
				SimonManager.getStopwatch(prefix).addTime(simonTime);
			} else {
				char c=(char)('A'+i);
				String simonName=prefix+"."+new String(new char[]{c});
				addSimons(simonName, depth-1);
			}
		}
	}
}
