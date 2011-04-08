package org.javasimon.jdbc4;

import org.javasimon.SimonManager;
import org.javasimon.Counter;
import org.javasimon.Split;

import java.util.Map;
import java.sql.*;
import java.util.Properties;

/**
 * Class implements Simon JDBC4 proxy connection.
 * <p>
 * Every method of this connection is implemented as call of real connection method.
 * Several methods have added work with Simons (starting, stoping, etc.) for monitoring
 * purposes.
 * </p>
 * <p>
 * From all statement-return-methods (<code>createStatement(*)</code>,
 * <code>prepareStatement(*)</code>, <code>prepareCall(*)</code>) connection returns own
 * implementation of statement classes. Those classes are also proxies and provides
 * additional Simons for monitoring features of JDBC driver.
 * </p>
 * Monitoring connection ensure following Simons:
 * <ul>
 * <li>lifespan (<code>org.javasimon.jdbc4.conn</code>, stopwatch) - measure connection life and count</li>
 * <li>commits (<code>org.javasimon.jdbc4.conn.commits</code>, counter) - measure executed commits of all connections</li>
 * <li>rollbacks (<code>org.javasimon.jdbc4.conn.rollbacks</code>, counter) - measure executed rollbacks of all connections</li>
 * </ul>
 *
 * @author Radovan Sninsky
 * @version $Revision: 184 $ $Date: 2009-02-26 23:38:29 +0100 (Thu, 26 Feb 2009) $
 * @see java.sql.Connection
 * @since 2.4
 */
public final class SimonConnection implements Connection {
	private Connection conn;
	private String suffix;

	private Split life;
	private Counter commits;
	private Counter rollbacks;

	/**
	 * Class constructor, initializes Simons (lifespan, active, commits
	 * and rollbacks) related to DB connection.
	 *
	 * @param conn real DB connection
	 * @param prefix hierarchy prefix for connection Simons
	 */
	public SimonConnection(Connection conn, String prefix) {
		this.conn = conn;
		this.suffix = prefix;

		commits = SimonManager.getCounter(prefix + ".conn.commits");
		rollbacks = SimonManager.getCounter(prefix + ".conn.rollbacks");
		life = SimonManager.getStopwatch(prefix + ".conn").start();
	}

	/**
	 * Closes real connection, stops lifespan Simon and decrease active Simon.
	 *
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public void close() throws SQLException {
		conn.close();

		life.stop();
	}

	/**
	 * Commits real connection and increase commits Simon.
	 *
	 * @throws java.sql.SQLException if real commit fails
	 */
	@Override
	public void commit() throws SQLException {
		conn.commit();

		commits.increase();
	}

