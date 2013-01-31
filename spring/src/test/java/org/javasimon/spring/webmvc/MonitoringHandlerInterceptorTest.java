package org.javasimon.spring.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test for {@link MonitoringHandlerInterceptor}.
 *
 * @author gquintana
 */
public class MonitoringHandlerInterceptorTest {
	/**
	 * Tested interceptor.
	 */
	private final MonitoringHandlerInterceptor interceptor = new MonitoringHandlerInterceptor();

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
		HttpServletResponse response = null;
		// Play scenario
		interceptor.preHandle(request, response, handler);
		Thread.sleep(controllerSleep); // Controller doing its job
		ModelAndView modelAndView = new ModelAndView(viewName);
		interceptor.postHandle(request, response, handler, modelAndView);
		Thread.sleep(viewSleep); // View doing its job
		interceptor.afterCompletion(request, response, handler, null);

	}

	/**
	 * Test MVC Interceptor with old school Controller.
	 */
	@Test
	public void testRequestWithHandlerMethod() throws Exception {
		// Initialize 
		SimonManager.clear();
		// Play scenario
		processRequest("request/uri", new HandlerMethod(new Object(), Object.class.getDeclaredMethod("toString", new Class[0])), 500L, "view", 100L);
		// Check that we grabbed something
		Stopwatch stopwatch = (Stopwatch) SimonManager.getSimon("org.javasimon.mvc.Object.toString.ctrl");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
		stopwatch = (Stopwatch) SimonManager.getSimon("org.javasimon.mvc.Object.toString.view");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
	}

	/**
	 * Test MVC Interceptor with @RequestMapping method handler.
	 */
	@Test
	public void testRequestWithHandlerObject() throws InterruptedException {
		// Initialize 
		SimonManager.clear();
		// Play scenario
		processRequest("request/uri", new Object(), 500L, "view", 100L);
		// Check that we grabbed something
		Stopwatch stopwatch = (Stopwatch) SimonManager.getSimon("org.javasimon.mvc.Object.ctrl");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
		stopwatch = (Stopwatch) SimonManager.getSimon("org.javasimon.mvc.Object.view");
		assertNotNull(stopwatch);
		assertEquals(stopwatch.getCounter(), 1);
	}
}
