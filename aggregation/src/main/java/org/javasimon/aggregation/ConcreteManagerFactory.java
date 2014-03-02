package org.javasimon.aggregation;

import org.javasimon.jmx.SimonManagerMXBean;

import java.util.Properties;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public interface ConcreteManagerFactory {
	SimonManagerMXBean createManager(Properties properties) throws ManagerCreationException;
}
