package org.javasimon.jdbc4;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link org.javasimon.jdbc4.WrapperSupport} class
 */
public class WrapperSupportTest {
	private Connection realConnection;
	private Connection simonConnection;
	@BeforeMethod
	public void setUpMethod() throws Exception {
		realConnection=mock(Connection.class);
		simonConnection=new SimonConnection(realConnection, getClass().getPackage().getName());
	}
	@Test
	public void testIsWrapper() throws SQLException {
		assertTrue(simonConnection.isWrapperFor(Connection.class));
		verifyNoMoreInteractions(realConnection);
	}
	@Test
	public void testIsWrapperFailure() throws SQLException {
		when(realConnection.isWrapperFor(eq(Comparable.class))).thenReturn(false);
		assertFalse(simonConnection.isWrapperFor(Comparable.class));
		verify(realConnection).isWrapperFor(eq(Comparable.class));
		verifyNoMoreInteractions(realConnection);
	}
	@Test
	public void testUnwrap() throws SQLException {
		when(realConnection.isWrapperFor(eq(Connection.class))).thenReturn(false);
		Connection connection=simonConnection.unwrap(Connection.class);
		assertSame(connection, realConnection);
		verify(realConnection).isWrapperFor(eq(Connection.class));
		verifyNoMoreInteractions(realConnection);
	}
	@Test
	public void testUnwrapDeeper() throws SQLException {
		when(realConnection.isWrapperFor(eq(Connection.class))).thenReturn(true);
		Connection deepConnection=mock(Connection.class);
		when(realConnection.unwrap(eq(Connection.class))).thenReturn(deepConnection);		
		when(deepConnection.isWrapperFor(eq(Connection.class))).thenReturn(false);
		Connection connection=simonConnection.unwrap(Connection.class);
		assertSame(connection, deepConnection);
		verify(realConnection).isWrapperFor(eq(Connection.class));
		verify(realConnection).unwrap(eq(Connection.class));
		verifyNoMoreInteractions(realConnection);
	}
}
