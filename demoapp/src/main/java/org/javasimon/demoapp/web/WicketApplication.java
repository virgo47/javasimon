package org.javasimon.demoapp.web;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Main application class of Simon Demo Web application in Wicket/Spring.
 */
public class WicketApplication extends WebApplication {
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<PersonsPage> getHomePage() {
		return PersonsPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();
	}
}
