package org.javasimon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * This class implements periodical removing of old incremental Simons for specified Manager.
 * Unused incremental samples can cause memory leaks and performance degradation and should be purged regularly.
 *
 * This class can be one of two states: stopped or started. When an instance of the class is create it is in the
 * stopped stated. Method {@link IncrementalSimonsPurger#start(long, java.util.concurrent.TimeUnit)}
 * changes state of the class to started. In this state periodical Simons purging is performed. To stop
 * purgin one need to call {@link IncrementalSimonsPurger#cancel()}. This method will change current state to the class
 * to stopped. All other state transitions except from stopped to started or from started to stopped are forbidden.
 *
 * This class is thread safe.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public final class IncrementalSimonsPurger {

	 // Interface that hides actual Simons purging for different types of managers
	interface ManagerPurger {
		void purgeManager(Manager manager, long thresholdMs);
	}

	// Simons purger for EnabledManager
	static final ManagerPurger ENABLED_MANAGER_PURGER = new ManagerPurger() {
		@Override
		public void purgeManager(Manager manager, long thresholdMs) {
			((EnabledManager) manager).purgeIncrementalSimonsOlderThan(thresholdMs);
		}
	};

	// Simons purger for SwitchingManager
	static final ManagerPurger SWITCH_MANAGER_PURGER = new ManagerPurger() {
		@Override
		public void purgeManager(Manager manager, long thresholdMs) {
			((SwitchingManager) manager).purgeIncrementalSimonsOlderThan(thresholdMs);
		}
	};

	// Simons purger for DisabledManager
	static final ManagerPurger DISABLED_MANAGER_PURGER = new ManagerPurger() {
		@Override
		public void purgeManager(Manager manager, long thresholdMs) {
		}
	};

	/** Manager where old incremental Simons will be purged */
	private final Manager manager;

	/** Instance of ManagerPurger that does actual purging */
	private final ManagerPurger purger;

	/** Scheduled executor service that should periodically execute purging task */
	private final ScheduledExecutorService executorService;

	/** Currently started purging task */
	private ScheduledFuture<?> scheduledFuture;

	/**
	 * Create Simons purger for the specfied Manager.
	 *
	 * @param manager manager for which old incremental Simons will be purged
	 */
	public IncrementalSimonsPurger(Manager manager) {
		this(manager, createExecutorService());
	}

	IncrementalSimonsPurger(Manager manager, ScheduledExecutorService executorService) {
		this.manager = manager;
		this.purger = getPurger(manager);
		this.executorService = executorService;
	}

	private static ScheduledExecutorService createExecutorService() {
		ThreadFactory threadFactory = new DaemonThreadFactory();
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(threadFactory);
		return executorService;
	}

	private ManagerPurger getPurger(Manager manager) {
		if (manager instanceof EnabledManager) {
			return ENABLED_MANAGER_PURGER;
		} else if (manager instanceof SwitchingManager) {
			return SWITCH_MANAGER_PURGER;
		} else {
			return DISABLED_MANAGER_PURGER;
		}
	}

	/**
	 * Start periodical Simons purging with the specified period.
	 *
	 * @param timeDuration duration of purging period
	 * @param timeUnit time unit of period duration
	 */
	public synchronized void start(long timeDuration, TimeUnit timeUnit) {
		if (scheduledFuture == null) {
			PurgerRunnable runnable = new PurgerRunnable(manager, purger);
			scheduledFuture = executorService.scheduleWithFixedDelay(runnable, timeDuration, timeDuration, timeUnit);
		} else {
			throw new IllegalStateException("IncrementSimonPurger has already been started");
		}
	}

	/**
	 * Cancel periodical Simons purging if it was started.
	 */
	public synchronized void cancel() {
		if (scheduledFuture != null) {
			boolean mayInterruptIfRunning = false;
			scheduledFuture.cancel(mayInterruptIfRunning);
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
		private ManagerPurger managerPurger;
		private long periodStartMs;

		public PurgerRunnable(Manager manager, ManagerPurger managerPurger) {
			this.manager = manager;
			this.managerPurger = managerPurger;
			periodStartMs = System.currentTimeMillis();
		}

		@Override
		public void run() {
			logger.debug("Purging old incremental Simons");
			managerPurger.purgeManager(manager, periodStartMs);
			periodStartMs = System.currentTimeMillis();
		}

		Manager getManager() {
			return manager;
		}

		ManagerPurger getManagerPurger() {
			return managerPurger;
		}
	}

	/**
	 * Thread factory that creates daemon thread for each Runnable. Using this factory
	 * for executor service will not prevent application stopping.
	 */
	static class DaemonThreadFactory implements ThreadFactory {

		private int threadNumber;

		@Override
		public Thread newThread(Runnable runnable) {
			Thread daemonThread = new Thread(runnable);
			daemonThread.setDaemon(true);
			daemonThread.setName("javasimon-simonsPurger-" + (++threadNumber));
			return daemonThread;
		}
	}
}
