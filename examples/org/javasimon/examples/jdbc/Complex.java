package org.javasimon.examples.jdbc;

import org.javasimon.utils.SimonUtils;
import org.javasimon.SimonFactory;
import org.javasimon.Stopwatch;
import org.javasimon.Counter;

import java.util.Random;
import java.sql.*;

/**
 * Trieda Complex.
 *
 * @author <a href="mailto:radovan.sninsky@siemens.com">Radovan Sninsky</a>
 * @version $ $
 * @created 16.8.2008 21:10:39
 * @since 1.0
 */
public class Complex extends Simple {

	private Random rand = new Random();

	public Complex() {
	}

	private void doInsertSimple(Connection c) throws SQLException {
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(" insert   into foo values ("+rand.nextInt(99999)+", 'This is an another text')  ");
		} finally {
			if (stmt != null) {
				// this is intentionally commented for monitoring purposes
				//stmt.close();
			}
		}
	}

	private void printMonitoringInfo() {
		System.out.println("Simon monitor hierarchy:");
		SimonUtils.printSimonTree(SimonFactory.getRootSimon());

		Stopwatch s = SimonFactory.getStopwatch("org.javasimon.jdbc.conn");
		Counter c = SimonFactory.getCounter("org.javasimon.jdbc.conn.active");
		Counter cc = SimonFactory.getCounter("org.javasimon.jdbc.conn.commit");
		Counter cr = SimonFactory.getCounter("org.javasimon.jdbc.conn.rollback");
		System.out.println("\nConnection info:");
		System.out.println("  active: "+c.getCounter());
		System.out.println("  max active: "+c.getMax());
		System.out.println("  opened: "+s.getCounter());
		System.out.println("  closed: "+(s.getCounter()-c.getCounter()));
		System.out.println("  min: "+SimonUtils.presentNanoTime(s.getMin()));
		System.out.println("  max: "+SimonUtils.presentNanoTime(s.getMax()));
		System.out.println("  avg: n/a");
		System.out.println("  commints: "+cc.getCounter());
		System.out.println("  rollbacks: "+cr.getCounter());

		s = SimonFactory.getStopwatch("org.javasimon.jdbc.stmt");
		c = SimonFactory.getCounter("org.javasimon.jdbc.stmt.active");
		System.out.println("\nStatement info:");
		System.out.println("  active: "+c.getCounter());
		System.out.println("  max active: "+c.getMax());
		System.out.println("  opened: "+s.getCounter());
		System.out.println("  closed: "+(s.getCounter()-c.getCounter()));
		System.out.println("  min: "+SimonUtils.presentNanoTime(s.getMin()));
		System.out.println("  max: "+SimonUtils.presentNanoTime(s.getMax()));
		System.out.println("  avg: n/a");
	}

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
