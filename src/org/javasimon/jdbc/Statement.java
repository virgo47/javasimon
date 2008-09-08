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
public class Statement implements java.sql.Statement {

	protected Connection conn;
	private java.sql.Statement stmt;

	protected final List<String> batchSql = new LinkedList<String>();

	protected String suffix;
	protected String sqlCmdLabel;
	protected SqlNormalizer sqlNormalizer;

	protected Stopwatch life;
	protected Counter active;

	Statement(Connection conn, java.sql.Statement stmt, String suffix) {
		this.conn = conn;
		this.stmt = stmt;
		this.suffix = suffix;

		active = SimonManager.getCounter(suffix + ".stmt.active").increment();
		life = SimonManager.getStopwatch(suffix + ".stmt").start();
	}

	public void close() throws SQLException {
		stmt.close();

		life.stop();
		active.decrement();
	}

	public Connection getConnection() throws SQLException {
		return conn;
	}

	protected Stopwatch prepare(String sql) {
		if (sql != null && !sql.isEmpty()) {
			sqlNormalizer = new SqlNormalizer(sql);
			sqlCmdLabel = suffix + "." + sqlNormalizer.getType();
			return SimonManager.getStopwatch(sqlCmdLabel + "." + sqlNormalizer.getNormalizedSql().hashCode()).start();
		} else {
			return null;
		}
	}

	protected Stopwatch prepare(List<String> sqls) {
		if (!sqls.isEmpty()) {
			sqlNormalizer = sqls.size() == 1 ? new SqlNormalizer(sqls.get(0)) : new SqlNormalizer(sqls);
			sqlCmdLabel = suffix + "." + sqlNormalizer.getType();
			return SimonManager.getStopwatch(sqlCmdLabel + "." + sqlNormalizer.getNormalizedSql().hashCode()).start();
		} else {
			return null;
		}
	}

	protected void finish(Stopwatch s) {
		if (s != null) {
			SimonManager.getStopwatch(sqlCmdLabel).addTime(s.stop());
			s.setNote(sqlNormalizer.getNormalizedSql());
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeQuery(sql);
		} finally {
			finish(s);
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql);
		} finally {
			finish(s);
		}
	}

	public int executeUpdate(String sql, int i) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, i);
		} finally {
			finish(s);
		}
	}

	public int executeUpdate(String sql, int[] ints) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, ints);
		} finally {
			finish(s);
		}
	}

	public int executeUpdate(String sql, String[] strings) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.executeUpdate(sql, strings);
		} finally {
			finish(s);
		}
	}

	public boolean execute(String sql) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql);
		} finally {
			finish(s);
		}
	}

	public boolean execute(String sql, int i) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql, i);
		} finally {
			finish(s);
		}
	}

	public boolean execute(String sql, int[] ints) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql, ints);
		} finally {
			finish(s);
		}
	}

	public boolean execute(String sql, String[] strings) throws SQLException {
		Stopwatch s = prepare(sql);
		try {
			return stmt.execute(sql, strings);
		} finally {
			finish(s);
		}
	}

	public void addBatch(String s) throws SQLException {
		batchSql.add(s);
		
		stmt.addBatch(s);
	}

	public int[] executeBatch() throws SQLException {
		Stopwatch s = prepare(batchSql);
		try {
			return stmt.executeBatch();
		} finally {
			finish(s);
		}
	}

	public void clearBatch() throws SQLException {
		batchSql.clear();

		stmt.clearBatch();
	}


/////////////////// Not interesting methods for monitoring

	public int getMaxFieldSize() throws SQLException {
		return stmt.getMaxFieldSize();
	}

	public void setMaxFieldSize(int i) throws SQLException {
		stmt.setMaxFieldSize(i);
	}

	public int getMaxRows() throws SQLException {
		return stmt.getMaxRows();
	}

	public void setMaxRows(int i) throws SQLException {
		stmt.setMaxRows(i);
	}

	public void setEscapeProcessing(boolean b) throws SQLException {
		stmt.setEscapeProcessing(b);
	}

	public int getQueryTimeout() throws SQLException {
		return stmt.getQueryTimeout();
	}

	public void setQueryTimeout(int i) throws SQLException {
		stmt.setQueryTimeout(i);
	}

	public void cancel() throws SQLException {
		stmt.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return stmt.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		stmt.clearWarnings();
	}

	public void setCursorName(String s) throws SQLException {
		stmt.setCursorName(s);
	}

	public ResultSet getResultSet() throws SQLException {
		return stmt.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return stmt.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return stmt.getMoreResults();
	}

	public void setFetchDirection(int i) throws SQLException {
		stmt.setFetchDirection(i);
	}

	public int getFetchDirection() throws SQLException {
		return stmt.getFetchDirection();
	}

	public void setFetchSize(int i) throws SQLException {
		stmt.setFetchSize(i);
	}

	public int getFetchSize() throws SQLException {
		return stmt.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return stmt.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return stmt.getResultSetType();
	}

	public boolean getMoreResults(int i) throws SQLException {
		return stmt.getMoreResults(i);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return stmt.getGeneratedKeys();
	}

	public int getResultSetHoldability() throws SQLException {
		return stmt.getResultSetHoldability();
	}

	public boolean isClosed() throws SQLException {
		return stmt.isClosed();
	}

	public void setPoolable(boolean b) throws SQLException {
		stmt.setPoolable(b);
	}

	public boolean isPoolable() throws SQLException {
		return stmt.isPoolable();
	}

	public <T> T unwrap(Class<T> tClass) throws SQLException {
		// Todo to be implemented
		return null;
	}

	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return aClass == stmt.getClass();
	}
}
