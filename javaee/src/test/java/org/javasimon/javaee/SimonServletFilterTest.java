package org.javasimon.javaee;

import org.javasimon.Manager;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.source.StopwatchSource;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class SimonServletFilterTest {

	private SimonServletFilter filter;
	private FilterConfig filterConfig;

	@BeforeMethod
	public void beforeMethod() {
		filter = new SimonServletFilter();
		filterConfig = mock(FilterConfig.class);

		ServletContext servletContext = mock(ServletContext.class);
		when(filterConfig.getServletContext()).thenReturn(servletContext);
	}

	@Test
	public void testHttpStopwatchSourceParameterIsSet() {
		when(filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_CLASS))
				.thenReturn(HttpStopwatchSource.class.getCanonicalName());
		when(filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_PROPS))
				.thenReturn("includeHttpMethodName=ALWAYS");

		filter.init(filterConfig);
		HttpStopwatchSource httpStopwatchSource = (HttpStopwatchSource) filter.getStopwatchSource();
		Assert.assertEquals(httpStopwatchSource.getIncludeHttpMethodName(), HttpStopwatchSource.IncludeHttpMethodName.ALWAYS);
	}

	@Test
	public void testBeanParametersAreSet() {
		when(filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_CLASS))
				.thenReturn(TestBean.class.getName());
		when(filterConfig.getInitParameter(SimonServletFilter.INIT_PARAM_STOPWATCH_SOURCE_PROPS))
				.thenReturn("strProp=str;intProp=123");

		filter.init(filterConfig);
		TestBean testBean = (TestBean) filter.getStopwatchSource();
		Assert.assertEquals(testBean.getStrProp(), "str");
		Assert.assertEquals(testBean.getIntProp(), 123);
	}

	public static class TestBean implements StopwatchSource {
		String strProp;
		int intProp;

		public TestBean(Manager manager) {

		}

		public String getStrProp() {
			return strProp;
		}

		public void setStrProp(String strProp) {
			this.strProp = strProp;
		}

		public int getIntProp() {
			return intProp;
		}

		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}

		@Override
		public Split start(Object location) {
			return null;
		}

		@Override
		public boolean isMonitored(Object location) {
			return false;
		}

		@Override
		public Simon getMonitor(Object location) {
			return null;
		}

		@Override
		public Manager getManager() {
			return null;
		}
	}
}
