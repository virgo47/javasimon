package org.javasimon.console;

import java.util.List;
import org.javasimon.console.action.DetailPlugin;
import org.javasimon.console.plugin.DummyDetailPlugin;
import org.javasimon.console.plugin.DummyOtherPlugin;
import org.javasimon.console.plugin.QuantilesDetailPlugin;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/**
 * Unit test for {@link SimonConsolePluginManager}
 * @author gerald
 */
public class SimonConsolePluginManagerTest {
	private SimonConsolePluginManager pluginManager;
	@BeforeMethod
	public void before() {
		pluginManager=new SimonConsolePluginManager();
	}
	private void assertPluginByType(Class<? extends SimonConsolePlugin> pluginType) {
		boolean found=false;
		for(SimonConsolePlugin plugin:pluginManager.getPlugins()) {
			if (plugin.getClass().equals(pluginType)) {
				found=true;
				break;
			}
		}
		assertTrue(found,"Plugin "+pluginType+" not found");
	}
	/**
	 * Test {@link SimonConsolePluginManager#addPlugins}
	 */
	@Test
	public void testAddPlugins() {
		pluginManager.addPlugins(" "+DummyDetailPlugin.class.getName()+",\r\n\t"+DummyOtherPlugin.class.getName()+"   ");
		assertPluginByType(DummyDetailPlugin.class);
		assertPluginByType(DummyOtherPlugin.class);
	}
	/**
	 * Test {@link SimonConsolePluginManager#addPlugins}
	 * when class can not be found.
	 */
	@Test(expectedExceptions={IllegalArgumentException.class})
	public void testAddPluginNotFound() {
		pluginManager.addPlugin("com.mycompany.myapp.MySimonPlugin");
	}
	/**
	 * Test {@link SimonConsolePluginManager#addPlugins}
	 * when class is not a plugin.
	 */
	@Test(expectedExceptions={IllegalArgumentException.class})
	public void testAddPluginType() {
		pluginManager.addPlugin("java.lang.String");
	}
	/**
	 * Test {@link SimonConsolePluginManager#addPlugins}
	 * when plugin is registered twice.
	 */
	@Test(expectedExceptions={IllegalArgumentException.class})
	public void testAddPluginDuplicate() {
		pluginManager.addPlugin(DummyDetailPlugin.class.getName()+","+DummyDetailPlugin.class.getName());
	}
	/**
	 * Test {@link SimonConsolePluginManager#getPluginsByType(java.lang.Class) }
	 */
	@Test
	public void testGetPluginByType() {
		pluginManager.addPlugin(DummyDetailPlugin.class);
		pluginManager.addPlugin(QuantilesDetailPlugin.class);
		pluginManager.addPlugin(DummyOtherPlugin.class);
		List<DetailPlugin> detailPlugins=pluginManager.getPluginsByType(DetailPlugin.class);
		assertEquals(detailPlugins.size(), 2);
	}
}
