package org.javasimon.jdbc;

import org.javasimon.Stopwatch;
import org.javasimon.SimonManager;

import java.sql.*;
import java.sql.Connection;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;
import java.net.URL;

/**
 * Simon jdbc proxy prepared statement implemntation class.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 9.8.2008 15:42:52
 * @since 1.0
 * @see java.sql.PreparedStatement
 */
public class SimonPreparedStatement extends SimonStatement implements java.sql.PreparedStatement {

	private java.sql.PreparedStatement stmt;

	protected String sql;

	SimonPreparedStatement(Connection conn, java.sql.PreparedStatement stmt, String sql, String prefix) {
		super(conn, stmt, prefix);

		this.stmt = stmt;
		this.sql = sql;
	}

	protected final Stopwatch prepare() {
		if (sql != null && !sql.equals("")) {
			sqlNormalizer = new SqlNormalizer(sql);
			sqlCmdLabel = prefix + ".sql." + sqlNormalizer.getType();
			return SimonManager.getStopwatch(sqlCmdLabel + "." + sqlNormalizer.getNormalizedSql().hashCode()).start();
		} else {
			return null;
		}
	}

	public final ResultSet executeQuery() throws SQLException {
		Stopwatch s = prepare();
		try {
			return stmt.executeQuery();
		} finally {
			finish(s);
		}
	}

	public final int executeUpdate() throws SQLException {
		Stopwatch s = prepare();
		try {
			return stmt.executeUpdate();
		} finally {
			finish(s);
		}
	}

	public final boolean execute() throws SQLException {
		Stopwatch s = prepare();
		try {
			return stmt.execute();
		} finally {
			finish(s);
		}
	}

	public final void addBatch() throws SQLException {
		batchSql.add(sql);

		stmt.addBatch();
	}

/////////////////// Not interesting methods for monitoring

	public final void setNull(int i, int i1) throws SQLException {
		stmt.setNull(i, i1);
	}

	public final void setBoolean(int i, boolean b) throws SQLException {
		stmt.setBoolean(i, b);
	}

	public final void setByte(int i, byte b) throws SQLException {
		stmt.setByte(i, b);
	}

	public final void setShort(int i, short i1) throws SQLException {
		stmt.setShort(i, i1);
	}

	public final void setInt(int i, int i1) throws SQLException {
		stmt.setInt(i, i1);
	}

	public final void setLong(int i, long l) throws SQLException {
		stmt.setLong(i, l);
	}

	public final void setFloat(int i, float v) throws SQLException {
		stmt.setFloat(i, v);
	}

	public final void setDouble(int i, double v) throws SQLException {
		stmt.setDouble(i, v);
	}

	public final void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		stmt.setBigDecimal(i, bigDecimal);
	}

	public final void setString(int i, String s) throws SQLException {
		stmt.setString(i, s);
	}

	public final void setBytes(int i, byte[] bytes) throws SQLException {
		stmt.setBytes(i, bytes);
	}

	public final void setDate(int i, Date date) throws SQLException {
		stmt.setDate(i, date);
	}

	public final void setTime(int i, Time time) throws SQLException {
		stmt.setTime(i, time);
	}

	public final void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		stmt.setTimestamp(i, timestamp);
	}

	public final void setAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setAsciiStream(i, inputStream, i1);
	}

	public final void setUnicodeStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setUnicodeStream(i, inputStream, i1);
	}

	public final void setBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setBinaryStream(i, inputStream, i1);
	}

	public final void clearParameters() throws SQLException {
		stmt.clearParameters();
	}

	public final void setObject(int i, Object o, int i1) throws SQLException {
		stmt.setObject(i, o, i1);
	}

	public final void setObject(int i, Object o) throws SQLException {
		stmt.setObject(i, o);
	}

	public final void setObject(int i, Object o, int i1, int i2) throws SQLException {
		stmt.setObject(i, o, i1, i2);
	}

	public final void setCharacterStream(int i, Reader reader, int i1) throws SQLException {
		stmt.setCharacterStream(i, reader, i1);
	}

	public final void setRef(int i, Ref ref) throws SQLException {
		stmt.setRef(i, ref);
	}

	public final void setBlob(int i, Blob blob) throws SQLException {
		stmt.setBlob(i, blob);
	}

	public final void setClob(int i, Clob clob) throws SQLException {
		stmt.setClob(i, clob);
	}

	public final void setArray(int i, Array array) throws SQLException {
		stmt.setArray(i, array);
	}

	public final ResultSetMetaData getMetaData() throws SQLException {
		return stmt.getMetaData();
	}

	public final void setDate(int i, Date date, Calendar calendar) throws SQLException {
		stmt.setDate(i, date, calendar);
	}

	public final void setTime(int i, Time time, Calendar calendar) throws SQLException {
		stmt.setTime(i, time, calendar);
	}

	public final void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
		stmt.setTimestamp(i, timestamp, calendar);
	}

	public final void setNull(int i, int i1, String s) throws SQLException {
		stmt.setNull(i, i1, s);
	}

	public final void setURL(int i, URL url) throws SQLException {
		stmt.setURL(i, url);
	}

	public final ParameterMetaData getParameterMetaData() throws SQLException {
		return stmt.getParameterMetaData();
	}

//////// from JDK 6, JDBC 4
	/*
	 public final void setRowId(int i, RowId rowId) throws SQLException {
		 stmt.setRowId(i, rowId);
	 }

	 public final void setNString(int i, String s) throws SQLException {
		 stmt.setNString(i, s);
	 }

	 public final void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
		 stmt.setNCharacterStream(i, reader, l);
	 }

	 public final void setNClob(int i, NClob nClob) throws SQLException {
		 stmt.setNClob(i, nClob);
	 }

	 public final void setClob(int i, Reader reader, long l) throws SQLException {
		 stmt.setClob(i, reader, l);
	 }

	 public final void setBlob(int i, InputStream inputStream, long l) throws SQLException {
		 stmt.setBlob(i, inputStream, l);
	 }

	 public final void setNClob(int i, Reader reader, long l) throws SQLException {
		 stmt.setNClob(i, reader, l);
	 }

	 public final void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
		 stmt.setSQLXML(i, sqlxml);
	 }

	 public final void setAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
		 stmt.setAsciiStream(i, inputStream, l);
	 }

	 public final void setBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
		 stmt.setBinaryStream(i, inputStream);
	 }

	 public final void setCharacterStream(int i, Reader reader, long l) throws SQLException {
		 stmt.setCharacterStream(i, reader, l);
	 }

	 public final void setAsciiStream(int i, InputStream inputStream) throws SQLException {
		 stmt.setAsciiStream(i, inputStream);
	 }

	 public final void setBinaryStream(int i, InputStream inputStream) throws SQLException {
		 stmt.setBinaryStream(i, inputStream);
	 }

	 public final void setCharacterStream(int i, Reader reader) throws SQLException {
		 stmt.setCharacterStream(i, reader);
	 }

	 public final void setNCharacterStream(int i, Reader reader) throws SQLException {
		 stmt.setNCharacterStream(i, reader);
	 }

	 public final void setClob(int i, Reader reader) throws SQLException {
		 stmt.setClob(i, reader);
	 }

	 public final void setBlob(int i, InputStream inputStream) throws SQLException {
		 stmt.setBlob(i, inputStream);
	 }

	 public final void setNClob(int i, Reader reader) throws SQLException {
		 stmt.setNClob(i, reader);
	 }
 */
}
