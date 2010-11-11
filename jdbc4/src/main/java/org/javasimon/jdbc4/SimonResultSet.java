package org.javasimon.jdbc4;

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
 * Simon JDBC4 proxy result set implementation class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision: $ $Date: $
 * @created 3.10.2010
 * @since 2.4
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
	 * @throws java.sql.SQLException if real next operation fails
	 */
	@Override
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
	 * @throws java.sql.SQLException if real close operation fails
	 */
	@Override
	public void close() throws SQLException {
		rset.close();

		split.stop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statement getStatement() throws SQLException {
		return stmt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean wasNull() throws SQLException {
		return rset.wasNull();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(int columnIndex) throws SQLException {
		return rset.getString(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rset.getBoolean(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return rset.getByte(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(int columnIndex) throws SQLException {
		return rset.getShort(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(int columnIndex) throws SQLException {
		return rset.getInt(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(int columnIndex) throws SQLException {
		return rset.getLong(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return rset.getFloat(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return rset.getDouble(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return rset.getBigDecimal(columnIndex, scale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rset.getBytes(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return rset.getDate(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return rset.getTime(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rset.getTimestamp(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rset.getAsciiStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rset.getUnicodeStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rset.getBinaryStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(String columnName) throws SQLException {
		return rset.getString(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(String columnName) throws SQLException {
		return rset.getBoolean(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(String columnName) throws SQLException {
		return rset.getByte(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(String columnName) throws SQLException {
		return rset.getShort(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String columnName) throws SQLException {
		return rset.getInt(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String columnName) throws SQLException {
		return rset.getLong(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String columnName) throws SQLException {
		return rset.getFloat(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String columnName) throws SQLException {
		return rset.getDouble(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return rset.getBigDecimal(columnName, scale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes(String columnName) throws SQLException {
		return rset.getBytes(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(String columnName) throws SQLException {
		return rset.getDate(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(String columnName) throws SQLException {
		return rset.getTime(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return rset.getTimestamp(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rset.getAsciiStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return rset.getUnicodeStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rset.getBinaryStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return rset.getWarnings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearWarnings() throws SQLException {
		rset.clearWarnings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCursorName() throws SQLException {
		return rset.getCursorName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return rset.getMetaData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return rset.getObject(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String columnName) throws SQLException {
		return rset.getObject(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int findColumn(String columnName) throws SQLException {
		return rset.findColumn(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rset.getCharacterStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getCharacterStream(String columnName) throws SQLException {
		return rset.getCharacterStream(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rset.getBigDecimal(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rset.getBigDecimal(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return rset.isBeforeFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAfterLast() throws SQLException {
		return rset.isAfterLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFirst() throws SQLException {
		return rset.isFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLast() throws SQLException {
		return rset.isLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeFirst() throws SQLException {
		rset.beforeFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterLast() throws SQLException {
		rset.afterLast();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean first() throws SQLException {
		return rset.first();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean last() throws SQLException {
		return rset.last();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRow() throws SQLException {
		return rset.getRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean absolute(int row) throws SQLException {
		return rset.absolute(row);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean relative(int rows) throws SQLException {
		return rset.relative(rows);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean previous() throws SQLException {
		return rset.previous();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFetchDirection(int direction) throws SQLException {
		rset.setFetchDirection(direction);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		return rset.getFetchDirection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFetchSize(int rows) throws SQLException {
		rset.setFetchSize(rows);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFetchSize() throws SQLException {
		return rset.getFetchSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getType() throws SQLException {
		return rset.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getConcurrency() throws SQLException {
		return rset.getConcurrency();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean rowUpdated() throws SQLException {
		return rset.rowUpdated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean rowInserted() throws SQLException {
		return rset.rowInserted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean rowDeleted() throws SQLException {
		return rset.rowDeleted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNull(int columnIndex) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBytes(int columnIndex, byte x[]) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNull(String columnName) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBoolean(String columnName, boolean x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateByte(String columnName, byte x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateShort(String columnName, short x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateInt(String columnName, int x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateLong(String columnName, long x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateFloat(String columnName, float x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDouble(String columnName, double x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateString(String columnName, String x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBytes(String columnName, byte x[]) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDate(String columnName, Date x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTime(String columnName, Time x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateObject(String columnName, Object x, int scale) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateObject(String columnName, Object x) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelRowUpdates() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveToInsertRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveToCurrentRow() throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(i, map);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ref getRef(int i) throws SQLException {
		return rset.getRef(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Blob getBlob(int i) throws SQLException {
		return rset.getBlob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clob getClob(int i) throws SQLException {
		return rset.getClob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array getArray(int i) throws SQLException {
		return rset.getArray(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(colName, map);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ref getRef(String colName) throws SQLException {
		return rset.getRef(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Blob getBlob(String colName) throws SQLException {
		return rset.getBlob(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clob getClob(String colName) throws SQLException {
		return rset.getClob(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array getArray(String colName) throws SQLException {
		return rset.getArray(colName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rset.getDate(columnIndex, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return rset.getDate(columnName, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTime(columnIndex, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return rset.getTime(columnName, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnIndex, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnName, cal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return rset.getURL(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getURL(String columnName) throws SQLException {
		return rset.getURL(columnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rset.updateRef(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRef(String columnName, Ref x) throws SQLException {
		rset.updateRef(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rset.updateBlob(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(String columnName, Blob x) throws SQLException {
		rset.updateBlob(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rset.updateClob(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(String columnName, Clob x) throws SQLException {
		rset.updateClob(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		rset.updateArray(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateArray(String columnName, Array x) throws SQLException {
		rset.updateArray(columnName, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return rset.getRowId(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return rset.getRowId(columnLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		rset.updateRowId(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		rset.updateRowId(columnLabel, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHoldability() throws SQLException {
		return rset.getHoldability();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return rset.isClosed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		rset.updateNString(columnIndex, nString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		rset.updateNString(columnLabel, nString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		rset.updateNClob(columnIndex, nClob);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		rset.updateNClob(columnLabel, nClob);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return rset.getNClob(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return rset.getNClob(columnLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return rset.getSQLXML(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return rset.getSQLXML(columnLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		rset.updateSQLXML(columnIndex, xmlObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		rset.updateSQLXML(columnLabel, xmlObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNString(int columnIndex) throws SQLException {
		return rset.getNString(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNString(String columnLabel) throws SQLException {
		return rset.getNString(columnLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return rset.getNCharacterStream(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return rset.getNCharacterStream(columnLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		rset.updateNCharacterStream(columnIndex, x, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateNCharacterStream(columnLabel, reader, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		rset.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		rset.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		rset.updateCharacterStream(columnIndex, x, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		rset.updateAsciiStream(columnLabel, x, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		rset.updateBinaryStream(columnLabel, x, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateCharacterStream(columnLabel, reader, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		rset.updateBlob(columnIndex, inputStream, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		rset.updateBlob(columnLabel, inputStream, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		rset.updateClob(columnIndex, reader, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateClob(columnLabel, reader, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		rset.updateNClob(columnIndex, reader, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateNClob(columnLabel, reader, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		rset.updateNCharacterStream(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		rset.updateNCharacterStream(columnLabel, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		rset.updateAsciiStream(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		rset.updateBinaryStream(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		rset.updateCharacterStream(columnIndex, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		rset.updateAsciiStream(columnLabel, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		rset.updateBinaryStream(columnLabel, x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		rset.updateCharacterStream(columnLabel, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		rset.updateBlob(columnIndex, inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		rset.updateBlob(columnLabel, inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		rset.updateClob(columnIndex, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		rset.updateClob(columnLabel, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		rset.updateNClob(columnIndex, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		rset.updateNClob(columnLabel, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return rset.unwrap(iface);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return rset.isWrapperFor(iface);
	}
}
