package org.javasimon.examples.jmx;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.jmx.SimonMXBeanImpl;
import org.javasimon.utils.SimonUtils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.JMException;
import java.util.Random;
import java.sql.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Trieda SimpleMonitoredApp.
 *
 * @author Radovan Sninsky
 * @version $Revision $ $Date $
 * @created 11.2.2009 13:23:38
 * @since 2
 */
public class MonitoredApplication {

	private final Random rand = new Random();

	/**
	 * Creates in-memory database <i>db1</i> and table <i>foo</i> through original H2 driver.
	 *
	 * @throws java.sql.SQLException if something goes wrong
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
	 * @param c connection to db
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
	 * @param c connection to db
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

	private static SimonMXBeanImpl register() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			SimonMXBeanImpl simon = new SimonMXBeanImpl(SimonManager.manager());
			mbs.registerMBean(simon, name);
			System.out.println("SimonMXBean registerd under name: "+name);
			return simon;
		} catch (JMException e) {
			System.out.println("SimonMXBean registration failed!\n"+e);
		}
		return null;
	}

	private static void unregister() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
			}
			System.out.println("SimonMXBean was unregisterd");
		} catch (JMException e) {
			System.out.println("SimonMXBean unregistration failed!\n"+e);
		}
	}

	private static void waitForEnterPressed() {
		try {
			System.out.println("\nPress <Enter> to continue...");
			System.in.read();
		} catch (IOException e) { /* do nothing */ }
	}

	/**
	 * Entry point of the example application.
	 *
	 * @param args command line arguments
	 * @throws Exception sometimes bad things can happen
	 */
	public static void main(String[] args) throws Exception {
		Split main = SimonManager.getStopwatch("org.javasimon.examples.jmx.main").start();
		Class.forName("org.h2.Driver");
		Class.forName("org.javasimon.jdbc.Driver");

		MonitoredApplication s = new MonitoredApplication();
		s.setUp();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:simon:h2:mem:db1");

			Split ops = SimonManager.getStopwatch("org.javasimon.examples.jmx.sql").start();
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

		register();
		waitForEnterPressed();
		unregister();
	}
}
