package org.javasimon.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * AspectJ aspect for Spring configuration bound to Monitored annotation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class MonitoredAspect {
	@Pointcut("@annotation(org.javasimon.aop.Monitored)")
	public void monitoredMethod() {
	}

	@Pointcut("@within(org.javasimon.aop.Monitored)")
	public void monitoredClass() {
	}

	@Around("monitoredMethod() || monitoredClass()")
	public Object mergeEntities(ProceedingJoinPoint pjp) throws Throwable {
		// TODO possibly inject the manager some time in the future
		Split split = SimonManager.getStopwatch(getSimonName(pjp)).start();
		try {
			return pjp.proceed();
		} finally {
			split.stop();
		}
	}

	private String getSimonName(ProceedingJoinPoint pjp) throws Exception {
		Class targetClass = pjp.getTarget().getClass();
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		method = targetClass.getMethod(method.getName(), method.getParameterTypes());

		return new MonitorNameHelper(targetClass, method).getStopwatchName();
	}
}
