package org.javasimon.aggregation;

import org.javasimon.jmx.SimonManagerMXBean;

import java.util.Properties;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface RemoteSimonManagerFactory {
	SimonManagerMXBean createSimonManager(String factoryClass, Properties properties) throws ManagerCreationException;
}
