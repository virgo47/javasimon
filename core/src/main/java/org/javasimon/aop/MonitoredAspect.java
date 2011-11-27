package org.javasimon.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * AspectJ aspect for Spring configuration bound to Monitored annotation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
// TODO hardly started, right now only AOP alliance aspect is usable
public class MonitoredAspect {
	@Pointcut("@annotation(org.javasimon.aop.Monitored)")
	public void mergeEntitiesMethod() {
	}

	@Pointcut("@within(org.javasimon.aop.Monitored)")
	public void mergeEntitiesClass() {
	}

	@Around("mergeEntitiesMethod() || mergeEntitiesClass()")
	public Object mergeEntities(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("pjp = " + pjp);
//		SimonManager.getSimon(pjp.get)
		try {
			return pjp.proceed();
		} finally {

		}
	}

	private Monitored getAnnotation(ProceedingJoinPoint pjp) throws Exception {
		Monitored annotation = pjp.getTarget().getClass().getAnnotation(Monitored.class);

		if ((annotation == null) && (pjp.getSignature() instanceof MethodSignature)) {
			Method method = ((MethodSignature) pjp.getSignature()).getMethod();
			method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
			annotation = method.getAnnotation(Monitored.class);
		}
		return annotation;
	}

}
