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
 * Simon jdbc proxy result set implemntation class.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 18.2.2009 20:01:26
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
	 * Class constructor, initializes simons (lifespan) related to result set.
	 *
	 * @param rset real resultset
	 * @param stmt simon statement
	 * @param prefix hierarchy preffix for JDBC Simons
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
		Split s = SimonManager.getStopwatch(stmtPrefix+".next").start();
		try {
			return rset.next();
		} finally {
			s.stop();	
		}
	}

	/**
	 * Closes real result set, stops lifespan simon.
	 *
	 * @throws SQLException if real close operation fails
	 */
	public void close() throws SQLException {
		rset.close();

		split.stop();
	}

	public Statement getStatement() throws SQLException {
		return stmt;
	}

	public boolean wasNull() throws SQLException {
		return rset.wasNull();
	}

	public String getString(int columnIndex) throws SQLException {
		return rset.getString(columnIndex);
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		return rset.getBoolean(columnIndex);
	}

	public byte getByte(int columnIndex) throws SQLException {
		return rset.getByte(columnIndex);
	}

	public short getShort(int columnIndex) throws SQLException {
		return rset.getShort(columnIndex);
	}

	public int getInt(int columnIndex) throws SQLException {
		return rset.getInt(columnIndex);
	}

	public long getLong(int columnIndex) throws SQLException {
		return rset.getLong(columnIndex);
	}

	public float getFloat(int columnIndex) throws SQLException {
		return rset.getFloat(columnIndex);
	}

	public double getDouble(int columnIndex) throws SQLException {
		return rset.getDouble(columnIndex);
	}

	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return rset.getBigDecimal(columnIndex, scale);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		return rset.getBytes(columnIndex);
	}

	public Date getDate(int columnIndex) throws SQLException {
		return rset.getDate(columnIndex);
	}

	public Time getTime(int columnIndex) throws SQLException {
		return rset.getTime(columnIndex);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rset.getTimestamp(columnIndex);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rset.getAsciiStream(columnIndex);
	}

	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rset.getUnicodeStream(columnIndex);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rset.getBinaryStream(columnIndex);
	}

	public String getString(String columnName) throws SQLException {
		return rset.getString(columnName);
	}

	public boolean getBoolean(String columnName) throws SQLException {
		return rset.getBoolean(columnName);
	}

	public byte getByte(String columnName) throws SQLException {
		return rset.getByte(columnName);
	}

	public short getShort(String columnName) throws SQLException {
		return rset.getShort(columnName);
	}

	public int getInt(String columnName) throws SQLException {
		return rset.getInt(columnName);
	}

	public long getLong(String columnName) throws SQLException {
		return rset.getLong(columnName);
	}

	public float getFloat(String columnName) throws SQLException {
		return rset.getFloat(columnName);
	}

	public double getDouble(String columnName) throws SQLException {
		return rset.getDouble(columnName);
	}

	@Deprecated
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return rset.getBigDecimal(columnName, scale);
	}

	public byte[] getBytes(String columnName) throws SQLException {
		return rset.getBytes(columnName);
	}

	public Date getDate(String columnName) throws SQLException {
		return rset.getDate(columnName);
	}

	public Time getTime(String columnName) throws SQLException {
		return rset.getTime(columnName);
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		return rset.getTimestamp(columnName);
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rset.getAsciiStream(columnName);
	}

	@Deprecated
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return rset.getUnicodeStream(columnName);
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rset.getBinaryStream(columnName);
	}

	public SQLWarning getWarnings() throws SQLException {
		return rset.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		rset.clearWarnings();
	}

	public String getCursorName() throws SQLException {
		return rset.getCursorName();
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return rset.getMetaData();
	}

	public Object getObject(int columnIndex) throws SQLException {
		return rset.getObject(columnIndex);
	}

	public Object getObject(String columnName) throws SQLException {
		return rset.getObject(columnName);
	}

	public int findColumn(String columnName) throws SQLException {
		return rset.findColumn(columnName);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rset.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		return rset.getCharacterStream(columnName);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rset.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rset.getBigDecimal(columnName);
	}

	public boolean isBeforeFirst() throws SQLException {
		return rset.isBeforeFirst();
	}

	public boolean isAfterLast() throws SQLException {
		return rset.isAfterLast();
	}

	public boolean isFirst() throws SQLException {
		return rset.isFirst();
	}

	public boolean isLast() throws SQLException {
		return rset.isLast();
	}

	public void beforeFirst() throws SQLException {
		rset.beforeFirst();
	}

	public void afterLast() throws SQLException {
		rset.afterLast();
	}

	public boolean first() throws SQLException {
		return rset.first();
	}

	public boolean last() throws SQLException {
		return rset.last();
	}

	public int getRow() throws SQLException {
		return rset.getRow();
	}

	public boolean absolute(int row) throws SQLException {
		return rset.absolute(row);
	}

	public boolean relative(int rows) throws SQLException {
		return rset.relative(rows);
	}

	public boolean previous() throws SQLException {
		return rset.previous();
	}

	public void setFetchDirection(int direction) throws SQLException {
		rset.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return rset.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		rset.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return rset.getFetchSize();
	}

	public int getType() throws SQLException {
		return rset.getType();
	}

	public int getConcurrency() throws SQLException {
		return rset.getConcurrency();
	}

	public boolean rowUpdated() throws SQLException {
		return rset.rowUpdated();
	}

	public boolean rowInserted() throws SQLException {
		return rset.rowInserted();
	}

	public boolean rowDeleted() throws SQLException {
		return rset.rowDeleted();
	}

	public void updateNull(int columnIndex) throws SQLException {
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
	}

	public void updateString(int columnIndex, String x) throws SQLException {
	}

	public void updateBytes(int columnIndex, byte x[]) throws SQLException {
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
	}

	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
	}

	public void updateNull(String columnName) throws SQLException {
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
	}

	public void updateByte(String columnName, byte x) throws SQLException {
	}

	public void updateShort(String columnName, short x) throws SQLException {
	}

	public void updateInt(String columnName, int x) throws SQLException {
	}

	public void updateLong(String columnName, long x) throws SQLException {
	}

	public void updateFloat(String columnName, float x) throws SQLException {
	}

	public void updateDouble(String columnName, double x) throws SQLException {
	}

	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
	}

	public void updateString(String columnName, String x) throws SQLException {
	}

	public void updateBytes(String columnName, byte x[]) throws SQLException {
	}

	public void updateDate(String columnName, Date x) throws SQLException {
	}

	public void updateTime(String columnName, Time x) throws SQLException {
	}

	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
	}

	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
	}

	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
	}

	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
	}

	public void updateObject(String columnName, Object x, int scale) throws SQLException {
	}

	public void updateObject(String columnName, Object x) throws SQLException {
	}

	public void insertRow() throws SQLException {
	}

	public void updateRow() throws SQLException {
	}

	public void deleteRow() throws SQLException {
	}

	public void refreshRow() throws SQLException {
	}

	public void cancelRowUpdates() throws SQLException {
	}

	public void moveToInsertRow() throws SQLException {
	}

	public void moveToCurrentRow() throws SQLException {
	}

	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(i, map);
	}

	public Ref getRef(int i) throws SQLException {
		return rset.getRef(i);
	}

	public Blob getBlob(int i) throws SQLException {
		return rset.getBlob(i);
	}

	public Clob getClob(int i) throws SQLException {
		return rset.getClob(i);
	}

	public Array getArray(int i) throws SQLException {
		return rset.getArray(i);
	}

	public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(colName, map);
	}

	public Ref getRef(String colName) throws SQLException {
		return rset.getRef(colName);
	}

	public Blob getBlob(String colName) throws SQLException {
		return rset.getBlob(colName);
	}

	public Clob getClob(String colName) throws SQLException {
		return rset.getClob(colName);
	}

	public Array getArray(String colName) throws SQLException {
		return rset.getArray(colName);
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rset.getDate(columnIndex, cal);
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return rset.getDate(columnName, cal);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTime(columnIndex, cal);
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return rset.getTime(columnName, cal);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnName, cal);
	}

	public URL getURL(int columnIndex) throws SQLException {
		return rset.getURL(columnIndex);
	}

	public URL getURL(String columnName) throws SQLException {
		return rset.getURL(columnName);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rset.updateRef(columnIndex, x);
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		rset.updateRef(columnName, x);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rset.updateBlob(columnIndex, x);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		rset.updateBlob(columnName, x);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rset.updateClob(columnIndex, x);
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		rset.updateClob(columnName, x);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		rset.updateArray(columnIndex, x);
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		rset.updateArray(columnName, x);
	}
}
