package com.commons.help.job.mbeans;

public interface LocalCacheHolderMBean {
	String getRegisteredCacheSet();
	  
	boolean clearCacheKey(String paramString1, String paramString2);
	  
	boolean clearAllCache(String paramString);
	  
	boolean clearAllCaches();
	  
	long getCacheSize(String paramString);
	  
	boolean isElementInCache(String paramString1, String paramString2);
	  
	void printByRegex(String paramString1, String paramString2);
}
