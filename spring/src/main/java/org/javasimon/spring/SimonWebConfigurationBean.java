package org.javasimon.spring;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

/**
 * Bean TODO.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 */
public class SimonWebConfigurationBean extends SimonConfigurationBean implements ServletContextAware {
	// the same like in SimonServletFilter
	public static final String MANAGER_SERVLET_CTX_ATTRIBUTE = "manager-servlet-ctx-attribute";

	@Override
	public void setServletContext(ServletContext servletContext) {
		System.out.println("Setting the manager to SC attribute");
		servletContext.setAttribute(MANAGER_SERVLET_CTX_ATTRIBUTE, getSimonManager());
	}
}