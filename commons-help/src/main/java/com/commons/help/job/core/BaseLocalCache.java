package com.commons.help.job.core;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.help.job.utils.StringUtils;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public abstract class BaseLocalCache<K, V> {
	
	private static final Logger Log = LoggerFactory.getLogger(BaseLocalCache.class);
	private final LoadingCache<K, V> cacheHolder = getCacheBuilder().build(getCacheLoader());

	private volatile boolean wasLoaded = false;

	protected abstract CacheBuilder<Object, Object> getCacheBuilder();

	protected abstract CacheLoader<K, V> getCacheLoader();

	public void init() {
	}

	public V getByKey(K key) {
		if (!this.wasLoaded) {
			init();
			this.wasLoaded = true;
		}
		V value = null;
		try {
			value = (V) this.cacheHolder.get(key);
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		}
		return value;
	}

	public Optional<V> getOptionalByKey(K key) {
		V value = null;
		try {
			value = getByKey(key);
		} catch (com.google.common.cache.CacheLoader.InvalidCacheLoadException e) {
			Log.debug("No value found for key: {} .Error: {}",
					new Object[] { key, StringUtils.throwableToString((Throwable) e) });
		}
		return Optional.fromNullable(value);
	}

	protected LoadingCache<K, V> getCache() {
		return this.cacheHolder;
	}

	public void removeKey(K key) {
		this.cacheHolder.invalidate(key);
	}
}
