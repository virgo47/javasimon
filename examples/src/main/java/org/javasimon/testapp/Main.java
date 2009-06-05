package org.javasimon.testapp;

import org.javasimon.testapp.test.Runner;
import org.javasimon.testapp.mm.AppMXBean;
import org.javasimon.jmx.SimonMXBeanImpl;
import org.javasimon.jmx.SimonMXBean;
import org.javasimon.jmx.JdbcMXBean;
import org.javasimon.jmx.JdbcMXBeanImpl;
import org.javasimon.SimonManager;
import org.h2.tools.RunScript;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.JMException;
import java.lang.management.ManagementFactory;
import java.sql.DriverManager;
import java.sql.Connection;

/**
 * Main class of the test application (DB+JMX).
 * Run it from main javasimon directory (where {@code examples} directory is). Compilation:
 * <pre>javac -cp lib/h2.jar:build/core:build/jdbc3:build/jmx examples/org/javasimon/testapp/*.java examples/org/javasimon/testapp/*\/*.java</pre>
 * Run it with command:
 * <pre>java -cp ../lib/h2.jar:build/core:build/jdbc3:ild/jmx:examples org.javasimon.testapp.Main</pre>
 *
 * To initialize JDBC logging callback following switch has to be added as the JVM option:
 * {@code java -cp -Djavasimon.config.file=examples/testapp-config.xml}
 *
 * @author Radovan Sninsky
 * @version $Revision$ $ Date: $
 * @created 19.3.2009 12:55:56
 * @since 2.0
 */
public class Main {

	private Runner runner;
	private Connection connection;

	/**
	 * Implementation of the application MX bean.
	 */
	public class AppMXBeanImpl implements AppMXBean {
		/**
		 * Shutdown operation.
		 */
		public void shutdown() {
			if (runner != null) {
				runner.stop();
			}
		}
	}

	private void setupDatabase() throws Exception {
		RunScript.execute("jdbc:h2:file:testappdb", "sa", "sa", "examples/testapp.db.sql", null, false);

		Class.forName("org.javasimon.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:simon:h2:file:testappdb;simon_prefix=org.javasimon.testapp.jdbc", "sa", "sa");
	}

	private void closeDatabase() throws Exception {
		connection.close();
	}

	private void setupRunner() {
		WeightController controller = new WeightController();
		controller.addAction(new InsertAction(new RandomNumberDataProvider(700), connection), 34);
		controller.addAction(new InsertBatchAction(connection), 12);
		controller.addAction(new UpdateAction(new RandomNumberDataProvider(400), connection), 28);
		controller.addAction(new DeleteAction(new RandomNumberDataProvider(600), connection), 26);

		UniformRandomTimer timer = new UniformRandomTimer(7100, 900);

		runner = new Runner(controller, timer);
	}

	private void setupJmx() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			AppMXBean app = new AppMXBeanImpl();
			mbs.registerMBean(app, new ObjectName("org.javasimon.testapp:type=App"));
			System.out.println("AppMXBean registerd");

			SimonMXBean simon = new SimonMXBeanImpl(SimonManager.manager());
			mbs.registerMBean(simon, new ObjectName("org.javasimon.testapp:type=Simon"));
			System.out.println("SimonMXBean registerd");

			JdbcMXBean jdbc = new JdbcMXBeanImpl(SimonManager.manager(), "org.javasimon.testapp.jdbc");
			mbs.registerMBean(jdbc, new ObjectName("org.javasimon.testapp:type=Jdbc"));
			System.out.println("JdbcMXBean registerd");
		} catch (JMException e) {
			System.out.println("JMX beans registration failed!\n"+e);
		}
	}

	private void run() {
		runner.run();
	}

	/**
	 * Entry point of the test application.
	 *
	 * @param args unused
	 * @throws Exception well, it's just an example!
	 */
	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.setupDatabase();
		m.setupRunner();
		m.setupJmx();

		m.run();

		m.closeDatabase();
	}
}
