package org.javasimon.javaee;

import javax.servlet.http.HttpServletRequest;

import org.javasimon.SimonManager;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link org.javasimon.javaee.HttpStopwatchSource}.
 *
 * @author gquintana
 */
public class HttpStopwatchSourceTest {
	private HttpStopwatchSource httpStopwatchSource;

	@BeforeMethod
	public void beforeMethod() {
		httpStopwatchSource = new HttpStopwatchSource(SimonManager.manager());
	}

	private void assertMonitorName(String actualURI, String expectedName) {
		HttpServletRequest httpRequest = mock(HttpServletRequest.class);
		when(httpRequest.getRequestURI()).thenReturn(actualURI);
		assertEquals(httpStopwatchSource.getMonitorName(httpRequest), expectedName);
	}

	private void assertMonitorName(String actualURI, String httpMethod, String expectedName) {
		HttpServletRequest httpRequest = mock(HttpServletRequest.class);
		when(httpRequest.getRequestURI()).thenReturn(actualURI);
		when(httpRequest.getMethod()).thenReturn(httpMethod);
		assertEquals(httpStopwatchSource.getMonitorName(httpRequest), expectedName);
	}

	@Test
	public void testIncludeNoMethodsByDefault() {
		Assert.assertEquals(httpStopwatchSource.getIncludeHttpMethodName(), HttpStopwatchSource.IncludeHttpMethodName.NEVER);
	}

	@Test
	public void testGetIncludeHttpMethodName() {
		httpStopwatchSource.setIncludeHttpMethodName(HttpStopwatchSource.IncludeHttpMethodName.ALWAYS);
		Assert.assertEquals(httpStopwatchSource.getIncludeHttpMethodName(), HttpStopwatchSource.IncludeHttpMethodName.ALWAYS);
	}

	@Test
	public void testGetMonitorName() {
		httpStopwatchSource.setPrefix(null);

		// Normal
		assertMonitorName("/foo/bar/quix", "foo.bar.quix");
		// Unallowed chars
		assertMonitorName("/foo/+bar/quix.png", "foo._bar.quix_png");
		// Doubled chars
		assertMonitorName("/foo//bar/quix..png", "foo.bar.quix_png");
		assertMonitorName("/foo/++bar/++/quix.png", "foo._bar._.quix_png");

		httpStopwatchSource.setPrefix(""); // should be the same
		assertMonitorName("/foo/++bar/++/quix.png", "foo._bar._.quix_png");

		httpStopwatchSource.setReplaceUnallowed("");
		// Unallowed chars
		// Unallowed chars
		assertMonitorName("/foo/+bar/quix.png", "foo.bar.quixpng");
		// Doubled chars
		assertMonitorName("/foo//bar/quix..png", "foo.bar.quixpng");
		assertMonitorName("/foo/++bar/++/quix.png", "foo.bar.quixpng");

		httpStopwatchSource.setPrefix("testing.prefix");
		assertMonitorName("/foo/++bar/++/quix.png", "testing.prefix.foo.bar.quixpng");
	}

	@Test
	public void testTrailingStuffAndDoubleSlashRemoval() {
		httpStopwatchSource.setPrefix(null);
		httpStopwatchSource.setReplaceUnallowed("_");

		assertMonitorName("/foo/bar/2345", "foo.bar");
		assertMonitorName("/foo/+bar/", "foo._bar");
		assertMonitorName("/foo/+bar//", "foo._bar");
		assertMonitorName("/foo//+bar/234/@#$%/23_+", "foo._bar");
	}

	@Test
	public void testJSessionIdRemoval() {
		httpStopwatchSource.setPrefix(null);
		httpStopwatchSource.setReplaceUnallowed("_");

		// real Simons will never have names with paramaters because processed URIs are without parameters,
		// these are here just to test jSessionId removal patterns
		assertMonitorName("/foo/bar/quix?jsessionId=234523;44", "foo.bar.quix_44");
		assertMonitorName("/foo/+bar/;JSESSIONID=2345245DDD72345{}?bubu&res=quix.png", "foo._bar._bubu_res_quix_png");
	}

	@DataProvider(name = "allMethodsUrlMappingTest")
	public static Object[][] allMethodsUrlMappingTest() {
		return new Object[][]
			{
				{ "foo/bar/quix", "GET", "foo.bar.quix.GET" },
				{ "foo/bar/quix", "POST", "foo.bar.quix.POST" },
				{ "foo/bar/quix", "DELETE", "foo.bar.quix.DELETE" },
				{ "foo/bar/quix", "PUT", "foo.bar.quix.PUT" },
				{ "foo//bar/image.png", "GET", "foo.bar.image_png.GET" }
			};
	}

	@Test(dataProvider = "allMethodsUrlMappingTest")
	public void testAllMethodsUrlMappingTest(String uri, String httpMethod, String expectedName) {
		httpStopwatchSource.setPrefix(null);
		httpStopwatchSource.setIncludeHttpMethodName(HttpStopwatchSource.IncludeHttpMethodName.ALWAYS);
		assertMonitorName(uri, httpMethod, expectedName);
	}

	@DataProvider(name = "nonGetUrlMappingTest")
	public static Object[][] primeNumbers() {
		return new Object[][]
			{
				{ "foo/bar/quix", "GET", "foo.bar.quix" },
				{ "foo/bar/quix", "POST", "foo.bar.quix.POST" },
				{ "foo/bar/quix", "PUT", "foo.bar.quix.PUT" },
				{ "foo/bar/quix", "DELETE", "foo.bar.quix.DELETE" },
				{ "foo//bar/image.png", "GET", "foo.bar.image_png" },
				{ "foo//bar/some.item", "POST", "foo.bar.some_item.POST" }
			};
	}

	@Test(dataProvider = "nonGetUrlMappingTest")
	public void testNonGetUrlMappingTest(String uri, String httpMethod, String expectedName) {
		httpStopwatchSource.setPrefix(null);
		httpStopwatchSource.setIncludeHttpMethodName(HttpStopwatchSource.IncludeHttpMethodName.NON_GET);
		assertMonitorName(uri, httpMethod, expectedName);
	}

}
