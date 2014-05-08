package org.javasimon.jdbc4;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.javasimon.Split;

/**
 * Simon JDBC proxy prepared statement implementation class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @see java.sql.PreparedStatement
 * @since 2.4
 */
@SuppressWarnings("deprecation")
public class SimonPreparedStatement extends SimonStatement implements PreparedStatement {
	/**
	 * SQL string.
	 */
	protected String sql;

	private PreparedStatement stmt;

	/**
	 * Class constructor, initializes Simons (lifespan, active) related to statement.
	 *
	 * @param conn database connection (simon impl.)
	 * @param stmt real prepared statement
	 * @param sql sql command
	 * @param prefix hierarchy prefix for statement Simons
	 */
	SimonPreparedStatement(Connection conn, PreparedStatement stmt, String sql, String prefix) {
		super(conn, stmt, prefix);

		this.stmt = stmt;
		this.sql = sql;
	}

	/**
	 * Called before each prepared SQL command execution. Prepares (obtains and starts)
	 * {@link org.javasimon.Stopwatch Stopwatch Simon} for measure SQL operation.
	 *
	 * @return Simon stopwatch object or null if sql is null or empty
	 */
	private Split prepare() {
		if (sql != null && !sql.equals("")) {
			sqlNormalizer = new SqlNormalizer(sql);
			sqlCmdLabel = prefix + ".sql." + sqlNormalizer.getType();
			return startSplit();
		} else {
			return null;
		}
	}

	/**
	 * Measure and execute prepared SQL operation.
	 *
	 * @return database rows and columns
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final ResultSet executeQuery() throws SQLException {
		Split split = prepare();
		try {
			return new SimonResultSet(stmt.executeQuery(), this, prefix, split.getStopwatch().getName());
		} finally {
			finish(split);
		}
	}

	/**
	 * Measure and execute prepared SQL operation.
	 *
	 * @return count of updated rows
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final int executeUpdate() throws SQLException {
		Split split = prepare();
		try {
			return stmt.executeUpdate();
		} finally {
			finish(split);
		}
	}

	/**
	 * Measure and execute prepared SQL operation.
	 *
	 * @return <code>true</code> if the first result is a <code>ResultSet</code> object;
	 *         <code>false</code> if it is an update count or there are no results
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final boolean execute() throws SQLException {
		Split split = prepare();
		try {
			return stmt.execute();
		} finally {
			finish(split);
		}
	}

	/**
	 * Adds prepared SQL command into batch list of sql and also into real batch.
	 *
	 * @throws java.sql.SQLException if real calls fails
	 */
	@Override
	public final void addBatch() throws SQLException {
		batchSql.add(sql);

		stmt.addBatch();
	}

	//// NOT MONITORED

	@Override
	public final void setNull(int i, int i1) throws SQLException {
		stmt.setNull(i, i1);
	}

	@Override
	public final void setBoolean(int i, boolean b) throws SQLException {
		stmt.setBoolean(i, b);
	}

	@Override
	public final void setByte(int i, byte b) throws SQLException {
		stmt.setByte(i, b);
	}

	@Override
	public final void setShort(int i, short i1) throws SQLException {
		stmt.setShort(i, i1);
	}

	@Override
	public final void setInt(int i, int i1) throws SQLException {
		stmt.setInt(i, i1);
	}

	@Override
	public final void setLong(int i, long l) throws SQLException {
		stmt.setLong(i, l);
	}

	@Override
	public final void setFloat(int i, float v) throws SQLException {
		stmt.setFloat(i, v);
	}

	@Override
	public final void setDouble(int i, double v) throws SQLException {
		stmt.setDouble(i, v);
	}

