package com.commons.help.job.jmx;

public enum StringConstants {
	MBEAN_BASE_NAME(StringConstants.class.getPackage().getName() + ".mbeans");

	private final String value;

	StringConstants(String value) {
		this.value = value;
	}

	public String get() {
		return this.value;
	}
}