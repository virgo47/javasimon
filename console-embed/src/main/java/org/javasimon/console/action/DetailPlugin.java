package org.javasimon.console.action;

import java.io.IOException;
import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.SimonConsolePlugin;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 * Base class for Detail plugins
 * @author gquintana
 */
public abstract class DetailPlugin extends SimonConsolePlugin{

	protected DetailPlugin(String id, String label) {
		super(id, label);
	}
	/**
	 * Indicates this plugin applies to this kind of Simon
	 * @param simon Simon
	 * @return {@code true} if plugin can be executed
	 */
	public boolean supports(Simon simon) {
		return true;
	}
	/**
	 * Callback for flat HTML rendering.
	 * @param context Context (Request, parameters...)
	 * @param htmlBuilder HTML Builder (Response generation helper)
	 * @param simon Simon to render.
	 */
	public DetailHtmlBuilder executeHtml(ActionContext context, DetailHtmlBuilder htmlBuilder, StringifierFactory htmlStringifierFactory, Simon simon) throws IOException {
		return htmlBuilder;
	}
	/**
	 * Callback for flat JSON rendering.
	 * @param context Context (Request, parameters...)
	 * @param pluginDataJS Plugin Data in JSON format.
	 * @param simon Simon to render.
	 */
	public ObjectJS executeJson(ActionContext context, StringifierFactory jsonStringifierFactory, Simon simon) {
		return null;
	}
}
