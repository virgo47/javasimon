package org.javasimon.spring;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration must be separated from {@link MonitoredPointcutStopwatchNameTest},
 * because otherwise beans autowired there would not be post-processed by
 * {@link BeanPostProcessor}s, e.g. {@link DefaultAdvisorAutoProxyCreator} - and monitoring aspect
 * would not work.
 *
 * Both
 */
@Configuration
public class MonitoredPointcutStopwatchNameConfig {

	@Bean
	public MonitoredService serviceOne() {
		return new ServiceOne();
	}

	@Bean
	public MonitoredService serviceTwo() {
		return new ServiceTwo();
	}

	@Bean
	public MonitoringInterceptor monitoringInterceptor() {
		return new MonitoringInterceptor();
	}

	@Bean
	public DefaultPointcutAdvisor monitoringAdvisor(MonitoringInterceptor monitoringInterceptor) {
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(monitoringInterceptor);
		advisor.setPointcut(new MonitoredMeasuringPointcut());
		return advisor;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}
}

