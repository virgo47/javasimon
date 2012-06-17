package org.javasimon.console;

import java.util.Random;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.quantiles.QuantilesCallback;

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
			SimonManager.callback().addCallback(new QuantilesCallback(5, 5));
			SimonData.initialize();
			addSimons("Z",4);
			final SimonConsoleServlet simonConsoleServlet = new SimonConsoleServlet();
			ServletHolder servletHolder=new ServletHolder(simonConsoleServlet);
			servletHolder.setInitParameter("console-path", "");
			context.addServlet(servletHolder, "/*");
			// Start server thread
			server.start();
			simonConsoleServlet.getRequestProcessor().getPluginManager().addPlugin(new DummyDetailPlugin());
			server.join();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	private static final Random random=new Random();
	private static void addSimons(String prefix,int depth)  {
		if (depth==0) {
			addStopwatches(prefix);
		} else {
			addGroups(prefix,depth);
		}
	}

	private static void addStopwatches(String name) {
		final int splits = 8+random.nextInt(4);
		final Stopwatch stopwatch = SimonManager.getStopwatch(name);
		System.out.print(name+" "+splits+" ");
		for(int i=0;i<splits;i++) {
			long simonTime=random.nextInt(100);
			System.out.print(simonTime+",");
//			stopwatch.addTime(simonTime);
			Split split=stopwatch.start();
			try {
				Thread.sleep(simonTime);
			} catch (InterruptedException interruptedException) {
			} finally {
				split.stop();
			}
		}
		System.out.println("");
	}
	private static void addGroups(String namePrefix, int depth) {
		final int sibblings = 1+random.nextInt(3);
		for(int i=0;i<sibblings;i++) {
			char c=(char)('A'+i);
			String name=namePrefix+"."+new String(new char[]{c});
			addSimons(name, depth-1);
		}
	}
}
