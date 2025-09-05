package com.commons.help.job.core.cache;

import com.commons.help.job.jmx.JmxUtils;
import com.commons.help.job.jmx.StringConstants;
import com.commons.help.job.mbeans.LocalCacheHolder;
import com.commons.help.job.mbeans.LocalCacheHolderMBean;
import com.google.common.cache.Cache;

public final class LocalCacheManager {

	private static final LocalCacheHolder localCacheHolder = new LocalCacheHolder();

	private static volatile boolean wasJmxLoaded = false;

	private static final void loadMBean( String name) {
		if (!wasJmxLoaded) {

			String objectNamePath = StringConstants.MBEAN_BASE_NAME.get() + ":name=lcache_holder_" + name;
			wasJmxLoaded = JmxUtils.getInstance().createMBean(objectNamePath, LocalCacheHolderMBean.class,
					localCacheHolder);
		}
	}

	public static void registerCache(String cacheName, Cache<?, ?> cache) {
		loadMBean(cacheName);
		localCacheHolder.registerCache(cacheName, cache);
	}

	public static <T> void registerCache(Class<T> typeClass, Cache<?, ?> cache) {
		loadMBean(typeClass.getName());
		localCacheHolder.registerCache(typeClass.getName(), cache);
	}
}