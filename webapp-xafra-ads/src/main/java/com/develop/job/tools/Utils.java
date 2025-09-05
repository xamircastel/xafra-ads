package com.develop.job.tools;

import java.util.UUID;

import com.google.gson.Gson;

public class Utils {

	private static Gson gSon = new Gson();

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static String toEntyJson(Object value) {
		if (value instanceof String)
			return String.valueOf(value);
		return gSon.toJson(value);
	}

	public static <T> T toJsonEntity(String json, Class<T> entity) {
		return gSon.fromJson(json, entity);
	}
}
