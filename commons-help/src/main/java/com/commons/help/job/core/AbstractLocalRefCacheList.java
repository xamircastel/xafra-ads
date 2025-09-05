package com.commons.help.job.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.help.job.core.cache.LocalCacheManager;
import com.commons.help.job.utils.StringUtils;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public abstract class AbstractLocalRefCacheList<R, K, V> extends AbstractLocalCache<K, V> {

	private static final Logger Log = LoggerFactory.getLogger(AbstractLocalRefCacheList.class);

	private final LoadingCache<R, List<K>> refCacheHolder = getCacheBuilder().build(getRefCacheLoader());

	protected AbstractLocalRefCacheList() {
		registerRefCacheOnJMX();
	}

	protected abstract List<V> loadValues(R paramR);

	protected CacheLoader<R, List<K>> getRefCacheLoader() {
		return new CacheLoader<R, List<K>>() {
			public List<K> load(R ref) throws Exception {
				Log.info("loading for ref:{}", ref);
				List<V> valueList = loadValues(ref);
				List<K> valueKeyList = new ArrayList<>();
				if (valueList != null && !valueList.isEmpty()) {
					valueKeyList = new ArrayList<>(valueList.size());
					for (V value : valueList) {
						K valueKey = (K) mapKey(value);
						valueKeyList.add(valueKey);
						getCache().put(valueKey, value);
					}
				}
				return valueKeyList;
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

	public LoadingCache<R, List<K>> getRefCache() {
		return this.refCacheHolder;
	}

	public List<V> getByReference(R ref) {
		List<V> valueList = new ArrayList<>();
		try {
			List<K> keyList = (List<K>) this.refCacheHolder.get(ref);
			if (keyList != null && !keyList.isEmpty()) {
				valueList = new ArrayList<>(keyList.size());
				for (K key : keyList) 
					valueList.add(getByKey(key));
			}
		} catch (com.google.common.cache.CacheLoader.InvalidCacheLoadException e) {
			Log.debug("No value found for ref: {} .Error: {}",
					new Object[] { ref, StringUtils.throwableToString((Throwable) e) });
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		}
		return valueList;
	}

	public List<V> getUncheckedByRef(R ref) {
		List<V> valueList = null;
		List<K> keyList = (List<K>) this.refCacheHolder.getUnchecked(ref);
		if (keyList != null && !keyList.isEmpty()) {
			valueList = new ArrayList<>(keyList.size());
			for (K key : keyList)
				valueList.add(getUncheckedByKey(key));
		}
		return valueList;
	}

	public Optional<List<V>> getOptionalByRef(R ref) {
		return Optional.fromNullable(getByReference(ref));
	}

	public void removeRef(R ref) {
		List<K> keyList = null;
		try {
			keyList = (List<K>) this.refCacheHolder.getUnchecked(ref);
			this.refCacheHolder.invalidate(ref);
			if (keyList != null && !keyList.isEmpty())
				for (K key : keyList)
					removeKey(key);
		} catch (Exception e) {
			Log.debug("Exception for ref: {} - error: {}", new Object[] { ref, e.getLocalizedMessage() });
		}
	}

	public void invalidateRef(R ref) {
		this.refCacheHolder.invalidate(ref);
	}

}
