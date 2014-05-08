package org.javasimon.jdbc4;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Simon JDBC proxy callable statement implementation class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
@SuppressWarnings("deprecation")
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

	@Override
	public void registerOutParameter(int i, int i1) throws SQLException {
		stmt.registerOutParameter(i, i1);
	}

	@Override
	public void registerOutParameter(int i, int i1, int i2) throws SQLException {
		stmt.registerOutParameter(i, i1, i2);
	}

	@Override
	public boolean wasNull() throws SQLException {
		return stmt.wasNull();
	}

	@Override
	public String getString(int i) throws SQLException {
		return stmt.getString(i);
	}

	@Override
	public boolean getBoolean(int i) throws SQLException {
		return stmt.getBoolean(i);
	}

	@Override
	public byte getByte(int i) throws SQLException {
		return stmt.getByte(i);
	}

	@Override
	public short getShort(int i) throws SQLException {
		return stmt.getShort(i);
	}

	@Override
	public int getInt(int i) throws SQLException {
		return stmt.getInt(i);
	}

	@Override
	public long getLong(int i) throws SQLException {
		return stmt.getLong(i);
	}

	@Override
	public float getFloat(int i) throws SQLException {
		return stmt.getFloat(i);
	}

	@Override
	public double getDouble(int i) throws SQLException {
		return stmt.getDouble(i);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
		return stmt.getBigDecimal(i, i1);
	}

	@Override
	public byte[] getBytes(int i) throws SQLException {
		return stmt.getBytes(i);
	}

	@Override
	public Date getDate(int i) throws SQLException {
		return stmt.getDate(i);
	}

	@Override
	public Time getTime(int i) throws SQLException {
		return stmt.getTime(i);
	}

	@Override
	public Timestamp getTimestamp(int i) throws SQLException {
		return stmt.getTimestamp(i);
	}

	@Override
	public Object getObject(int i) throws SQLException {
		return stmt.getObject(i);
	}

	@Override
	public BigDecimal getBigDecimal(int i) throws SQLException {
		return stmt.getBigDecimal(i);
	}

	@Override
	public Object getObject(int i, Map<String, Class<?>> stringClassMap) throws SQLException {
		return stmt.getObject(i, stringClassMap);
	}

	@Override
	public Ref getRef(int i) throws SQLException {
		return stmt.getRef(i);
	}

	@Override
	public Blob getBlob(int i) throws SQLException {
		return stmt.getBlob(i);
	}

	@Override
	public Clob getClob(int i) throws SQLException {
		return stmt.getClob(i);
	}

	@Override
	public Array getArray(int i) throws SQLException {
		return stmt.getArray(i);
	}

	@Override
	public Date getDate(int i, Calendar calendar) throws SQLException {
		return stmt.getDate(i, calendar);
	}

	@Override
	public Time getTime(int i, Calendar calendar) throws SQLException {
		return stmt.getTime(i, calendar);
	}

	@Override
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return stmt.getTimestamp(i, calendar);
	}

	@Override
	public void registerOutParameter(int i, int i1, String s) throws SQLException {
		stmt.registerOutParameter(i, i1, s);
	}

	@Override
	public void registerOutParameter(String s, int i) throws SQLException {
		stmt.registerOutParameter(s, i);
	}

	@Override
	public void registerOutParameter(String s, int i, int i1) throws SQLException {
		stmt.registerOutParameter(s, i, i1);
	}

	@Override
	public void registerOutParameter(String s, int i, String s1) throws SQLException {
		stmt.registerOutParameter(s, i, s1);
	}

	@Override
	public URL getURL(int i) throws SQLException {
		return stmt.getURL(i);
	}

	@Override
	public void setURL(String s, URL url) throws SQLException {
		stmt.setURL(s, url);
	}

	@Override
	public void setNull(String s, int i) throws SQLException {
		stmt.setNull(s, i);
	}

	@Override
	public void setBoolean(String s, boolean b) throws SQLException {
		stmt.setBoolean(s, b);
	}

	@Override
	public void setByte(String s, byte b) throws SQLException {
		stmt.setByte(s, b);
	}

	@Override
	public void setShort(String s, short i) throws SQLException {
		stmt.setShort(s, i);
	}

	@Override
	public void setInt(String s, int i) throws SQLException {
		stmt.setInt(s, i);
	}

	@Override
	public void setLong(String s, long l) throws SQLException {
		stmt.setLong(s, l);
	}

	@Override
	public void setFloat(String s, float v) throws SQLException {
		stmt.setFloat(s, v);
	}

	@Override
	public void setDouble(String s, double v) throws SQLException {
		stmt.setDouble(s, v);
	}

	@Override
	public void setBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
		stmt.setBigDecimal(s, bigDecimal);
	}

	@Override
	public void setString(String s, String s1) throws SQLException {
		stmt.setString(s, s1);
	}

	@Override
	public void setBytes(String s, byte[] bytes) throws SQLException {
		stmt.setBytes(s, bytes);
	}

	@Override
	public void setDate(String s, Date date) throws SQLException {
		stmt.setDate(s, date);
	}

	@Override
	public void setTime(String s, Time time) throws SQLException {
		stmt.setTime(s, time);
	}

	@Override
	public void setTimestamp(String s, Timestamp timestamp) throws SQLException {
		stmt.setTimestamp(s, timestamp);
	}

	@Override
	public void setAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
		stmt.setAsciiStream(s, inputStream, i);
	}

	@Override
	public void setBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
		stmt.setBinaryStream(s, inputStream, i);
	}

	@Override
	public void setObject(String s, Object o, int i, int i1) throws SQLException {
		stmt.setObject(s, o, i, i1);
	}

	@Override
	public void setObject(String s, Object o, int i) throws SQLException {
		stmt.setObject(s, o, i);
	}

	@Override
	public void setObject(String s, Object o) throws SQLException {
		stmt.setObject(s, o);
	}

	@Override
	public void setCharacterStream(String s, Reader reader, int i) throws SQLException {
		stmt.setCharacterStream(s, reader, i);
	}

	@Override
	public void setDate(String s, Date date, Calendar calendar) throws SQLException {
		stmt.setDate(s, date, calendar);
	}

	@Override
	public void setTime(String s, Time time, Calendar calendar) throws SQLException {
		stmt.setTime(s, time, calendar);
	}

	@Override
	public void setTimestamp(String s, Timestamp timestamp, Calendar calendar) throws SQLException {
		stmt.setTimestamp(s, timestamp, calendar);
	}

	@Override
	public void setNull(String s, int i, String s1) throws SQLException {
		stmt.setNull(s, i, s1);
	}

	@Override
	public String getString(String s) throws SQLException {
		return stmt.getString(s);
	}

	@Override
	public boolean getBoolean(String s) throws SQLException {
		return stmt.getBoolean(s);
	}

	@Override
	public byte getByte(String s) throws SQLException {
		return stmt.getByte(s);
	}

	@Override
	public short getShort(String s) throws SQLException {
		return stmt.getShort(s);
	}

	@Override
	public int getInt(String s) throws SQLException {
		return stmt.getInt(s);
	}

	@Override
	public long getLong(String s) throws SQLException {
		return stmt.getLong(s);
	}

	@Override
	public float getFloat(String s) throws SQLException {
		return stmt.getFloat(s);
	}

	@Override
	public double getDouble(String s) throws SQLException {
		return stmt.getDouble(s);
	}

	@Override
	public byte[] getBytes(String s) throws SQLException {
		return stmt.getBytes(s);
	}

	@Override
	public Date getDate(String s) throws SQLException {
		return stmt.getDate(s);
	}

	@Override
	public Time getTime(String s) throws SQLException {
		return stmt.getTime(s);
	}

	@Override
	public Timestamp getTimestamp(String s) throws SQLException {
		return stmt.getTimestamp(s);
	}

	@Override
	public Object getObject(String s) throws SQLException {
		return stmt.getObject(s);
	}

	@Override
	public BigDecimal getBigDecimal(String s) throws SQLException {
		return stmt.getBigDecimal(s);
	}

	@Override
	public Object getObject(String s, Map<String, Class<?>> stringClassMap) throws SQLException {
		return stmt.getObject(s, stringClassMap);
	}

	@Override
	public Ref getRef(String s) throws SQLException {
		return stmt.getRef(s);
	}

	@Override
	public Blob getBlob(String s) throws SQLException {
		return stmt.getBlob(s);
	}

	@Override
	public Clob getClob(String s) throws SQLException {
		return stmt.getClob(s);
	}

	@Override
	public Array getArray(String s) throws SQLException {
		return stmt.getArray(s);
	}

	@Override
	public Date getDate(String s, Calendar calendar) throws SQLException {
		return stmt.getDate(s, calendar);
	}

	@Override
	public Time getTime(String s, Calendar calendar) throws SQLException {
		return stmt.getTime(s, calendar);
	}

	@Override
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		return stmt.getTimestamp(s, calendar);
	}

	@Override
	public URL getURL(String s) throws SQLException {
		return stmt.getURL(s);
	}

	@Override
	public RowId getRowId(int i) throws SQLException {
		return stmt.getRowId(i);
	}

	@Override
	public RowId getRowId(String s) throws SQLException {
		return stmt.getRowId(s);
	}

	@Override
	public void setRowId(String s, RowId rowId) throws SQLException {
		stmt.setRowId(s, rowId);
	}

	@Override
	public void setNString(String s, String s1) throws SQLException {
		stmt.setNString(s, s1);
	}

	@Override
	public void setNCharacterStream(String s, Reader reader, long l) throws SQLException {
		stmt.setNCharacterStream(s, reader, l);
	}

	@Override
	public void setNClob(String s, NClob nClob) throws SQLException {
		stmt.setNClob(s, nClob);
	}

	@Override
	public void setClob(String s, Reader reader, long l) throws SQLException {
		stmt.setClob(s, reader, l);
	}

	@Override
	public void setBlob(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setBlob(s, inputStream, l);
	}

	@Override
	public void setNClob(String s, Reader reader, long l) throws SQLException {
		stmt.setNClob(s, reader, l);
	}

	@Override
	public NClob getNClob(int i) throws SQLException {
		return stmt.getNClob(i);
	}

	@Override
	public NClob getNClob(String s) throws SQLException {
		return stmt.getNClob(s);
	}

	@Override
	public void setSQLXML(String s, SQLXML sqlxml) throws SQLException {
		stmt.setSQLXML(s, sqlxml);
	}

	@Override
	public SQLXML getSQLXML(int i) throws SQLException {
		return stmt.getSQLXML(i);
	}

	@Override
	public SQLXML getSQLXML(String s) throws SQLException {
		return stmt.getSQLXML(s);
	}

	@Override
	public String getNString(int i) throws SQLException {
		return stmt.getNString(i);
	}

	@Override
	public String getNString(String s) throws SQLException {
		return stmt.getNString(s);
	}

	@Override
	public Reader getNCharacterStream(int i) throws SQLException {
		return stmt.getNCharacterStream(i);
	}

	@Override
	public Reader getNCharacterStream(String s) throws SQLException {
		return stmt.getNCharacterStream(s);
	}

	@Override
	public Reader getCharacterStream(int i) throws SQLException {
		return stmt.getCharacterStream(i);
	}

	@Override
	public Reader getCharacterStream(String s) throws SQLException {
		return stmt.getCharacterStream(s);
	}

	@Override
	public void setBlob(String s, Blob blob) throws SQLException {
		stmt.setBlob(s, blob);
	}

	@Override
	public void setClob(String s, Clob clob) throws SQLException {
		stmt.setClob(s, clob);
	}

	@Override
	public void setAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setAsciiStream(s, inputStream, l);
	}

	@Override
	public void setBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
		stmt.setBinaryStream(s, inputStream, l);
	}

	@Override
	public void setCharacterStream(String s, Reader reader, long l) throws SQLException {
		stmt.setCharacterStream(s, reader, l);
	}

	@Override
	public void setAsciiStream(String s, InputStream inputStream) throws SQLException {
		stmt.setAsciiStream(s, inputStream);
	}

	@Override
	public void setBinaryStream(String s, InputStream inputStream) throws SQLException {
		stmt.setBinaryStream(s, inputStream);
	}

	@Override
	public void setCharacterStream(String s, Reader reader) throws SQLException {
		stmt.setCharacterStream(s, reader);
	}

	@Override
	public void setNCharacterStream(String s, Reader reader) throws SQLException {
		stmt.setNCharacterStream(s, reader);
	}

	@Override
	public void setClob(String s, Reader reader) throws SQLException {
		stmt.setClob(s, reader);
	}

	@Override
	public void setBlob(String s, InputStream inputStream) throws SQLException {
		stmt.setBlob(s, inputStream);
	}

	@Override
	public void setNClob(String s, Reader reader) throws SQLException {
		stmt.setNClob(s, reader);
	}

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return stmt.getObject(parameterIndex, type);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return stmt.getObject(parameterName, type);
    }
}
