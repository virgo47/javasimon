package org.javasimon.jdbc4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 * Simon JDBC proxy statement implementation class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see java.sql.Statement
 * @since 2.4
 */
public class SimonStatement implements Statement {
	/**
	 * List of batched SQL statements.
	 */
	protected final List<String> batchSql = new LinkedList<>();

	/**
	 * SQL connection.
	 */
	protected Connection conn;

	/**
	 * Hierarchy prefix for JDBC Simons.
	 */
	protected String prefix;

	/**
	 * SQL statement label containing part up to the SQL command type.
	 */
	protected String sqlCmdLabel;

	/**
	 * SQL normalizer helper object.
	 */
	protected SqlNormalizer sqlNormalizer;

	/**
	 * Stopwatch split measuring the lifespan of the statement until it is closed across all executes.
	 */
	protected Split split;

	private final Statement stmt;

	private final WrapperSupport<Statement> wrapperSupport;

	/**
	 * Class constructor, initializes Simons (lifespan, active) related to statement.
	 *
	 * @param conn database connection (simon impl.)
	 * @param stmt real statement
	 * @param prefix hierarchy preffix for JDBC Simons
	 */
	SimonStatement(Connection conn, Statement stmt, String prefix) {
		this.conn = conn;
		this.stmt = stmt;
		this.prefix = prefix;
		this.wrapperSupport = new WrapperSupport<>(stmt, Statement.class);
		split = SimonManager.getStopwatch(prefix + ".stmt").start();
	}

	/**
	 * Closes real statement, stops lifespan Simon and decrease active Simon.
	 *
	 * @throws java.sql.SQLException if real operation fails
	 */
	@Override
	public final void close() throws SQLException {
		stmt.close();

		split.stop();
	}

	/**
	 * Returns a connection object (simon impl.).
	 *
	 * @return connection object
	 */
	@Override
	public final Connection getConnection() {
		return conn;
	}

	/**
	 * Called before each SQL command execution. Prepares (obtains and starts) {@link org.javasimon.Stopwatch Stopwatch Simon}
	 * for measure SQL operation.
	 *
	 * @param sql sql command for execution
	 * @return Simon stopwatch object or null if sql is null or empty
	 */
	protected final Split prepare(String sql) {
		if (sql != null && !sql.equals("")) {
			sqlNormalizer = new SqlNormalizer(sql);
			sqlCmdLabel = prefix + ".sql." + sqlNormalizer.getType();
			return startSplit();
		} else {
			return null;
		}
	}

	/**
	 * Called before each SQL command execution. Prepares (obtains and starts) {@link org.javasimon.Stopwatch Stopwatch Simon}
	 * for measure bach SQL operations.
	 *
	 * @param sqls list of sql commands
	 * @return Simon stopwatch object or null if sql is null or empty
	 */
	protected final Split prepare(List<String> sqls) {
		if (!sqls.isEmpty()) {
			sqlNormalizer = sqls.size() == 1 ? new SqlNormalizer(sqls.get(0)) : new SqlNormalizer(sqls);
			sqlCmdLabel = prefix + ".sql." + sqlNormalizer.getType();
			return startSplit();
		} else {
			return null;
		}
	}

	/**
	 * Starts the split for the SQL specific stopwatch, sets the note and returns the split.
	 * Used in the statment and prepared statement classes to measure runs of "execute" methods.
	 *
	 * @return split for the execution of the specific SQL command
	 */
	protected Split startSplit() {
		Stopwatch stopwatch = SimonManager.getStopwatch(sqlCmdLabel + Manager.HIERARCHY_DELIMITER + sqlNormalizer.getNormalizedSql().hashCode());
		if (stopwatch.getNote() == null) {
			stopwatch.setNote(sqlNormalizer.getNormalizedSql());
		}
		return stopwatch.start();
	}

	/**
	 * Called after each SQL command execution. Stops concrete SQL stopwatch (started in {@link #prepare(String)}),
	 * also adds time to SQL command type Simon and sets human readable SQL cmd as note.
	 *
	 * @param split started Stopwatch split
	 */
	protected final void finish(Split split) {
		if (split != null) {
			SimonManager.getStopwatch(sqlCmdLabel).addSplit(split.stop());
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @return database rows and columns
	 * @throws java.sql.SQLException if real calls fails
	 * @see SimonResultSet
	 */
	@Override
	public final ResultSet executeQuery(String sql) throws SQLException {
		Split s = prepare(sql);
		try {
			return new SimonResultSet(stmt.executeQuery(sql), this, prefix, s.getStopwatch().getName());
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @return count of updated rows
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final int executeUpdate(String sql) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.executeUpdate(sql);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @param autoGeneratedKeys autoGeneratedKeys flag
	 * @return count of updated rows
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, autoGeneratedKeys);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @param columnIndexes an array of column indexes indicating the columns that should be
	 * returned from the inserted row
	 * @return count of updated rows
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, columnIndexes);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @param columnNames an array of column indexes indicating the columns that should be
	 * returned from the inserted row
	 * @return count of updated rows
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final int executeUpdate(String sql, String[] columnNames) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, columnNames);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @return <code>true</code> if the first result is a <code>ResultSet</code> object;
	 *         <code>false</code> if it is an update count or there are no results
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final boolean execute(String sql) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.execute(sql);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @param autoGeneratedKeys autoGeneratedKeys flag
	 * @return <code>true</code> if the first result is a <code>ResultSet</code> object;
	 *         <code>false</code> if it is an update count or there are no results
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.execute(sql, autoGeneratedKeys);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @param columnIndexes an array of column indexes indicating the columns that should be
	 * returned from the inserted row
	 * @return <code>true</code> if the first result is a <code>ResultSet</code> object;
	 *         <code>false</code> if it is an update count or there are no results
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final boolean execute(String sql, int[] columnIndexes) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.execute(sql, columnIndexes);
		} finally {
			finish(s);
		}
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @param sql sql command
	 * @param columnNames an array of column indexes indicating the columns that should be
	 * returned from the inserted row
	 * @return <code>true</code> if the first result is a <code>ResultSet</code> object;
	 *         <code>false</code> if it is an update count or there are no results
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final boolean execute(String sql, String[] columnNames) throws SQLException {
		Split s = prepare(sql);
		try {
			return stmt.execute(sql, columnNames);
		} finally {
			finish(s);
		}
	}

