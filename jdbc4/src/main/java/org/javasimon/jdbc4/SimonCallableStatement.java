package org.javasimon.jdbc4;

import java.sql.*;
import java.sql.Connection;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Calendar;
import java.net.URL;
import java.io.InputStream;
import java.io.Reader;

/**
 * Simon JDBC4 proxy callable statement implementation class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision: $ $Date: $
 * @since 2.4
 */
public final class SimonCallableStatement extends SimonPreparedStatement implements CallableStatement {
	private CallableStatement stmt;

	/**
	 * Class constructor, initializes Simons (lifespan, active) related to statement.
	 *
	 * @param conn database connection (simon impl.)
	 * @param stmt real callable statement
	 * @param sql sql command
	 * @param prefix hierarchy prefix for statement Simons
	 */
	SimonCallableStatement(Connection conn, CallableStatement stmt, String sql, String prefix) {
		super(conn, stmt, sql, prefix);

		this.stmt = stmt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerOutParameter(int i, int i1) throws SQLException {
		stmt.registerOutParameter(i, i1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerOutParameter(int i, int i1, int i2) throws SQLException {
		stmt.registerOutParameter(i, i1, i2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean wasNull() throws SQLException {
		return stmt.wasNull();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(int i) throws SQLException {
		return stmt.getString(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(int i) throws SQLException {
		return stmt.getBoolean(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(int i) throws SQLException {
		return stmt.getByte(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(int i) throws SQLException {
		return stmt.getShort(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(int i) throws SQLException {
		return stmt.getInt(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(int i) throws SQLException {
		return stmt.getLong(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(int i) throws SQLException {
		return stmt.getFloat(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(int i) throws SQLException {
		return stmt.getDouble(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
		return stmt.getBigDecimal(i, i1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes(int i) throws SQLException {
		return stmt.getBytes(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(int i) throws SQLException {
		return stmt.getDate(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(int i) throws SQLException {
		return stmt.getTime(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(int i) throws SQLException {
		return stmt.getTimestamp(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(int i) throws SQLException {
		return stmt.getObject(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(int i) throws SQLException {
		return stmt.getBigDecimal(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(int i, Map<String, Class<?>> stringClassMap) throws SQLException {
		return stmt.getObject(i, stringClassMap);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ref getRef(int i) throws SQLException {
		return stmt.getRef(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Blob getBlob(int i) throws SQLException {
		return stmt.getBlob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clob getClob(int i) throws SQLException {
		return stmt.getClob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array getArray(int i) throws SQLException {
		return stmt.getArray(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(int i, Calendar calendar) throws SQLException {
		return stmt.getDate(i, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(int i, Calendar calendar) throws SQLException {
		return stmt.getTime(i, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return stmt.getTimestamp(i, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerOutParameter(int i, int i1, String s) throws SQLException {
		stmt.registerOutParameter(i, i1, s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerOutParameter(String s, int i) throws SQLException {
		stmt.registerOutParameter(s, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerOutParameter(String s, int i, int i1) throws SQLException {
		stmt.registerOutParameter(s, i, i1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerOutParameter(String s, int i, String s1) throws SQLException {
		stmt.registerOutParameter(s, i, s1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getURL(int i) throws SQLException {
		return stmt.getURL(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setURL(String s, URL url) throws SQLException {
		stmt.setURL(s, url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNull(String s, int i) throws SQLException {
		stmt.setNull(s, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBoolean(String s, boolean b) throws SQLException {
		stmt.setBoolean(s, b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setByte(String s, byte b) throws SQLException {
		stmt.setByte(s, b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShort(String s, short i) throws SQLException {
		stmt.setShort(s, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInt(String s, int i) throws SQLException {
		stmt.setInt(s, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLong(String s, long l) throws SQLException {
		stmt.setLong(s, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFloat(String s, float v) throws SQLException {
		stmt.setFloat(s, v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDouble(String s, double v) throws SQLException {
		stmt.setDouble(s, v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
		stmt.setBigDecimal(s, bigDecimal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setString(String s, String s1) throws SQLException {
		stmt.setString(s, s1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBytes(String s, byte[] bytes) throws SQLException {
		stmt.setBytes(s, bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDate(String s, Date date) throws SQLException {
		stmt.setDate(s, date);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTime(String s, Time time) throws SQLException {
		stmt.setTime(s, time);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTimestamp(String s, Timestamp timestamp) throws SQLException {
		stmt.setTimestamp(s, timestamp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
		stmt.setAsciiStream(s, inputStream, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
		stmt.setBinaryStream(s, inputStream, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setObject(String s, Object o, int i, int i1) throws SQLException {
		stmt.setObject(s, o, i, i1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setObject(String s, Object o, int i) throws SQLException {
		stmt.setObject(s, o, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setObject(String s, Object o) throws SQLException {
		stmt.setObject(s, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCharacterStream(String s, Reader reader, int i) throws SQLException {
		stmt.setCharacterStream(s, reader, i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDate(String s, Date date, Calendar calendar) throws SQLException {
		stmt.setDate(s, date, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTime(String s, Time time, Calendar calendar) throws SQLException {
		stmt.setTime(s, time, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTimestamp(String s, Timestamp timestamp, Calendar calendar) throws SQLException {
		stmt.setTimestamp(s, timestamp, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNull(String s, int i, String s1) throws SQLException {
		stmt.setNull(s, i, s1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(String s) throws SQLException {
		return stmt.getString(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(String s) throws SQLException {
		return stmt.getBoolean(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByte(String s) throws SQLException {
		return stmt.getByte(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(String s) throws SQLException {
		return stmt.getShort(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String s) throws SQLException {
		return stmt.getInt(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String s) throws SQLException {
		return stmt.getLong(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String s) throws SQLException {
		return stmt.getFloat(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String s) throws SQLException {
		return stmt.getDouble(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes(String s) throws SQLException {
		return stmt.getBytes(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(String s) throws SQLException {
		return stmt.getDate(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(String s) throws SQLException {
		return stmt.getTime(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(String s) throws SQLException {
		return stmt.getTimestamp(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String s) throws SQLException {
		return stmt.getObject(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(String s) throws SQLException {
		return stmt.getBigDecimal(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(String s, Map<String, Class<?>> stringClassMap) throws SQLException {
		return stmt.getObject(s, stringClassMap);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ref getRef(String s) throws SQLException {
		return stmt.getRef(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Blob getBlob(String s) throws SQLException {
		return stmt.getBlob(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clob getClob(String s) throws SQLException {
		return stmt.getClob(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Array getArray(String s) throws SQLException {
		return stmt.getArray(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate(String s, Calendar calendar) throws SQLException {
		return stmt.getDate(s, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Time getTime(String s, Calendar calendar) throws SQLException {
		return stmt.getTime(s, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		return stmt.getTimestamp(s, calendar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getURL(String s) throws SQLException {
		return stmt.getURL(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowId getRowId(int i) throws SQLException {
		return stmt.getRowId(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowId getRowId(String s) throws SQLException {
		return stmt.getRowId(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRowId(String s, RowId rowId) throws SQLException {
		stmt.setRowId(s, rowId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNString(String s, String s1) throws SQLException {
		stmt.setNString(s, s1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNCharacterStream(String s, Reader reader, long l) throws SQLException {
		stmt.setNCharacterStream(s, reader, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNClob(String s, NClob nClob) throws SQLException {
		stmt.setNClob(s, nClob);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClob(String s, Reader reader, long l) throws SQLException {
		stmt.setClob(s, reader, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBlob(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setBlob(s, inputStream, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNClob(String s, Reader reader, long l) throws SQLException {
		stmt.setNClob(s, reader, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NClob getNClob(int i) throws SQLException {
		return stmt.getNClob(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NClob getNClob(String s) throws SQLException {
		return stmt.getNClob(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSQLXML(String s, SQLXML sqlxml) throws SQLException {
		stmt.setSQLXML(s, sqlxml);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLXML getSQLXML(int i) throws SQLException {
		return stmt.getSQLXML(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SQLXML getSQLXML(String s) throws SQLException {
		return stmt.getSQLXML(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNString(int i) throws SQLException {
		return stmt.getNString(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNString(String s) throws SQLException {
		return stmt.getNString(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getNCharacterStream(int i) throws SQLException {
		return stmt.getNCharacterStream(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getNCharacterStream(String s) throws SQLException {
		return stmt.getNCharacterStream(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getCharacterStream(int i) throws SQLException {
		return stmt.getCharacterStream(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getCharacterStream(String s) throws SQLException {
		return stmt.getCharacterStream(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBlob(String s, Blob blob) throws SQLException {
		stmt.setBlob(s, blob);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClob(String s, Clob clob) throws SQLException {
		stmt.setClob(s, clob);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setAsciiStream(s, inputStream, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setBinaryStream(s, inputStream, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCharacterStream(String s, Reader reader, long l) throws SQLException {
		stmt.setCharacterStream(s, reader, l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAsciiStream(String s, InputStream inputStream) throws SQLException {
		stmt.setAsciiStream(s, inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBinaryStream(String s, InputStream inputStream) throws SQLException {
		stmt.setBinaryStream(s, inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCharacterStream(String s, Reader reader) throws SQLException {
		stmt.setCharacterStream(s, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNCharacterStream(String s, Reader reader) throws SQLException {
		stmt.setNCharacterStream(s, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClob(String s, Reader reader) throws SQLException {
		stmt.setClob(s, reader);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBlob(String s, InputStream inputStream) throws SQLException {
		stmt.setBlob(s, inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNClob(String s, Reader reader) throws SQLException {
		stmt.setNClob(s, reader);
	}
}
