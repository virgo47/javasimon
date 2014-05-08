package org.javasimon.utils.bean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class SimonBeanUtilsTest extends SimonUnitTest {

	private SimonBeanUtils simonBeanUtils;

	@BeforeMethod
	public void beforeMethod() {
		simonBeanUtils = new SimonBeanUtils();
	}

	private static class TestClass {
	}

	private static class TestBean {
		private String strProp;

		private boolean booleanProp;
		private byte byteProp;
		private short shortProp;
		private int intProp;
		private long longProp;
		private float floatProp;
		private double doubleProp;
		private char charProp;
		private TestClass testClassProp;

		public boolean isBooleanProp() {
			return booleanProp;
		}

		public void setBooleanProp(boolean booleanProp) {
			this.booleanProp = booleanProp;
		}

		public String getStrProp() {
			return strProp;
		}

		public void setStrProp(String strProp) {
			this.strProp = strProp;
		}

		public byte getByteProp() {
			return byteProp;
		}

		public void setByteProp(byte byteProp) {
			this.byteProp = byteProp;
		}

		public short getShortProp() {
			return shortProp;
		}

		public void setShortProp(short shortProp) {
			this.shortProp = shortProp;
		}

		public int getIntProp() {
			return intProp;
		}

		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}

		public long getLongProp() {
			return longProp;
		}

		public void setLongProp(long longProp) {
			this.longProp = longProp;
		}

		public float getFloatProp() {
			return floatProp;
		}

		public void setFloatProp(float floatProp) {
			this.floatProp = floatProp;
		}

		public double getDoubleProp() {
			return doubleProp;
		}

		public void setDoubleProp(double doubleProp) {
			this.doubleProp = doubleProp;
		}

		public char getCharProp() {
			return charProp;
		}

		public void setCharProp(char charProp) {
			this.charProp = charProp;
		}

		public TestClass getTestClassProp() {
			return testClassProp;
		}

		public void setTestClassProp(TestClass testClassProp) {
			this.testClassProp = testClassProp;
		}
	}

	@Test
	public void testSetStringProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "strProp", "val");
		Assert.assertEquals(testBean.getStrProp(), "val");
	}

	@Test
	public void testSetBooleanProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "booleanProp", true);
		Assert.assertEquals(testBean.isBooleanProp(), true);
	}

	@Test
	public void testSetByteProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "byteProp", "10");
		Assert.assertEquals(testBean.getByteProp(), 10);
	}

	@Test
	public void testSetShortProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "shortProp", "10");
		Assert.assertEquals(testBean.getShortProp(), 10);
	}

	@Test
	public void testSetIntProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "intProp", "10");
		Assert.assertEquals(testBean.getIntProp(), 10);
	}

	@Test
	public void testSetLongProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "longProp", "10");
		Assert.assertEquals(testBean.getLongProp(), 10);
	}

	@Test
	public void testSetFloatProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "floatProp", "123.456");
		Assert.assertEquals(testBean.getFloatProp(), 123.456, 0.00001);
	}

	@Test
	public void testSetDoubleProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "doubleProp", "123.456");
		Assert.assertEquals(testBean.getDoubleProp(), 123.456, 0.00001);
	}

	@Test
	public void testSetCharProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "charProp", "a");
		Assert.assertEquals(testBean.getCharProp(), 'a');
	}

	@Test
	public void testSetPropertyOfUnknownClass() {
		TestBean testBean = new TestBean();
		TestClass val = new TestClass();
		simonBeanUtils.setProperty(testBean, "testClassProp", val);
		Assert.assertEquals(testBean.getTestClassProp(), val);
	}

	private static class NoSetterTestBean {
		private String strVal;
		private boolean booleanVal;
		private int intVal;
		private double doubleVal;

		private TestClass testClassVal;
	}

	@Test
	public void testSetStringField() {
		NoSetterTestBean testBean = new NoSetterTestBean();
		simonBeanUtils.setProperty(testBean, "strVal", "val");
		Assert.assertEquals(testBean.strVal, "val");
	}

	@Test
	public void testSetBooleanField() {
		NoSetterTestBean testBean = new NoSetterTestBean();
		simonBeanUtils.setProperty(testBean, "booleanVal", "true");
		Assert.assertEquals(testBean.booleanVal, true);
	}

	@Test
	public void testSetIntField() {
		NoSetterTestBean testBean = new NoSetterTestBean();
		simonBeanUtils.setProperty(testBean, "intVal", "123");
		Assert.assertEquals(testBean.intVal, 123);
	}

	@Test
	public void testSetDoubleField() {
		NoSetterTestBean testBean = new NoSetterTestBean();
		simonBeanUtils.setProperty(testBean, "doubleVal", "123.456");
		Assert.assertEquals(testBean.doubleVal, 123.456);
	}

	@Test
	public void testSetOtherClassField() {
		NoSetterTestBean testBean = new NoSetterTestBean();
		TestClass classInst = new TestClass();
		simonBeanUtils.setProperty(testBean, "testClassVal", classInst);
		Assert.assertEquals(testBean.testClassVal, classInst);
	}

	private static class NoBackendPropertyBean {
		private String otherStrPropName;
		private int otherIntPropName;

		public String getStrProp() {
			return otherStrPropName;
		}

		public void setStrProp(String strProp) {
			this.otherStrPropName = strProp;
		}

		public int getIntProp() {
			return otherIntPropName;
		}

		public void setIntProp(int intProp) {
			this.otherIntPropName = intProp;
		}
	}

	@Test
	public void testSetStringPropertyWithNoBackendField() {
		NoBackendPropertyBean testBean = new NoBackendPropertyBean();
		simonBeanUtils.setProperty(testBean, "strProp", "val");
		Assert.assertEquals(testBean.getStrProp(), "val");
	}

	@Test
	public void testSetIntPropertyWithNoBackendField() {
		NoBackendPropertyBean testBean = new NoBackendPropertyBean();
		simonBeanUtils.setProperty(testBean, "intProp", "20");
		Assert.assertEquals(testBean.getIntProp(), 20);
	}

	private static class SeveralSettersBean {
		private TestClass prop;
		private String strVal;

		public void setProp(TestClass val) {
		}

		public void setProp(String val) {
			this.strVal = val;
		}

		public String getStrVal() {
			return strVal;
		}
	}

	@Test
	public void testSeveralSettersSetString() {
		SeveralSettersBean bean = new SeveralSettersBean();
		simonBeanUtils.setProperty(bean, "prop", "val");
		Assert.assertEquals(bean.getStrVal(), "val");
	}

	private static class SeveralSettersIntBean {
		private TestClass prop;
		private int intVal;

		public void setProp(TestClass val) {
		}

		public void setProp(int val) {
			this.intVal = val;
		}

		public int getIntVal() {
			return intVal;
		}

	}

	@Test
	public void testSeveralSettersSetInt() {
		SeveralSettersIntBean bean = new SeveralSettersIntBean();
		simonBeanUtils.setProperty(bean, "prop", "123");
		Assert.assertEquals(bean.getIntVal(), 123);
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void testSetNonExistingProperty() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "nonExistingProp", "10");
	}

	@Test(expectedExceptions = {BeanUtilsException.class})
	public void testSetNonExistingPropertyOfNonConvertableType() {
		TestBean testBean = new TestBean();
		simonBeanUtils.setProperty(testBean, "nonExistingProp", new TestClass());
	}

	private static class NoUsualConverterBean {
		private TestClass testClass;

		public TestClass getTestClass() {
			return testClass;
		}

		public void setTestClass(TestClass testClass) {
			this.testClass = testClass;
		}
	}

	@Test
	public void testCustomConverterIsUsed() {
		NoUsualConverterBean bean = new NoUsualConverterBean();
		TestClass convertedVal = new TestClass();

		Converter converter = mock(Converter.class);
		when(converter.convert(TestClass.class, "strVal")).thenReturn(convertedVal);

		simonBeanUtils.registerConverter(TestClass.class, converter);
		simonBeanUtils.setProperty(bean, "testClass", "strVal");
		Assert.assertEquals(bean.getTestClass(), convertedVal);

		verify(converter).convert(TestClass.class, "strVal");
	}

	private static class PrivateSetterBean {
		private String strProp;

		public String getStrProp() {
			return strProp;
		}

		private void setStrProp(String strVal) {
			this.strProp = strVal;
		}
	}

	@Test
	public void testSetPrivateSetter() {
		PrivateSetterBean bean = new PrivateSetterBean();
		simonBeanUtils.setProperty(bean, "strProp", "strVal");
		Assert.assertEquals(bean.getStrProp(), "strVal");
	}

	private static class TestBeanExtension extends TestBean {
		private TestClass prop;

		public TestClass getOtherTestClassProp() {
			return prop;
		}

		private void setOtherTestClassProp(TestClass val) {
			this.prop = val;
		}
	}

	private static class TestBeanExtension2 extends TestBeanExtension {
	}

	@Test
	public void testSetInheritedProperty() {
		TestBeanExtension bean = new TestBeanExtension();
		simonBeanUtils.setProperty(bean, "strProp", "val");
		Assert.assertEquals(bean.getStrProp(), "val");
	}

	@Test
	public void testSetInheritedPropertyWithoutConversion() {
		TestBeanExtension testBean = new TestBeanExtension();
		TestClass val = new TestClass();
		simonBeanUtils.setProperty(testBean, "testClassProp", val);
		Assert.assertEquals(testBean.getTestClassProp(), val);
	}

	@Test
	public void testSetPrivateInheritedPropertyWithoutConversion() {
		TestBeanExtension2 testBean = new TestBeanExtension2();
		TestClass val = new TestClass();
		simonBeanUtils.setProperty(testBean, "otherTestClassProp", val);
		Assert.assertEquals(testBean.getOtherTestClassProp(), val);
	}

	private static class BaseNoSetterBean {
		protected int intField;
	}

	private static class BaseNoSetterBeanInherited extends BaseNoSetterBean {
	}

	@Test
	public void testSetInheritedField() {
		BaseNoSetterBeanInherited bean = new BaseNoSetterBeanInherited();
		simonBeanUtils.setProperty(bean, "intField", "123");
		Assert.assertEquals(bean.intField, 123);
	}


	private static class NestedBean {
		private TestBean bean = new TestBean();
	}

	@Test
	public void testSetNestedBean() {
		NestedBean nestedBean = new NestedBean();
		simonBeanUtils.setProperty(nestedBean, "bean.intProp", "123");
		Assert.assertEquals(nestedBean.bean.getIntProp(), 123);
	}
}
