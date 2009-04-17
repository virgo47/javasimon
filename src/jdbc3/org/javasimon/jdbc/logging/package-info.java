/**
 * Logging feature of Simon JDBC Proxy driver.
 * <p>
 * This logging feature is based on Callback feature of Javasimon core library, see {@link org.javasimon.Callback}.
 * In short, Callback feature allow intercept event from simon (like start, stop, etc.) and perform custom action,
 * in this case log message with details into file or to console.
 * <p>
 * By default, start and stop event from all jdbc stopwatch simons are logged. Jdbc stopwatches are determined by
 * provided jdbc prefix. This means that logged events are connections starts and stops, statements starts and stops
 * and sql executions.
 * <p>
 * Java standard logging feature ({@link java.util.logging}) is used for logging from Simon JDBC Proxy driver. There
 * are three targets where to log and also three ways how to setup logging.
 * Where to log:
 * <ul>
 * <li>
 * <b>to console</b> - log messages are routed through standard {@link java.util.logging.ConsoleHandler} so messages
 * are printed to error output (or somewhere where an output is redirected)
 * </li>
 * <li>
 * <b>to logger</b>< - logger is provided by name, all messages are then logged by obtained
 * {@link java.util.logging.Logger}
 * </li>
 * <li>
 * <b>to file</b> - filename is provided, log messages are routed through standard {@link java.util.logging.FileHandler}
 * </li>
 * </ul>
 * Log messages are by default formated to human readable form by build in human formatter (identificator: HUMAN).
 * There is also other build-in csv formatter (identifier CSV). There is also possibility to provide custom formatter.
 * Only requriment is that custom formatter extends from {@link org.javasimon.jdbc.logging.SimonFormatter}.
 * <br>
 * How to setup jdbc logging:
 * <ul>
 * <li>
 * <b>via JDBC URL</b> - Simon JDBC URL can by enhanced with Simon specific parameters to setup logging to file, logger
 * or console and format of log messages. For details about Simon URL specific parameters see
 * {@link org.javasimon.jdbc.Driver}.
 * </li>
 * <li><b>via Simon configuration file</b> - Callback configuration can be added into the configuration file:
 * <pre>&lt;simon-configuration>
	&lt;callback class="org.javasimon.jdbc.logging.LoggingCallback"> &lt!-- don't mix it with org.javasimon.utils.LoggingCallback! -->
		&lt;set property="prefix" value="org.javasimon.testdb"/>
		&lt;set property="loggerName" value="myapp.testdb"/>
		&lt;set property="logFilename" value="log/myapp.log"/>
 		&lt;set property="logToConsole"/> &lt;!-- without value! -->
		&lt;set property="logFormat" value="csv"/> &lt;!-- can be also human -->
	&lt;/callback>
    ...
&lt;/simon-configuration></pre>
 * If {@code logToConsole} or {@logFilename} is omitted respective handler will not be used.</li>
 * <li>
 * <b>by jmx</b> - jmx package offers {@link org.javasimon.jmx.JdbcMXBean} that provides methods for starting logging
 * to file, logger or console and for stopping too. Each method has its parameters to setup specific details.
 * </li>
 * </ul>
 */
package org.javasimon.jdbc.logging;