package org.javasimon.jdbc;

import org.javasimon.Split;
import org.javasimon.SimonManager;

import java.sql.*;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Calendar;
import java.net.URL;

/**
 * Simon JDBC proxy result set implementation class.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @since 2.0
 */
public final class SimonResultSet implements ResultSet {

	/**
	 * Stopwatch split measuring the lifespan of the statement until it is closed.
	 */
	private Split split;

	private ResultSet rset;
	private SimonStatement stmt;
	private String stmtPrefix;

	/**
	 * Class constructor, initializes Simons (lifespan) related to result set.
	 *
	 * @param rset real resultset
	 * @param stmt Simon statement
	 * @param prefix hierarchy prefix for JDBC Simons
	 * @param stmtPrefix statement prefix
	 */
	public SimonResultSet(ResultSet rset, SimonStatement stmt, String prefix, String stmtPrefix) {
		this.rset = rset;
		this.stmt = stmt;
		this.stmtPrefix = stmtPrefix;

		split = SimonManager.getStopwatch(prefix + ".rset").start();
	}

	/**
	 * Measure next operation.
	 *
	 * @return {@code true} if the new current row is valid; {@code false} if there are no more rows
	 * @throws SQLException if real next operation fails
	 */
	public boolean next() throws SQLException {
		Split s = SimonManager.getStopwatch(stmtPrefix + ".next").start();
		try {
			return rset.next();
		} finally {
			s.stop();
		}
	}

	/**
	 * Closes real result set, stops lifespan Simon.
	 *
	 * @throws SQLException if real close operation fails
	 */
	public void close() throws SQLException {
		rset.close();

		split.stop();
	}

