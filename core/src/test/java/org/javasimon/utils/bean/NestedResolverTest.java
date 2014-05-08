package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class NestedResolverTest extends SimonUnitTest {

	private static class NestedTestBean {
		private int field;
	}

	private static class TestBean {
		private NestedTestBean bean;
	}

	@Test
	public void testResolveCurrentBean() {
		NestedTestBean nestedTestBean = new NestedTestBean();
		NestedResolver nestedResolver = new NestedResolver(nestedTestBean, "field");

		Assert.assertEquals(nestedResolver.getNestedTarget(), nestedTestBean);
		Assert.assertEquals(nestedResolver.getProperty(), "field");
	}

	@Test
	public void testResolveNestedBean() {
		TestBean testBean = new TestBean();
		NestedTestBean nestedTestBean = new NestedTestBean();
		testBean.bean = nestedTestBean;
		NestedResolver nestedResolver = new NestedResolver(testBean, "bean.field");

		Assert.assertEquals(nestedResolver.getNestedTarget(), nestedTestBean);
		Assert.assertEquals(nestedResolver.getProperty(), "field");
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void testResolveNonExistingNestedBean() {
		TestBean testBean = new TestBean();
		NestedTestBean nestedTestBean = new NestedTestBean();
		testBean.bean = nestedTestBean;
		NestedResolver nestedResolver = new NestedResolver(testBean, "nonExisting.field");
	}

	private static class PropertyTestBean {
		private NestedTestBean nestedBean;

		public NestedTestBean getBean() {
			return nestedBean;
		}
	}

	@Test
	public void testResolveNestedBeanUsingGetter() {
		PropertyTestBean testBean = new PropertyTestBean();
		NestedTestBean nestedTestBean = new NestedTestBean();
		testBean.nestedBean = nestedTestBean;
		NestedResolver nestedResolver = new NestedResolver(testBean, "bean.field");

		Assert.assertEquals(nestedResolver.getNestedTarget(), nestedTestBean);
		Assert.assertEquals(nestedResolver.getProperty(), "field");
	}

}
