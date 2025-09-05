package com.commons.help.job.collection;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class StringMap extends HashMap<String, String> {

	private static final long serialVersionUID = -2955883266741248371L;
	
	private Gson gson = new Gson();

	public StringMap(Map<String, String> values) {
		if (values != null)
			this.putAll(values);
	}

	public StringMap() {
	}

	public String queryString() {
		StringBuilder b = new StringBuilder();
		this.forEach((key, value) -> {
			b.append("&").append(key).append("=").append(value);
		});

		return b.toString();
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
}
