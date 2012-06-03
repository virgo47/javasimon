package org.javasimon.console.action;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import org.javasimon.console.ActionContext;
import org.javasimon.console.ActionException;
import org.javasimon.console.SimonConsolePlugin;
import org.javasimon.console.json.ArrayJS;

/**
 * Action to get plugin list in JSON format.
 * @author gquintana
 */
public class PluginsJsonAction extends AbstractJsonAction {
	public static final String PATH="/data/plugins.json";
	/**
	 * Expected plugin type
	 */
	private Class<? extends SimonConsolePlugin> type;
	private boolean invalidType=false;
	
	public PluginsJsonAction(ActionContext context) {
		super(context);
	}
	@Override
	public void readParameters() {
		super.readParameters();
		String sType=getContext().getParameterAsString("type", null);
		if (sType!=null) {
			try {
				Class<?> oType = Class.forName(sType);
				if (SimonConsolePlugin.class.isAssignableFrom(oType)) {
					type = (Class<? extends SimonConsolePlugin>) oType;
				} else {
					invalidType = true;
				}
			} catch (Exception e) {
				invalidType = true;
			}
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		super.execute();
		// Get plugin list
		List<? extends SimonConsolePlugin> plugins;
		if (invalidType) {
			plugins=Collections.emptyList();
		} else if (type==null) {
			plugins=getContext().getPluginManager().getPlugins();
		} else {
			plugins=getContext().getPluginManager().getPluginsByType(type);
		}
		// Convert list to JSON
		ArrayJS pluginsJS=new ArrayJS();
		for(SimonConsolePlugin plugin:plugins) {
			pluginsJS.addElement(plugin.toJson(jsonStringifierFactory));
		}
		pluginsJS.write(getContext().getWriter());
	}

	
}
