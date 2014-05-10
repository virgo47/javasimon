package org.javasimon.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collection of plugin used by Simon console actions
 * @author gquintana
 */
public class SimonConsolePluginManager {
	/**
	 * List of plugins
	 */
	private final List<SimonConsolePlugin> plugins=new ArrayList<>();
	/**
	 * Return plugin list.
	 */
	public List<SimonConsolePlugin> getPlugins() {
		return Collections.unmodifiableList(plugins);
	}
	/**
	 * Return plugin list, filtered by plugin type.
	 */
	public <T extends SimonConsolePlugin> List<T> getPluginsByType(Class<T> pluginType) {
		List<T> specPlugins=new ArrayList<>();
		for(SimonConsolePlugin plugin:plugins) {
			if (pluginType.isInstance(plugin)) {
				specPlugins.add(pluginType.cast(plugin));
			}
		}
		return Collections.unmodifiableList(specPlugins);
	}
	/**
	 * Return plugin, filtered by plugin Id.
	 */
	public SimonConsolePlugin getPluginById(String id) {
		for(SimonConsolePlugin plugin:plugins) {
			if (plugin.getId().equals(id)) {
				return plugin;
			}
		}
		return null;
	}
	/**
	 * Register a plugin
	 * @param plugin Plugin
	 */
	public void addPlugin(SimonConsolePlugin plugin) {
		SimonConsolePlugin existingPlugin=getPluginById(plugin.getId());
		if (existingPlugin!=null) {
			throw new IllegalArgumentException("Plugin "+plugin.getId()+" already registered");
		}
		plugins.add(plugin);
	}
	/**
	 * Register a plugin
	 * @param pluginType Plugin type
	 */
	public <T extends SimonConsolePlugin> void addPlugin(Class<T> pluginType) {
		try {
			SimonConsolePlugin plugin = pluginType.newInstance();
			addPlugin(plugin);
		} catch (InstantiationException instantiationException) {
			throw new IllegalArgumentException("Invalid plugin type "+pluginType.getName(), instantiationException);
		} catch (IllegalAccessException illegalAccessException) {
			throw new IllegalArgumentException("Invalid plugin type "+pluginType.getName(), illegalAccessException);
		}
	}
	/**
	 * Register a plugin
	 * @param pluginTypeName Plugin type
	 */
	public void addPlugin(String pluginTypeName) {
		try {
			Class<?> pluginType = Class.forName(pluginTypeName);
			if (SimonConsolePlugin.class.isAssignableFrom(pluginType)) {
				addPlugin((Class<? extends SimonConsolePlugin>) pluginType);
			} else {
				throw new IllegalArgumentException("Invalid plugin type "+pluginTypeName);
			}
		} catch (ClassNotFoundException classNotFoundException) {
			throw new IllegalArgumentException("Invalid plugin type "+pluginTypeName, classNotFoundException);
		}
	}
	/**
	 * Register plugins
	 * @param pluginTypeNames Comma separated list for plugin classes
	 */
	public void addPlugins(String pluginTypeNames) {
		String[] pluginTypeNameList=pluginTypeNames.split(",");
		for(String pluginTypeName:pluginTypeNameList) {
			pluginTypeName=pluginTypeName.trim();
			if (!pluginTypeName.equals("")) {
				addPlugin(pluginTypeName);
			}
		}
	}
	/**
	 * Return plugin, filtered by plugin Id.
	 */
	public boolean removePluginById(String id) {
		SimonConsolePlugin plugin=getPluginById(id);
		if (plugin==null) {
			return false;
		} else {
			return plugins.remove(plugin);
		}
	}
	/**
	 * Get all action bindings of all plugins.
	 */
	public List<ActionBinding> getActionBindings() {
		List<ActionBinding> actionBindings=new ArrayList<>();
		for(SimonConsolePlugin plugin: plugins) {
			actionBindings.addAll(plugin.getActionBindings());
		}
		return Collections.unmodifiableList(actionBindings);
	}
}
