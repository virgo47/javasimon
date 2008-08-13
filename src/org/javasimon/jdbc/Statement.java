package org.javasimon.jdbc;

import org.javasimon.SimonFactory;
import org.javasimon.Stopwatch;
import org.javasimon.Counter;

import java.sql.*;
import java.sql.Connection;
import java.util.Map;
import java.util.HashMap;

/**
 * Trieda Statement.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 8.8.2008 0:25:33
 * @since 1.0
 */
public class Statement implements java.sql.Statement {

	private java.sql.Statement stmt;
	private String sql;
	private Stopwatch life;
	private Counter active;

	private Map<String, Stopwatch> cmds = new HashMap<String, Stopwatch>();

	public Statement(java.sql.Statement stmt, String suffix) {
		this.stmt = stmt;

		cmds.put("select", SimonFactory.getStopwatch(suffix + ".stmt.select"));
		cmds.put("insert", SimonFactory.getStopwatch(suffix + ".stmt.insert"));
		cmds.put("update", SimonFactory.getStopwatch(suffix + ".stmt.update"));
		cmds.put("delete", SimonFactory.getStopwatch(suffix + ".stmt.delete"));
		cmds.put("call", SimonFactory.getStopwatch(suffix + ".stmt.call"));
		active = SimonFactory.getCounter(suffix + ".stmt.active").increment();
		life = SimonFactory.getStopwatch(suffix + ".stmt").start();
	}

	public void close() throws SQLException {
		stmt.close();

		life.stop();
		active.decrement();
	}

	public ResultSet executeQuery(String s) throws SQLException {
		// Todo do monitoring
		// determine sql cmd monitor
		// get sql cmd monitor

		// normalize sql string
		// find monitor with sql normalized string
		// if not create new one
		// start sql

		// start sql cmd
		// finally { stop sql cmd & stop sql }

		Stopwatch sw = cmds.get("select").start();
		try {
			return stmt.executeQuery(s);
		} finally {
			sw.stop();
		}
	}

	public int executeUpdate(String s) throws SQLException {
		// Todo do monitoring
		return stmt.executeUpdate(s);
	}

	public boolean execute(String s) throws SQLException {
		// Todo do monitoring
		return stmt.execute(s);
	}

	public void addBatch(String s) throws SQLException {
		// Todo do monitoring
		stmt.addBatch(s);
	}

	public int[] executeBatch() throws SQLException {
		// Todo do monitoring
		return stmt.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		// Todo do monitoring
		return stmt.getConnection();
	}

	public int executeUpdate(String s, int i) throws SQLException {
		// Todo do monitoring
		return stmt.executeUpdate(s, i);
	}

	public int executeUpdate(String s, int[] ints) throws SQLException {
		// Todo do monitoring
		return stmt.executeUpdate(s, ints);
	}

	public int executeUpdate(String s, String[] strings) throws SQLException {
		// Todo do monitoring
		return stmt.executeUpdate(s, strings);
	}

	public boolean execute(String s, int i) throws SQLException {
		// Todo do monitoring
		return stmt.execute(s, i);
	}

	public boolean execute(String s, int[] ints) throws SQLException {
		// Todo do monitoring
		return stmt.execute(s, ints);
	}

	public boolean execute(String s, String[] strings) throws SQLException {
		// Todo do monitoring
		return stmt.execute(s, strings);
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

	public void clearBatch() throws SQLException {
		stmt.clearBatch();
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
