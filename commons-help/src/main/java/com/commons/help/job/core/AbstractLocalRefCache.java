package com.commons.help.job.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.help.job.core.cache.LocalCacheManager;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;

public abstract class AbstractLocalRefCache<R, K, V> extends AbstractLocalCache<K, V> {

	private static final Logger Log = LoggerFactory.getLogger(AbstractLocalRefCache.class);

	private final LoadingCache<R, K> refCacheHolder = getCacheBuilder().build(getRefCacheLoader());

	protected AbstractLocalRefCache() {
		registerRefCacheOnJMX();
	}

	protected abstract V loadValues(R paramR);

	protected CacheLoader<R, K> getRefCacheLoader() {
		return new CacheLoader<R, K>() {
			public K load(R ref) throws Exception {
				Log.debug("loading for ref:{}", ref);
				V value = (V) loadValues(ref);
				K valueKey = null;
				if (value != null) {
					valueKey = (K) mapKey(value);
					getCache().put(valueKey, value);
				}
				return valueKey;
			}

			public ListenableFuture<K> reload(R key, K oldValue) throws Exception {
				return reloadImpl(key, oldValue);
			}

			protected ListenableFuture<K> reloadImpl(final R key, final K oldValue) {
				ListenableFuture<K> listenableFuture = BaseCacheLoader.getRefreshExecutorService()
						.submit(new Callable<K>() {
							public K call() throws Exception {
								return asyncReload((R) key, (K) oldValue);
							}
						});
				return listenableFuture;
			}

			protected K asyncReload(R key, K oldValue) throws Exception {
				K value = null;
				String id = String.valueOf(System.currentTimeMillis());
				try {
					value = load(key);
					if (value == null) {
						Log.debug("cacheAsyncReload", String.valueOf(System.currentTimeMillis()),
								"Null value found for key: {} .Using oldValue: {}", new Object[] { key, oldValue });
						value = oldValue;
					}
				} catch (Throwable t) {
					Log.debug("cacheAsyncReload", id, "Error while loading key: " + key + ".Falling back to old value: "
							+ oldValue + "|Stacktrace: " + LogContent.throwableToString(t), new Object[0]);
					value = oldValue;
				}
				return value;
			}
		};
	}

	protected void registerRefCacheOnJMX() {
		try {
			LocalCacheManager.registerCache(getCacheName() + "#REF", (Cache<?, ?>) getRefCache());
		} catch (Exception e) {
			Log.info("[registerRefCacheOnJMX] :: Error registering cache:{} on JMX! {}",
					new Object[] { getCacheName(), e });
		}
	}

	public LoadingCache<R, K> getRefCache() {
		return this.refCacheHolder;
	}

	public V getByReference(R ref) {
		V value = null;
		try {
			K key = (K) this.refCacheHolder.get(ref);
			if (key != null)
				value = getByKey(key);
		} catch (com.google.common.cache.CacheLoader.InvalidCacheLoadException e) {
			Log.debug("No value found for ref: {} .", new Object[] { ref });
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		}
		return value;
	}

	public V getUncheckedByRef(R ref) {
		K key = (K) this.refCacheHolder.getUnchecked(ref);
		if (key != null)
			return getUncheckedByKey(key);
		return null;
	}

	public Optional<V> getOptionalByRef(R ref) {
		return Optional.fromNullable(getByReference(ref));
	}

	public void removeRef(R ref) {
		K key = null;
		try {
			key = (K) this.refCacheHolder.getUnchecked(ref);
			this.refCacheHolder.invalidate(ref);
			if (key != null)
				removeKey(key);
		} catch (Exception exception) {
		}
	}

	public void invalidateRef(R ref) {
		this.refCacheHolder.invalidate(ref);
	}
}