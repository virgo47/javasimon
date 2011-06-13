/**
 * Support classes to automatically monitor Spring beans with the {@link org.javasimon.aop.Monitored} annotation.
 *
 * <p>Usage instructions:
 *
 * <p>Step -1- Make sure the Spring configuration file {@literal org/javasimon/spring/monitoring.xml} is loaded as one
 * of the first configuration files.
 *
 * <p>For example, if you use the {@code org.springframework.web.context.ContextLoaderListener} in your
 * {@literal web.xml}, the {@literal contextConfigLocation} context parameter will look something like this:
 * <pre>{@literal
<context-param>
<param-name>contextConfigLocation</param-name>
<param-value>
classpath:org/javasimon/spring/monitoring.xml
classpath:context/services.xml
classpath:context/data-access-layer.xml
/WEB-INF/applicationContext.xml
</param-value>
</context-param>}</pre>
 *
 * <p>Step -2- Annotate all classes and/or methods you want to monitor (see {@link org.javasimon.aop.Monitored} for
 * more details). As step 1 configured annotation detection only for spring beans, make sure these classes are
 * instantiated through Spring (i.e. declare them as a bean in a Spring configuration file).
 */
package org.javasimon.spring;
