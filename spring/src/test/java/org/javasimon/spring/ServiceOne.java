package org.javasimon.spring;

import org.javasimon.aop.Monitored;

public class ServiceOne implements MonitoredService {

	@Monitored
	public void run() {
	}
}
