package org.javasimon.jdbc4;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for JDBC real URL detection.
 *
 * @author Radovan Sninsky
 * @since 2.4
 */
public class SimonConnectionConfigurationTest {
	/**
	 * Data set 1
	 */
	@DataProvider(name = "data1")
	public Object[][] createTestData1() {
		return new Object[][]{
			{
				"jdbc:simon:sqlserver://test;databaseName=testdb",
				"sqlserver",
				"com.microsoft.sqlserver.jdbc.SQLServerDriver",
				"jdbc:sqlserver://test;databaseName=testdb"
			},
			{
				"jdbc:simon:h2:tcp://localhost:6762/testdb",
				"h2",
				"org.h2.Driver",
				"jdbc:h2:tcp://localhost:6762/testdb"
			},
			{
				"jdbc:simon:mysql://localhost/someDb?useUnicode=yes&characterEncoding=UTF-8",
				"mysql",
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/someDb?useUnicode=yes&characterEncoding=UTF-8"
			},
			{
				"jdbc:mysql://localhost/someDb?useUnicode=yes&characterEncoding=UTF-8",
				"mysql",
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/someDb?useUnicode=yes&characterEncoding=UTF-8"
			}
		};
	}

	/**
	 * Test URL parsing for different databases
	 */
	@Test(dataProvider = "data1")
	public void testDatabasesUrlParsing(String simonUrl, String driverId, String realDriver, String realUrl) {
		SimonConnectionConfiguration configuration = new SimonConnectionConfiguration(simonUrl);
		assertEquals(configuration.getDriverId(), driverId);
		assertEquals(configuration.getRealDriver(), realDriver);
		assertEquals(configuration.getRealUrl(), realUrl);
	}

	/**
	 * Data set 2
	 */
	@DataProvider(name = "data2")
	public Object[][] createTestData2() {
		return new Object[][]{
			{
				"jdbc:simon:h2:tcp://localhost:6762/testdb;simon_real_drv=org.h2db.OtherDriver;simon_prefix=org.test",
				"org.h2db.OtherDriver",
				"org.test",
				"jdbc:h2:tcp://localhost:6762/testdb"
			},
			{
				"jdbc:simon:h2:tcp://localhost:6762/testdb;option=option;simon_prefix=org.test2",
				"org.h2.Driver",
				"org.test2",
				"jdbc:h2:tcp://localhost:6762/testdb;option=option"
			}
		};
	}

	/**
	 * Test URL parsing for different databases
	 */
	@Test(dataProvider = "data2")
	public void testOptionParsing(String simonUrl, String realDriver, String prefix, String realUrl) {
		SimonConnectionConfiguration configuration = new SimonConnectionConfiguration(simonUrl);
		assertEquals(configuration.getRealDriver(), realDriver);
		assertEquals(configuration.getRealUrl(), realUrl);
		assertEquals(configuration.getPrefix(), prefix);
	}

	/**
	 * Test URL parsing for different databases
	 */
	@Test
	public void testDataSource() {
		SimonConnectionConfiguration configuration = new SimonConnectionConfiguration("jdbc:simon:mysql://localhost/someDb?useUnicode=yes&characterEncoding=UTF-8");
		assertEquals(configuration.getRealDataSourceName(), "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		assertEquals(configuration.getRealConnectionPoolDataSourceName(), "com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource");
		assertEquals(configuration.getRealXADataSourceName(), "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
	}
}
