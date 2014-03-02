package org.javasimon.aggregation.metricsDao;

import org.h2.jdbcx.JdbcDataSource;
import org.javasimon.jmx.StopwatchSample;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class JDBCMetricsDaoTest {
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";

	static final String USER = "username";
	static final String PASSWORD = "password";

	private JDBCMetricsDao jdbcMetricsDao;
	private JdbcDataSource dataSource;

	@BeforeClass
	public void beforeClass() throws Exception {
		Class.forName(JDBC_DRIVER);

		dataSource = new JdbcDataSource();
		dataSource.setURL(DB_URL);
		dataSource.setUser(USER);
		dataSource.setPassword(PASSWORD);
	}

	@BeforeMethod
	public void beforeMethod() throws Exception {
		jdbcMetricsDao = new JDBCMetricsDao(dataSource);
		jdbcMetricsDao.init();
	}

	@AfterMethod
	public void afterMethod() {
		dropDb();
	}

	private void dropDb() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("DROP TABLE " + JDBCMetricsDao.STOPWATCH_TABLE + ";");
	}

	@Test
	public void testGetStoredSamples() throws Exception {
		StopwatchSample sample1 = createTestStopwatchSample(1);
		StopwatchSample sample2 = createTestStopwatchSample(2);

		String managerId = "managerId";
		jdbcMetricsDao.storeStopwatchSamples(managerId, Arrays.asList(sample1, sample2));
		List<StopwatchSample> samples = jdbcMetricsDao.getStopwatchSamples (managerId);

		Assert.assertEquals(samples.size(), 2);
		Assert.assertTrue(samples.contains(sample1));
		Assert.assertTrue(samples.contains(sample2));
	}

	private StopwatchSample createTestStopwatchSample(int i) {
		return new StopwatchSample("name", i, 0, 0, 0, "note", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
}
