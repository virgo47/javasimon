package org.javasimon.jdbc;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.Counter;

import java.util.Map;
import java.util.Properties;
import java.sql.*;

/**
 * Class implements simon jdbc proxy connection.
 * <p>
 * Every method of this connection is implemented as call of real connection method.
 * Several methods have added work with simons (starting, stoping, etc.) for monitoring
 * purposes.
 * </p>
 * <p>
 * From all statement-return-methods (<code>createStatement(*)</code>,
 * <code>prepareStatement(*)</code>, <code>prepareCall(*)</code>) connection returns own
 * implementation of statement classes. Those classes are also proxies and provides
 * additional simons for monitoring features of JDBC driver.
 * </p>
 * Monitoring connection ensure following simons:
 * <ul>
 * <li>lifespan (<code>org.javasimon.jdbc.conn</code>, stopwatch) - measure connection life and count</li>
 * <li>active (<code>org.javasimon.jdbc.conn.active</code>, counter) - measure active (opened) connections</li>
 * <li>commits (<code>org.javasimon.jdbc.conn.commits</code>, counter) - measure executed commits of all connections</li>
 * <li>rollbacks (<code>org.javasimon.jdbc.conn.rollbacks</code>, counter) - measure executed rollbacks of all connections</li>
 * </ul>
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 6.8.2008 23:50:57
 * @since 1.0
 * @see java.sql.Connection
 */
public final class SimonConnection implements java.sql.Connection {

	private java.sql.Connection conn;
	private String suffix;

	private Stopwatch life;
	private Counter active;
	private Counter commits;
	private Counter rollbacks;

	/**
	 * Class constructor, initializes simons (lifespan, active, commits
	 * and rollbacks) related to db connection.
	 *
	 * @param conn real db connection
	 * @param prefix hierarchy preffix for connection simons
	 */
	SimonConnection(java.sql.Connection conn, String prefix) {
		this.conn = conn;
		this.suffix = prefix;

		active = SimonManager.getCounter(prefix + ".conn.active").increment();
		commits = SimonManager.getCounter(prefix + ".conn.commits");
		rollbacks = SimonManager.getCounter(prefix + ".conn.rollbacks");
		life = SimonManager.getStopwatch(prefix + ".conn").start();
	}

	/**
	 * Closes real connection, stops lifespan simon and decrease active simon.
	 *
	 * @throws SQLException if real operation fails
	 */
	public void close() throws SQLException {
		conn.close();

		life.stop();
		active.decrement();
	}

	/**
	 * Commits real connection and increase commits simon.
	 *
	 * @throws SQLException if real commit fails
	 */
	public void commit() throws SQLException {
		conn.commit();

		commits.increment();
	}

	/**
	 * Rollback real connection and increase rollbacks simon.
	 *
	 * @throws SQLException if real operation fails
	 */
	public void rollback() throws SQLException {
		conn.rollback();

		rollbacks.increment();
	}

	/**
	 * Rollback real connection and increase rollbacks simon.
	 *
	 * @throws SQLException if real operation fails
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);

		rollbacks.increment();
	}

	/**
	 * Calls real createStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.Statement createStatement() throws SQLException {
		return new SimonStatement(this, conn.createStatement(), suffix);
	}

	/**
	 * Calls real createStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.Statement createStatement(int i, int i1) throws SQLException {
		return new SimonStatement(this, conn.createStatement(i, i1), suffix);
	}

	/**
	 * Calls real createStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.Statement createStatement(int i, int i1, int i2) throws SQLException {
		return new SimonStatement(this, conn.createStatement(i, i1, i2), suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.PreparedStatement prepareStatement(String s) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(s), s, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.PreparedStatement prepareStatement(String s, int i) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(s, i), s, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.PreparedStatement prepareStatement(String s, int i, int i1) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(s, i, i1), s, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.PreparedStatement prepareStatement(String s, int i, int i1, int i2) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(s, i, i1, i2), s, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(s, ints), s, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(s, strings), s, suffix);
	}

	/**
	 * Calls real prepareCall and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.CallableStatement prepareCall(String s) throws SQLException {
		return new SimonCallableStatement(conn, conn.prepareCall(s), s, suffix);
	}

	/**
	 * Calls real prepareCall and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.CallableStatement prepareCall(String s, int i, int i1) throws SQLException {
		return new SimonCallableStatement(conn, conn.prepareCall(s, i, i1), s, suffix);
	}

	/**
	 * Calls real prepareCall and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws SQLException if real operation fails
	 */
	public java.sql.CallableStatement prepareCall(String s, int i, int i1, int i2) throws SQLException {
		return new SimonCallableStatement(conn, conn.prepareCall(s, i, i1, i2), s, suffix);
	}


/////////////////// Not interesting methods for monitoring

	public String nativeSQL(String s) throws SQLException {
		return conn.nativeSQL(s);
	}

	public void setAutoCommit(boolean b) throws SQLException {
		conn.setAutoCommit(b);
	}

	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
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

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);
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
		throw new SQLException("not implemented");
	}

	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		throw new SQLException("not implemented");
	}
}
