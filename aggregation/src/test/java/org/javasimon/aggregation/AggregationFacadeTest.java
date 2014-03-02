package org.javasimon.aggregation;

import org.javasimon.jmx.SimonManagerMXBean;
import org.javasimon.jmx.StopwatchSample;
import org.mockito.ArgumentMatcher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class AggregationFacadeTest {

	private AggregationFacade aggregationFacade;
	private RemoteSimonManagerFactory remoteSimonManagerFactory;
	private SimonManagerMXBean simonManagerMXBean;
	private MetricsDao metricsDao;

	private static final String SERVER_CLASS = "org.javasimon.RemoteSimonManager";
	private static final Properties TEST_PROPERTIES = createTestProperties();
    private static final String MANAGER_ID = "testId";

	private static Properties createTestProperties() {
		Properties properties = new Properties();
		properties.put("managerId", MANAGER_ID);
		return properties;
	}

	private static final long TIME_PERIOD = 1;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	private AggregationFacade.Args args;

	@BeforeMethod
	public void beforeMethod() throws Exception {
		remoteSimonManagerFactory = mock(RemoteSimonManagerFactory.class);
		metricsDao = mock(MetricsDao.class);

		args = new AggregationFacade.Args();
		args.setRemoteSimonManagerFactory(remoteSimonManagerFactory);
		args.setExecutor(Executors.newScheduledThreadPool(1));
		args.setTimePeriod(TIME_PERIOD);
		args.setTimeUnit(TIME_UNIT);
		args.setMetricsDao(metricsDao);

		aggregationFacade = new AggregationFacade(args);
		simonManagerMXBean = mock(SimonManagerMXBean.class);

		when(remoteSimonManagerFactory.createSimonManager(SERVER_CLASS, TEST_PROPERTIES)).thenReturn(simonManagerMXBean);
	}

	@Test
	public void testNoEndpointsRegistredInNewAggregationFacade() throws Exception {
		Set<String> endpoints = aggregationFacade.getManagerIds();
		Assert.assertTrue(endpoints.isEmpty());
	}

	@Test
	public void testRemoteManagerIsCreated() throws Exception {
		aggregationFacade.addManager(SERVER_CLASS, TEST_PROPERTIES);

		verify(remoteSimonManagerFactory).createSimonManager(SERVER_CLASS, TEST_PROPERTIES);
		Set<String> managerIds = aggregationFacade.getManagerIds();
		Assert.assertEquals(managerIds.size(), 1);
		Assert.assertTrue(managerIds.contains(MANAGER_ID));
	}

	@Test
	public void testNewPoolTaskIsSubmitted() throws Exception {
		ScheduledExecutorService executorServiceMock = mock(ScheduledExecutorService.class);
		args.setExecutor(executorServiceMock);

		aggregationFacade = new AggregationFacade(args);
		aggregationFacade.addManager(SERVER_CLASS, TEST_PROPERTIES);

		verify(executorServiceMock).scheduleAtFixedRate(
				argThat(is(new ManagerRunnableMatcher(simonManagerMXBean, MANAGER_ID))), eq(0L), eq(TIME_PERIOD), eq(TIME_UNIT));
	}

	private class ManagerRunnableMatcher extends ArgumentMatcher<Runnable> {
		private final String managerId;
		private SimonManagerMXBean manager;

		public ManagerRunnableMatcher(SimonManagerMXBean manager, String managerId) {
			this.manager = manager;
			this.managerId = managerId;
		}

		@Override
		public boolean matches(Object obj) {
			AggregationFacade.PollRunnable actualRunnable = (AggregationFacade.PollRunnable) obj;
			return actualRunnable.getSimonManager().equals(manager) && actualRunnable.getManagerId().equals(managerId);
		}
	}

	@Test
	public void testManagerCalled() throws Exception {
		aggregationFacade.addManager(SERVER_CLASS, TEST_PROPERTIES);

		int periodsNum = 3;
		long periodMillis = TIME_UNIT.toMillis(TIME_PERIOD);
		long sleepTime = periodMillis * periodsNum;
		Thread.sleep(sleepTime);

		int expectedPollsCount = periodsNum - 1;
		verify(simonManagerMXBean, atLeast(expectedPollsCount)).getStopwatchSamples();

		aggregationFacade.shutdown();
		verifyNoMoreInteractions(simonManagerMXBean);
		Thread.sleep(sleepTime);
	}

	@Test
	public void testDataStoredToDao() throws Exception {
		AggregationFacade.PollRunnable pollRunnable = new AggregationFacade.PollRunnable(MANAGER_ID, simonManagerMXBean, metricsDao);

		List<StopwatchSample> remoteSamples = Collections.singletonList(createTestStopwatchSample());
		when(simonManagerMXBean.getStopwatchSamples()).thenReturn(remoteSamples);
		pollRunnable.run();

		verify(metricsDao).storeStopwatchSamples(MANAGER_ID, remoteSamples);
	}

	private StopwatchSample createTestStopwatchSample() {
		return new StopwatchSample("name", 0, 0, 0, 0, "note", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

}
