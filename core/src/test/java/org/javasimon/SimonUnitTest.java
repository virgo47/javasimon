package org.javasimon;

import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class SimonUnitTest {

	@BeforeMethod
	public void clearManagerAndPrintMethod(Method method) {
		SimonManager.clear();
		SimonManager.enable();
		System.out.println("TEST METHOD: " + getClass().getSimpleName() + '.' +  method.getName());
	}
}
