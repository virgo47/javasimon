package org.javasimon.examples.jdbc;

import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonManager;
import org.javasimon.Split;

import java.sql.*;
import java.util.Random;

/**
 * A very simple example of Simon JDBC Proxy Driver usage.
 * <p>
 * The example exploits embeded H2 Database Engine. First, a in-memory database <i>db1</i> is created.
 * Then, "simon" connection to same <i>db1</i> is created and two sql commands (insert and
 * select) are executed. Finally, hierarchy of Simons is printed.
 * </p>
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 14.8.2008 23:38:46
 * @since 1.0
 */
public class Simple {

	private final Random rand = new Random();

	/**
	 * Creates in-memory database <i>db1</i> and table <i>foo</i> through original H2 driver.
	 *
	 * @throws SQLException if something goes wrong
	 */
	protected void setUp() throws SQLException {
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

	/**
	 * Executes prepared insert into table <i>foo</i>.
	 *
	 * @param c connection to DB
	 * @throws SQLException if something goes wrong
	 */
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

	/**
	 * Executes select all records from table <i>foo</i>.
	 *
	 * @param c connection to DB
	 * @return record count
	 * @throws SQLException if something goes wrong
	 */
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
	 * Entry point of the example application.
	 *
	 * @param args command line arguments
	 * @throws Exception sometimes bad things can happen
	 */
	public static void main(String[] args) throws Exception {
		Split main = SimonManager.getStopwatch("org.javasimon.examples.jdbc.main").start();
		Class.forName("org.h2.Driver");
		Class.forName("org.javasimon.jdbc.Driver");

		Simple s = new Simple();
		s.setUp();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:simon:h2:mem:db1");

			Split ops = SimonManager.getStopwatch("org.javasimon.examples.jdbc.sql").start();
			s.doInsert(conn);
			s.doSelect(conn);
			ops.stop();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		main.stop();

		System.out.println("Simon monitor hierarchy:\n" + SimonUtils.simonTreeString(SimonManager.getRootSimon()));
	}
}
