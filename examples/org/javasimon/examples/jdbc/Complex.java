package org.javasimon.examples.jdbc;

import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.Counter;

import java.util.Random;
import java.sql.*;

/**
 * Trieda Complex.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 16.8.2008 21:10:39
 * @since 1.0
 */
public final class Complex extends Simple {

	private Random rand = new Random();

	private void doInsertSimple(Connection c) throws SQLException {
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(" insert   into foo values (" + rand.nextInt(99999) + ", 'This is an another text')  ");
		} finally {
			if (stmt != null) {
				// this is intentionally commented for monitoring purposes
				//stmt.close();
			}
		}
	}

	private void doCal1(Connection c) throws SQLException {
		CallableStatement stmt = null;
		try {
			stmt = c.prepareCall("{call foo_ins_proc(" + rand.nextInt(99999) + ", 'This text is inserted from stored procedure')}");
			stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private void printMonitoringInfo() {
		System.out.println("Simon monitor hierarchy:\n" + SimonUtils.simonTreeString(SimonManager.getRootSimon()));

		Stopwatch s = SimonManager.getStopwatch("org.javasimon.jdbc.conn");
		Counter c = SimonManager.getCounter("org.javasimon.jdbc.conn.active");
		Counter cc = SimonManager.getCounter("org.javasimon.jdbc.conn.commit");
		Counter cr = SimonManager.getCounter("org.javasimon.jdbc.conn.rollback");
		System.out.println("\nConnection info:");
		System.out.println("  active: " + c.getCounter());
		System.out.println("  max active: " + c.getMax());
		System.out.println("  opened: " + s.getCounter());
		System.out.println("  closed: " + (s.getCounter() - c.getCounter()));
		System.out.println("  min: " + SimonUtils.presentNanoTime(s.getMin()));
		System.out.println("  max: " + SimonUtils.presentNanoTime(s.getMax()));
		System.out.println("  avg: n/a");
		System.out.println("  commints: " + cc.getCounter());
		System.out.println("  rollbacks: " + cr.getCounter());

		s = SimonManager.getStopwatch("org.javasimon.jdbc.stmt");
		c = SimonManager.getCounter("org.javasimon.jdbc.stmt.active");
		System.out.println("\nStatement info:");
		System.out.println("  active: " + c.getCounter());
		System.out.println("  max active: " + c.getMax());
		System.out.println("  opened: " + s.getCounter());
		System.out.println("  closed: " + (s.getCounter() - c.getCounter()));
		System.out.println("  min: " + SimonUtils.presentNanoTime(s.getMin()));
		System.out.println("  max: " + SimonUtils.presentNanoTime(s.getMax()));
		System.out.println("  avg: n/a");
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

		Complex s = new Complex();
		s.setUp();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:simon:h2:mem:db1");
			conn.setAutoCommit(false);

			s.doInsert(conn);
			s.doInsertSimple(conn);
			conn.commit();

			s.doSelect(conn);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		s.printMonitoringInfo();
	}
}
