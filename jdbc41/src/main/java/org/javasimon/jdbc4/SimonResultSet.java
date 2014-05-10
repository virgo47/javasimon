package org.javasimon.jdbc4;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * Simon JDBC proxy result set implementation class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
@SuppressWarnings("deprecation")
public final class SimonResultSet implements ResultSet {
	/**
	 * Stopwatch split measuring the lifespan of the statement until it is closed.
	 */
	private Split split;

	private final ResultSet rset;
	private final WrapperSupport<ResultSet> wrapperSupport;
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
		this.wrapperSupport = new WrapperSupport<>(rset, ResultSet.class);
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

	//// NOT MONITORED

	@Override
	public Statement getStatement() throws SQLException {
		return stmt;
	}

	@Override
	public boolean wasNull() throws SQLException {
		return rset.wasNull();
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return rset.getString(columnIndex);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rset.getBoolean(columnIndex);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return rset.getByte(columnIndex);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return rset.getShort(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return rset.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return rset.getLong(columnIndex);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return rset.getFloat(columnIndex);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return rset.getDouble(columnIndex);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return rset.getBigDecimal(columnIndex, scale);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rset.getBytes(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return rset.getDate(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return rset.getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rset.getTimestamp(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rset.getAsciiStream(columnIndex);
	}

	@Deprecated
	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rset.getUnicodeStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rset.getBinaryStream(columnIndex);
	}

	@Override
	public String getString(String columnName) throws SQLException {
		return rset.getString(columnName);
	}

	@Override
	public boolean getBoolean(String columnName) throws SQLException {
		return rset.getBoolean(columnName);
	}

	@Override
	public byte getByte(String columnName) throws SQLException {
		return rset.getByte(columnName);
	}

	@Override
	public short getShort(String columnName) throws SQLException {
		return rset.getShort(columnName);
	}

	@Override
	public int getInt(String columnName) throws SQLException {
		return rset.getInt(columnName);
	}

	@Override
	public long getLong(String columnName) throws SQLException {
		return rset.getLong(columnName);
	}

	@Override
	public float getFloat(String columnName) throws SQLException {
		return rset.getFloat(columnName);
	}

	@Override
	public double getDouble(String columnName) throws SQLException {
		return rset.getDouble(columnName);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return rset.getBigDecimal(columnName, scale);
	}

	@Override
	public byte[] getBytes(String columnName) throws SQLException {
		return rset.getBytes(columnName);
	}

	@Override
	public Date getDate(String columnName) throws SQLException {
		return rset.getDate(columnName);
	}

	@Override
	public Time getTime(String columnName) throws SQLException {
		return rset.getTime(columnName);
	}

	@Override
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return rset.getTimestamp(columnName);
	}

	@Override
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rset.getAsciiStream(columnName);
	}

	@Deprecated
	@Override
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return rset.getUnicodeStream(columnName);
	}

