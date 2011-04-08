package org.javasimon.jmx;

/**
 * Interface of JDBC management bean (MXBean).
 * <p/>
 * JDBC MXBean provides management operations to monitoring and logging JDBC driver
 * and operations to retrieve gathered statistics from special JDBC hierarchy of Simons.<br>
 * This JDBC hierarchy of Simon is well known and specified in JDBC javadoc
 * ({@link org.javasimon.jdbc}). There is prefix of this hierarchy that is associated with
 * one instance of monitored JDBC driver. Typically, thare is only one driver instance used
 * in application, becouse of connection to one type of database. If application is connected
 * to different types of database (i.e. oracle and mysql), there is possibility to differentiate
 * each used driver by specifying Simon hierarchy prefix for driver.<br>
 * Jdbc mxbean has prefix associated with it. It's is initialized through mxbean implementation
 * class constructor ({@link org.javasimon.jmx.JdbcMXBeanImpl#JdbcMXBeanImpl(org.javasimon.Manager, String)})
 * or later through setter ({@link #setPrefix(String)}). Then is also possible to register Jdbc mxbean
 * for each prefix.
 * <p/>
 * Monitoring and logging JDBC driver by javasimons is possible only if javasimon JDBC driver is used
 * over orginal JDBC driver (eg. Oracle, PostgreSQL, MySQL etc.). For more details how to do
 * that, please look at {@link org.javasimon.jdbc.Driver} a package {@link org.javasimon.jdbc}
 * javadoc.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @see org.javasimon.jmx.JdbcMXBeanImpl
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
	 * Returns actual prefix associated with mxbean.
	 *
	 * @return actual prefix
	 */
	String getPrefix();

	/**
	 * Sets custom prefix of JDBC Simon hierarchy, see more {@link org.javasimon.jmx.JdbcMXBean}.
	 *
	 * @param value new prefix
	 */
	void setPrefix(String value);

	/**
	 * Enables monitoring (gathering statistics) by associated JDBC Simon hierarchy, see
	 * ({@code org.javasimon.jdbc}). Works if SimonManager is enabled, look
	 * {@link org.javasimon.SimonManager#isEnabled()}.
	 */
	void enableMonitoring();

	/**
	 * Disables monitoring (gathering statistics) by associated JDBC Simon hierarchy, see
	 * ({@code org.javasimon.jdbc}). If SimonManager is already disabled has no effect,
	 * look {@link org.javasimon.SimonManager#disable()}.
	 */
	void disableMonitoring();

	/**
	 * Returns state of monitoring JDBC driver.
	 *
	 * @return {@code true} if associated prefix is enabled; otherwise {@code false}
	 */
	boolean isMonitoringEnabled();

	/**
	 * Starts logging JDBC events from driver (associated with prefix) to file.
	 * This is going to be removed in 3.0 as the whole design is now not future proof, now it is stuck to JDBC 3.
	 *
	 * @param filename path to file on disk
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 */
	@Deprecated
	void logToFile(String filename, String format);

	/**
	 * Starts logging JDBC events from driver (associated with prefix)to {@link java.util.logging.Logger}.
	 * This is going to be removed in 3.0 as the whole design is now not future proof, now it is stuck to JDBC 3.
	 *
	 * @param logger name of JDK14 logger
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 */
	@Deprecated
	void logToLogger(String logger, String format);

	/**
	 * Starts logging JDBC events from driver (associated with prefix) to {@link java.util.logging.ConsoleHandler}
	 * ({@code System.out.err}).
	 * This is going to be removed in 3.0 as the whole design is now not future proof, now it is stuck to JDBC 3.
	 *
	 * @param format could identifier of build-in formatters {@code human} or {@code csv}
	 * or classname of custom formatter derived from {@link org.javasimon.jdbc.logging.SimonFormatter}
	 */
	@Deprecated
	void logToConsole(String format);

	/**
	 * Stops logging events from driver (associated with prefix).
	 * This is going to be removed in 3.0 as the whole design is now not future proof, now it is stuck to JDBC 3.
	 *
	 * @see #logToFile(String, String)
	 * @see #logToLogger(String, String)
	 * @see #logToConsole(String)
	 */
	@Deprecated
	void stopLogging();

	/**
	 * Retrieves summary data about JDBC connection objects.
	 *
	 * @return value object {@link org.javasimon.jmx.JdbcObjectInfo}
	 */
	JdbcObjectInfo connectionsStat();

	/**
	 * Retrieves summary data about JDBC statement objects.
	 *
	 * @return value object {@link org.javasimon.jmx.JdbcObjectInfo}
	 */
	JdbcObjectInfo statementsStat();

	/**
	 * Retrieves summary data about JDBC result set objects.
	 *
	 * @return value object {@link org.javasimon.jmx.JdbcObjectInfo}
	 */
	JdbcObjectInfo resultsetsStat();

	/**
	 * Returns SQL command types ({@code select}, {@code insert}, {@code delete}, etc).
	 * If client application uses DDL commands like {@code create}, {@code alter} and other,
	 * those are included too. Special case are batch-es, they are referenced as {@code batch}.
	 *
	 * @return list of string (references to used SQL command types in monitored (simoned) application)
	 * @see #getSqlCommandStat(String)
	 * @see #getSqls(String)
	 */
	String[] getSqlCommands();

	/**
	 * Retrieves summary data about all executed SQL commands of eneterd type (for instance
	 * summary data of all executed selects).
	 *
	 * @param cmd SQL command type
	 * @return populated object {@link org.javasimon.jmx.StopwatchSample}, or {@code null} if
	 *         entered sql command type has no associated javasimon (it means, no sql of this type was
	 *         executed yet, for instance no update was executed yet)
	 * @see #connectionsStat()
	 */
	StopwatchSample getSqlCommandStat(String cmd);

	/**
	 * Returns hashs of different SQL commands of one type ({@code select}, {@code insert},
	 * {@code delete}, etc). Each hash represent one sql command with (and its many executions).
	 *
	 * @param cmd cmd SQL command type ({@code select}, {@code insert}, {@code delete}, etc)
	 * @return list of string (hash references to used SQL command in monitored (simoned) application)
	 * @see #getSqlCommands()
	 * @see #getSqlStat(String)
	 */
	String[] getSqls(String cmd);

	/**
	 * Retrieves summary data about all same executed SQLs (for instance
	 * summary data of all executed {@code select * from foo where bar => 0}).
	 *
	 * @param sql hash code of sql command
	 * @return populated object {@link org.javasimon.jmx.StopwatchSample}, or {@code null} if
	 *         entered sql has no associated javasimon (it means, no sql like this was executed yet,
	 *         for instance no update was executed yet)
	 * @see #getSqls(String)
	 */
	StopwatchSample getSqlStat(String sql);
}