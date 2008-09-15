package org.javasimon.examples.jdbc;

import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonManager;

import java.sql.*;
import java.util.Random;

/**
 * Trieda Simple.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 14.8.2008 23:38:46
 * @since 1.0
 */
public class Simple {

	private final Random rand = new Random();

	protected final void setUp() throws SQLException {
		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1");
			Statement stmt = c.createStatement();
			stmt.execute("create table foo (id number(6), dsc varchar2(256))");
			stmt.close();
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}

	protected final void doInsert(Connection c) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement("insert into foo values (?, ?)  ");
			stmt.setInt(1, rand.nextInt(99999));
			stmt.setString(2, "This is a text");
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	protected final int doSelect(Connection c) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			rs = stmt.executeQuery("select * from foo");
			int count = 0;
			while (rs.next()) {
				count++;
			}
			return count;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Entry point of the demo application.
	 *
	 * @param args command line arguments
	 * @throws Exception sometimes bad things can happen
	 */
	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");
		Class.forName("org.javasimon.jdbc.Driver");

		Simple s = new Simple();
		s.setUp();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:simon:h2:mem:db1");

			s.doInsert(conn);
			s.doSelect(conn);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		System.out.println("Simon monitor hierarchy:\n" + SimonUtils.simonTreeString(SimonManager.getRootSimon()));
	}
}
