package org.javasimon.jdbc;

import org.javasimon.Stopwatch;
import org.javasimon.SimonFactory;

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
public class PreparedStatement extends Statement implements java.sql.PreparedStatement {

	private java.sql.PreparedStatement stmt;

	protected String sql;

	PreparedStatement(Connection conn, java.sql.PreparedStatement stmt, String sql, String suffix) {
		super(conn, stmt, suffix);

		this.stmt = stmt;
		this.sql = sql;
	}

	protected Stopwatch prepare() {
		if (sql != null && !sql.isEmpty()) {
			sqlCmdLabel = suffix + "." + determineSqlCmdType(sql);
			normalizedSql = normalizeSql(sql);
			return SimonFactory.getStopwatch(sqlCmdLabel + "." + normalizedSql.hashCode()).start();
		} else {
			return null;
		}
	}

	public ResultSet executeQuery() throws SQLException {
		Stopwatch s = prepare();
		try {
			return stmt.executeQuery();
		} finally {
			finish(s);
		}
	}

	public int executeUpdate() throws SQLException {
		Stopwatch s = prepare();
		try {
			return stmt.executeUpdate();
		} finally {
			finish(s);
		}
	}

	public boolean execute() throws SQLException {
		Stopwatch s = prepare();
		try {
			return stmt.execute();
		} finally {
			finish(s);
		}
	}

	public void addBatch() throws SQLException {
		batchSql.add(sql);

		stmt.addBatch();
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
}
