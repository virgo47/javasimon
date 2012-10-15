package org.javasimon.console;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.CompositeCallback;
import org.javasimon.callback.CompositeCallbackImpl;
import org.javasimon.callback.async.AsyncCallbackProxyFactory;
import org.javasimon.callback.calltree.CallTreeCallback;
import org.javasimon.callback.quantiles.AutoQuantilesCallback;
import org.javasimon.callback.timeline.TimelineCallback;
import org.javasimon.console.plugin.CallTreeDetailPlugin;
import org.javasimon.console.plugin.QuantilesDetailPlugin;
import org.javasimon.console.plugin.TimelineDetailPlugin;
import org.javasimon.utils.SimonUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main using Jetty to test Simon Console.
 * @author gquintana
 */
public class JettyMain {
    /**
     * Jetty Server
     */
    private Server server;
	private Lock lock=new ReentrantLock();
    /**
     * Time for changing Simons
     */
    private Timer timer=new Timer();
    /**
     * Random generator to generate Simons
     */
    private final RandomHelper random=new RandomHelper();
    /**
     * Initialize Jetty Server
     */
	private void initServer() {
        // Server
        server = new Server(8080);
        // Context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        // Callbacks
        CompositeCallback compositeCallback=new CompositeCallbackImpl();
        // QuantilesCallback automatically configured after 5 splits (5 buckets)
        compositeCallback.addCallback(new AutoQuantilesCallback(5, 5));
        // QuantilesCallback manually configured 5 duration buckets 200ms wide each
        // compositeCallback.addCallback(new FixedQuantilesCallback(0L, 200L, 5));
        // TimelineCallback 10 time range buckets of 1 minute each
        compositeCallback.addCallback(new TimelineCallback(10, 60000L));
        SimonManager.callback().addCallback(new AsyncCallbackProxyFactory(compositeCallback).newProxy());
        // CallTreeCallback doesn't support asynchronism
        SimonManager.callback().addCallback(new CallTreeCallback(50));
        // Simon Servlet
        final SimonConsoleServlet simonConsoleServlet = new SimonConsoleServlet();
        ServletHolder servletHolder=new ServletHolder(simonConsoleServlet);
        servletHolder.setInitParameter("console-path", "");
        servletHolder.setInitParameter("plugin-classes",
                QuantilesDetailPlugin.class.getName()
                +","+CallTreeDetailPlugin.class.getName()
                +","+TimelineDetailPlugin.class.getName());
        context.addServlet(servletHolder, "/*");
    }

    /**
     * Add Simons
     */
    private void initData() {
        addDefaultSimons();
        addChangingSimons();
        addStackedSimons();
        addManySimons();
    }

    /**
     * Run Jetty and wait till it stops
     */
    private void runAndWait() throws Exception {
        // Start server thread
        server.start();
        server.join();
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        try {
            JettyMain main=new JettyMain();
            main.initServer();
            main.initData();
            main.runAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add basic simons A, B, C and X.
     * X is used to test Counter rendering
     * 
     */
    private void addDefaultSimons() {
        SimonData.initialize();
    }

    /**
     * Starts a timer which changes Simons values.
     * TL is used to test Timeline and Quantiles plugins rendering
     */
    private void addChangingSimons() {
        timer.schedule(new TimerTask(){
            final Stopwatch tlStopwatch= SimonManager.getStopwatch("TL");
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("TL "+ addStopwatchSplit(tlStopwatch));
                } finally {
                    lock.unlock();
                }
            }
        }, 0, 10000L);
    }

    /**
     * Add many Simons for performances testing
     * Z.* Simons are used for performance testing
     */
    private void addManySimons() {
        new Thread() {
            @Override
            public void run() {
                addManySimons("Z", 4, 3, 3, 6);
            }
        }.start();

    }
    private static final String ALPHABET="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * Recursively Add many Simons for performances testing
     */
	private void addManySimons(String prefix, int depth, int groupWidth, int leafWidth, int splitWidth)  {
		if (depth==0) {
            // Generate Simons of type Stopwatch
            final int sibblings = random.randomInt(Math.min(1, groupWidth / 2), leafWidth);
            for(int i=0;i<sibblings;i++) {
                String name=prefix+"."+ALPHABET.charAt(i);
                addStopwatchSplits(SimonManager.getStopwatch(name), splitWidth);
            }
		} else {
            // Generate Simons of type Unknown
            final int sibblings = random.randomInt(Math.min(1, groupWidth / 2), groupWidth);
            for(int i=0;i<sibblings;i++) {
                String name=prefix+"."+ALPHABET.charAt(i);
                addManySimons(name, depth - 1, groupWidth, leafWidth, splitWidth);
            }
		}
	}

    /**
     * Generate a split for a Stopwatch
     */
	private long addStopwatchSplit(Stopwatch stopwatch) {
		Split split=stopwatch.start();
		try {
			random.randomWait(50,150);
		} finally {
			split.stop();
		}
		return split.runningFor()/SimonUtils.NANOS_IN_MILLIS;
	}

    /**
     * Generate a Simon of type "Stopwatch" and fill it with some Splits
     * @param splitWidth Max number of splits per Stopwatch
     */
	private void addStopwatchSplits(Stopwatch stopwatch, int splitWidth) {
		final int splits = random.randomInt(splitWidth / 2, splitWidth);
		try {
			lock.lock();
			System.out.print(stopwatch.getName()+" "+splits+": ");
            for(int i=0;i<splits;i++) {
          		System.out.print(addStopwatchSplit(stopwatch)+",");
          	}
			System.out.println();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * Stacked stopwatches to test call tree
	 */
	private void addStackedSimons() {
		Split splitA=SimonManager.getStopwatch("Y.A").start();
		Split splitB=SimonManager.getStopwatch("Y.B").start();
		addStopwatchSplits(SimonManager.getStopwatch("Y.C"), 6);
		splitB.stop();
		Split splitD=SimonManager.getStopwatch("Y.D").start();
		random.randomWait(100,250);
		random.randomWait(100,250);
		splitD.stop();
		splitA.stop();
	}

}
