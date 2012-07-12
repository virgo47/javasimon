package org.javasimon.console;

import org.javasimon.console.plugin.DummyDetailPlugin;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.calltree.CallTreeCallback;
import org.javasimon.callback.quantiles.QuantilesCallback;
import org.javasimon.callback.timeline.TimelineCallback;
import org.javasimon.console.plugin.CallTreeDetailPlugin;
import org.javasimon.console.plugin.QuantilesDetailPlugin;
import org.javasimon.console.plugin.TimelineDetailPlugin;

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
			SimonManager.callback().addCallback(new CallTreeCallback(50));
			SimonManager.callback().addCallback(new TimelineCallback(10, 60000L));
			SimonData.initialize();
			Timer timer=new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					Split split=SimonManager.getStopwatch("TL").start();
					long wait=randomWait();
					split.stop();
					System.out.println("TL "+wait);
				}
			}, 0, 10000L);
			addSimons("Z",4);
			addStackedSimons();
			final SimonConsoleServlet simonConsoleServlet = new SimonConsoleServlet();
			ServletHolder servletHolder=new ServletHolder(simonConsoleServlet);
			servletHolder.setInitParameter("console-path", "");
			servletHolder.setInitParameter("plugin-classes", 
					QuantilesDetailPlugin.class.getName()
					+","+CallTreeDetailPlugin.class.getName()
					+","+TimelineDetailPlugin.class.getName());
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
	/**
	 * Wait random time 
	 * @return Waited random time
	 */
	private static long randomWait() {
		final long waitTime=random.nextInt(100);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException interruptedException) {
		}
		return waitTime;
	}
	private static void addStopwatches(String name) {
		final int splits = 8+random.nextInt(4);
		final Stopwatch stopwatch = SimonManager.getStopwatch(name);
		System.out.print(name+" "+splits+" ");
		for(int i=0;i<splits;i++) {
			Split split=stopwatch.start();
			try {
				System.out.print(randomWait()+",");
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
	/**
	 * Stacked stopwatches to test call tree
	 */
	private static void addStackedSimons() {
		Split splitA=SimonManager.getStopwatch("Y.A").start();
		Split splitB=SimonManager.getStopwatch("Y.B").start();
		for(int i=0;i<3;i++) {
			Split splitC=SimonManager.getStopwatch("Y.C").start();
			randomWait();
			splitC.stop();
		}
		splitB.stop();
		Split splitD=SimonManager.getStopwatch("Y.D").start();
		randomWait();
		randomWait();
		splitD.stop();
		splitA.stop();
	}
}
