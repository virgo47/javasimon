package org.javasimon.examples.jmx;

import java.io.IOException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.javasimon.jmx.SimonInfo;
import org.javasimon.jmx.SimonManagerMXBean;

/**
 * Trieda ExampleClient.
 *
 * @author Radovan Sninsky
 * @since 1.0
 */
public class MonitoringClient {
	/**
	 * Entry point to this monitoring client.
	 *
	 * @param args host:port part of the JMX connector server
	 * @throws java.io.IOException thrown in case of some I/O exception
	 * @throws javax.management.MalformedObjectNameException thrown when the name of the JMX object is incorrect
	 */
	public static void main(String[] args) throws IOException, MalformedObjectNameException {
		final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + args[0] + "/jmxrmi");

//		final Map<String, String[]> env = new HashMap<String, String[]>();
//		env.put(JMXConnector.CREDENTIALS, new String[]{login, pass});

		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
		SimonManagerMXBean simonManagerMXBean = JMX.newMXBeanProxy(mbsc, new ObjectName("org.javasimon.jmx.example:type=Simon"), SimonManagerMXBean.class);

		System.out.println("List of retrieved Simons:");
		for (String n : simonManagerMXBean.getSimonNames()) {
			System.out.println("  " + n);
		}

		System.out.println("List of stopwatch Simons:");
		for (SimonInfo si : simonManagerMXBean.getSimonInfos()) {
			if (si.getType().equals(SimonInfo.STOPWATCH)) {
				System.out.println("  " + si.getName());
			}
		}

		simonManagerMXBean.printSimonTree();

		jmxc.close();
	}
}
