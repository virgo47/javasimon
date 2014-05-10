package org.javasimon.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.javasimon.console.html.HtmlResource;
import org.javasimon.console.html.HtmlResourceType;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;

/**
 * Base class for plugins.
 * @author gquintana
 */
public abstract class SimonConsolePlugin {
	/**
	 * Plugin Id.
	 */
	private String id;
	/**
	 * Label, title short description.
	 */
	private String label;
	/**
	 * JavaScript and CSS resources used by this plugin
	 */
	private List<HtmlResource> resources=new ArrayList<>();
	/**
	 * Default constructor.
	 */
	protected SimonConsolePlugin(String id, String label) {
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	public Collection<ActionBinding> getActionBindings() {
		return Collections.emptyList();
	}
	/**
	 * Add a resource to this plugin.
	 * @param path Resource path
	 * @param type  Resource type
	 */
	public final void addResource(String path, HtmlResourceType type) {
		resources.add(new HtmlResource(path, type));
	}
	/**
	 * Get resources used by this plugin.
	 */
	public final List<HtmlResource> getResources() {
		return Collections.unmodifiableList(resources);
	}
		/**
	 * Gather resources used by all Detail plugins in the plugin manager
	 * @param context Context containing plugin manager
	 * @return Detail plugins resources.
	 */
	public static List<HtmlResource> getResources(ActionContext context, Class<? extends SimonConsolePlugin> pluginType) {
		List<HtmlResource> resources=new ArrayList<>();
		for(SimonConsolePlugin plugin:context.getPluginManager().getPluginsByType(pluginType)) {
			resources.addAll(plugin.getResources());
		}
		return resources;
	}
	/**
	 * Serialize plugin data into a JSON object
	 * @param jsonStringifierFactory Stringifier factory
	 * @return JSON object representing plugin 
	 */
	public final ObjectJS toJson(StringifierFactory jsonStringifierFactory) {
		final ObjectJS pluginJS=new ObjectJS();
		final Stringifier<String> stringStringifier=jsonStringifierFactory.getStringifier(String.class);
		pluginJS.setSimpleAttribute("id", getId(), stringStringifier);
		pluginJS.setSimpleAttribute("label", getLabel(), stringStringifier);
		pluginJS.setAttribute("resources", HtmlResource.toJson(
			getResources(), 
			jsonStringifierFactory));
		return pluginJS;
	}
}
