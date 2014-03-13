/**
 * JMX capabilities for Simons. Package provides two ways of working with Simons via JMX:
 * <ul>
 *     <li>generic single-point Simon MBean;</li>
 *     <li>MX Bean for every Simon (using {@link org.javasimon.jmx.JmxRegisterCallback}.</li>
 * </ul>
 * <b>Simon MBean</b> ({@link org.javasimon.jmx.SimonManagerMXBean}) implements jmx support for core functionality,
 * it means firstly management of java Simons during runtime and secondly, very requested,
 * way how to get gathered data by Simons out of application (jvm) for additional processing.
 * <p>
 * From management point of view it provides similar functionality like {@link org.javasimon.Manager},
 * but this functionality is accessible remotely. Management features are:
 * <ul>
 * <li>enable/disable whole manager, what practically means turn on/off all Simons</li>
 * <li>enable/disable particular Simon without touching linked hierarchy</li>
 * <li>list all existing Simons</li>
 * <li>clear all Simons (all values of the Manager are cleared)</li>
 * </ul>
 * For retrieving data from Simons is used new feature: <b>return custom object from MBean's method</b>.
 * This is one of news in JMX 1.4 introduced with Java 6. Compared to calling many getters on an MBean
 * custom object as a return value has following advantages: there is only a single call (faster - especially
 * in case of a remote call) and all data is consistent (from one moment) which makes it better for graphing,
 * logging, etc.
 * <p>
 * Technically, there are two methods, each for one Simon
 * type, {@link org.javasimon.jmx.SimonManagerMXBean#getCounterSample(String)} for retrieving data from Counter Simon
 * and {@link org.javasimon.jmx.SimonManagerMXBean#getStopwatchSample(String)} for retrieving data from Stopwatch Simon. Returned
 * value object is basically Sample object for each type of Simon as is known from core package. Each
 * sample has all needed access methods of its Simon. Parameter for retrieving methods is Simon hierarchical
 * name. To know all existing Simons and its type, use {@link org.javasimon.jmx.SimonManagerMXBean#getSimonInfos()}.
 * <p>
 * Both MBean needs to be registered first. Snippet bellow registers the Simon MBean:
 * <pre>
 MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
 try {
 ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
 if (mbs.isRegistered(name)) {
 mbs.unregisterMBean(name);
 }
 SimonManagerMXBean simon = new SimonManagerMXBeanImpl(SimonManager.manager());
 mbs.registerMBean(simon, name);
 System.out.println("SimonManagerMXBean registered under name: "+name);
 } catch (JMException e) {
 System.out.println("SimonManagerMXBean registration failed!\n"+e);
 }
 * </pre>
 * Following code then unregisters the MBean.
 * <pre>
 MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
 try {
 ObjectName name = new ObjectName("org.javasimon.jmx.example:type=Simon");
 if (mbs.isRegistered(name)) {
 mbs.unregisterMBean(name);
 }
 System.out.println("SimonManagerMXBean was unregistered");
 } catch (JMException e) {
 System.out.println("SimonManagerMXBean unregistration failed!\n"+e);
 }
 * </pre>
 * Java Simon doesn't provide any automatic mechanism or util functions to register
 * or unregister Simon MBean because there are simply too many things which could be customized. It is
 * programmer's responsibility to properly register and later unregister MBean from MBean server.
 */
package org.javasimon.jmx;
