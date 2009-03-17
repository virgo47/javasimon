package org.javasimon.jmx;

/**
 * Interface of JDBC management bean (MXBean).
 * <p>
 * JDBC MXBean provides management operations to monitoring and logging JDBC driver
 * and operations to retrieve gathered statistics from hierarchy of javasimons. Monitoring
 * and logging JDBC driver by javasimons is possible only if javasimon JDBC driver is used
 * over orginal JDBC driver (eg. Oracle, PostgreSQL, MySQL etc.). For more details how to do
 * that, please look at {@link org.javasimon.jdbc.Driver} a package {@link org.javasimon.jdbc}
 * javadoc.
 *
 * @author Radovan Sninsky
 * @version $Revision: $ $Date: $
 * @created 3.10.2008 15:46:36
 * @since 2
 */
public interface JdbcMXBean {

	/**
	 * Identifier of build-in format used to format JDBC log events, human redable format.
	 */
	static final String HUMAN_FORMAT = "human";

	/**
	 * Identifier of build-in format used to format JDBC log events, CSV format.
	 */
	static final String CSV_FORMAT = "csv";

	/**
	 * Enables monitoring (gathering statistics) by default JDBC javasimon hierarchy
	 * ({@code org.javasimon.jdbc}). Works if SimonManager is enabled, look
	 * {@link org.javasimon.SimonManager#isEnabled()}.
	 */
	void enableMonitoring();

	/**
	 * Disables monitoring (gathering statistics) by default JDBC javasimon hierarchy
	 * ({@code org.javasimon.jdbc}). If SimonManager is already disabled has no effect,
	 * look {@link org.javasimon.SimonManager#disable()}.
	 */
	void disableMonitoring();

	/**
	 * Returns state of monitoring JDBC by default javasimon hierarchy.
	 *
	 * @return {@code true} if default JDBC javasimon hierarchy root is enabled,
	 * itherwise {@code false}  
	 */
	boolean isMonitoringEnabled();

	/**
	 * Enables monitoring (gathering statistics) by custom JDBC javasimon hierarchy.
	 * Works if SimonManager is enabled, look
	 * {@link org.javasimon.SimonManager#isEnabled()}.
	 *
	 * @param prefix prefix of custom JDBC javasimon hierarchy
	 */
	void enableMonitoring(String prefix);

	/**
	 * Disables monitoring (gathering statistics) by custom JDBC javasimon hierarchy.
	 * If SimonManager is already disabled has no effect,
	 * look {@link org.javasimon.SimonManager#disable()}.
	 *
	 * @param prefix prefix of custom JDBC javasimon hierarchy
	 */
	void disableMonitoring(String prefix);

	/**
	 * Returns state of monitoring JDBC by custom javasimon hierarchy.
	 *
	 * @param prefix prefix of custom JDBC javasimon hierarchy
	 * @return {@code true} if default JDBC javasimon hierarchy root is enabled,
	 * itherwise {@code false}
	 */
	boolean isMonitoringEnabled(String prefix);

	/**
	 * Starts logging JDBC events from default driver to file.
	 *
	 * @param filename path to file on disk
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 */
	void logToFile(String filename, String format);

	/**
	 * Starts logging JDBC events from driver with prefix to file.
	 *
	 * @param filename path to file on disk
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 * @param prefix prefix of javasimon hierarchy for one jdbc driver, used to distinguish
	 * between more jdbc drivers
	 */
	void logToFile(String filename, String format, String prefix);

	/**
	 * Starts logging JDBC events from default driver to {@link java.util.logging.Logger}.
	 *
	 * @param logger name of JDK14 logger
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 */
	void logToLogger(String logger, String format);

	/**
	 * Starts logging JDBC events from driver with prefix to {@link java.util.logging.Logger}.
	 *
	 * @param logger name of JDK14 logger
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 * @param prefix prefix of javasimon hierarchy for one jdbc driver, used to distinguish
	 * between more jdbc drivers
	 */
	void logToLogger(String logger, String format, String prefix);

