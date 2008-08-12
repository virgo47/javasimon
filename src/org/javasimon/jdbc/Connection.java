package org.javasimon.jdbc;

import org.javasimon.SimonFactory;
import org.javasimon.Stopwatch;
import org.javasimon.Counter;

import java.util.Map;
import java.util.Properties;
import java.sql.*;

/**
 * Trieda Conncetion.
 *
 * @author <a href="mailto:radovan.sninsky@siemens.com">Radovan Sninsky</a>
 * @version $Revision$ $Date$
 * @created 6.8.2008 23:50:57
 * @since 1.0
 */
public final class Connection implements java.sql.Connection {

	private java.sql.Connection conn;
	private String suffix;

	private Stopwatch stopwatch;
	private Counter active;
	private Counter commits;
	private Counter rollbacks;

	public Connection(java.sql.Connection conn, String suffix) {
		this.conn = conn;
		this.suffix = suffix;

		stopwatch = SimonFactory.getStopwatch(suffix + ".conn").start();
		active = SimonFactory.getCounter(suffix + ".conn.active").increment();
		commits = SimonFactory.getCounter(suffix + ".conn.commits");
		rollbacks = SimonFactory.getCounter(suffix + ".conn.rollbacks");
	}

	public java.sql.Statement createStatement() throws SQLException {
		// Todo return simon impl
		return conn.createStatement();
	}

	public java.sql.PreparedStatement prepareStatement(String s) throws SQLException {
		// Todo return simon impl
		return conn.prepareStatement(s);
	}

	public CallableStatement prepareCall(String s) throws SQLException {
		// Todo return simon impl
		return conn.prepareCall(s);
	}

	public String nativeSQL(String s) throws SQLException {
		return conn.nativeSQL(s);
	}

	public void setAutoCommit(boolean b) throws SQLException {
		conn.setAutoCommit(b);
	}

	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	public void commit() throws SQLException {
		conn.commit();
		
		commits.increment();
	}

	public void rollback() throws SQLException {
		conn.rollback();

		rollbacks.increment();
	}

	public void close() throws SQLException {
		conn.close();

		stopwatch.stop();
		active.decrement();
	}

	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	public void setReadOnly(boolean b) throws SQLException {
		conn.setReadOnly(b);
	}

	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}

	public void setCatalog(String s) throws SQLException {
		conn.setCatalog(s);
	}

	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}

	public void setTransactionIsolation(int i) throws SQLException {
		conn.setTransactionIsolation(i);
	}

	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}

	public java.sql.Statement createStatement(int i, int i1) throws SQLException {
		// Todo return simon impl
		return conn.createStatement(i, i1);
	}

	public java.sql.PreparedStatement prepareStatement(String s, int i, int i1) throws SQLException {
		// Todo return simon impl
		return conn.prepareStatement(s, i, i1);
	}

	public CallableStatement prepareCall(String s, int i, int i1) throws SQLException {
		// Todo return simon impl
		return conn.prepareCall(s, i, i1);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> stringClassMap) throws SQLException {
		conn.setTypeMap(stringClassMap);
	}

	public void setHoldability(int i) throws SQLException {
		conn.setHoldability(i);
	}

	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}

	public Savepoint setSavepoint(String s) throws SQLException {
		return conn.setSavepoint(s);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);
	}

	public java.sql.Statement createStatement(int i, int i1, int i2) throws SQLException {
		// Todo return simon impl
		return conn.createStatement(i, i1, i2);
	}

	public java.sql.PreparedStatement prepareStatement(String s, int i, int i1, int i2) throws SQLException {
		// Todo return simon impl
		return conn.prepareStatement(s, i, i1, i2);
	}

	public CallableStatement prepareCall(String s, int i, int i1, int i2) throws SQLException {
		// Todo return simon impl
		return conn.prepareCall(s, i, i1, i2);
	}

	public java.sql.PreparedStatement prepareStatement(String s, int i) throws SQLException {
		// Todo return simon impl
		return conn.prepareStatement(s, i);
	}

	public java.sql.PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
		// Todo return simon impl
		return conn.prepareStatement(s, ints);
	}

	public java.sql.PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
		// Todo return simon impl
		return conn.prepareStatement(s, strings);
	}

	public Clob createClob() throws SQLException {
		return conn.createClob();
	}

	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}

	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return conn.createSQLXML();
	}

	public boolean isValid(int i) throws SQLException {
		return conn.isValid(i);
	}

	public void setClientInfo(String s, String s1) throws SQLClientInfoException {
		conn.setClientInfo(s, s1);
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		conn.setClientInfo(properties);
	}

	public String getClientInfo(String s) throws SQLException {
		return conn.getClientInfo(s);
	}

	public Properties getClientInfo() throws SQLException {
		return conn.getClientInfo();
	}

	public Array createArrayOf(String s, Object[] objects) throws SQLException {
		return conn.createArrayOf(s, objects);
	}

	public Struct createStruct(String s, Object[] objects) throws SQLException {
		return conn.createStruct(s, objects);
	}

	public <T> T unwrap(Class<T> tClass) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return aClass == conn.getClass();
	}
}
