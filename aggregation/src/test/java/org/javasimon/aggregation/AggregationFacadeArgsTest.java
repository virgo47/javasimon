package org.javasimon.aggregation;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class AggregationFacadeArgsTest {

	private AggregationFacade.Args args;

	@BeforeMethod
	public void beforeMethod() {
		args = new AggregationFacade.Args();
	}

	@Test
	public void testGetRemoteSimonManagerFactory() {
		RemoteSimonManagerFactory expectedRemoteSimonManagerFactory = mock(RemoteSimonManagerFactory.class);
		args.setRemoteSimonManagerFactory(expectedRemoteSimonManagerFactory);
		Assert.assertEquals(args.getRemoteSimonManagerFactory(), expectedRemoteSimonManagerFactory);
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testNullRemoteSimonManagerFactoyr() {
		args.setRemoteSimonManagerFactory(null);
	}

	@Test
	public void testGetExecutor() {
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		args.setExecutor(executorService);
		Assert.assertEquals(args.getExecutor(), executorService);
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testSetNullExecutor() {
		args.setExecutor(null);
	}

	@Test
	public void testGetTimePeriod() {
		long expectedTimePeriod = 15;
		args.setTimePeriod(expectedTimePeriod);
		Assert.assertEquals(args.getTimePeriod(), expectedTimePeriod);
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testSetNegativeTimePeriod() {
		args.setTimePeriod(-1);
	}

	@Test
	public void testGetTimeUnit() {
		TimeUnit expectedTimeUnit = TimeUnit.HOURS;
		args.setTimeUnit(expectedTimeUnit);
		Assert.assertEquals(args.getTimeUnit(), expectedTimeUnit);
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testSetNullTimeUnit() {
		args.setTimeUnit(null);
	}

	@Test
	public void testGetMetricsDao() {
		MetricsDao metricsDao = mock(MetricsDao.class);
		args.setMetricsDao(metricsDao);
		Assert.assertEquals(args.getMetricsDao(), metricsDao);
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testSetNullMetricsDao() {
		args.setMetricsDao(null);
	}

	@Test
	public void testGetSamplesAggregator() {
		SamplesAggregator aggregator = mock(SamplesAggregator.class);
		args.setAggregator(aggregator);
		Assert.assertEquals(args.getAggregator(), aggregator);
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testSetNullAggregator() {
		args.setAggregator(null);
	}

}