	@Override
	public final void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		stmt.setBigDecimal(i, bigDecimal);
	}

	@Override
	public final void setString(int i, String s) throws SQLException {
		stmt.setString(i, s);
	}

	@Override
	public final void setBytes(int i, byte[] bytes) throws SQLException {
		stmt.setBytes(i, bytes);
	}

	@Override
	public final void setDate(int i, Date date) throws SQLException {
		stmt.setDate(i, date);
	}

	@Override
	public final void setTime(int i, Time time) throws SQLException {
		stmt.setTime(i, time);
	}

	@Override
	public final void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		stmt.setTimestamp(i, timestamp);
	}

	@Override
	public final void setAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setAsciiStream(i, inputStream, i1);
	}

	@Deprecated
	@Override
	public final void setUnicodeStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setUnicodeStream(i, inputStream, i1);
	}

	@Override
	public final void setBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
		stmt.setBinaryStream(i, inputStream, i1);
	}

	@Override
	public final void clearParameters() throws SQLException {
		stmt.clearParameters();
	}

	@Override
	public final void setObject(int i, Object o, int i1) throws SQLException {
		stmt.setObject(i, o, i1);
	}

	@Override
	public final void setObject(int i, Object o) throws SQLException {
		stmt.setObject(i, o);
	}

	@Override
	public final void setObject(int i, Object o, int i1, int i2) throws SQLException {
		stmt.setObject(i, o, i1, i2);
	}

	@Override
	public final void setCharacterStream(int i, Reader reader, int i1) throws SQLException {
		stmt.setCharacterStream(i, reader, i1);
	}

	@Override
	public final void setRef(int i, Ref ref) throws SQLException {
		stmt.setRef(i, ref);
	}

	@Override
	public final void setBlob(int i, Blob blob) throws SQLException {
		stmt.setBlob(i, blob);
	}

	@Override
	public final void setClob(int i, Clob clob) throws SQLException {
		stmt.setClob(i, clob);
	}

	@Override
	public final void setArray(int i, Array array) throws SQLException {
		stmt.setArray(i, array);
	}

	@Override
	public final ResultSetMetaData getMetaData() throws SQLException {
		return stmt.getMetaData();
	}

	public final void setDate(int i, Date date, Calendar calendar) throws SQLException {
		stmt.setDate(i, date, calendar);
	}

	@Override
	public final void setTime(int i, Time time, Calendar calendar) throws SQLException {
		stmt.setTime(i, time, calendar);
	}

	@Override
	public final void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
		stmt.setTimestamp(i, timestamp, calendar);
	}

	@Override
	public final void setNull(int i, int i1, String s) throws SQLException {
		stmt.setNull(i, i1, s);
	}

	@Override
	public final void setURL(int i, URL url) throws SQLException {
		stmt.setURL(i, url);
	}

	@Override
	public final ParameterMetaData getParameterMetaData() throws SQLException {
		return stmt.getParameterMetaData();
	}

	@Override
	public final void setRowId(int i, RowId rowId) throws SQLException {
		stmt.setRowId(i, rowId);
	}

	@Override
	public final void setNString(int i, String s) throws SQLException {
		stmt.setNString(i, s);
	}

	@Override
	public final void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
		stmt.setNCharacterStream(i, reader, l);
	}

	@Override
	public final void setNClob(int i, NClob nClob) throws SQLException {
		stmt.setNClob(i, nClob);
	}

	@Override
	public final void setClob(int i, Reader reader, long l) throws SQLException {
		stmt.setClob(i, reader, l);
	}

	@Override
	public final void setBlob(int i, InputStream inputStream, long l) throws SQLException {
		stmt.setBlob(i, inputStream, l);
	}

	@Override
	public final void setNClob(int i, Reader reader, long l) throws SQLException {
		stmt.setNClob(i, reader, l);
	}

	@Override
	public final void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
		stmt.setSQLXML(i, sqlxml);
	}

	@Override
	public final void setAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
		stmt.setAsciiStream(i, inputStream, l);
	}

	@Override
	public final void setBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
		stmt.setBinaryStream(i, inputStream);
	}

	@Override
	public final void setCharacterStream(int i, Reader reader, long l) throws SQLException {
		stmt.setCharacterStream(i, reader, l);
	}

	@Override
	public final void setAsciiStream(int i, InputStream inputStream) throws SQLException {
		stmt.setAsciiStream(i, inputStream);
	}

	@Override
	public final void setBinaryStream(int i, InputStream inputStream) throws SQLException {
		stmt.setBinaryStream(i, inputStream);
	}

	@Override
	public final void setCharacterStream(int i, Reader reader) throws SQLException {
		stmt.setCharacterStream(i, reader);
	}

	@Override
	public final void setNCharacterStream(int i, Reader reader) throws SQLException {
		stmt.setNCharacterStream(i, reader);
	}

	@Override
	public final void setClob(int i, Reader reader) throws SQLException {
		stmt.setClob(i, reader);
	}

	@Override
	public final void setBlob(int i, InputStream inputStream) throws SQLException {
		stmt.setBlob(i, inputStream);
	}

	@Override
	public final void setNClob(int i, Reader reader) throws SQLException {
		stmt.setNClob(i, reader);
	}

}
