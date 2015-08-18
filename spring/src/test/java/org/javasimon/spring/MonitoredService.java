package org.javasimon.spring;

import org.javasimon.aop.Monitored;

public interface MonitoredService {

	@Monitored
	void run();
}
