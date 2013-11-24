package org.javasimon.spring;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.SwitchingManager;

import org.springframework.beans.factory.InitializingBean;

/**
 * Spring bean that configures Simon manager using {@link org.javasimon.ManagerConfiguration} facility.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class SimonConfigurationBean implements InitializingBean {
	private Manager simonManager = SimonManager.manager();
	private String configurationPath;

	public Manager getSimonManager() {
		return simonManager;
	}

	public void setSimonManager(Manager simonManager) {
		this.simonManager = simonManager;
	}

	/**
	 * Creates new {@link org.javasimon.SwitchingManager} instead of using {@link org.javasimon.SimonManager#manager()}.
	 */
	public void setNewManager() {
		simonManager = new SwitchingManager();
	}

	/**
	 * Loads configuration for the manager from the specified resource path.
	 *
	 * @param configurationPath resource path to the configuration XML
	 * @throws IOException thrown if the resource is not found or the configuration XML is not well formed
	 */
	public void setConfiguration(String configurationPath) throws IOException {
		this.configurationPath = configurationPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (configurationPath != null) {
			InputStream is = getClass().getClassLoader().getResourceAsStream(configurationPath);
			if (is == null) {
				throw new FileNotFoundException(configurationPath);
			}
			simonManager.configuration().readConfig(new InputStreamReader(is));
		}
	}
}
