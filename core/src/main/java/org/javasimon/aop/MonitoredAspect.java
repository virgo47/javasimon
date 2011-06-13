package org.javasimon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

/**
 * AspectJ aspect for Spring configuration bound to Monitored annotation.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
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
}
