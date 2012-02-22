package org.javasimon.demoapp.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class PersonsPage extends WebPage {
	private static final long serialVersionUID = 1L;

	public PersonsPage(final PageParameters parameters) {
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		// TODO Add your page's components here
	}
}
