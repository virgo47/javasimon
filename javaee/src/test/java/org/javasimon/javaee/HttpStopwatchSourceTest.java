package org.javasimon.javaee;

import javax.servlet.http.HttpServletRequest;

import static org.testng.Assert.*;

import org.javasimon.SimonManager;
import org.testng.annotations.*;

import static org.mockito.Mockito.*;

/**
 * Unit test for {@link org.javasimon.javaee.HttpStopwatchSource}.
 *
 * @author gquintana
 */
public class HttpStopwatchSourceTest {
	private HttpStopwatchSource httpStopwatchSource = new HttpStopwatchSource(SimonManager.manager());

	private void assertMonitorName(String actualURI, String expectedName) {
		HttpServletRequest httpRequest = mock(HttpServletRequest.class);
		when(httpRequest.getRequestURI()).thenReturn(actualURI);
		assertEquals(httpStopwatchSource.getMonitorName(httpRequest), expectedName);
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
	public void testJSessionIdRemove() {
		httpStopwatchSource.setPrefix(null);
		httpStopwatchSource.setReplaceUnallowed("_");

		assertMonitorName("/foo/bar/quix?jsessionId=234523;44", "foo.bar.quix_44");
		assertMonitorName("/foo/+bar/;JSESSIONID=2345245DDD72345{}?bubu&res=quix.png", "foo._bar._bubu_res_quix_png");
	}
}
