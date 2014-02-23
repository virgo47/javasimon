package org.javasimon.utils.bean;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ClassUtilsTest {
	public ClassUtils classUtils = new ClassUtils();

	private static class TestBean {
		 int intField;
		 String strField;

		public int getIntField() {
			return intField;
		}

		public void setIntField(int intField) {
			this.intField = intField;
		}

		public String getStrField() {
			return strField;
		}

		public void setStrField(String strField) {
			this.strField = strField;
		}

		public void setStrField(TestBean bean) {

		}

		public void otherMethod(String str, int i) {

		}
	}

	private static class InheritedTestBean extends TestBean {
	}

	@Test
	public void testGetField() {
		Field field = classUtils.getField(TestBean.class, "intField");
		Assert.assertEquals(field.getType(), int.class);
	}

	@Test
	public void testGetNonExistingField() {
		Field field = classUtils.getField(TestBean.class, "nonExistingField");
		Assert.assertEquals(field, null);
	}

	@Test
	public void testGetInhertiedField() {
		Field field = classUtils.getField(InheritedTestBean.class, "intField");
		Assert.assertEquals(field.getType(), int.class);
	}

	@Test
	public void testGetSetter() {
		Method method = classUtils.getSetter(TestBean.class, "strField", String.class);
		Assert.assertEquals(method.getName(), "setStrField");
	}

	@Test
	public void testGetSetterOfWrongType() {
		Method method = classUtils.getSetter(TestBean.class, "strField", int.class);
		Assert.assertEquals(method, null);
	}

	@Test
	public void testGetNonExistingSetter() {
		Method method = classUtils.getSetter(TestBean.class, "someNoneExistng", int.class);
		Assert.assertEquals(method, null);
	}

	@Test
	public void testGetInheritedSetter() {
		Method method = classUtils.getSetter(InheritedTestBean.class, "strField", String.class);
		Assert.assertEquals(method.getName(), "setStrField");
	}

	@Test
	public void getMultiplePotentialSetters() {
		Set<Method> methods = classUtils.getSetters(TestBean.class, "strField");
		Assert.assertEquals(methods.size(), 2);
	}

	@Test
	public void getSinglePotentialSetter() {
		Set<Method> methods = classUtils.getSetters(TestBean.class, "intField");
		Assert.assertEquals(methods.size(), 1);
	}

	@Test
	public void getNonExistingPotentialSetters() {
		Set<Method> methods = classUtils.getSetters(TestBean.class, "someNonExisting");
		Assert.assertEquals(methods.size(), 0);
	}

	@Test
	public void getInheritedPotentialSetters() {
		Set<Method> methods = classUtils.getSetters(InheritedTestBean.class, "strField");
		Assert.assertEquals(methods.size(), 2);
	}

	@Test
	public void getSetterType() {
		Method setter = classUtils.getSetter(TestBean.class, "strField", String.class);
		Class<?> type = classUtils.getSetterType(setter);
		Assert.assertEquals(type, String.class);
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void getSetterTypeWithIncorrectSignature() throws Exception {
		Method getter = TestBean.class.getDeclaredMethod("getStrField");
		Class<?> type = classUtils.getSetterType(getter);
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void getSetterTypeWithIncorrectSignature2() throws Exception {
		Method otherMethod = TestBean.class.getDeclaredMethod("otherMethod", String.class, int.class);
		Class<?> type = classUtils.getSetterType(otherMethod);
	}

}