	@Override
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rset.getBinaryStream(columnName);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return rset.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		rset.clearWarnings();
	}

	@Override
	public String getCursorName() throws SQLException {
		return rset.getCursorName();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return rset.getMetaData();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return rset.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnName) throws SQLException {
		return rset.getObject(columnName);
	}

	@Override
	public int findColumn(String columnName) throws SQLException {
		return rset.findColumn(columnName);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rset.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnName) throws SQLException {
		return rset.getCharacterStream(columnName);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rset.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rset.getBigDecimal(columnName);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return rset.isBeforeFirst();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return rset.isAfterLast();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return rset.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return rset.isLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		rset.beforeFirst();
	}

	@Override
	public void afterLast() throws SQLException {
		rset.afterLast();
	}

	@Override
	public boolean first() throws SQLException {
		return rset.first();
	}

	@Override
	public boolean last() throws SQLException {
		return rset.last();
	}

	@Override
	public int getRow() throws SQLException {
		return rset.getRow();
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		return rset.absolute(row);
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		return rset.relative(rows);
	}

	@Override
	public boolean previous() throws SQLException {
		return rset.previous();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		rset.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return rset.getFetchDirection();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		rset.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return rset.getFetchSize();
	}

	@Override
	public int getType() throws SQLException {
		return rset.getType();
	}

	@Override
	public int getConcurrency() throws SQLException {
		return rset.getConcurrency();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return rset.rowUpdated();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return rset.rowInserted();
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return rset.rowDeleted();
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
	}

	@Override
	public void updateBytes(int columnIndex, byte x[]) throws SQLException {
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
	}

	@Override
	public void updateNull(String columnName) throws SQLException {
	}

	@Override
	public void updateBoolean(String columnName, boolean x) throws SQLException {
	}

	@Override
	public void updateByte(String columnName, byte x) throws SQLException {
	}

	@Override
	public void updateShort(String columnName, short x) throws SQLException {
	}

	@Override
	public void updateInt(String columnName, int x) throws SQLException {
	}

	@Override
	public void updateLong(String columnName, long x) throws SQLException {
	}

	@Override
	public void updateFloat(String columnName, float x) throws SQLException {
	}

	@Override
	public void updateDouble(String columnName, double x) throws SQLException {
	}

	@Override
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
	}

	@Override
	public void updateString(String columnName, String x) throws SQLException {
	}

	@Override
	public void updateBytes(String columnName, byte x[]) throws SQLException {
	}

	@Override
	public void updateDate(String columnName, Date x) throws SQLException {
	}

	@Override
	public void updateTime(String columnName, Time x) throws SQLException {
	}

	@Override
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
	}

	@Override
	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
	}

	@Override
	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
	}

	@Override
	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
	}

	@Override
	public void updateObject(String columnName, Object x, int scale) throws SQLException {
	}

	@Override
	public void updateObject(String columnName, Object x) throws SQLException {
	}

	@Override
	public void insertRow() throws SQLException {
	}

	@Override
	public void updateRow() throws SQLException {
	}

	@Override
	public void deleteRow() throws SQLException {
	}

	@Override
	public void refreshRow() throws SQLException {
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
	}

	@Override
	public void moveToInsertRow() throws SQLException {
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
	}

	@Override
	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(i, map);
	}

	@Override
	public Ref getRef(int i) throws SQLException {
		return rset.getRef(i);
	}

	@Override
	public Blob getBlob(int i) throws SQLException {
		return rset.getBlob(i);
	}

	@Override
	public Clob getClob(int i) throws SQLException {
		return rset.getClob(i);
	}

	@Override
	public Array getArray(int i) throws SQLException {
		return rset.getArray(i);
	}

	@Override
	public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
		return rset.getObject(colName, map);
	}

	@Override
	public Ref getRef(String colName) throws SQLException {
		return rset.getRef(colName);
	}

	@Override
	public Blob getBlob(String colName) throws SQLException {
		return rset.getBlob(colName);
	}

	@Override
	public Clob getClob(String colName) throws SQLException {
		return rset.getClob(colName);
	}

	@Override
	public Array getArray(String colName) throws SQLException {
		return rset.getArray(colName);
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rset.getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return rset.getDate(columnName, cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return rset.getTime(columnName, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return rset.getTimestamp(columnName, cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return rset.getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnName) throws SQLException {
		return rset.getURL(columnName);
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rset.updateRef(columnIndex, x);
	}

	@Override
	public void updateRef(String columnName, Ref x) throws SQLException {
		rset.updateRef(columnName, x);
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rset.updateBlob(columnIndex, x);
	}

	@Override
	public void updateBlob(String columnName, Blob x) throws SQLException {
		rset.updateBlob(columnName, x);
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rset.updateClob(columnIndex, x);
	}

	@Override
	public void updateClob(String columnName, Clob x) throws SQLException {
		rset.updateClob(columnName, x);
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		rset.updateArray(columnIndex, x);
	}

	@Override
	public void updateArray(String columnName, Array x) throws SQLException {
		rset.updateArray(columnName, x);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return rset.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return rset.getRowId(columnLabel);
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		rset.updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		rset.updateRowId(columnLabel, x);
	}

	@Override
	public int getHoldability() throws SQLException {
		return rset.getHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return rset.isClosed();
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		rset.updateNString(columnIndex, nString);
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		rset.updateNString(columnLabel, nString);
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		rset.updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		rset.updateNClob(columnLabel, nClob);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return rset.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return rset.getNClob(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return rset.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return rset.getSQLXML(columnLabel);
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		rset.updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		rset.updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return rset.getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return rset.getNString(columnLabel);
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return rset.getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return rset.getNCharacterStream(columnLabel);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		rset.updateNCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		rset.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		rset.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		rset.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		rset.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		rset.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		rset.updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		rset.updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		rset.updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		rset.updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		rset.updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		rset.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		rset.updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		rset.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		rset.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		rset.updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		rset.updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		rset.updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		rset.updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		rset.updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		rset.updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		rset.updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		rset.updateClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		rset.updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		rset.updateNClob(columnLabel, reader);
	}

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return rset.getObject(columnIndex, type);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return rset.getObject(columnLabel, type);
    }

    @Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return wrapperSupport.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return wrapperSupport.isWrapperFor(iface);
	}
}
