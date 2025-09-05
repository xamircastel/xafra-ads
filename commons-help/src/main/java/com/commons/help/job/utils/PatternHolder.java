package com.commons.help.job.utils;

import java.util.regex.Pattern;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PatternHolder {
	public static final PatternHolder instance = new PatternHolder();

	private static final LoadingCache<String, Optional<Pattern>> patternHolderMap = CacheBuilder.newBuilder()
			.concurrencyLevel(50).build(new CacheLoader<String, Optional<Pattern>>() {
				public Optional<Pattern> load(String regexPattern) throws Exception {
					Optional<Pattern> optPattern = Optional.fromNullable(Pattern.compile(regexPattern, 106));
					return optPattern;
				}
			});

	public Pattern getPattern(String regex) {
		Pattern pattern = null;
		try {
			Optional<Pattern> optPattern = (Optional<Pattern>) patternHolderMap.get(regex);
			if (optPattern.isPresent())
				pattern = (Pattern) optPattern.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pattern;
	}
}
