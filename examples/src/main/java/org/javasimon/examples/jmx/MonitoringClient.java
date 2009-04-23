package org.javasimon.examples.jmx;

import org.javasimon.jmx.SimonMXBean;
import org.javasimon.jmx.SimonInfo;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnector;
import javax.management.MBeanServerConnection;
import javax.management.JMX;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import java.io.IOException;

/**
 * Trieda ExampleClient.
 *
 * @author Radovan Sninsky
 * @version $Revision $ $Date $
 * @created 27.1.2009 18:50:41
 * @since 1.0
 */
public class MonitoringClient {

	public static void main(String[] args) throws IOException, MalformedObjectNameException {
		final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + args[0] + "/jmxrmi");

//		final Map<String, String[]> env = new HashMap<String, String[]>();
//		env.put(JMXConnector.CREDENTIALS, new String[]{login, pass});

		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
		SimonMXBean simon = JMX.newMXBeanProxy(mbsc, new ObjectName("org.javasimon.jmx.example:type=Simon"), SimonMXBean.class);

		System.out.println("List of retrieved simons:");
		for (String n : simon.getSimonNames()) {
			System.out.println("  " + n);
		}

		System.out.println("List of stopwatch simons:");
		for (SimonInfo si : simon.getSimonInfos()) {
			if (si.getType().equals(SimonInfo.STOPWATCH)) {
				System.out.println("  " + si.getName());
			}
		}

		simon.printSimonTree();

		jmxc.close();
	}
}
