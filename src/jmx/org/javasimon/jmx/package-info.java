/**
 * JMX cabalities for core simons and jdbc proxy driver. Pacakge contains two management beans:
 * general management bean for Simons and JDBC management bean.
 * <p>
 * <b>Simon mbean</b> ({@link org.javasimon.jmx.SimonMXBean}) implements jmx support for core functionality,
 * it means firstly management of java simons during runtime and secondly, very requested,
 * way how to get gathered data by simons out of application (jvm) for aditional processing.
 * <p>
 * From management point of view it provides similar functionality like {@link org.javasimon.Manager},
 * but this functionality is accessible remotely. Management features are:
 * <ul>
 * <li>enable/disable whole manager, what practically means turn on/off all simons</li>
 * <li>enable/disable particular simon without touching linked hierarchy</li>
 * <li>list all existing simons</li>
 * <li>clear all simons (all values are cleared)</li>
 * </ul>
 * For retrieving data from simons is used new feature: <b>return custom object from MBean's method</b>.
 * This is one of news in JMX 1.4 introduced with Java 6. Against standard MBean this has its pros: all
 * retrieved data are from one moment and also all data are retrieved within single call, so
 * for retrieving data from one simon only one network roundtrip is needed. However, a con is that
 * classes are not compatible with Java 5 anymore and have to be compiled with Java 6 logicaly. This
 * creates ugly dependency mess between javasimon modules. It complicates build script slightly, but
 * end-user wouldn't be touched by this.
 * <p>
 * Technically, there are two methods, each for one Simon
 * type, {@link org.javasimon.jmx.SimonMXBean#getCounterSample(String)} for retrieving data from Counter simon
 * and {@link org.javasimon.jmx.SimonMXBean#getStopwatchSample(String)} for retrieving data from Stopwatch simon. Returned
 * value object is basically Sample object for each type of Simon as is known from core package. Each
 * sample has all needed access methods of its simon. Parameter for retrieving methods is simon hierarchical
 * name. To know all existing simons and its type, use {@link org.javasimon.jmx.SimonMXBean#getSimonInfos()}.
 * <p>
 * <b>JDBC mbean</b> ({@link org.javasimon.jmx.JdbcMXBean}) provides jmx support for monitoring and logging capabilities
 * of Simon JDBC Driver which covers standard JDBC driver, you can find more details in package {@link org.javasimon.jdbc}.
 * This comprises features like enable and disable monitoring (gathering statistics data) by hierarchy of javasimons,
 * starting and stoping logging events from JDBC driver, configuring logging settings and also retrieve
 * gathered data from javasimons through special designed interface.
 * <p>
 * Both MBeans needs to be registered first. Bellow are two sniplets, one for registring Simon MBean:
 * <pre>
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
try {
	ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
	if (mbs.isRegistered(name)) {
		mbs.unregisterMBean(name);
	}
	SimonMXBean simon = new SimonMXBeanImpl(SimonManager.manager());
	mbs.registerMBean(simon, name);
	System.out.println("SimonMXBean registerd under name: "+name);
} catch (JMException e) {
	System.out.println("SimonMXBean registration failed!\n"+e);
}
 * </pre>
 * and other for unregistering Simon MBean:
 * <pre>
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
try {
	ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
	if (mbs.isRegistered(name)) {
		mbs.unregisterMBean(name);
	}
	System.out.println("SimonMXBean was unregisterd");
} catch (JMException e) {
	System.out.println("SimonMXBean unregistration failed!\n"+e);
}
 * </pre>
 * Javasimon doesn't provide any automatic mechanism or util functions to register
 * or unregister Simon MBean becouse there are simply too many things which could be customized. This is
 * client's programmer responsibility to properly register and later unregister MBean from MBean server.
 */
package org.javasimon.jmx;
