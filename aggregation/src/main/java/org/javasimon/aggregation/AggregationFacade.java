package org.javasimon.aggregation;

import org.javasimon.aggregation.metricsDao.DaoException;
import org.javasimon.jmx.SimonManagerMXBean;
import org.javasimon.jmx.StopwatchSample;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class AggregationFacade {
	public static class Args {
		private RemoteSimonManagerFactory remoteSimonManagerFactory;
		private ScheduledExecutorService executor;
		private long timePeriod;
		private TimeUnit timeUnit;
		private MetricsDao metricsDao;

		public void setRemoteSimonManagerFactory(RemoteSimonManagerFactory remoteSimonManagerFactory) {
			if (remoteSimonManagerFactory == null) {
				throw new IllegalArgumentException("No remoteSimonManagerFactory was specified");
			}

			this.remoteSimonManagerFactory = remoteSimonManagerFactory;
		}

		public RemoteSimonManagerFactory getRemoteSimonManagerFactory() {
			return remoteSimonManagerFactory;
		}

		public void setExecutor(ScheduledExecutorService executor) {
			if (executor == null) {
				throw new IllegalArgumentException("No executor was specified");
			}

			this.executor = executor;
		}

		public ScheduledExecutorService getExecutor() {
			return executor;
		}

		public void setTimePeriod(long timePeriod) {
			if (timePeriod < 0) {
				throw new IllegalArgumentException("Time period cannot be negative");
			}

			this.timePeriod = timePeriod;
		}

		public long getTimePeriod() {
			return timePeriod;
		}

		public void setTimeUnit(TimeUnit timeUnit) {
			if (timeUnit == null) {
				throw new IllegalArgumentException("No time unit was specified");
			}

			this.timeUnit = timeUnit;
		}

		public TimeUnit getTimeUnit() {
			return timeUnit;
		}

		public void setMetricsDao(MetricsDao metricsDao) {
			if (metricsDao == null) {
				throw new IllegalArgumentException("No metrics DAO was specified");
			}
			this.metricsDao = metricsDao;
		}

		public MetricsDao getMetricsDao() {
			return metricsDao;
		}
	}

	static class PollRunnable implements Runnable {

		private final MetricsDao metricsDao;
		private final String managerId;
		private SimonManagerMXBean simonManager;

		public PollRunnable(String managerId, SimonManagerMXBean simonManager, MetricsDao metricsDao) {
			this.simonManager = simonManager;
			this.managerId = managerId;
			this.metricsDao = metricsDao;
		}

		@Override
		public void run() {
			try {
				List<StopwatchSample> samples = simonManager.getStopwatchSamples ();
				metricsDao.storeStopwatchSamples(managerId, samples);
			} catch (DaoException e) {
				e.printStackTrace();
			}
		}

		SimonManagerMXBean getSimonManager() {
			return simonManager;
		}

		String getManagerId() {
			return managerId;
		}
	}

	private RemoteSimonManagerFactory remoteSimonManagerFactory;
	private ScheduledExecutorService executorService;
	private Map<String, SimonManagerMXBean> managers = new HashMap<String, SimonManagerMXBean>();
	private final long timePeriod;
	private final TimeUnit timeUnit;
	private final MetricsDao metricsDao;

	public AggregationFacade(Args args) {
		this.remoteSimonManagerFactory = args.getRemoteSimonManagerFactory();
		this.executorService = args.getExecutor();
		this.timePeriod = args.getTimePeriod();
		this.timeUnit = args.getTimeUnit();
		this.metricsDao = args.getMetricsDao();
	}

	public void addManager(String managerClass, Properties properties) throws ManagerCreationException {
		String managerId = properties.getProperty("managerId");
		SimonManagerMXBean manager = remoteSimonManagerFactory.createSimonManager(managerClass, properties);
		managers.put(managerId, manager);

		executorService.scheduleAtFixedRate(new PollRunnable(managerId, manager, metricsDao), 0, timePeriod, timeUnit);
	}

	public Set<String> getManagerIds() {
		return managers.keySet();
	}

	public void shutdown() {
		executorService.shutdown();
	}
}
