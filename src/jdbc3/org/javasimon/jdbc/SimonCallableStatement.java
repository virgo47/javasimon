package org.javasimon.jdbc;

import java.sql.*;
import java.sql.Connection;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Calendar;
import java.net.URL;
import java.io.InputStream;
import java.io.Reader;

/**
 * Simon jdbc proxy callable statement implemntation class.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 16.8.2008 22:00:26
 * @since 1.0
 */
public final class SimonCallableStatement extends SimonPreparedStatement implements java.sql.CallableStatement {

	private java.sql.CallableStatement stmt;

	/**
	 * Class constructor, initializes simons (lifespan, active) related to statement.
	 *
	 * @param conn database connection (simon impl.)
	 * @param stmt real callable statement
	 * @param sql sql command
	 * @param prefix hierarchy preffix for statement simons
	 */
	SimonCallableStatement(Connection conn, java.sql.CallableStatement stmt, String sql, String prefix) {
		super(conn, stmt, sql, prefix);

		this.stmt = stmt;
	}

	/** {@inheritDoc} */
	public void registerOutParameter(int i, int i1) throws SQLException {
		stmt.registerOutParameter(i, i1);
	}

	/** {@inheritDoc} */
	public void registerOutParameter(int i, int i1, int i2) throws SQLException {
		stmt.registerOutParameter(i, i1, i2);
	}

	/** {@inheritDoc} */
	public boolean wasNull() throws SQLException {
		return stmt.wasNull();
	}

	/** {@inheritDoc} */
	public String getString(int i) throws SQLException {
		return stmt.getString(i);
	}

	/** {@inheritDoc} */
	public boolean getBoolean(int i) throws SQLException {
		return stmt.getBoolean(i);
	}

	/** {@inheritDoc} */
	public byte getByte(int i) throws SQLException {
		return stmt.getByte(i);
	}

	/** {@inheritDoc} */
	public short getShort(int i) throws SQLException {
		return stmt.getShort(i);
	}

	/** {@inheritDoc} */
	public int getInt(int i) throws SQLException {
		return stmt.getInt(i);
	}

	/** {@inheritDoc} */
	public long getLong(int i) throws SQLException {
		return stmt.getLong(i);
	}

	/** {@inheritDoc} */
	public float getFloat(int i) throws SQLException {
		return stmt.getFloat(i);
	}

	/** {@inheritDoc} */
	public double getDouble(int i) throws SQLException {
		return stmt.getDouble(i);
	}

