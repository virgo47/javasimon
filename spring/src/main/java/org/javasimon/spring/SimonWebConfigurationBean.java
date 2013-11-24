package org.javasimon.spring;

import javax.servlet.ServletContext;

import org.javasimon.utils.SimonUtils;

import org.springframework.web.context.ServletContextAware;

/**
 * {@link SimonConfigurationBean} with extended function - just pushes configured manager to the {@link ServletContext} attribute
 * {@link SimonUtils#MANAGER_SERVLET_CTX_ATTRIBUTE}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class SimonWebConfigurationBean extends SimonConfigurationBean implements ServletContextAware {
	@Override
	public void setServletContext(ServletContext servletContext) {
		servletContext.setAttribute(SimonUtils.MANAGER_SERVLET_CTX_ATTRIBUTE, getSimonManager());
	}
}