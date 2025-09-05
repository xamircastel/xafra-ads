package com.develop.job.db.dao.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.help.job.core.AbstractLocalRefCacheList;
import com.develop.job.db.dao.DAOCampaign;
import com.develop.job.db.entity.Campaign;

@Service
public class CampainCache extends AbstractLocalRefCacheList<String, String, Campaign> {

	@Autowired
	private DAOCampaign campain;

	@Override
	protected List<Campaign> loadValues(String paramR) {
		String[] sp = paramR.split("#");
		return campain.getAllCampainByProductId(Long.valueOf(sp[1]));
	}

	@Override
	protected String mapKey(Campaign paramV) {
		return key(paramV.getId(), paramV.getTraking());
	}

	@Override
	protected Campaign loadValue(String paramK) {
		String[] sp = paramK.split("#");
		return campain.getCampainByProductIdAndTraking(Long.valueOf(sp[0]), sp[1]);
	}

	@Override
	protected String getCacheName() {
		return getClass().getName();
	}

	public Campaign getCampainByProductIdAndTraking(Long productId, String traking) {
		return getByKey(key(productId, traking));
	}

	public List<Campaign> getAllCampainByProductId(Long productId) {
		return getByReference(keyRef(productId));
	}

	private String key(Long productId, String traking) {
		return productId + "#" + traking;
	}

	private String keyRef(Long productId) {
		return "listCampainProduct#" + productId;
	}
}
