package org.javasimon.console.action;

import java.io.IOException;
import javax.servlet.ServletException;
import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.ObjectJS;

/**
 * Export one Simons as a JSON object for display in detail view.
 * All attributes of simons are exported.
 * Path: http://.../data/detail.html?name=o.j...SimonName&timeFormat=MILLISECOND
 *
 * @author gquintana
 */
public class DetailJsonAction extends AbstractJsonAction {

	public static final String PATH = "/data/detail.json";
	/**
	 * Name of the simon from where to start.
	 * {@code null} means root.
	 */
	private String name;
	public DetailJsonAction(ActionContext context) {
		super(context);
	}

	@Override
	public void readParameters() {
		super.readParameters();
		name = getContext().getParameterAsString("name", null);
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		if (name==null) {
			throw new ActionException("Null name");
		}
		Simon simon=getContext().getManager().getSimon(name);
		if (simon==null) {
			throw new ActionException("Simon \""+name+"\" not found");
		}
		getContext().setContentType("application/json");
		ObjectJS simonJS = createObjectJS(simon);
		// Plugins
		ArrayJS pluginsJS=new ArrayJS();
		for(DetailPlugin plugin:getContext().getPluginManager().getPluginsByType(DetailPlugin.class)) {
			if (plugin.supports(simon)) {
				ObjectJS pluginJS=plugin.toJson(jsonStringifierFactory);
				final ObjectJS pluginDataJS = plugin.executeJson(getContext(), jsonStringifierFactory, simon);
				if (pluginDataJS!=null) {
					pluginJS.setAttribute("data", pluginDataJS);
				}
				pluginsJS.addElement(pluginJS);
			}
		}
		simonJS.setAttribute("plugins", pluginsJS);
		simonJS.write(getContext().getWriter());
	}
}
