package com.commons.help.job.core;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.help.job.concurrency.ConcurrencyUtils;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public abstract class BaseCacheLoader<K, V> extends CacheLoader<K, V> {

	private static final Logger Log = LoggerFactory.getLogger(BaseCacheLoader.class);

	private static final ListeningExecutorService refreshedExecutorService = ConcurrencyUtils
			.getListeningExecutorService(Integer.parseInt(System.getProperty("guava.refresher.threads", "20")));

	private final String threadName;

	public BaseCacheLoader(String threadName) {
		this.threadName = threadName;
	}

	public static final ListeningExecutorService getRefreshExecutorService() {
		return refreshedExecutorService;
	}

	public BaseCacheLoader() {
		this(Thread.currentThread().getName());
	}

	public V load(K key) throws Exception {
		if (this.threadName != null)
			Log.debug("name {}", this.threadName);
		return loadImpl(key);
	}

	public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
		return reloadImpl(key, oldValue);
	}

	protected ListenableFuture<V> reloadImpl(final K key, final V oldValue) {
		ListenableFuture<V> listenableFuture = refreshedExecutorService.submit(new Callable<V>() {
			public V call() throws Exception {
				return BaseCacheLoader.this.asyncReload(key, (V) oldValue);
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
				Log.debug("cacheAsyncReload {}", String.valueOf(System.currentTimeMillis()),
						"Null value found for key: {} .Using oldValue: {}", key, oldValue);
				value = oldValue;
			}
		} catch (Throwable t) {
			Log.debug("cacheAsyncReload", id,
					"Error while loading key: " + key + ".Falling back to old value: " + oldValue);
			value = oldValue;
		}
		return value;
	}

	protected abstract V loadImpl(K paramK);
}
