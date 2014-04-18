package org.javasimon.aggregation;

import org.javasimon.jmx.SimonManagerMXBean;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class RemoteSimonManagerFactoryImplTest {

	RemoteSimonManagerFactoryImpl remoteSimonManagerFactory = new RemoteSimonManagerFactoryImpl();

	@Test
	public void testRemoteSimonManagerIsCreated() throws Exception {
		Properties expectedProperties = new Properties();
		expectedProperties.setProperty("key", "val");

		TestSimonManager manager = (TestSimonManager)
				remoteSimonManagerFactory.createSimonManager(TestSimonManagerFactory.class.getName(), expectedProperties);

		Assert.assertEquals(manager.getProperties(), expectedProperties);
	}

	private static class TestSimonManagerFactory implements ConcreteManagerFactory {

		public TestSimonManagerFactory() {}

		@Override
		public SimonManagerMXBean createManager(Properties properties) throws ManagerCreationException {
			TestSimonManager testManager = mock(TestSimonManager.class);
			when(testManager.getProperties()).thenReturn(properties);

			return testManager;
		}
	}

	private static interface TestSimonManager extends  SimonManagerMXBean {
		Properties getProperties();
	}

	@Test(expectedExceptions = {ManagerCreationException.class})
	public void testWrongClassCreation() throws Exception {
		Properties expectedProperties = new Properties();

		TestSimonManager manager = (TestSimonManager)
				remoteSimonManagerFactory.createSimonManager(String.class.getName(), expectedProperties);
	}



}
