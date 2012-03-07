package org.javasimon.spring;

import java.util.ArrayList;
import java.util.List;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.SwitchingManager;
import org.javasimon.callback.Callback;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean which produces Simon manager.
 * Spring configuration:
 * <code><pre>
 *	&lt;bean id="jmxServer" class="org.springframework.jmx.support.MBeanServerFactoryBean"&gt;
 *		&lt;property name="locateExistingServerIfPossible" value="true"/&gt;
 *	&lt;/bean&gt;
 *	&lt;bean id="simonManager" class="org.javasimon.spring.ManagerFactoryBean"&gt;
 *		&lt;property name="callbacks"&gt;
 *			&lt;list&gt;
 *				&lt;bean class="org.javasimon.jmx.JmxRegisterCallback"&gt;
 *					&lt;constructor-arg ref="jmxServer"/&gt;
 *					&lt;constructor-arg value="org.javasimon"/&gt;
 *				&lt;/bean&gt;
 *				&lt;bean class="org.javasimon.utils.LoggingCallback"/&gt;
 *			&lt;/list&gt;
 *		&lt;/property&gt;
 *	&lt;/bean&gt;
 *	&lt;bean id="simonInterceptor" class="org.javasimon.spring.MonitoringInterceptor"&gt;
 *		&lt;property name="manager" ref="simonManager"/&gt;
 *	&lt;/bean&gt;
 * </pre></code>
 * @author gquintana
 */
public class ManagerFactoryBean implements FactoryBean<Manager> {
	/**
	 * Flag indicating whether Simon manager singleton should be used.
	 * Defaults to true.
	 */
	private boolean singleton=true;
	/**
	 * List of callbacks to add to the manager
	 */
	private List<Callback> callbacks=new ArrayList<Callback>();
	/**
	 * Flag indicated whether Simon manager should enabled or not.
	 * Defaults to true.
	 */
	private boolean enabled=true;
	/**
	 * Get simon manager instance.
	 * If singleton is enabled SimonManager.manager() is invoked else new
	 * Manager is created.
	 * Then callbacks are appended to this manager
	 * @return Simon manager
	 */
	public Manager getObject() throws Exception {
		Manager manager;
		if (singleton) {
			manager=SimonManager.manager();
		} else {
			manager=new SwitchingManager();
		}
		registerCallbacks(manager);
		configureEnabled(manager);
		return manager;
	}

	/**
	 * @return Always Manager class
	 */
	public Class<?> getObjectType() {
		return Manager.class;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	/**
	 * Register callbacks in given manager
	 * @param manager Manager
	 */
	private void registerCallbacks(Manager manager) {
		if (callbacks!=null) {
			for(Callback callback:callbacks) {
				manager.callback().addCallback(callback);
			}
		}
	}
	public List<Callback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<Callback> callbacks) {
		this.callbacks = callbacks;
	}
	/**
	 * When needed toggle the enabled flag of given Simon manager
	 * @param manager Simon manager
	 */
	private void configureEnabled(Manager manager) {
		if (enabled!=manager.isEnabled()) {
			if (enabled) {
				manager.enable();
			} else {
				manager.disable();
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