	/**
	 * Rollback real connection and increase rollbacks Simon.
	 *
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public void rollback() throws SQLException {
		conn.rollback();

		rollbacks.increase();
	}

	/**
	 * Rollback real connection and increase rollbacks Simon.
	 *
	 * @param savepoint the <code>Savepoint</code> object to roll back to
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);

		rollbacks.increase();
	}

	/**
	 * Calls real createStatement and wraps returned statement by Simon's statement.
	 *
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public Statement createStatement() throws SQLException {
		return new SimonStatement(this, conn.createStatement(), suffix);
	}

	/**
	 * Calls real createStatement and wraps returned statement by Simon's statement.
	 *
	 * @param rsType result set type
	 * @param rsConcurrency result set concurrency
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public Statement createStatement(int rsType, int rsConcurrency) throws SQLException {
		return new SimonStatement(this, conn.createStatement(rsType, rsConcurrency), suffix);
	}

	/**
	 * Calls real createStatement and wraps returned statement by Simon's statement.
	 *
	 * @param rsType result set type
	 * @param rsConcurrency result set concurrency
	 * @param rsHoldability result set holdability
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public Statement createStatement(int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
		return new SimonStatement(this, conn.createStatement(rsType, rsConcurrency, rsHoldability), suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @param sql SQL statement
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(sql), sql, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @param sql SQL statement
	 * @param autoGeneratedKeys auto generated keys
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(sql, autoGeneratedKeys), sql, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @param sql SQL statement
	 * @param rsType result set type
	 * @param rsConcurrency result set concurrency
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int rsType, int rsConcurrency) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(sql, rsType, rsConcurrency), sql, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @param sql SQL statement
	 * @param rsType result set type
	 * @param rsConcurrency result set concurrency
	 * @param rsHoldability result set holdability
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(sql, rsType, rsConcurrency, rsHoldability), sql, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @param sql SQL statement
	 * @param columnIndexes an array of column indexes indicating the columns
	 * that should be returned from the inserted row or rows
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(sql, columnIndexes), sql, suffix);
	}

	/**
	 * Calls real prepareStatement and wraps returned statement by Simon's statement.
	 *
	 * @param sql SQL statement
	 * @param columnNames an array of column names indicating the columns
	 * that should be returned from the inserted row or rows
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return new SimonPreparedStatement(this, conn.prepareStatement(sql, columnNames), sql, suffix);
	}

	/**
	 * Calls real prepareCall and wraps returned statement by Simon's statement.
	 *
	 * @param sql an SQL statement, typically a JDBC function call escape string
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return new SimonCallableStatement(conn, conn.prepareCall(sql), sql, suffix);
	}

	/**
	 * Calls real prepareCall and wraps returned statement by Simon's statement.
	 *
	 * @param sql an SQL statement, typically a JDBC function call escape string
	 * @param rsType result set type
	 * @param rsConcurrency result set concurrency
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public CallableStatement prepareCall(String sql, int rsType, int rsConcurrency) throws SQLException {
		return new SimonCallableStatement(conn, conn.prepareCall(sql, rsType, rsConcurrency), sql, suffix);
	}

	/**
	 * Calls real prepareCall and wraps returned statement by Simon's statement.
	 *
	 * @param sql an SQL statement, typically a JDBC function call escape string
	 * @param rsType result set type
	 * @param rsConcurrency result set concurrency
	 * @param rsHoldability result set holdability
	 * @return Simon's statement with wraped real statement
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public CallableStatement prepareCall(String sql, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
		return new SimonCallableStatement(conn, conn.prepareCall(sql, rsType, rsConcurrency, rsHoldability), sql, suffix);
	}

/////////////////// Not interesting methods for monitoring

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String nativeSQL(String s) throws SQLException {
		return conn.nativeSQL(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutoCommit(boolean b) throws SQLException {
		conn.setAutoCommit(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReadOnly(boolean b) throws SQLException {
		conn.setReadOnly(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCatalog(String s) throws SQLException {
		conn.setCatalog(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransactionIsolation(int i) throws SQLException {
		conn.setTransactionIsolation(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTypeMap(Map<String, Class<?>> stringClassMap) throws SQLException {
		conn.setTypeMap(stringClassMap);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHoldability(int i) throws SQLException {
		conn.setHoldability(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Savepoint setSavepoint(String s) throws SQLException {
		return conn.setSavepoint(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clob createClob() throws SQLException {
		return conn.createClob();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		return conn.createSQLXML();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid(int i) throws SQLException {
		return conn.isValid(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClientInfo(String s, String s1) throws SQLClientInfoException {
		conn.setClientInfo(s, s1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		conn.setClientInfo(properties);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClientInfo(String s) throws SQLException {
		return conn.getClientInfo(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Properties getClientInfo() throws SQLException {
		return conn.getClientInfo();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array createArrayOf(String s, Object[] objects) throws SQLException {
		return conn.createArrayOf(s, objects);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Struct createStruct(String s, Object[] objects) throws SQLException {
		return conn.createStruct(s, objects);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T unwrap(Class<T> tClass) throws SQLException {
		throw new SQLException("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		throw new SQLException("not implemented");
	}
}