	/** {@inheritDoc} */
	public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
		return stmt.getBigDecimal(i, i1);
	}

	/** {@inheritDoc} */
	public byte[] getBytes(int i) throws SQLException {
		return stmt.getBytes(i);
	}

	/** {@inheritDoc} */
	public Date getDate(int i) throws SQLException {
		return stmt.getDate(i);
	}

	/** {@inheritDoc} */
	public Time getTime(int i) throws SQLException {
		return stmt.getTime(i);
	}

	/** {@inheritDoc} */
	public Timestamp getTimestamp(int i) throws SQLException {
		return stmt.getTimestamp(i);
	}

	/** {@inheritDoc} */
	public Object getObject(int i) throws SQLException {
		return stmt.getObject(i);
	}

	/** {@inheritDoc} */
	public BigDecimal getBigDecimal(int i) throws SQLException {
		return stmt.getBigDecimal(i);
	}

	/** {@inheritDoc} */
	public Object getObject(int i, Map<String, Class<?>> stringClassMap) throws SQLException {
		return stmt.getObject(i, stringClassMap);
	}

	/** {@inheritDoc} */
	public Ref getRef(int i) throws SQLException {
		return stmt.getRef(i);
	}

	/** {@inheritDoc} */
	public Blob getBlob(int i) throws SQLException {
		return stmt.getBlob(i);
	}

	/** {@inheritDoc} */
	public Clob getClob(int i) throws SQLException {
		return stmt.getClob(i);
	}

	/** {@inheritDoc} */
	public Array getArray(int i) throws SQLException {
		return stmt.getArray(i);
	}

	/** {@inheritDoc} */
	public Date getDate(int i, Calendar calendar) throws SQLException {
		return stmt.getDate(i, calendar);
	}

	/** {@inheritDoc} */
	public Time getTime(int i, Calendar calendar) throws SQLException {
		return stmt.getTime(i, calendar);
	}

	/** {@inheritDoc} */
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return stmt.getTimestamp(i, calendar);
	}

	/** {@inheritDoc} */
	public void registerOutParameter(int i, int i1, String s) throws SQLException {
		stmt.registerOutParameter(i, i1, s);
	}

	/** {@inheritDoc} */
	public void registerOutParameter(String s, int i) throws SQLException {
		stmt.registerOutParameter(s, i);
	}

	/** {@inheritDoc} */
	public void registerOutParameter(String s, int i, int i1) throws SQLException {
		stmt.registerOutParameter(s, i, i1);
	}

	/** {@inheritDoc} */
	public void registerOutParameter(String s, int i, String s1) throws SQLException {
		stmt.registerOutParameter(s, i, s1);
	}

	/** {@inheritDoc} */
	public URL getURL(int i) throws SQLException {
		return stmt.getURL(i);
	}

	/** {@inheritDoc} */
	public void setURL(String s, URL url) throws SQLException {
		stmt.setURL(s, url);
	}

	/** {@inheritDoc} */
	public void setNull(String s, int i) throws SQLException {
		stmt.setNull(s, i);
	}

	/** {@inheritDoc} */
	public void setBoolean(String s, boolean b) throws SQLException {
		stmt.setBoolean(s, b);
	}

	/** {@inheritDoc} */
	public void setByte(String s, byte b) throws SQLException {
		stmt.setByte(s, b);
	}

	/** {@inheritDoc} */
	public void setShort(String s, short i) throws SQLException {
		stmt.setShort(s, i);
	}

	/** {@inheritDoc} */
	public void setInt(String s, int i) throws SQLException {
		stmt.setInt(s, i);
	}

	/** {@inheritDoc} */
	public void setLong(String s, long l) throws SQLException {
		stmt.setLong(s, l);
	}

	/** {@inheritDoc} */
	public void setFloat(String s, float v) throws SQLException {
		stmt.setFloat(s, v);
	}

	/** {@inheritDoc} */
	public void setDouble(String s, double v) throws SQLException {
		stmt.setDouble(s, v);
	}

	/** {@inheritDoc} */
	public void setBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
		stmt.setBigDecimal(s, bigDecimal);
	}

	/** {@inheritDoc} */
	public void setString(String s, String s1) throws SQLException {
		stmt.setString(s, s1);
	}

	/** {@inheritDoc} */
	public void setBytes(String s, byte[] bytes) throws SQLException {
		stmt.setBytes(s, bytes);
	}

	/** {@inheritDoc} */
	public void setDate(String s, Date date) throws SQLException {
		stmt.setDate(s, date);
	}

	/** {@inheritDoc} */
	public void setTime(String s, Time time) throws SQLException {
		stmt.setTime(s, time);
	}

	/** {@inheritDoc} */
	public void setTimestamp(String s, Timestamp timestamp) throws SQLException {
		stmt.setTimestamp(s, timestamp);
	}

	/** {@inheritDoc} */
	public void setAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
		stmt.setAsciiStream(s, inputStream, i);
	}

	/** {@inheritDoc} */
	public void setBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
		stmt.setBinaryStream(s, inputStream, i);
	}

	/** {@inheritDoc} */
	public void setObject(String s, Object o, int i, int i1) throws SQLException {
		stmt.setObject(s, o, i, i1);
	}

	/** {@inheritDoc} */
	public void setObject(String s, Object o, int i) throws SQLException {
		stmt.setObject(s, o, i);
	}

	/** {@inheritDoc} */
	public void setObject(String s, Object o) throws SQLException {
		stmt.setObject(s, o);
	}

	/** {@inheritDoc} */
	public void setCharacterStream(String s, Reader reader, int i) throws SQLException {
		stmt.setCharacterStream(s, reader, i);
	}

	/** {@inheritDoc} */
	public void setDate(String s, Date date, Calendar calendar) throws SQLException {
		stmt.setDate(s, date, calendar);
	}

	/** {@inheritDoc} */
	public void setTime(String s, Time time, Calendar calendar) throws SQLException {
		stmt.setTime(s, time, calendar);
	}

	/** {@inheritDoc} */
	public void setTimestamp(String s, Timestamp timestamp, Calendar calendar) throws SQLException {
		stmt.setTimestamp(s, timestamp, calendar);
	}

	/** {@inheritDoc} */
	public void setNull(String s, int i, String s1) throws SQLException {
		stmt.setNull(s, i, s1);
	}

	/** {@inheritDoc} */
	public String getString(String s) throws SQLException {
		return stmt.getString(s);
	}

	/** {@inheritDoc} */
	public boolean getBoolean(String s) throws SQLException {
		return stmt.getBoolean(s);
	}

	/** {@inheritDoc} */
	public byte getByte(String s) throws SQLException {
		return stmt.getByte(s);
	}

	/** {@inheritDoc} */
	public short getShort(String s) throws SQLException {
		return stmt.getShort(s);
	}

	/** {@inheritDoc} */
	public int getInt(String s) throws SQLException {
		return stmt.getInt(s);
	}

	/** {@inheritDoc} */
	public long getLong(String s) throws SQLException {
		return stmt.getLong(s);
	}

	/** {@inheritDoc} */
	public float getFloat(String s) throws SQLException {
		return stmt.getFloat(s);
	}

	/** {@inheritDoc} */
	public double getDouble(String s) throws SQLException {
		return stmt.getDouble(s);
	}

	/** {@inheritDoc} */
	public byte[] getBytes(String s) throws SQLException {
		return stmt.getBytes(s);
	}

	/** {@inheritDoc} */
	public Date getDate(String s) throws SQLException {
		return stmt.getDate(s);
	}

	/** {@inheritDoc} */
	public Time getTime(String s) throws SQLException {
		return stmt.getTime(s);
	}

	/** {@inheritDoc} */
	public Timestamp getTimestamp(String s) throws SQLException {
		return stmt.getTimestamp(s);
	}

	/** {@inheritDoc} */
	public Object getObject(String s) throws SQLException {
		return stmt.getObject(s);
	}

	/** {@inheritDoc} */
	public BigDecimal getBigDecimal(String s) throws SQLException {
		return stmt.getBigDecimal(s);
	}

	/** {@inheritDoc} */
	public Object getObject(String s, Map<String, Class<?>> stringClassMap) throws SQLException {
		return stmt.getObject(s, stringClassMap);
	}

	/** {@inheritDoc} */
	public Ref getRef(String s) throws SQLException {
		return stmt.getRef(s);
	}

	/** {@inheritDoc} */
	public Blob getBlob(String s) throws SQLException {
		return stmt.getBlob(s);
	}

	/** {@inheritDoc} */
	public Clob getClob(String s) throws SQLException {
		return stmt.getClob(s);
	}

	/** {@inheritDoc} */
	public Array getArray(String s) throws SQLException {
		return stmt.getArray(s);
	}

	/** {@inheritDoc} */
	public Date getDate(String s, Calendar calendar) throws SQLException {
		return stmt.getDate(s, calendar);
	}

	/** {@inheritDoc} */
	public Time getTime(String s, Calendar calendar) throws SQLException {
		return stmt.getTime(s, calendar);
	}

	/** {@inheritDoc} */
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		return stmt.getTimestamp(s, calendar);
	}

	/** {@inheritDoc} */
	public URL getURL(String s) throws SQLException {
		return stmt.getURL(s);
	}

