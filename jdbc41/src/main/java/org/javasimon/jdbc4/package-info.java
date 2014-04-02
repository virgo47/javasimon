/**
 * Simon JDBC 4.1 (Java SE 7) Proxy Driver for monitoring JDBC drivers through the Java Simon API.
 * <p>
 * Main goal of monitoring the JDBC driver through the Simon JDBC Proxy Driver is to gain data
 * about so-called monitored entities of the JDBC driver, which could provide reasonable information
 * about their usage in the monitored system.
 * <p>
 * Following JDBC entities are monitored at this time:
 * <ul>
 * <li>Connections,
 * <li>statements - objects in JVM,
 * <li>SQL commands - commands like select, insert, update, etc, but also create, truncate and others,
 * so than you can know how many selects vs. inserts vs. updates are present in your system or how
 * long took their execution,
 * <li>SQL command groups - command group means same command with different parameters, typically
 * systems in general has final set of different select or updates (unless it is SQL editor).
 * </ul>
 *
 * Each entity has several monitoring parameters:
 * <p>
 * <b>Connection:</b>
 * <ul>
 * <li><i>active conns</i> - count of actual active opened connections;
 * <li><i>max active conns</i> - maximum active opened connection at same time;
 * <li><i>opened</i> - summary count of all opened connections - difference between this number
 * and closed should equals active in any time, if not this could indicate unclosed connections
 * ({@code close} was not invoked);
 * <li><i>closed</i> - summary count of all closed connections - difference between this number
 * and opened should equals active in any time;
 * <li><i>minimum lifespan</i> - minimum time of all connections life (time between {@code getConnection}
 * and {@code close};
 * <li><i>average lifespan</i> - average time of all connection life;
 * <li><i>maximum lifespan</i> - maximum time of all connection life;
 * <li><i>commits</i> - summary count of all commits issued by {@code commit} - this counter is increased
 * only by invoking method {@code commit}, therefore if autocommit is true counter is not representative;
 * <li><i>rollbacks</i> - summary count of all rollbacks issued by {@code rollback} - this counter is
 * increased only by invoking method {@code rollback}, therefore if autocommit is true counter is not
 * representative.
 * </ul>
 *
 * <b>Statement:</b>
 * <ul>
 * <li><i>active stmt</i> - count of actual active opened statements;
 * <li><i>max active stmt</i> - maximum active opened statements at same time;
 * <li><i>opened</i> - summary count of all opened statements - difference between this number and closed
 * should equals active in any time, if not this could indicate unclosed statements {@code close} was
 * not invoked;
 * <li><i>closed</i> - summary count of all closed statements - difference between this number and opened
 * should equals active in any time;
 * <li><i>minimum lifespan</i> - minimum time of all statements life (time between {@code createStatement},
 * {@code prepareStatement} or {@code prepareCall} and {@code close});
 * <li><i>average lifespan</i> - average time of all statements life;
 * <li><i>maximum lifespan</i> - maximum time of all statements life.
 * </ul>
 *
 * <b>SQL command (select, insert, delete, call, ...):</b>
 * <ul>
 * <li><i>minimum execution</i> - minimum time of all desired sql command execution times - typically
 * execution time of {@code executeQuery}, {@code executeUpdate} or {@code execute};
 * <li><i>average execution</i> - average time of all desired sql command execution times;
 * <li><i>maximum execution</i> - maximum time of all desired sql command execution times;
 * <li><i>first</i> - time of first occurrence of desired SQL command;
 * <li><i>last</i> - time of last occurrence of desired SQL command.
 * </ul>
 *
 * <b>SQL:</b>
 * <ul>
 * <li><i>minimum execution</i> - minimum time of all desired sql command execution times (typically
 * execution time of {@code executeQuery}, {@code executeUpdate} or {@code execute});
 * <li><i>average execution</i> - average time of all desired sql command execution times;
 * <li><i>maximum execution</i> - maximum time of all desired sql command execution times;
 * <li><i>first</i> - time of first occurrence of desired SQL command;
 * <li><i>last</i> - time of last occurrence of desired SQL command;
 * <li><i>note</i> - "normalized" SQL command (SQL command without concrete values with question marks
 * instead - similar to prepared statement syntax).
 * </ul>
 *
 * From technical point of view, Simon JDBC Proxy Driver is based on a simple idea of the proxy driver
 * that delegates invocations to the real driver which is wrapped. This allows to intercept invocations
 * to the real driver and measure (or count) them.
 * <p>
 * The goal is not to measure each invocation on every possible function of JDBC driver (there
 * are another techniques like profiling for that purpose), but just monitor those functions of driver
 * which somehow influence parameter values of monitored entities mentioned earlier.
 * <p>
 * Simon driver implements just a few basic JDBC interfaces, like Connection and all Statements. Rest of
 * the JDBC interfaces (from java.sql.* package) are not implemented by Simon driver. Therefore
 * result of some invocations are not Simon driver classes, but directly classes from real driver. For
 * example, as result of invoke method {@code Connection.createStatement} is returned Simon driver
 * class {@code org.javasimon.jdbc4.Statement}, however result of invoking method {@code Statement.executeQuery}
 * is returned concrete implementation class of real driver, i.e. for H2 driver class {@code org.h2.jdbc.JdbcResultSet}.
 * <p>
 * Simons are used to monitor aforementioned entities. To measure time parameters like execution time
 * Stopwatch Simons are used. To count how many statements are open Counter Simons are used.
 * <p>
 * Each Simon is placed in the tree hierarchy (basic feature of Simon) and the place is strictly defined
 * inside the driver. Therefore you can use static configuration of those Simons by defining their state
 * in the config file (for more information see {@code SimonConfigTest.java} example).
 * <p>
 * Hierarchy of Simons in Simon JDBC proxy driver is following:
 * <pre>{@literal
 * org.javasimon.jdbc
 * |
 * +-> .conn
 * |     +-> .commits
 * |     +-> .rollbacks
 * |
 * +-> .stmt
 * |     +-> .active
 * |
 * +-> .sql
 * |     +-> .<sql type (select, insert, ...)>
 * |           +-> .<sql hash>}</pre>
 *
 * For choosing Simon's name is used prefix. If non is defined, default is {@code org.javasimon.jdbc}.
 * If default prefix value is not sufficient or you need to differentiate between two different drivers
 * (or its configuration) you can define your own prefix as parameter {@code SIMON_PREFIX=<prefix>}
 * within JDBC connection string. For example, {@code jdbc:simon:....;SIMON_PREFIX=com.foo.bar}. More
 * about setting up Simon JDBC proxy driver see {@link org.javasimon.jdbc4.Driver}.
 * <p>
 * For printing information from Simons to standard output you can use:
 * <pre>
 * SimonUtils.printSimonTree(SimonManager.getRootSimon());</pre>
 *
 * Also see examples classes {@code org.javasimon.examples.jdbc.Simple} and {@code org.javasimon.examples.jdbc.Complex}.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 2.4
 */
package org.javasimon.jdbc4;
