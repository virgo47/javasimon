package org.javasimon.jdbc;

import org.javasimon.Stopwatch;
import org.javasimon.SimonFactory;
import org.javasimon.Counter;

import java.sql.*;
import java.sql.Connection;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;
import java.net.URL;

/**
 * Trieda PreparedStatement.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 9.8.2008 15:42:52
 * @since 1.0
 */
public class PreparedStatement implements java.sql.PreparedStatement {

	private java.sql.PreparedStatement stmt;
	private String sql;
	private Stopwatch life;
	private Counter active;

	public PreparedStatement(java.sql.PreparedStatement stmt, String sql, String suffix) {
		this.stmt = stmt;
		this.sql = sql;

		life = SimonFactory.getStopwatch(suffix + ".stmt").start();
		active = SimonFactory.getCounter(suffix + ".stmt.active").increment();
	}

	public void close() throws SQLException {
		stmt.close();

		life.stop();
		active.decrement();
	}

	public ResultSet executeQuery() throws SQLException {
		// Todo do monitoring
		return stmt.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		// Todo do monitoring
		return stmt.executeUpdate();
	}

	public boolean execute() throws SQLException {
		// Todo do monitoring
		return stmt.execute();
	}

	public void addBatch() throws SQLException {
		// Todo do monitoring
		stmt.addBatch();
	}

	public ResultSet executeQuery(String s) throws SQLException {
		// Todo do monitoring
		return stmt.executeQuery(s);
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

	public void clearBatch() throws SQLException {
		// Todo do monitoring
		stmt.clearBatch();
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

	public void setNull(int i, int i1) throws SQLException {
		stmt.setNull(i, i1);
	}

	public void setBoolean(int i, boolean b) throws SQLException {
		stmt.setBoolean(i, b);
	}

	public void setByte(int i, byte b) throws SQLException {
		stmt.setByte(i, b);
	}

	public void setShort(int i, short i1) throws SQLException {
		stmt.setShort(i, i1);
	}

	public void setInt(int i, int i1) throws SQLException {
		stmt.setInt(i, i1);
	}

	public void setLong(int i, long l) throws SQLException {
		stmt.setLong(i, l);
	}

	public void setFloat(int i, float v) throws SQLException {
		stmt.setFloat(i, v);
	}

	public void setDouble(int i, double v) throws SQLException {
		stmt.setDouble(i, v);
	}

	public void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		stmt.setBigDecimal(i, bigDecimal);
	}

	public void setString(int i, String s) throws SQLException {
		stmt.setString(i, s);
	}

	public void setBytes(int i, byte[] bytes) throws SQLException {
		stmt.setBytes(i, bytes);
	}

	public void setDate(int i, Date date) throws SQLException {
		stmt.setDate(i, date);
	}

	public void setTime(int i, Time time) throws SQLException {
		stmt.setTime(i, time);
	}

	public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		stmt.setTimestamp(i, timestamp);
	}

	public void setAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setAsciiStream(i, inputStream, i1);
	}

	public void setUnicodeStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setUnicodeStream(i, inputStream, i1);
	}

	public void setBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setBinaryStream(i, inputStream, i1);
	}

	public void clearParameters() throws SQLException {
		stmt.clearParameters();
	}

	public void setObject(int i, Object o, int i1) throws SQLException {
		stmt.setObject(i, o, i1);
	}

	public void setObject(int i, Object o) throws SQLException {
		stmt.setObject(i, o);
	}

	public void setCharacterStream(int i, Reader reader, int i1) throws SQLException {
		stmt.setCharacterStream(i, reader, i1);
	}

	public void setRef(int i, Ref ref) throws SQLException {
		stmt.setRef(i, ref);
	}

	public void setBlob(int i, Blob blob) throws SQLException {
		stmt.setBlob(i, blob);
	}

	public void setClob(int i, Clob clob) throws SQLException {
		stmt.setClob(i, clob);
	}

	public void setArray(int i, Array array) throws SQLException {
		stmt.setArray(i, array);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return stmt.getMetaData();
	}

	public void setDate(int i, Date date, Calendar calendar) throws SQLException {
		stmt.setDate(i, date, calendar);
	}

	public void setTime(int i, Time time, Calendar calendar) throws SQLException {
		stmt.setTime(i, time, calendar);
	}

	public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
		stmt.setTimestamp(i, timestamp, calendar);
	}

	public void setNull(int i, int i1, String s) throws SQLException {
		stmt.setNull(i, i1, s);
	}

	public void setURL(int i, URL url) throws SQLException {
		stmt.setURL(i, url);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return stmt.getParameterMetaData();
	}

	public void setRowId(int i, RowId rowId) throws SQLException {
		stmt.setRowId(i, rowId);
	}

	public void setNString(int i, String s) throws SQLException {
		stmt.setNString(i, s);
	}

	public void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
		stmt.setNCharacterStream(i, reader, l);
	}

	public void setNClob(int i, NClob nClob) throws SQLException {
		stmt.setNClob(i, nClob);
	}

	public void setClob(int i, Reader reader, long l) throws SQLException {
		stmt.setClob(i, reader, l);
	}

	public void setBlob(int i, InputStream inputStream, long l) throws SQLException {
		stmt.setBlob(i, inputStream, l);
	}

	public void setNClob(int i, Reader reader, long l) throws SQLException {
		stmt.setNClob(i, reader, l);
	}

	public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
		stmt.setSQLXML(i, sqlxml);
	}

	public void setObject(int i, Object o, int i1, int i2) throws SQLException {
		stmt.setObject(i, o, i1, i2);
	}

	public void setAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
		stmt.setAsciiStream(i, inputStream, l);
	}

	public void setBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
		stmt.setBinaryStream(i, inputStream);
	}

	public void setCharacterStream(int i, Reader reader, long l) throws SQLException {
		stmt.setCharacterStream(i, reader, l);
	}

	public void setAsciiStream(int i, InputStream inputStream) throws SQLException {
		stmt.setAsciiStream(i, inputStream);
	}

	public void setBinaryStream(int i, InputStream inputStream) throws SQLException {
		stmt.setBinaryStream(i, inputStream);
	}

	public void setCharacterStream(int i, Reader reader) throws SQLException {
		stmt.setCharacterStream(i, reader);
	}

	public void setNCharacterStream(int i, Reader reader) throws SQLException {
		stmt.setNCharacterStream(i, reader);
	}

	public void setClob(int i, Reader reader) throws SQLException {
		stmt.setClob(i, reader);
	}

	public void setBlob(int i, InputStream inputStream) throws SQLException {
		stmt.setBlob(i, inputStream);
	}

	public void setNClob(int i, Reader reader) throws SQLException {
		stmt.setNClob(i, reader);
	}

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
