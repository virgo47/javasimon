package org.javasimon.console.plugin;

import org.javasimon.console.SimonConsolePlugin;
import org.javasimon.console.html.HtmlResourceType;

/**
 * Plugin for testing purpose
 * @author gquintana
 */
public class DummyOtherPlugin extends SimonConsolePlugin {

	public DummyOtherPlugin() {
		super("other", "Other Plugin");
		addResource("js/other.js", HtmlResourceType.JS);
		addResource("js/other.css", HtmlResourceType.CSS);
	}
	
}
