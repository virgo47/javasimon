package org.javasimon.console.plugin;

import java.io.IOException;
import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.action.DetailHtmlBuilder;
import org.javasimon.console.action.DetailPlugin;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 * Detail plugin for testing purpose
 * @author gquintana
 */
public class DummyDetailPlugin extends DetailPlugin {

	public DummyDetailPlugin() {
		super("dummy", "Dummy Plugin");
	}

	@Override
	public DetailHtmlBuilder executeHtml(ActionContext context, DetailHtmlBuilder htmlBuilder, StringifierFactory htmlStringifierFactory, Simon simon) throws IOException {
		return htmlBuilder.beginRow()
			.labelCell("Message")
			.valueCell("Hello world!")
			.endRow();
	}

	@Override
	public ObjectJS executeJson(ActionContext context, StringifierFactory jsonStringifierFactory, Simon simon) {
		ObjectJS pluginDataJS=new ObjectJS();
		pluginDataJS.setSimpleAttribute("message", "Hello world!", jsonStringifierFactory.getStringifier(String.class));
		return pluginDataJS;
	}
	
	
}
