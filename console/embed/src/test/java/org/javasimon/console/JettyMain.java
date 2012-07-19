package org.javasimon.console;

import org.javasimon.console.plugin.DummyDetailPlugin;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
import org.javasimon.utils.SimonUtils;

/**
 * Main using Jetty to test Simon Console.
 * @author gquintana
 */
public class JettyMain {
	private static Lock lock=new ReentrantLock();
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
					try {
						lock.lock();
						System.out.println("TL "+addRandomStopwatchSplit(null));
					} finally {
						lock.unlock();
					}
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
	private static int randomInt(int min, int max) {
		return min+random.nextInt(max-min);
	}
	/**
	 * Wait random time 
	 * @return Waited random time
	 */
	private static long randomWait() {
		final long waitTime=randomInt(50, 100);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException interruptedException) {
		}
		return waitTime;
	}
	private static long addRandomStopwatchSplit(Stopwatch stopwatch) {
		Split split=stopwatch.start();
		try {
			randomWait();
		} finally {
			split.stop();
		}
		return split.runningFor()/SimonUtils.NANOS_IN_MILLIS;
	}
	private static void addRandomStopwatchSplits(final Stopwatch stopwatch, final int splits) {
		for(int i=0;i<splits;i++) {
			System.out.print(addRandomStopwatchSplit(stopwatch));
		}
	}
	private static void addStopwatches(String name) {
		final int splits = randomInt(4,8);
		final Stopwatch stopwatch = SimonManager.getStopwatch(name);
		try {
			lock.lock();
			System.out.print(name+" "+splits+": ");
			addRandomStopwatchSplits(stopwatch, splits);
			System.out.println();
		} finally {
			lock.unlock();
		}
	}
	private static void addGroups(String namePrefix, int depth) {
		final int sibblings = randomInt(1,3);
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
		addRandomStopwatchSplits(SimonManager.getStopwatch("Y.C"), 3);
		splitB.stop();
		Split splitD=SimonManager.getStopwatch("Y.D").start();
		randomWait();
		randomWait();
		splitD.stop();
		splitA.stop();
	}

}
