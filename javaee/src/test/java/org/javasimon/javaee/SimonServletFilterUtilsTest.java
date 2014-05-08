package org.javasimon.javaee;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import org.javasimon.DisabledManager;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.source.AbstractStopwatchSource;
import org.javasimon.source.CachedMonitorSource;
import org.javasimon.source.MonitorSource;
import org.javasimon.source.StopwatchSource;

import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimonServletFilterUtilsTest {

	@Test
	public void testDefaultInitStopwatchSource() {
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig(null, null), null);
		Assert.assertNull(monitorSource.getManager());
		Assert.assertFalse(monitorSource instanceof CachedMonitorSource);
		Assert.assertTrue(monitorSource instanceof HttpStopwatchSource);
	}

	@Test
	public void testDefaultInitStopwatchSourceWithManager() {
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig(null, Boolean.FALSE.toString()), SimonManager.manager());
		Assert.assertEquals(monitorSource.getManager(), SimonManager.manager());
		Assert.assertFalse(monitorSource instanceof CachedMonitorSource);
		Assert.assertTrue(monitorSource instanceof HttpStopwatchSource);
	}

	@Test
	public void testCachedInitStopwatchSource() {
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig(null, Boolean.TRUE.toString()), null);
		Assert.assertNull(monitorSource.getManager());
		Assert.assertTrue(monitorSource instanceof CachedMonitorSource);
	}

	@Test
	public void testInitStopwatchSourceWithCustomManager() {
		Manager manager = new DisabledManager();
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig(null, Boolean.TRUE.toString()), manager);
		Assert.assertEquals(monitorSource.getManager(), manager);
		Assert.assertTrue(monitorSource instanceof CachedMonitorSource);
	}

	@Test
	public void testInitStopwatchCustomSourceManagerViaConstructor() {
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig("org.javasimon.javaee.MonitorSourceWithManagerViaConstructor", null), SimonManager.manager());
		Assert.assertEquals(monitorSource.getManager(), SimonManager.manager());
		Stopwatch stopwatch = SimonManager.manager().getStopwatch(MonitorSourceWithManagerViaConstructor.CONSTANT_NAME);
		Assert.assertEquals(monitorSource.getMonitor(new MockHttpServletRequest("GET", "whatever")), stopwatch);
		Assert.assertEquals(monitorSource.getMonitor(new MockHttpServletRequest("GET", "something else")), stopwatch);
	}

	@Test
	public void testInitStopwatchCustomSourceManagerViaSetter() {
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig("org.javasimon.javaee.MonitorSourceNoConstructorManagerViaSetter", null), SimonManager.manager());
		Assert.assertEquals(monitorSource.getManager(), SimonManager.manager());
		Assert.assertEquals(monitorSource.getMonitor(new MockHttpServletRequest("GET", "whatever")).getName(), "whatever");
		Assert.assertEquals(monitorSource.getMonitor(new MockHttpServletRequest("GET", "somethingElse")).getName(), "somethingElse");
	}

	@Test
	public void testInitStopwatchCustomSourceManagerViaConstructorBroken() {
		MonitorSource<HttpServletRequest, Stopwatch> monitorSource = SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig("org.javasimon.javaee.MonitorSourceWithManagerViaConstructorBroken", null), SimonManager.manager());
		Assert.assertEquals(monitorSource.getManager(), null);
		Stopwatch stopwatch = SimonManager.manager().getStopwatch(MonitorSourceWithManagerViaConstructorBroken.CONSTANT_NAME);
		try {
			Assert.assertEquals(monitorSource.getMonitor(new MockHttpServletRequest("GET", "whatever")), stopwatch);
		} catch (NullPointerException e) {
			return;
		}
		Assert.fail("Missing NPE now");
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Stopwatch source class must have public constructor or public setter with Manager argument \\(used class org.javasimon.javaee.MonitorSourceNoConstructorNoSetter\\)")
	public void testInitStopwatchCustomSourceManagerNoConstructorNoSetter() {
		SimonServletFilterUtils.initStopwatchSource(
			prepareFilterConfig("org.javasimon.javaee.MonitorSourceNoConstructorNoSetter", null), SimonManager.manager());
	}

	private FilterConfig prepareFilterConfig(String monitorSourceClassName, String caching) {
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_CLASS, monitorSourceClassName);
		filterConfig.addInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_CACHE, caching);
		return filterConfig;
	}
}

class MonitorSourceWithManagerViaConstructor extends AbstractStopwatchSource {
	public static final String CONSTANT_NAME = "constantName";

	public MonitorSourceWithManagerViaConstructor(Manager manager) {
		super(manager);
	}

	@Override
	protected String getMonitorName(Object location) {
		return CONSTANT_NAME;
	}
}

class MonitorSourceWithManagerViaConstructorBroken extends AbstractStopwatchSource {
	public static final String CONSTANT_NAME = "constantName";

	public MonitorSourceWithManagerViaConstructorBroken(@SuppressWarnings("UnusedParameters") Manager manager) {
		super(null);
	}

	@Override
	protected String getMonitorName(Object location) {
		return CONSTANT_NAME;
	}
}

@SuppressWarnings("UnusedDeclaration")
class MonitorSourceNoConstructorManagerViaSetter implements StopwatchSource<HttpServletRequest> {
	private Manager manager;

	@Override
	public boolean isMonitored(HttpServletRequest location) {
		return true;
	}

	@Override
	public Stopwatch getMonitor(HttpServletRequest location) {
		return manager.getStopwatch(location.getRequestURI());
	}

	@Override
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Override
	public Split start(HttpServletRequest location) {
		return null; // not used here
	}
}

/**
 * Source should either have constructor or setter (though setter is not a solution for AbstractStopwatchSource, because manager there is final)
 * to get initialized by SimonServletFilter.
 */
@SuppressWarnings("UnusedDeclaration")
class MonitorSourceNoConstructorNoSetter extends AbstractStopwatchSource {

	public MonitorSourceNoConstructorNoSetter() {
		super(null);
	}

	@Override
	protected String getMonitorName(Object location) {
		return null;
	}

}
