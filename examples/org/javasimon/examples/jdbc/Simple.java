package org.javasimon.examples.jdbc;

import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonFactory;

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

	private static final Random rand = new Random();

	private static void setUp() throws SQLException {
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

	private static void insertRecord(Connection c) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement("insert into foo values (?, ?)");
			stmt.setInt(1, rand.nextInt(99999));
			stmt.setString(2, "This is a text");
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void selectRecord(Connection c) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.createStatement();
			rs = stmt.executeQuery("select * from foo");
			int count = 0;
			while (rs.next()) {
				count++;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");
		Class.forName("org.javasimon.jdbc.Driver");

		setUp();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:simon:h2:mem:db1");

			insertRecord(conn);

			selectRecord(conn);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		SimonUtils.printSimonTree(SimonFactory.getRootSimon());
	}
}