	/**
	 * Starts logging JDBC events from default driver to {@link java.util.logging.ConsoleHandler}
	 * ({@code System.out.err}).
	 *
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 */
	void logToConsole(String format);

	/**
	 * Starts logging JDBC events from driver with prefix to {@link java.util.logging.ConsoleHandler}
	 * ({@code System.out.err}).
	 *
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 * @param prefix prefix of javasimon hierarchy for one jdbc driver, used to distinguish
	 * between more jdbc drivers
	 */
	void logToConsole(String format, String prefix);

	/**
	 * Stops logging events from javasimon JDBC driver with default prefix.
	 *
	 * @see #logToFile(String, String)
	 * @see #logToLogger(String, String)  
	 * @see #logToConsole(String)
	 */
	void stopLogging();

	/**
	 * Stops logging events from javasimon JDBC driver with prefix.
	 *
	 * @param prefix prefix of javasimon hierarchy for one jdbc driver, used to distinguish
	 * between more jdbc drivers
	 * @see #logToFile(String, String)
	 * @see #logToLogger(String, String)
	 * @see #logToConsole(String)
	 */
	void stopLogging(String prefix);

	/**
	 * Retrieves summary data about jdbc connection objects.
	 *
	 * @return value object {@link org.javasimon.jmx.JdbcObjectInfo}
	 */
	JdbcObjectInfo getConnInfo();

	/**
	 * Retrieves summary data about jdbc statement objects. 
	 *
	 * @return value object {@link org.javasimon.jmx.JdbcObjectInfo}
	 */
	JdbcObjectInfo getStmtInfo();

	/**
	 * Retrieves summary data about jdbc result set objects. 
	 *
	 * @return value object {@link org.javasimon.jmx.JdbcObjectInfo}
	 */
	JdbcObjectInfo getRsetInfo();

	/**
	 * Returns SQL command types ({@code select}, {@code insert}, {@code delete}, etc).
	 * If client application uses DDL commands like {@code create}, {@code alter} and other,
	 * those are included too. Special case are batch-es, they are referenced as {@code batch}.   
	 *
	 * @return list of string (references to used SQL command types in monitored (simoned) application)
	 * @see #getSqlCommandInfo(String)
	 * @see #getSqls(String)
	 */
	String[] getSqlCommands();

	/**
	 * Retrieves summary data about all executed SQL commands of eneterd type (for instance
	 * summary data of all executed selects).
	 *
	 * @param cmd SQL command type
	 * @return populated object {@link org.javasimon.jmx.StopwatchSample}, or {@code null} if
	 * entered sql command type has no associated javasimon (it means, no sql of this type was
	 * executed yet, for instance no update was executed yet)
	 * @see #getSqlCommands() 
	 */
	StopwatchSample getSqlCommandInfo(String cmd);

	/**
	 * Returns hashs of different SQL commands of one type ({@code select}, {@code insert},
	 * {@code delete}, etc). Each hash represent one sql command with (and its many executions).
	 *
	 * @param cmd cmd SQL command type ({@code select}, {@code insert}, {@code delete}, etc)
	 * @return list of string (hash references to used SQL command in monitored (simoned) application)
	 * @see #getSqlCommands()
	 * @see #getSqlInfo(String)
	 */
	String[] getSqls(String cmd);

	/**
	 * Retrieves summary data about all same executed SQLs (for instance
	 * summary data of all executed {@code select * from foo where bar => 0}).
	 *
	 * @param sql hash code of sql command
	 * @return populated object {@link org.javasimon.jmx.StopwatchSample}, or {@code null} if
	 * entered sql has no associated javasimon (it means, no sql like this was executed yet,
	 * for instance no update was executed yet)
	 * @see #getSqls(String)
	 */
	StopwatchSample getSqlInfo(String sql);
}