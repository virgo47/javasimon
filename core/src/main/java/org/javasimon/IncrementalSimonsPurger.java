package org.javasimon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * This class implements periodical removing of old incremental Simons for specified Manager.
 * Unused incremental samples can cause memory leaks and performance degradation and should be purged regularly.
 *
 * Purger can be in one of two states: stopped or started. When an instance of the class is create it is in the
 * stopped stated. Method {@link IncrementalSimonsPurger#start(long, java.util.concurrent.TimeUnit)}
 * changes state of the class to started. In this state periodical Simons purging is performed. To stop
 * purging one need to call {@link IncrementalSimonsPurger#cancel()}. This method will change current state of the object
 * to stopped. All other state transitions except from stopped to started or from started to stopped are forbidden.
 *
 * This class is thread safe.
 *
 * Here is a code example of how to configure {@link org.javasimon.IncrementalSimonsPurger} to clean all outdated incremental
 * Simons once a day for default manager:
 * <pre>
 * {@code
 * Manager manager = SimonManager.manager();
 * IncrementalSimonsPurger incrementalSimonsPurger = new IncrementalSimonsPurger(manager);
 * incrementalSimonsPurger.start(1, TimeUnit.DAYS);
 * }
 * </pre>
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public final class IncrementalSimonsPurger {

	/** Manager where old incremental Simons will be purged */
	private final Manager manager;

	/** Scheduled executor service that should periodically execute purging task */
	private final ScheduledExecutorService executorService;

	/** Currently started purging task */
	private ScheduledFuture<?> scheduledFuture;

	/**
	 * Create Simons purger for the specified Manager.
	 *
	 * @param manager manager for which old incremental Simons will be purged
	 */
	public IncrementalSimonsPurger(Manager manager) {
		this(manager, createExecutorService());
	}

	IncrementalSimonsPurger(Manager manager, ScheduledExecutorService executorService) {
		this.manager = manager;
		this.executorService = executorService;
	}

	private static ScheduledExecutorService createExecutorService() {
		ThreadFactory threadFactory = new DaemonThreadFactory();
		return Executors.newSingleThreadScheduledExecutor(threadFactory);
	}

	/**
	 * Start periodical Simons purging with the specified period.
	 *
	 * @param period duration of purging period
	 * @param timeUnit time unit of period duration
	 */
	public synchronized void start(long period, TimeUnit timeUnit) {
		if (scheduledFuture == null) {
			PurgerRunnable runnable = new PurgerRunnable(manager);
			scheduledFuture = executorService.scheduleWithFixedDelay(runnable, period, period, timeUnit);
		} else {
			throw new IllegalStateException("IncrementSimonPurger has already been started");
		}
	}

	/**
	 * Cancel periodical Simons purging if it was started.
	 */
	public synchronized void cancel() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
			scheduledFuture = null;
		} else {
			throw new IllegalStateException("IncrementSimonPurger is either cancelled or was not started");
		}
	}

	/**
	 * Task submitted to scheduled executor. It is periodically executed to remove
	 * old incremental Simons.
	 */
	static class PurgerRunnable implements Runnable {

		private static final Logger logger = LoggerFactory.getLogger(PurgerRunnable.class);

		private Manager manager;
		private long periodStartMs;

		public PurgerRunnable(Manager manager) {
			this(manager, manager.milliTime());
		}

		PurgerRunnable(Manager manager, long periodStartMs) {
			this.manager = manager;
			this.periodStartMs = periodStartMs;
		}

		@Override
		public void run() {
			logger.debug("Purging old incremental Simons");
			if (manager instanceof EnabledManager) {
				((EnabledManager) manager).purgeIncrementalSimonsOlderThan(periodStartMs);
			} else if (manager instanceof SwitchingManager) {
				((SwitchingManager) manager).purgeIncrementalSimonsOlderThan(periodStartMs);
			}
			periodStartMs = manager.milliTime();
		}

		Manager getManager() {
			return manager;
		}
	}

	/**
	 * Thread factory that creates daemon thread for each Runnable. Using this factory
	 * for executor service will not prevent application stopping.
	 */
	static class DaemonThreadFactory implements ThreadFactory {

		private int threadNumber;

		@Override
		public synchronized Thread newThread(Runnable runnable) {
			Thread daemonThread = new Thread(runnable);
			daemonThread.setDaemon(true);
			daemonThread.setName("javasimon-simonsPurger-" + (++threadNumber));
			return daemonThread;
		}
	}
}
