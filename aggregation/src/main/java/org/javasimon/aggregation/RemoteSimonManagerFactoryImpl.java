package org.javasimon.aggregation;

import org.javasimon.jmx.SimonManagerMXBean;

import java.util.Properties;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class RemoteSimonManagerFactoryImpl implements RemoteSimonManagerFactory {

	@Override
	public SimonManagerMXBean createSimonManager(String factoryClassName, Properties properties) throws ManagerCreationException {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			Class factoryClass = classLoader.loadClass(factoryClassName);

			Object obj = factoryClass.newInstance();
			if (!(obj instanceof ConcreteManagerFactory)) {
				throw new ManagerCreationException(
						String.format("Class %s does not inherits %s", factoryClassName, ConcreteManagerFactory.class.getName()));
			}

			ConcreteManagerFactory factoryInst = (ConcreteManagerFactory) obj;

			return factoryInst.createManager(properties);
		} catch (InstantiationException e) {
			wrapAndRethrow(e, factoryClassName);
		} catch (IllegalAccessException e) {
			wrapAndRethrow(e, factoryClassName);
		} catch (ClassNotFoundException e) {
			wrapAndRethrow(e, factoryClassName);
		}

		return null;
	}

	private void wrapAndRethrow(Exception e, String factoryClassName) throws ManagerCreationException {
		throw new ManagerCreationException("Failed to created  manager using " + factoryClassName, e);
	}
}
