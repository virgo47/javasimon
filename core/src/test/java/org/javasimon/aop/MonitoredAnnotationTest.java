package org.javasimon.aop;

import org.testng.annotations.Test;

/**
 * Tests whether {@link Monitored} is found and name/suffix is resolved properly using {@link MonitorNameHelper}.
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 */
public class MonitoredAnnotationTest {
	@Test
	public void testMonitorNameClean() throws Exception {
		Class targetClass = MonitoredClean.class;
//		Assert.assertEquals(new MonitorNameHelper(targetClass, targetClass.getMethod("methodWithoutAnnotation")).getStopwatchName(),
//			"org.javasimon.aop.MonitoredClean.methodWithoutAnnotation");
//		Assert.assertEquals(new MonitorNameHelper(targetClass, targetClass.getMethod("methodWithSuffix")).getStopwatchName(),
//			"org.javasimon.aop.MonitoredClean.suffixed");
//		Assert.assertEquals(new MonitorNameHelper(targetClass, targetClass.getMethod("methodWithName")).getStopwatchName(),
//			"renamed.altogether");
	}
}

@Monitored
interface MonitoredInterface {
	void methodWithoutAnnotation();

	@Monitored(suffix = "suffixed")
	void methodWithSuffix();

	@Monitored(name = "renamed.altogether")
	void methodWithName();
}

class MonitoredClean implements MonitoredInterface {
	@Override
	public void methodWithoutAnnotation() {
	}

	@Override
	public void methodWithSuffix() {
	}

	@Override
	public void methodWithName() {
	}
}

@Monitored(name = "dirty", suffix = "ignored")
class MonitoredDirty implements MonitoredInterface {
	@Override
	public void methodWithoutAnnotation() {
	}

	@Override
	public void methodWithSuffix() {
	}

	@Override
	public void methodWithName() {
	}
}

@Monitored(name = "dirty", suffix = "ignored")
class MonitoredDirtyMethods implements MonitoredInterface {
	@Override
	@Monitored(name = "xxx")
	public void methodWithoutAnnotation() {
	}

	@Override
	@Monitored(name = "xxx", suffix = "hereIgnored")
	public void methodWithSuffix() {
	}

	@Override
	public void methodWithName() {
	}
}

@Monitored(name = "dirty", suffix = "ignored")
class MonitoredVeryDirty implements MonitoredInterface {
	@Override
	public void methodWithoutAnnotation() {
	}

	@Override
	@Monitored(suffix = "suffix")
	public void methodWithSuffix() {
	}

	@Override
	@Monitored(name = "xxx", suffix = "hereIgnored")
	public void methodWithName() {
	}
}

class CleanSubclass extends MonitoredClean {
	@Override
	public void methodWithoutAnnotation() {
	}

	@Override
	public void methodWithSuffix() {
	}

	@Override
	public void methodWithName() {
	}
}