//////// from JDK 6, JDBC 4
/*
	public RowId getRowId(int i) throws SQLException {
		return stmt.getRowId(i);
	}

	public RowId getRowId(String s) throws SQLException {
		return stmt.getRowId(s);
	}

	public void setRowId(String s, RowId rowId) throws SQLException {
		stmt.setRowId(s, rowId);
	}

	public void setNString(String s, String s1) throws SQLException {
		stmt.setNString(s, s1);
	}

	public void setNCharacterStream(String s, Reader reader, long l) throws SQLException {
		stmt.setNCharacterStream(s, reader, l);
	}

	public void setNClob(String s, NClob nClob) throws SQLException {
		stmt.setNClob(s, nClob);
	}

	public void setClob(String s, Reader reader, long l) throws SQLException {
		stmt.setClob(s, reader, l);
	}

	public void setBlob(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setBlob(s, inputStream, l);
	}

	public void setNClob(String s, Reader reader, long l) throws SQLException {
		stmt.setNClob(s, reader, l);
	}

	public NClob getNClob(int i) throws SQLException {
		return stmt.getNClob(i);
	}

	public NClob getNClob(String s) throws SQLException {
		return stmt.getNClob(s);
	}

	public void setSQLXML(String s, SQLXML sqlxml) throws SQLException {
		stmt.setSQLXML(s, sqlxml);
	}

	public SQLXML getSQLXML(int i) throws SQLException {
		return stmt.getSQLXML(i);
	}

	public SQLXML getSQLXML(String s) throws SQLException {
		return stmt.getSQLXML(s);
	}

	public String getNString(int i) throws SQLException {
		return stmt.getNString(i);
	}

	public String getNString(String s) throws SQLException {
		return stmt.getNString(s);
	}

	public Reader getNCharacterStream(int i) throws SQLException {
		return stmt.getNCharacterStream(i);
	}

	public Reader getNCharacterStream(String s) throws SQLException {
		return stmt.getNCharacterStream(s);
	}

	public Reader getCharacterStream(int i) throws SQLException {
		return stmt.getCharacterStream(i);
	}

	public Reader getCharacterStream(String s) throws SQLException {
		return stmt.getCharacterStream(s);
	}

	public void setBlob(String s, Blob blob) throws SQLException {
		stmt.setBlob(s, blob);
	}

	public void setClob(String s, Clob clob) throws SQLException {
		stmt.setClob(s, clob);
	}

	public void setAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setAsciiStream(s, inputStream, l);
	}

	public void setBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setBinaryStream(s, inputStream, l);
	}

	public void setCharacterStream(String s, Reader reader, long l) throws SQLException {
		stmt.setCharacterStream(s, reader, l);
	}

	public void setAsciiStream(String s, InputStream inputStream) throws SQLException {
		stmt.setAsciiStream(s, inputStream);
	}

	public void setBinaryStream(String s, InputStream inputStream) throws SQLException {
		stmt.setBinaryStream(s, inputStream);
	}

	public void setCharacterStream(String s, Reader reader) throws SQLException {
		stmt.setCharacterStream(s, reader);
	}

	public void setNCharacterStream(String s, Reader reader) throws SQLException {
		stmt.setNCharacterStream(s, reader);
	}

	public void setClob(String s, Reader reader) throws SQLException {
		stmt.setClob(s, reader);
	}

	public void setBlob(String s, InputStream inputStream) throws SQLException {
		stmt.setBlob(s, inputStream);
	}

	public void setNClob(String s, Reader reader) throws SQLException {
		stmt.setNClob(s, reader);
	}
*/
}
