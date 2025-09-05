package com.commons.help.job.mbeans;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.help.job.core.LogContent;
import com.commons.help.job.utils.PatternHolder;
import com.commons.help.job.utils.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;

public class LocalCacheHolder implements LocalCacheHolderMBean {

	private static final Logger Log = LoggerFactory.getLogger(LocalCacheHolder.class);
	private static final ConcurrentMap<String, Cache<?, ?>> cacheRegister = Maps.newConcurrentMap();

	public void registerCache(String cacheName, Cache<?, ?> cache) {
		if (!cacheRegister.containsKey(cacheName))
			cacheRegister.put(cacheName, cache);
	}

	public String getRegisteredCacheSet() {
		Set<String> keySet = cacheRegister.keySet();
		return StringUtils.fromListToString(keySet, ",");
	}

	public boolean clearCacheKey(String cacheName, String key) {
		boolean wasCleared = false;
		LogContent.Builder logBuilder = new LogContent.Builder("clearCacheKey", "-->");
		try {
			if (cacheName == null || cacheName.isEmpty() || key == null || key.isEmpty())
				throw new IllegalArgumentException("CacheName: " + cacheName + " or key: " + key + " are empty");
			Cache<?, ?> cache = cacheRegister.get(cacheName);
			if (cache != null) {
				try {
					cache.invalidate(key);
					wasCleared = true;
				} catch (Exception e) {
					Log.error("", e);
				} finally {
					logBuilder.add("Executed invalidate for cacheName: " + cacheName + " and key: " + key + " ? "
							+ wasCleared);
				}
			} else {
				logBuilder.addError("No cache found for name: " + cacheName);
			}
		} catch (Exception e) {
			logBuilder.addError(e);
		} finally {
			Log.info(logBuilder.build());
		}
		return wasCleared;
	}

	public boolean clearAllCache(String cacheName) {
		boolean wasCleared = false;
		LogContent.Builder logBuilder = new LogContent.Builder("clearAllCache", "-->");
		try {
			if (cacheName == null || cacheName.isEmpty())
				throw new IllegalArgumentException("CacheName param is empty");
			Cache<?, ?> cache = cacheRegister.get(cacheName);
			if (cache != null) {
				try {
					cache.invalidateAll();
					wasCleared = true;
				} catch (Exception e) {
					Log.error("", e);
				} finally {
					logBuilder.add("Executed invalidateAll for cacheName: " + cacheName + " ? " + wasCleared);
				}
			} else {
				logBuilder.add("No cache found for name: " + cacheName);
			}
		} catch (Exception e) {
			logBuilder.addError(e);
		} finally {
			Log.info(logBuilder.build());
		}
		return wasCleared;
	}

	public long getCacheSize(String cacheName) {
		long cacheSize = 0L;
		LogContent.Builder logBuilder = new LogContent.Builder("getCacheSize", "-->");
		try {
			if (cacheName == null || cacheName.isEmpty())
				throw new IllegalArgumentException("CacheName param is empty");
			Cache<?, ?> cache = cacheRegister.get(cacheName);
			if (cache != null) {
				cacheSize = cache.size();
				logBuilder.add("size: " + cacheSize);
			} else {
				logBuilder.add("No cache found for cache: " + cacheName);
			}
		} catch (Exception e) {
			logBuilder.addError(e);
		} finally {
			Log.info(logBuilder.build());
		}
		return cacheSize;
	}

	public boolean isElementInCache(String cacheName, String key) {
		boolean isElementInCache = false;
		LogContent.Builder logBuilder = new LogContent.Builder("isElementInCache", "-->");
		try {
			if (cacheName == null || cacheName.isEmpty() || key == null || key.isEmpty())
				throw new IllegalArgumentException("CacheName: " + cacheName + " or key: " + key + " are empty");
			Cache<?, ?> cache = cacheRegister.get(cacheName);
			if (cache != null) {
				Object value = cache.getIfPresent(key);
				if (value != null) {
					logBuilder.add("Value for cacheName: " + cacheName + " and key: " + key + " is: " + value);
					isElementInCache = true;
				} else {
					logBuilder.add("No value found for key: " + key);
				}
			} else {
				logBuilder.add("No cache found for cacheName: " + cacheName);
			}
		} catch (Exception e) {
			logBuilder.addError(e);
		} finally {
			Log.info(logBuilder.build());
		}
		return isElementInCache;
	}

	public void printByRegex(String cacheName, String regex) {
		LogContent.Builder logBuilder = new LogContent.Builder("printByRegex", "-->");
		try {
			if (cacheName == null || cacheName.isEmpty() || regex == null || regex.isEmpty())
				throw new IllegalArgumentException("CacheName: " + cacheName + " or regex: " + regex + " are empty");
			Cache<?, ?> cache = cacheRegister.get(cacheName);
			if (cache != null) {
				logBuilder.add("validating regex: " + regex);
				ConcurrentMap<?, ?> map = cache.asMap();
				Pattern pattern = PatternHolder.instance.getPattern(regex);
				for (Map.Entry<?, ?> entry : map.entrySet()) {
					Object keyInstance = entry.getKey();
					if (keyInstance instanceof String) {
						String key = (String) keyInstance;
						if (pattern.matcher(key).matches())
							logBuilder.add("key: " + key + " matches.Printing value: " + entry.getValue());
					}
				}
			} else {
				logBuilder.add("No cache found for cacheName: " + cacheName);
			}
		} catch (Exception e) {
			logBuilder.addError(e);
		} finally {
			Log.info(logBuilder.build(), new Object[0]);
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean clearAllCaches() {
		LogContent.Builder logBuilder = new LogContent.Builder("clearAllCaches", "-->");
		for (Map.Entry<String, Cache<?, ?>> entry : cacheRegister.entrySet()) {
			((Cache) entry.getValue()).invalidateAll();
			logBuilder.add("Invalidated cache: " + (String) entry.getKey());
		}
		Log.info(logBuilder.build(), new Object[0]);
		return true;
	}
}