	/**
	 * Adds given SQL command into batch list of sql and also into real batch.
	 *
	 * @param s sql command
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final void addBatch(String s) throws SQLException {
		batchSql.add(s);

		stmt.addBatch(s);
	}

	/**
	 * Measure and execute SQL operation.
	 *
	 * @return an array of update counts containing one element for each
	 *         command in the batch.
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		Split s = prepare(batchSql);
		try {
			return stmt.executeBatch();
		} finally {
			finish(s);
		}
	}

	/**
	 * Clears batch sql list and real batch too.
	 *
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public void clearBatch() throws SQLException {
		batchSql.clear();

		stmt.clearBatch();
	}

	//// NOT MONITORED

	@Override
	public final int getMaxFieldSize() throws SQLException {
		return stmt.getMaxFieldSize();
	}

	@Override
	public final void setMaxFieldSize(int i) throws SQLException {
		stmt.setMaxFieldSize(i);
	}

	@Override
	public final int getMaxRows() throws SQLException {
		return stmt.getMaxRows();
	}

	@Override
	public final void setMaxRows(int i) throws SQLException {
		stmt.setMaxRows(i);
	}

	@Override
	public final void setEscapeProcessing(boolean b) throws SQLException {
		stmt.setEscapeProcessing(b);
	}

	@Override
	public final int getQueryTimeout() throws SQLException {
		return stmt.getQueryTimeout();
	}

	@Override
	public final void setQueryTimeout(int i) throws SQLException {
		stmt.setQueryTimeout(i);
	}

	@Override
	public final void cancel() throws SQLException {
		stmt.cancel();
	}

	@Override
	public final SQLWarning getWarnings() throws SQLException {
		return stmt.getWarnings();
	}

	@Override
	public final void clearWarnings() throws SQLException {
		stmt.clearWarnings();
	}

	@Override
	public final void setCursorName(String s) throws SQLException {
		stmt.setCursorName(s);
	}

	@Override
	public final ResultSet getResultSet() throws SQLException {
		return stmt.getResultSet();
	}

	@Override
	public final int getUpdateCount() throws SQLException {
		return stmt.getUpdateCount();
	}

	@Override
	public final boolean getMoreResults() throws SQLException {
		return stmt.getMoreResults();
	}

	@Override
	public final void setFetchDirection(int i) throws SQLException {
		stmt.setFetchDirection(i);
	}

	@Override
	public final int getFetchDirection() throws SQLException {
		return stmt.getFetchDirection();
	}

	@Override
	public final void setFetchSize(int i) throws SQLException {
		stmt.setFetchSize(i);
	}

	@Override
	public final int getFetchSize() throws SQLException {
		return stmt.getFetchSize();
	}

	@Override
	public final int getResultSetConcurrency() throws SQLException {
		return stmt.getResultSetConcurrency();
	}

	@Override
	public final int getResultSetType() throws SQLException {
		return stmt.getResultSetType();
	}

	@Override
	public final boolean getMoreResults(int i) throws SQLException {
		return stmt.getMoreResults(i);
	}

	@Override
	public final ResultSet getGeneratedKeys() throws SQLException {
		return stmt.getGeneratedKeys();
	}

	@Override
	public final int getResultSetHoldability() throws SQLException {
		return stmt.getResultSetHoldability();
	}

	@Override
	public final boolean isClosed() throws SQLException {
		return stmt.isClosed();
	}

	@Override
	public final void setPoolable(boolean b) throws SQLException {
		stmt.setPoolable(b);
	}

	@Override
	public final boolean isPoolable() throws SQLException {
		return stmt.isPoolable();
	}

    @Override
    public void closeOnCompletion() throws SQLException {
        stmt.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return stmt.isCloseOnCompletion();
    }

    @Override
	public final <T> T unwrap(Class<T> tClass) throws SQLException {
		return wrapperSupport.unwrap(tClass);
	}

	@Override
	public final boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return wrapperSupport.isWrapperFor(aClass);
	}
}
