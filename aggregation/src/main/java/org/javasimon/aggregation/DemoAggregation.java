package org.javasimon.aggregation;

import org.javasimon.aggregation.managers.JMXRemoteSimonManagerFactory;
import org.javasimon.aggregation.metricsDao.LogMetricsDao;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class DemoAggregation {
	public static void main(String... a) throws Exception {
		RemoteSimonManagerFactory remoteSimonManagerFactory = new RemoteSimonManagerFactoryImpl();

		AggregationFacade.Args args = new AggregationFacade.Args();
		args.setMetricsDao(new LogMetricsDao());
		args.setRemoteSimonManagerFactory(remoteSimonManagerFactory);
		args.setExecutor(Executors.newScheduledThreadPool(1));
		args.setTimePeriod(2);
		args.setTimeUnit(TimeUnit.SECONDS);
		args.setAggregator(new ReplaceSamplesAggregator());

		AggregationFacade facade = new AggregationFacade(args);

		Properties properties = new Properties();
		properties.setProperty("managerId", "jmxServer");
		properties.setProperty("host", "localhost");
		properties.setProperty("port", "1099");
		properties.setProperty("objectName", "org.javasimon.jmx.example:type=Simon");

		facade.addManager(JMXRemoteSimonManagerFactory.class.getName(), properties);


		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		facade.shutdown();

		System.out.println("Aggregated values:");
		System.out.println(facade.getAggregatedStopwatchSamples("jmxServer"));

        System.out.println("Used servers: ");
        System.out.println(facade.getManagerIds());
	}
}