	/**
	 * {@inheritDoc}
	 */
	public Statement getStatement() throws SQLException {
		return stmt;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean wasNull() throws SQLException {
		return rset.wasNull();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getString(int columnIndex) throws SQLException {
		return rset.getString(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rset.getBoolean(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return rset.getByte(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public short getShort(int columnIndex) throws SQLException {
		return rset.getShort(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInt(int columnIndex) throws SQLException {
		return rset.getInt(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLong(int columnIndex) throws SQLException {
		return rset.getLong(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return rset.getFloat(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return rset.getDouble(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return rset.getBigDecimal(columnIndex, scale);
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rset.getBytes(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return rset.getDate(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return rset.getTime(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rset.getTimestamp(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rset.getAsciiStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rset.getUnicodeStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rset.getBinaryStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getString(String columnName) throws SQLException {
		return rset.getString(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getBoolean(String columnName) throws SQLException {
		return rset.getBoolean(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public byte getByte(String columnName) throws SQLException {
		return rset.getByte(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public short getShort(String columnName) throws SQLException {
		return rset.getShort(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInt(String columnName) throws SQLException {
		return rset.getInt(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLong(String columnName) throws SQLException {
		return rset.getLong(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public float getFloat(String columnName) throws SQLException {
		return rset.getFloat(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public double getDouble(String columnName) throws SQLException {
		return rset.getDouble(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return rset.getBigDecimal(columnName, scale);
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] getBytes(String columnName) throws SQLException {
		return rset.getBytes(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDate(String columnName) throws SQLException {
		return rset.getDate(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Time getTime(String columnName) throws SQLException {
		return rset.getTime(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return rset.getTimestamp(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rset.getAsciiStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return rset.getUnicodeStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rset.getBinaryStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public SQLWarning getWarnings() throws SQLException {
		return rset.getWarnings();
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearWarnings() throws SQLException {
		rset.clearWarnings();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCursorName() throws SQLException {
		return rset.getCursorName();
	}

	/**
	 * {@inheritDoc}
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return rset.getMetaData();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return rset.getObject(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject(String columnName) throws SQLException {
		return rset.getObject(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public int findColumn(String columnName) throws SQLException {
		return rset.findColumn(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rset.getCharacterStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Reader getCharacterStream(String columnName) throws SQLException {
		return rset.getCharacterStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rset.getBigDecimal(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rset.getBigDecimal(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBeforeFirst() throws SQLException {
		return rset.isBeforeFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAfterLast() throws SQLException {
		return rset.isAfterLast();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFirst() throws SQLException {
		return rset.isFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLast() throws SQLException {
		return rset.isLast();
	}

	/**
	 * {@inheritDoc}
	 */
	public void beforeFirst() throws SQLException {
		rset.beforeFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterLast() throws SQLException {
		rset.afterLast();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean first() throws SQLException {
		return rset.first();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean last() throws SQLException {
		return rset.last();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRow() throws SQLException {
		return rset.getRow();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean absolute(int row) throws SQLException {
		return rset.absolute(row);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean relative(int rows) throws SQLException {
		return rset.relative(rows);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean previous() throws SQLException {
		return rset.previous();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFetchDirection(int direction) throws SQLException {
		rset.setFetchDirection(direction);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getFetchDirection() throws SQLException {
		return rset.getFetchDirection();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFetchSize(int rows) throws SQLException {
		rset.setFetchSize(rows);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getFetchSize() throws SQLException {
		return rset.getFetchSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getType() throws SQLException {
		return rset.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getConcurrency() throws SQLException {
		return rset.getConcurrency();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean rowUpdated() throws SQLException {
		return rset.rowUpdated();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean rowInserted() throws SQLException {
		return rset.rowInserted();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean rowDeleted() throws SQLException {
		return rset.rowDeleted();
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateNull(int columnIndex) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateByte(int columnIndex, byte x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateShort(int columnIndex, short x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateInt(int columnIndex, int x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateLong(int columnIndex, long x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateFloat(int columnIndex, float x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateDouble(int columnIndex, double x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateString(int columnIndex, String x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBytes(int columnIndex, byte x[]) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateDate(int columnIndex, Date x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTime(int columnIndex, Time x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateObject(int columnIndex, Object x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateNull(String columnName) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBoolean(String columnName, boolean x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateByte(String columnName, byte x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateShort(String columnName, short x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateInt(String columnName, int x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateLong(String columnName, long x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateFloat(String columnName, float x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateDouble(String columnName, double x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateString(String columnName, String x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBytes(String columnName, byte x[]) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateDate(String columnName, Date x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTime(String columnName, Time x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateObject(String columnName, Object x, int scale) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateObject(String columnName, Object x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancelRowUpdates() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void moveToInsertRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void moveToCurrentRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(i, map);
	}

	/**
	 * {@inheritDoc}
	 */
	public Ref getRef(int i) throws SQLException {
		return rset.getRef(i);
	}

	/**
	 * {@inheritDoc}
	 */
	public Blob getBlob(int i) throws SQLException {
		return rset.getBlob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	public Clob getClob(int i) throws SQLException {
		return rset.getClob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	public Array getArray(int i) throws SQLException {
		return rset.getArray(i);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(colName, map);
	}

	/**
	 * {@inheritDoc}
	 */
	public Ref getRef(String colName) throws SQLException {
		return rset.getRef(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Blob getBlob(String colName) throws SQLException {
		return rset.getBlob(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Clob getClob(String colName) throws SQLException {
		return rset.getClob(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Array getArray(String colName) throws SQLException {
		return rset.getArray(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rset.getDate(columnIndex, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return rset.getDate(columnName, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTime(columnIndex, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return rset.getTime(columnName, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnIndex, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnName, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return rset.getURL(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public URL getURL(String columnName) throws SQLException {
		return rset.getURL(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rset.updateRef(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateRef(String columnName, Ref x) throws SQLException {
		rset.updateRef(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rset.updateBlob(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateBlob(String columnName, Blob x) throws SQLException {
		rset.updateBlob(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rset.updateClob(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateClob(String columnName, Clob x) throws SQLException {
		rset.updateClob(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		rset.updateArray(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateArray(String columnName, Array x) throws SQLException {
		rset.updateArray(columnName, x);
	}
}
