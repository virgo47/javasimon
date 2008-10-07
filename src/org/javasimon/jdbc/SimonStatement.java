package org.javasimon.jdbc;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.Counter;

import java.sql.*;
import java.sql.Connection;
import java.util.List;
import java.util.LinkedList;

/**
 * Simon jdbc proxy statement implemntation class.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 8.8.2008 0:25:33
 * @since 1.0
 * @see java.sql.Statement
 */
public class SimonStatement implements java.sql.Statement {

	protected Connection conn;
	private java.sql.Statement stmt;

	protected final List<String> batchSql = new LinkedList<String>();

	protected String prefix;
	protected String sqlCmdLabel;
	protected SqlNormalizer sqlNormalizer;

	protected Stopwatch life;
	protected Counter active;

	SimonStatement(Connection conn, java.sql.Statement stmt, String prefix) {
		this.conn = conn;
		this.stmt = stmt;
		this.prefix = prefix;

		active = SimonManager.getCounter(prefix + ".stmt.active").increment();
		life = SimonManager.getStopwatch(prefix + ".stmt").start();
	}

	public final void close() throws SQLException {
		stmt.close();

		life.stop();
		active.decrement();
	}

	public final Connection getConnection() throws SQLException {
		return conn;
	}

	protected final Stopwatch prepare(String sql) {
		if (sql != null && !sql.equals("")) {
			sqlNormalizer = new SqlNormalizer(sql);
			sqlCmdLabel = prefix + ".sql." + sqlNormalizer.getType();
			return SimonManager.getStopwatch(sqlCmdLabel + "." + sqlNormalizer.getNormalizedSql().hashCode()).start();
		} else {
			return null;
		}
	}

	protected final Stopwatch prepare(List<String> sqls) {
		if (!sqls.isEmpty()) {
			sqlNormalizer = sqls.size() == 1 ? new SqlNormalizer(sqls.get(0)) : new SqlNormalizer(sqls);
			sqlCmdLabel = prefix + ".sql." + sqlNormalizer.getType();
			return SimonManager.getStopwatch(sqlCmdLabel + "." + sqlNormalizer.getNormalizedSql().hashCode()).start();
		} else {
			return null;
		}
	}

	protected final void finish(Stopwatch s) {
		if (s != null) {
			SimonManager.getStopwatch(sqlCmdLabel).addTime(s.stop());
			s.setNote(sqlNormalizer.getNormalizedSql());
		}
	}

	public final ResultSet executeQuery(String sql) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeQuery(sql);
		} finally {
			finish(s);
		}
	}

	public final int executeUpdate(String sql) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql);
		} finally {
			finish(s);
		}
	}

	public final int executeUpdate(String sql, int i) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, i);
		} finally {
			finish(s);
		}
	}

	public final int executeUpdate(String sql, int[] ints) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, ints);
		} finally {
			finish(s);
		}
	}

	public final int executeUpdate(String sql, String[] strings) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, strings);
		} finally {
			finish(s);
		}
	}

	public final boolean execute(String sql) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql);
		} finally {
			finish(s);
		}
	}

	public final boolean execute(String sql, int i) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql, i);
		} finally {
			finish(s);
		}
	}

	public final boolean execute(String sql, int[] ints) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql, ints);
		} finally {
			finish(s);
		}
	}

	public final boolean execute(String sql, String[] strings) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql, strings);
		} finally {
			finish(s);
		}
	}

	public final void addBatch(String s) throws SQLException {
		batchSql.add(s);
		
		stmt.addBatch(s);
	}

	public final int[] executeBatch() throws SQLException {
		Stopwatch s = prepare(batchSql);
		try {
			return stmt.executeBatch();
		} finally {
			finish(s);
		}
	}

	public final void clearBatch() throws SQLException {
		batchSql.clear();

		stmt.clearBatch();
	}


/////////////////// Not interesting methods for monitoring

	public final int getMaxFieldSize() throws SQLException {
		return stmt.getMaxFieldSize();
	}

	public final void setMaxFieldSize(int i) throws SQLException {
		stmt.setMaxFieldSize(i);
	}

	public final int getMaxRows() throws SQLException {
		return stmt.getMaxRows();
	}

	public final void setMaxRows(int i) throws SQLException {
		stmt.setMaxRows(i);
	}

	public final void setEscapeProcessing(boolean b) throws SQLException {
		stmt.setEscapeProcessing(b);
	}

	public final int getQueryTimeout() throws SQLException {
		return stmt.getQueryTimeout();
	}

	public final void setQueryTimeout(int i) throws SQLException {
		stmt.setQueryTimeout(i);
	}

	public final void cancel() throws SQLException {
		stmt.cancel();
	}

	public final SQLWarning getWarnings() throws SQLException {
		return stmt.getWarnings();
	}

	public final void clearWarnings() throws SQLException {
		stmt.clearWarnings();
	}

	public final void setCursorName(String s) throws SQLException {
		stmt.setCursorName(s);
	}

	public final ResultSet getResultSet() throws SQLException {
		return stmt.getResultSet();
	}

	public final int getUpdateCount() throws SQLException {
		return stmt.getUpdateCount();
	}

	public final boolean getMoreResults() throws SQLException {
		return stmt.getMoreResults();
	}

	public final void setFetchDirection(int i) throws SQLException {
		stmt.setFetchDirection(i);
	}

	public final int getFetchDirection() throws SQLException {
		return stmt.getFetchDirection();
	}

	public final void setFetchSize(int i) throws SQLException {
		stmt.setFetchSize(i);
	}

	public final int getFetchSize() throws SQLException {
		return stmt.getFetchSize();
	}

	public final int getResultSetConcurrency() throws SQLException {
		return stmt.getResultSetConcurrency();
	}

	public final int getResultSetType() throws SQLException {
		return stmt.getResultSetType();
	}

	public final boolean getMoreResults(int i) throws SQLException {
		return stmt.getMoreResults(i);
	}

	public final ResultSet getGeneratedKeys() throws SQLException {
		return stmt.getGeneratedKeys();
	}

	public final int getResultSetHoldability() throws SQLException {
		return stmt.getResultSetHoldability();
	}

//////// from JDK 6, JDBC 4
/*
	public final boolean isClosed() throws SQLException {
		return stmt.isClosed();
	}

	public final void setPoolable(boolean b) throws SQLException {
		stmt.setPoolable(b);
	}

	public final boolean isPoolable() throws SQLException {
		return stmt.isPoolable();
	}

	public final <T> T unwrap(Class<T> tClass) throws SQLException {
		throw new SQLException("not implemented");
	}

	public final boolean isWrapperFor(Class<?> aClass) throws SQLException {
		throw new SQLException("not implemented");
	}
*/
}
