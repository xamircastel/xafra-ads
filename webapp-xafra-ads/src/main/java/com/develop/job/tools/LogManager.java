package com.develop.job.tools;

import org.slf4j.MDC;

public class LogManager {

	public static void setLogName(String nameLogger) {
		MDC.put("threadName", nameLogger);
	}
}
