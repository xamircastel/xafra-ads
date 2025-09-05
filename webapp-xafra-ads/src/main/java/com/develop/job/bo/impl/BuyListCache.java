package com.develop.job.bo.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.commons.help.job.core.AbstractLocalRefCache;
import com.develop.job.model.BuyModel;

@Service
public class BuyListCache extends AbstractLocalRefCache<String, Integer, List<BuyModel>> {

	@Override
	protected List<BuyModel> loadValues(String paramR) {
		
		return null;
	}

	@Override
	protected Integer mapKey(List<BuyModel> paramV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<BuyModel> loadValue(Integer paramK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCacheName() {
		return BuyListCache.class.getSimpleName();
	}

	public List<BuyModel> getByReference(Long msisdnControlId, String msisdn) {
		return getByReference(getRefKey(msisdnControlId, msisdn));
	}

	public String getRefKey(Long msisdnControlId, String msisdn) {
		return msisdnControlId + "##" + msisdn;
	}

}
