package org.javasimon.spring.webmvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.EnabledManager;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.clock.SimonClock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link MonitoringHandlerInterceptor}.
 *
 * @author gquintana
 */
public class MonitoringHandlerInterceptorTest {

	public static final HttpServletResponse NULL_RESPONSE = null;

	/** Tested interceptor. */
	private MonitoringHandlerInterceptor interceptor;
	private SimonClock clock;
	private EnabledManager manager;

	@BeforeMethod
	public void beforeMethod() {
		clock = mock(SimonClock.class);
		manager = new EnabledManager(clock);
		interceptor = new MonitoringHandlerInterceptor(manager);
	}

	/**
	 * Simulate the Spring MVC Controller view flow.
	 *
	 * @param requestURI Request URI
	 * @param handler Handler
	 * @param controllerSleep Controller wait time
	 * @param viewName View name
	 * @param viewSleep View wait time
	 */
	private void processRequest(String requestURI, Object handler, long controllerSleep, String viewName, long viewSleep) throws InterruptedException {
		HttpServletRequest request = new MockHttpServletRequest("GET", requestURI);
		// Play scenario
		long currentTime = 10L;
		setMockTime(currentTime);

		interceptor.preHandle(request, NULL_RESPONSE, handler);

		// Controller doing its job
		currentTime += controllerSleep;
		setMockTime(currentTime);

		ModelAndView modelAndView = new ModelAndView(viewName);
		interceptor.postHandle(request, NULL_RESPONSE, handler, modelAndView);

		// View doing its job
		currentTime += viewSleep;
		setMockTime(currentTime);

		interceptor.afterCompletion(request, NULL_RESPONSE, handler, null);
	}

	private void setMockTime(long currentTime) {
		when(clock.milliTime()).thenReturn(currentTime);
		when(clock.nanoTime()).thenReturn(currentTime * SimonClock.NANOS_IN_MILLIS);
	}

	/** Test MVC Interceptor with old school Controller. */
	@Test
	public void testRequestWithHandlerMethod() throws Exception {
		// Initialize
		SimonManager.clear();
		// Play scenario
		processRequest("request/uri", new HandlerMethod(new Object(), Object.class.getDeclaredMethod("toString")), 500L, "view", 100L);
		// Check that we grabbed something
		Stopwatch stopwatch = (Stopwatch) manager.getSimon("org.javasimon.mvc.Object.toString.ctrl");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
		stopwatch = (Stopwatch) manager.getSimon("org.javasimon.mvc.Object.toString.view");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
	}

	/** Test MVC Interceptor with @RequestMapping method handler. */
	@Test
	public void testRequestWithHandlerObject() throws InterruptedException {
		// Initialize
		SimonManager.clear();
		// Play scenario
		processRequest("request/uri", new Object(), 500L, "view", 100L);
		// Check that we grabbed something
		Stopwatch stopwatch = (Stopwatch) manager.getSimon("org.javasimon.mvc.Object.ctrl");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
		stopwatch = (Stopwatch) manager.getSimon("org.javasimon.mvc.Object.view");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
	}
}
