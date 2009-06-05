package org.javasimon.testapp.model;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * DAO (Data Access Object) for Tuple data object.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 18.3.2009 11:48:49
 * @since 1.0
 */
public class TupleDAO {
	private Connection conn;
	private String table;

	/**
	 * DAO constructor with SQL connection and table name.
	 *
	 * @param connection SQL connection used to acces the table
	 * @param tableName name of the SQL table
	 */
	public TupleDAO(Connection connection, String tableName) {
		this.conn = connection;
		this.table = tableName;
	}

	/**
	 * Inserts tuple into the table.
	 *
	 * @param t tuple
	 * @throws SQLException thrown if SQL operation goes wrong
	 */
	public void save(Tuple t) throws SQLException {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(
				"insert into " + table + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			);

			populateStatement(stmt, t);

			stmt.executeUpdate();
			stmt.getConnection().commit();
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * Inserts list of tuples into the table.
	 *
	 * @param list list of tuples
	 * @throws SQLException thrown if SQL operation goes wrong
	 */
	public void save(List<Tuple> list) throws SQLException {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(
				"insert into " + table + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			);

			for (Tuple t : list) {
				populateStatement(stmt, t);
				stmt.addBatch();
			}

			stmt.executeBatch();
			stmt.getConnection().commit();
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * Updates tuple in the table - it just sets one string to a fixed value in the tuple.
	 *
	 * @param unique1 unique key
	 * @throws SQLException thrown if SQL operation goes wrong
	 */
	public int update(int unique1) throws SQLException {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("update " + table + " set string4 = 'UPDATED' where unique1 = ?");
			stmt.setInt(1, unique1);
			int res = stmt.executeUpdate();
			stmt.getConnection().commit();
			return res;
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * Deletes the tuple by id.
	 *
	 * @param unique1 unique key
	 * @return should return 1 if the tuple was deleted, otherwise 0
	 * @throws SQLException thrown if SQL operation goes wrong
	 */
	public int deleteByUnique1(int unique1) throws SQLException {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("delete from " + table + " where unique1 = ?");
			stmt.setInt(1, unique1);
			int res = stmt.executeUpdate();
			stmt.getConnection().commit();
			return res;
		} finally {
			closeStatement(stmt);
		}
	}

	private void populateStatement(PreparedStatement s, Tuple t) throws SQLException {
		int j = 0;
		s.setInt(++j, t.getUnique1());
		s.setInt(++j, t.getIdx());
		s.setInt(++j, t.getOne());
		s.setInt(++j, t.getTen());
		s.setInt(++j, t.getTwenty());
		s.setInt(++j, t.getTwentyFive());
		s.setInt(++j, t.getFifty());
		s.setInt(++j, t.getEvenOnePercent());
		s.setInt(++j, t.getOddOnePercent());
		s.setString(++j, t.getStringU1());
		s.setString(++j, t.getStringU2());
		s.setString(++j, t.getString4());
		s.setDate(++j, new java.sql.Date(t.getCreated()));
	}

	private void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
