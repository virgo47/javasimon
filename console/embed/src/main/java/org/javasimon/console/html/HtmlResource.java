package org.javasimon.console.html;

import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.text.StringifierFactory;

/**
 * JavaScript or CSS resource used by a plugin
 * @author gquintana
 */
public final class HtmlResource {
	/**
	 * Path/location of the resource
	 */
	private final String path;
	/**
	 * Resource type: either JavaScript or CSS
	 */
	private final HtmlResourceType type;

	public HtmlResource(String path, HtmlResourceType type) {
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public HtmlResourceType getType() {
		return type;
	}
	public ObjectJS toJson(StringifierFactory jsonStringifierFactory) {
		return ObjectJS.create(this, jsonStringifierFactory);
	}
	public static ArrayJS toJson(Iterable<HtmlResource> resources, StringifierFactory jsonStringifierFactory) {
		ArrayJS resourcesJS=new ArrayJS();
		for(HtmlResource resource:resources) {
			resourcesJS.addElement(resource.toJson(jsonStringifierFactory));
		}
		return resourcesJS;
	}
}
