package org.javasimon.aggregation.managers;

import org.javasimon.aggregation.ConcreteManagerFactory;
import org.javasimon.aggregation.ManagerCreationException;
import org.javasimon.jmx.SimonManagerMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class JMXRemoteSimonManagerFactory implements ConcreteManagerFactory {

	private static final String HOST_KEY = "host";
	private static final String OBJECT_NAME_KEY = "objectName";

	private static final Logger logger = LoggerFactory.getLogger(JMXRemoteSimonManagerFactory.class);

	@Override
	public SimonManagerMXBean createManager(Properties properties) throws ManagerCreationException {
		try {
			String host = properties.getProperty("host");
			String objectName = properties.getProperty("objectName");
			String port = properties.getProperty("port");

			String url = String.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", host, port);
			logger.debug("Connecting to JMX server using URL {}", url);
			JMXServiceURL jmxUrl = new JMXServiceURL(url);

			JMXConnector jmxc = JMXConnectorFactory.connect(jmxUrl, null);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			SimonManagerMXBean simonManagerMXBean = JMX.newMXBeanProxy(mbsc, new ObjectName(objectName), SimonManagerMXBean.class);
			return simonManagerMXBean;
		} catch (MalformedURLException e) {
			wrapAndRethrow(properties, e);
		} catch (MalformedObjectNameException e) {
			wrapAndRethrow(properties, e);
		} catch (IOException e) {
			wrapAndRethrow(properties, e);
		}

		return null;
	}

	private void wrapAndRethrow(Properties properties, Exception e) throws ManagerCreationException {
		throw new ManagerCreationException(String.format("Failed to create JMX manager with configuration: %s", properties.toString()), e);
	}
}
