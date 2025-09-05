package com.develop.job.bo.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.commons.help.job.core.AbstractLocalCache;
import com.develop.job.model.BuyModel;

@Service
public class BuyCache extends AbstractLocalCache<Integer, BuyModel>{

	private static final Logger log = LoggerFactory.getLogger(BuyCache.class);
			
	@Override
	protected Integer mapKey(BuyModel paramV) {
		return paramV.getId();
	}

	@Override
	protected BuyModel loadValue(Integer paramK) {
		log.info("entra a cargar");
		return new BuyModel(paramK, "test", false);
	}

	@Override
	protected String getCacheName() {
		return BuyCache.class.getSimpleName();
	}

	protected long getExpireTimeInMinutes() {
		return 2L;
	}

	protected long getRefreshTimeInMinutes() {
		return 2L;
	}
}
