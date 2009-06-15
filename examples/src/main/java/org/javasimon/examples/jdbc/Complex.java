package org.javasimon.examples.jdbc;

import org.javasimon.utils.SimonUtils;
import org.javasimon.*;

import java.util.*;
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

	/**
	 * Creates in-memory database <i>db1</i> and table <i>foo</i> through original H2 driver.
	 *
	 * @throws SQLException if something goes wrong
	 */
	protected final void setUp() throws SQLException {
		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1");
			Statement stmt = c.createStatement();
			stmt.execute("create table foo (id number(6), dsc varchar2(256))");
			stmt.execute("create alias foo_proc for \"org.javasimon.examples.jdbc.h2.StoredProcedures.fooProc\"");
			stmt.close();
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}

	private void doInsertSimple(Connection c) throws SQLException {
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(" insert   into foo values (" + rand.nextInt(99999) + ", 'This is an another text')  ");
		} finally {
			if (stmt != null) {
				// this close is intentionally commented for monitoring purposes
				//stmt.close();
			}
		}
	}

	private void doCal1(Connection c) throws SQLException {
		CallableStatement stmt = null;
		try {
			stmt = c.prepareCall("{call foo_proc(" + rand.nextInt(99999) + ", 'This text is inserted from stored procedure')}");
			stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private void printMonitoringInfo() {
		System.out.println("Simon monitor hierarchy:\n" + SimonUtils.simonTreeString(SimonManager.getRootSimon()));

		Simon jdbcSimon = SimonManager.getSimon(org.javasimon.jdbc.Driver.DEFAULT_PREFIX);
		System.out.println(printJdbcConnectionInfo(jdbcSimon));
		System.out.println(printJdbcStatementInfo(jdbcSimon));
	}

	/**
	 * Returns summary information about monitored JDBC connections as a human readable string -
	 * main JDBC Simon has to be provided.
	 *
	 * @param jdbcSimon top JDBC Simon (typically prefix of the JDBC proxy driver)
	 * @return information/stats about JDBC connections
	 * @see org.javasimon.jdbc.Driver#DEFAULT_PREFIX
	 */
	private String printJdbcConnectionInfo(Simon jdbcSimon) {
		if (SimonManager.getSimon(jdbcSimon.getName() + ".conn") != null) {
			StopwatchSample sws = (StopwatchSample) SimonManager.getStopwatch(jdbcSimon.getName() + ".conn").sample();
			Counter cc = SimonManager.getCounter(jdbcSimon.getName() + ".conn.commits");
			Counter cr = SimonManager.getCounter(jdbcSimon.getName() + ".conn.rollbacks");
			StringBuilder sb = new StringBuilder(512).append("Connection info:").append('\n')
				.append("  act: ").append(sws.getActive()).append('\n')
				.append("  max act: ").append(sws.getMaxActive()).append('\n')
				.append("  max act ts: ").append(new java.util.Date(sws.getMaxActiveTimestamp())).append('\n')
				.append("  opn: ").append(sws.getCounter()).append('\n')
				.append("  cls: ").append(sws.getCounter() - sws.getActive()).append('\n')
				.append("  min: ").append(SimonUtils.presentNanoTime(sws.getMin()))
				.append(", avg: ").append(SimonUtils.presentNanoTime(sws.getTotal() / sws.getCounter()))
				.append(", max: ").append(SimonUtils.presentNanoTime(sws.getMax())).append('\n')
				.append("  max ts: ").append(new java.util.Date(sws.getMaxTimestamp())).append('\n')
				.append("  comm: ").append(cc != null ? ((CounterSample) cc.sample()).getCounter() : 0).append('\n')
				.append("  roll: ").append(cr != null ? ((CounterSample) cr.sample()).getCounter() : 0)
				.append('\n');

			return sb.toString();
		}
		return null;
	}

	/**
	 * Returns summary information about monitored JDBC statements as a human readable string -
	 * main JDBC Simon has to be provided.
	 *
	 * @param jdbcSimon top JDBC Simon (typically prefix of the JDBC proxy driver)
	 * @return information/stats about JDBC statements
	 * @see org.javasimon.jdbc.Driver#DEFAULT_PREFIX
	 */
	private String printJdbcStatementInfo(Simon jdbcSimon) {
		if (SimonManager.getSimon(jdbcSimon.getName() + ".stmt") != null) {
			StopwatchSample sws = (StopwatchSample) SimonManager.getStopwatch(jdbcSimon.getName() + ".stmt").sample();
			StringBuilder sb = new StringBuilder(512).append("Statement info:").append('\n')
				.append("  act: ").append(sws.getActive()).append('\n')
				.append("  max act: ").append(sws.getMaxActive()).append('\n')
				.append("  max act ts: ").append(new java.util.Date(sws.getMaxActiveTimestamp())).append('\n')
				.append("  opn: ").append(sws.getCounter()).append('\n')
				.append("  cls: ").append(sws.getCounter() - sws.getActive()).append('\n')
				.append("  min: ").append(SimonUtils.presentNanoTime(sws.getMin()))
				.append(", avg: ").append(SimonUtils.presentNanoTime(sws.getTotal() / sws.getCounter()))
				.append(", max: ").append(SimonUtils.presentNanoTime(sws.getMax())).append('\n')
				.append("  max ts: ").append(new java.util.Date(sws.getMaxTimestamp()))
				.append('\n');

			return sb.toString();
		}
		return null;
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

		Complex complex = new Complex();
		complex.setUp();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:simon:h2:mem:db1");
			conn.setAutoCommit(false);

			complex.doInsert(conn);
			complex.doInsertSimple(conn);
			conn.commit();

			complex.doCal1(conn);
			conn.commit();

			complex.doSelect(conn);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		complex.printMonitoringInfo();
	}
}
