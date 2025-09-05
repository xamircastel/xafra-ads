package com.commons.help.job.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.help.job.core.cache.LocalCacheManager;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;

public abstract class AbstractLocalCache<K, V> {

	private static final Logger Log = LoggerFactory.getLogger(AbstractLocalCache.class);

	private static final long DEFAULT_MAX_CACHE_SIZE = 1000L;

	private static final long DEFAULT_EXPIRE_TIME_IN_MINUTES = 60L;

	private static final long DEFAULT_REFRESH_TIME_IN_MINUTES = 65L;

	private final LoadingCache<K, V> cacheHolder = getCacheBuilder().build(getCacheLoader());

	protected AbstractLocalCache() {
		registerCacheOnJMX();
	}

	protected abstract K mapKey(V paramV);

	protected abstract V loadValue(K paramK);

	protected CacheLoader<K, V> getCacheLoader() {
		return new CacheLoader<K, V>() {			
			public V load(K key) throws Exception {
				Log.debug("loading for key:{}", key);
				V value = loadValue(key);
				if (value != null) {
					K valueKey = mapKey(value);
					if (getCache().getIfPresent(valueKey) == null) {
						getCache().put(valueKey, value);
						Log.debug("localcache key {}", valueKey);
					}
				}
				return value;
			}

			public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
				return reloadImpl(key, oldValue);
			}

			protected ListenableFuture<V> reloadImpl(final K key, final V oldValue) {
				ListenableFuture<V> listenableFuture = BaseCacheLoader.getRefreshExecutorService()
						.submit(new Callable<V>() {
							public V call() throws Exception {
								return asyncReload((K) key, (V) oldValue);
							}
						});
				return listenableFuture;
			}

			protected V asyncReload(K key, V oldValue) throws Exception {
				V value = null;
				String id = String.valueOf(System.currentTimeMillis());
				try {
					value = load(key);
					if (value == null) {
						Log.debug("cacheAsyncReload", String.valueOf(System.currentTimeMillis()),
								"Null value found for key: {} .Using oldValue: {}", new Object[] { key, oldValue });
						value = oldValue;
					}
				} catch (Throwable t) {
					Log.debug("cacheAsyncReload", id,
							"Error while loading key: " + key + ".Falling back to old value: " + oldValue);
					value = oldValue;
				}
				return value;
			}
		};
	}

	protected abstract String getCacheName();

	protected void registerCacheOnJMX() {
		try {
			LocalCacheManager.registerCache(getCacheName(), (Cache<?, ?>) getCache());
		} catch (Exception e) {
			Log.info("[registerCacheOnJMX] :: Error registering cache:{} on JMX! {}", getCacheName(), e);
		}
	}

	protected CacheBuilder<Object, Object> getCacheBuilder() {
		return CacheBuilder.newBuilder().maximumSize(getMaximumSize())
				.expireAfterAccess(getExpireTimeInMinutes(), TimeUnit.MINUTES)
				.refreshAfterWrite(getRefreshTimeInMinutes(), TimeUnit.MINUTES);
	}

	protected long getRefreshTimeInMinutes() {
		return DEFAULT_REFRESH_TIME_IN_MINUTES;
	}

	protected long getExpireTimeInMinutes() {
		return DEFAULT_EXPIRE_TIME_IN_MINUTES;
	}

	protected long getMaximumSize() {
		return DEFAULT_MAX_CACHE_SIZE;
	}

	public V getByKey(K key) {
		V value = null;
		try {
			value = (V) this.cacheHolder.get(key);
		} catch (com.google.common.cache.CacheLoader.InvalidCacheLoadException e) {
			Log.debug("No value found for key: {} .", key );
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		}
		return value;
	}

	public V getUncheckedByKey(K key) {
		return (V) this.cacheHolder.getUnchecked(key);
	}

	public Optional<V> getOptionalByKey(K key) {
		return Optional.fromNullable(getByKey(key));
	}

	public LoadingCache<K, V> getCache() {
		return this.cacheHolder;
	}

	public void removeKey(K key) {
		this.cacheHolder.invalidate(key);
	}

	public void removeAll() {
		this.cacheHolder.invalidateAll();
	}

	@PostConstruct
	void initialize() {
		Log.debug("@PostConstruct: {}", getClass().getName());
	}
}