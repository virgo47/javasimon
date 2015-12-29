package org.javasimon.examples.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.utils.SimonUtils;

/**
 * Example runs in-memory database with some inserts and selects, printing the Simon tree
 * and 5 last inserted events every 10 seconds. Inserted events are random splits (0-3 s)
 * on a single stopwatch.
 */
public class JdbcExample {

	public static final int PAUSE_BETWEEN_PRINT_MS = 10_000;

	public static void main(String[] args) throws Exception {
		Class.forName("org.javasimon.jdbc4.Driver");
		// JDBC URL:
		// 1) it has :simon: between jdbc and real driver name
		// 2) it specifies the simon namespace where to put JDBC simons (any.sql instead of default)
		try (Connection connection = DriverManager.getConnection("jdbc:simon:h2:mem:;simon_prefix=any.sql")) {
			prepareTable(connection);
			runDemo(connection);
		}
	}

	private static void runDemo(final Connection connection) throws SQLException, InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					startInsertSelectThread(connection);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}).start();

		while (true) {
			printResults(connection);
			Thread.sleep(PAUSE_BETWEEN_PRINT_MS);
		}
	}

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static void printResults(Connection connection) throws SQLException {
		System.out.println(SimonUtils.simonTreeString(SimonManager.getRootSimon()));
		System.out.println("Last 5 events:");
		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM events ORDER BY id DESC LIMIT 5");
			while (resultSet.next()) {
				System.out.printf("%6d %-20s %-20s %s\n",
					resultSet.getInt("id"),
					resultSet.getString("stopwatch"),
					SDF.format(resultSet.getTimestamp("start")),
					SimonUtils.presentNanoTime(resultSet.getLong("nanos"))
				);
			}
		}
		System.out.println();
	}

	private static void startInsertSelectThread(Connection connection) throws SQLException {
		while (true) {
			Split split = SimonManager.getStopwatch("any.stopwatch").start();
			try {
				Thread.sleep((long) (Math.random() * 3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			split.stop();
			try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO events (stopwatch, start, nanos) VALUES (?,?,?)")) {
				statement.setString(1, split.getStopwatch().getName());
				statement.setTimestamp(2, new Timestamp(split.getStartMillis()));
				statement.setLong(3, split.runningFor());
				statement.execute();
			}

			// now some select
			try (Statement statement = connection.createStatement()) {
				ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM events");
				// but we ignore the result :-)
			}
		}
	}

	private static void prepareTable(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.execute("DROP TABLE IF EXISTS events");
			statement.execute(
				"CREATE TABLE events (" +
					" id INT IDENTITY," +
					" stopwatch VARCHAR(100)," +
					" start TIMESTAMP," +
					" nanos BIGINT)");
		}
	}
}
