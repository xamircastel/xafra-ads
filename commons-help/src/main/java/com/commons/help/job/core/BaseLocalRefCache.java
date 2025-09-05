package com.commons.help.job.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public abstract class BaseLocalRefCache<R, K, V> {
	
	private boolean injectInPrimary;

	private LoadingCache<K, V> primaryCacheHolder;

	private final LoadingCache<R, List<K>> refCacheHolder = getCacheBuilder().build(new CacheLoader<R, List<K>>() {
		public List<K> load(R key) throws Exception {
			List<V> valueList = loadValues(key);
			List<K> keyList = new ArrayList<>(valueList.size());
			for (V value : valueList) {
				if (value != null) {
					K valueKey = (K) BaseLocalRefCache.this.mapKey(value);
					keyList.add(valueKey);
					if (BaseLocalRefCache.this.injectInPrimary
							&& BaseLocalRefCache.this.primaryCacheHolder.getIfPresent(valueKey) == null)
						BaseLocalRefCache.this.primaryCacheHolder.put(valueKey, value);
				}
			}
			return keyList;
		}
	});

	public BaseLocalRefCache(BaseLocalCache<K, V> cacheHolder) {
		this.primaryCacheHolder = cacheHolder.getCache();
		this.injectInPrimary = true;
	}

	protected void setInjectInPrimary(boolean injectInPrimary) {
		this.injectInPrimary = injectInPrimary;
	}

	protected LoadingCache<R, List<K>> getCache() {
		return this.refCacheHolder;
	}

	public List<V> getByKey(R key) {
		List<K> refKeyList = null;
		List<V> valueList = null;
		try {
			refKeyList = (List<K>) this.refCacheHolder.get(key);
			if (refKeyList != null) {
				valueList = new ArrayList<>(refKeyList.size());
				for (K refKey : refKeyList)
					valueList.add((V) this.primaryCacheHolder.get(refKey));
			}
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		}
		return valueList;
	}

	public void removeKey(R key) {
		List<K> refKeyList = (List<K>) this.refCacheHolder.getIfPresent(key);
		if (refKeyList != null) {
			this.refCacheHolder.invalidate(key);
			this.primaryCacheHolder.invalidateAll(refKeyList);
		}
	}

	protected abstract CacheBuilder<Object, Object> getCacheBuilder();

	protected abstract List<V> loadValues(R paramR);

	protected abstract K mapKey(V paramV);
}