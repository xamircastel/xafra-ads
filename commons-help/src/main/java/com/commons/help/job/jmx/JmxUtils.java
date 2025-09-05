package com.commons.help.job.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JmxUtils extends AbstractJmxManager {
	private static final Logger Log = LoggerFactory.getLogger(JmxUtils.class);

	private static final JmxUtils instance = new JmxUtils();

	public static final JmxUtils getInstance() {
		return instance;
	}

	public void logError(String msg, Exception e) {
		Log.error(msg, e);
	}

	public void logError(Exception e) {
		Log.error("", e);
	}

	public void logInfo(String value) {
		Log.info(value);
	}
}