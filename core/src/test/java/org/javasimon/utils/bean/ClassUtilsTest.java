package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ClassUtilsTest extends SimonUnitTest {

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
		Field field = ClassUtils.getField(TestBean.class, "intField");
		Assert.assertEquals(field.getType(), int.class);
	}

	@Test
	public void testGetNonExistingField() {
		Field field = ClassUtils.getField(TestBean.class, "nonExistingField");
		Assert.assertEquals(field, null);
	}

	@Test
	public void testGetInhertiedField() {
		Field field = ClassUtils.getField(InheritedTestBean.class, "intField");
		Assert.assertEquals(field.getType(), int.class);
	}

	@Test
	public void testGetSetter() {
		Method method = ClassUtils.getSetter(TestBean.class, "strField", String.class);
		Assert.assertEquals(method.getName(), "setStrField");
	}

	@Test
	public void testGetSetterOfWrongType() {
		Method method = ClassUtils.getSetter(TestBean.class, "strField", int.class);
		Assert.assertEquals(method, null);
	}

	@Test
	public void testGetNonExistingSetter() {
		Method method = ClassUtils.getSetter(TestBean.class, "someNoneExistng", int.class);
		Assert.assertEquals(method, null);
	}

	@Test
	public void testGetInheritedSetter() {
		Method method = ClassUtils.getSetter(InheritedTestBean.class, "strField", String.class);
		Assert.assertEquals(method.getName(), "setStrField");
	}

	@Test
	public void getMultiplePotentialSetters() {
		Set<Method> methods = ClassUtils.getSetters(TestBean.class, "strField");
		Assert.assertEquals(methods.size(), 2);
	}

	@Test
	public void getSinglePotentialSetter() {
		Set<Method> methods = ClassUtils.getSetters(TestBean.class, "intField");
		Assert.assertEquals(methods.size(), 1);
	}

	@Test
	public void getNonExistingPotentialSetters() {
		Set<Method> methods = ClassUtils.getSetters(TestBean.class, "someNonExisting");
		Assert.assertEquals(methods.size(), 0);
	}

	@Test
	public void getInheritedPotentialSetters() {
		Set<Method> methods = ClassUtils.getSetters(InheritedTestBean.class, "strField");
		Assert.assertEquals(methods.size(), 2);
	}

	@Test
	public void getSetterType() {
		Method setter = ClassUtils.getSetter(TestBean.class, "strField", String.class);
		Class<?> type = ClassUtils.getSetterType(setter);
		Assert.assertEquals(type, String.class);
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void getSetterTypeWithIncorrectSignature() throws Exception {
		Method getter = TestBean.class.getDeclaredMethod("getStrField");
		Class<?> type = ClassUtils.getSetterType(getter);
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void getSetterTypeWithIncorrectSignature2() throws Exception {
		Method otherMethod = TestBean.class.getDeclaredMethod("otherMethod", String.class, int.class);
		Class<?> type = ClassUtils.getSetterType(otherMethod);
	}

	@Test
	public void getGetter() throws Exception {
		Method getter = ClassUtils.getGetter(TestBean.class, "intField");
		Assert.assertEquals(getter, TestBean.class.getDeclaredMethod("getIntField"));
	}

	@Test
	public void getNonExistingGetterMethod() throws Exception {
		Method getter = ClassUtils.getGetter(TestBean.class, "nonExistingField");
		Assert.assertEquals(getter, null);
	}

	@Test
	public void getInheritedGetter() throws Exception {
		Method getter = ClassUtils.getGetter(InheritedTestBean.class, "intField");
		Assert.assertEquals(getter, TestBean.class.getDeclaredMethod("getIntField"));
	}

	private static final class ProtectedTestBean {
		private int field;

		protected int getField() {
			return field;
		}
	}

	@Test
	public void getProtectedGetter() throws Exception {
		Method getter = ClassUtils.getGetter(ProtectedTestBean.class, "field");
		Assert.assertEquals(getter, ProtectedTestBean.class.getDeclaredMethod("getField"));
	}
}
