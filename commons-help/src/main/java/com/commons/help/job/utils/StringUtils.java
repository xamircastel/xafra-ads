package com.commons.help.job.utils;

import com.commons.help.job.core.LogContent;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	private static final Logger Log = LoggerFactory.getLogger(StringUtils.class);

	public static final String generateUuid(String... prefix) {
		String separator = "-";
		return Joiner.on("-").appendTo(new StringBuilder(), (Object[]) prefix) + "-"
				+ Joiner.on(separator).join(LocalhostInfo.instance.getIp(),
						Long.valueOf(Thread.currentThread().getId()),
						new Object[] { Long.valueOf(System.currentTimeMillis()), UUID.randomUUID() });
	}

	public static String capitaliseFirstLetter(String word, boolean useRestLowercase) {
		String changedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
		return useRestLowercase ? changedWord.toLowerCase() : changedWord;
	}

	public static String capitaliseFirstLetter(String word) {
		return capitaliseFirstLetter(word, true);
	}

	public static StringBuilder stringListToStringBuilder(List<String> stringList) {
		return stringListToStringBuilder(stringList, "\n");
	}

	public static StringBuilder stringListToStringBuilder(List<String> stringList, String breakLineChar) {
		StringBuilder sb = new StringBuilder();
		for (String s : stringList)
			sb.append(s).append(breakLineChar);
		return sb;
	}

	public static String convertToHexaOctets(String source) {
		char[] chars = source.toCharArray();
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < chars.length; i++) {
			String temp = Integer.toHexString(chars[i]);
			if (temp.length() == 1)
				result.append("0");
			result.append(temp);
		}
		return result.toString().toUpperCase();
	}

	public static String convertFromHexOctets(String hexa) {
		StringBuffer result = new StringBuffer("");
		int i;
		for (i = 0; i < hexa.length(); i += 2) {
			String temp = hexa.substring(i, i + 2);
			int n = Integer.parseInt(temp, 16);
			char c = (char) n;
			result.append(c);
		}
		return result.toString();
	}

	public static Map<String, String> getIndexedMapperFromProps(Properties props, String keyPrefix,
			String valuePrefix) {
		TreeMap<Integer, String> orderValueMap = getOrderValueMap(props, keyPrefix);
		Map<String, String> map = Maps.newLinkedHashMap();
		for (Map.Entry<Integer, String> entry : orderValueMap.entrySet()) {
			String key = valuePrefix + "." + entry.getKey();
			if (props.containsKey(key)) {
				String value = props.getProperty(key);
				map.put(entry.getValue(), value);
			}
		}
		return map;
	}

	public static TreeMap<Integer, String> getOrderValueMap(Properties props, String keyPrefix) {
		TreeMap<Integer, String> regexKwMap = Maps.newTreeMap();
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			String prefix = keyPrefix + ".";
			if (key.startsWith(prefix)) {
				String digitValue = key.substring(prefix.length());
				if (isDigit(digitValue))
					regexKwMap.put(Integer.valueOf(digitValue), value);
			}
		}
		return regexKwMap;
	}

	public static String throwableToString(Throwable t) {
		return LogContent.throwableToString(t);
	}

	public static boolean isDigit(String value) {
		boolean isDigit = true;
		char[] charArray = value.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (!Character.isDigit(charArray[i])) {
				isDigit = false;
				break;
			}
		}
		return isDigit;
	}

	public static String replaceLast(String origin, String targetChar, String replacement) {
		StringBuilder builder = new StringBuilder(origin);
		if (origin.contains(targetChar)) {
			int startIndex = origin.lastIndexOf(targetChar);
			int endIndex = startIndex + targetChar.length();
			builder.replace(startIndex, endIndex, replacement);
		}
		return builder.toString();
	}

	public static <K, V> String mapToJoinedString(Map<K, V> map, String keyValueSeparator, String joiner) {
		int i = 0;
		int mapSize = map.size();
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			builder.append(entry.getKey()).append(keyValueSeparator).append(entry.getValue());
			i++;
			if (i < mapSize)
				builder.append(joiner);
		}
		return builder.toString();
	}

	public static boolean isInConcatString(String concatString, String separator, String target) {
		boolean isInConcatString = false;
		Iterable<String> splitted = Splitter.on(separator).omitEmptyStrings().trimResults().split(concatString);
		for (String currentPart : splitted) {
			if (currentPart.trim().equals(target)) {
				isInConcatString = true;
				break;
			}
		}
		return isInConcatString;
	}

	public static <T> List<T> fromConcatStringToList(String concatString, String separator, Class<T> clazz) {
		Iterable<String> splitted = Splitter.on(separator).omitEmptyStrings().trimResults().split(concatString);
		List<T> list = Lists.newArrayList();
		for (String currentPart : splitted) {
			T value = clazz.cast(currentPart);
			list.add(value);
		}
		return list;
	}

	public static <T> String fromListToString(Iterable<T> iterable, String separator) {
		return Joiner.on(separator).appendTo(new StringBuilder(), iterable).toString();
	}

	public static String getProperty(Properties props, String defaultValue, String... propertyNames) {
		String value = getProperty(props, defaultValue, false, propertyNames);
		return value;
	}

	public static Map<String, String> getNameValuePairFromString(String originalValue, String bounderChar,
			String elementSeparator, String keyValueSeparatorRegex) {
		Map<String, String> extIdMap = Maps.newHashMap();
		try {
			if (originalValue.contains(bounderChar)) {
				String[] valuePairGroup = org.apache.commons.lang.StringUtils.substringsBetween(originalValue,
						bounderChar, bounderChar);
				if (valuePairGroup.length == 1) {
					String valuePairElement = valuePairGroup[0];
					String[] subValuePairGroup = valuePairElement.split(elementSeparator);
					for (String subValuePair : subValuePairGroup) {
						String[] nameValuePair = subValuePair.split(keyValueSeparatorRegex);
						String key = nameValuePair[0];
						String value = nameValuePair[1];
						extIdMap.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			if (extIdMap.isEmpty())
				Log.error("Map is empty for original value: " + originalValue + " | bounderChar: " + bounderChar
						+ " | elementSeparator: " + elementSeparator + " | keyValueSeparator: " + keyValueSeparatorRegex
						+ ".Please validate client msg format.");
		} catch (Exception exception) {
		}
		return extIdMap;
	}

	public static String getProperty(Properties props, String defaultValue, boolean isToPrintDefault,
			String... propertyNames) {
		String value = null;
		for (String propertyName : propertyNames) {
			value = props.getProperty(propertyName);
			if (value != null && !value.isEmpty())
				break;
		}
		if (value == null) {
			String logMessage = "No value found from prop group: "
					+ fromListToString(Arrays.asList((Object[]) propertyNames), ",") + " Using default value "
					+ defaultValue;
			if (isToPrintDefault)
				Log.info(logMessage);
			value = defaultValue;
		}
		return value;
	}

	public static List<Integer> stringGroupToIntegerGroup(String[] stringGroup) {
		List<Integer> list = Lists.newArrayList();
		for (String value : stringGroup) {
			if (org.apache.commons.lang.StringUtils.isNumeric(value))
				list.add(Integer.valueOf(value));
		}
		return list;
	}

	public static List<Long> stringGroupToLongGroup(String[] stringGroup) {
		List<Long> list = Lists.newArrayList();
		for (String value : stringGroup) {
			if (org.apache.commons.lang.StringUtils.isNumeric(value))
				list.add(Long.valueOf(value));
		}
		return list;
	}

	public static final <T> String getInstanceString(T instance) {
		LogContent.Builder logBuilder = new LogContent.Builder(instance.getClass().getSimpleName());
		Field[] fields = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			try {
				Object value = field.get(instance);
				if (value != null)
					logBuilder.add(fieldName + ":" + value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		logBuilder.add("remaining fields are null.");
		return logBuilder.build();
	}
}