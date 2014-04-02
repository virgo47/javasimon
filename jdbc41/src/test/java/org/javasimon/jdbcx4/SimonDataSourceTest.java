package org.javasimon.jdbcx4;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.jdbcx.JdbcDataSource;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link org.javasimon.jdbcx4.SimonDataSource} and {@link org.javasimon.jdbcx4.AbstractSimonDataSource
 */
public class SimonDataSourceTest {	
	@Test
	public void testSimple() throws SQLException {
		// Prepare
		SimonDataSource simonDataSource=new SimonDataSource();
		simonDataSource.setUrl("jdbc:h2:mem:SimonDataSourceTest");
		simonDataSource.setRealDataSourceClassName(JdbcDataSource.class.getName());
		simonDataSource.setUser("sa");
		// Act
		Connection connection=simonDataSource.getConnection();
		// Verify
		assertEquals(connection.getMetaData().getDatabaseProductName(),"H2");
		assertEquals(connection.getMetaData().getUserName(), "SA");
	}
	@Test
	public void testFromUrl() throws SQLException {
		// Prepare
		SimonDataSource simonDataSource=new SimonDataSource();
		simonDataSource.setUrl("jdbc:simon:h2:mem:SimonDataSourceTest");
		simonDataSource.setUser("sa");
		// Act
		Connection connection=simonDataSource.getConnection();
		// Verify
		assertEquals(connection.getMetaData().getDatabaseProductName(),"H2");
		assertEquals(connection.getMetaData().getUserName(), "SA");
	}
	@Test
	public void testProperties() throws SQLException {
		// Prepare
		SimonDataSource simonDataSource=new SimonDataSource();
		simonDataSource.setUrl("jdbc:simon:h2:mem:SimonDataSourceTest");
		simonDataSource.setUser("sa");
		Properties properties=new Properties();
		properties.setProperty("description", "testProperties");
		simonDataSource.setProperties(properties);
		// Act
		Connection connection=simonDataSource.getConnection();
		// Verify
		assertEquals(((JdbcDataSource) simonDataSource.datasource()).getDescription(), "testProperties");
	}
}
