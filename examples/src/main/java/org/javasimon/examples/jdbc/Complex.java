package org.javasimon.examples.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Random;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.SimonPattern;
import org.javasimon.StopwatchSample;
import org.javasimon.jdbc4.SimonConnectionConfiguration;
import org.javasimon.utils.SimonUtils;

/**
 * Complicated example.
 *
 * @author Radovan Sninsky
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
//		System.out.println("Simon monitor hierarchy:\n" + SimonUtils.simonTreeString(SimonManager.getRootSimon()));
		System.out.println(SimonUtils.simonTreeString(SimonManager.getSimon(SimonConnectionConfiguration.DEFAULT_PREFIX)));
		Collection<Simon> simons = SimonManager.getSimons(new SimonPattern(SimonConnectionConfiguration.DEFAULT_PREFIX + ".sql.select.*"));
		System.out.println("simons = " + simons);

//		Simon jdbcSimon = SimonManager.getSimon(org.javasimon.jdbc4.Driver.DEFAULT_PREFIX);
//		System.out.println(printJdbcConnectionInfo(jdbcSimon));
//		System.out.println(printJdbcStatementInfo(jdbcSimon));
	}

	/**
	 * Returns summary information about monitored JDBC connections as a human readable string -
	 * main JDBC Simon has to be provided.
	 *
	 * @param jdbcSimon top JDBC Simon (typically prefix of the JDBC proxy driver)
	 * @return information/stats about JDBC connections
	 * @see org.javasimon.jdbc4.SimonConnectionConfiguration#DEFAULT_PREFIX
	 */
	private String printJdbcConnectionInfo(Simon jdbcSimon) {
		if (SimonManager.getSimon(jdbcSimon.getName() + ".conn") != null) {
			StopwatchSample sws = SimonManager.getStopwatch(jdbcSimon.getName() + ".conn").sample();
			Counter cc = SimonManager.getCounter(jdbcSimon.getName() + ".conn.commits");
			Counter cr = SimonManager.getCounter(jdbcSimon.getName() + ".conn.rollbacks");
			return "Connection info:\n  act: " + sws.getActive() + "\n  max act: " + sws.getMaxActive() + '\n' + "  max act ts: " + SimonUtils.presentTimestamp(sws.getMaxActiveTimestamp()) +
				"\n  opn: " + sws.getCounter() + '\n' + "  cls: " + (sws.getCounter() - sws.getActive()) + '\n' + "  min: " + SimonUtils.presentNanoTime(sws.getMin()) + ", avg: " + SimonUtils.presentNanoTime((long) sws.getMean()) + ", max: " + SimonUtils.presentNanoTime(sws.getMax()) + '\n' + "  max ts: " + SimonUtils.presentTimestamp(sws.getMaxTimestamp()) + '\n' + "  comm: " + (cc != null ? cc.sample().getCounter() : 0) + '\n' + "  roll: " + (cr != null ? cr.sample().getCounter() : 0) + '\n';
		}
		return null;
	}

	/**
	 * Returns summary information about monitored JDBC statements as a human readable string -
	 * main JDBC Simon has to be provided.
	 *
	 * @param jdbcSimon top JDBC Simon (typically prefix of the JDBC proxy driver)
	 * @return information/stats about JDBC statements
	 * @see org.javasimon.jdbc4.SimonConnectionConfiguration#DEFAULT_PREFIX
	 */
	private String printJdbcStatementInfo(Simon jdbcSimon) {
		if (SimonManager.getSimon(jdbcSimon.getName() + ".stmt") != null) {
			StopwatchSample sws = SimonManager.getStopwatch(jdbcSimon.getName() + ".stmt").sample();

			return "Statement info:\n  act: " + sws.getActive() + "\n  max act: " + sws.getMaxActive() +
				"\n  max act ts: " + SimonUtils.presentTimestamp(sws.getMaxActiveTimestamp()) +
				"\n  opn: " + sws.getCounter() + "\n  cls: " + (sws.getCounter() - sws.getActive()) +
				"\n  min: " + SimonUtils.presentNanoTime(sws.getMin()) + ", avg: " + SimonUtils.presentNanoTime((long) sws.getMean()) +
				", max: " + SimonUtils.presentNanoTime(sws.getMax()) + "\n  max ts: " + SimonUtils.presentTimestamp(sws.getMaxTimestamp()) + '\n';
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
		Class.forName("org.javasimon.jdbc4.Driver");

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
