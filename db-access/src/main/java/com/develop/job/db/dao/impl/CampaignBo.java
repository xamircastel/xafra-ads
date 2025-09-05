package com.develop.job.db.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.develop.job.db.dao.DAOCampaign;
import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.db.dao.cache.CampainCache;
import com.develop.job.db.entity.Campaign;

@Service
public class CampaignBo implements ICampaign {

	@Autowired
	private CampainCache cacheBo;

	@Autowired
	private DAOCampaign campainDao;

	@Override
	public void insert(Campaign campain) {
		campainDao.insert(campain);
	}

	@Override
	public void update(Long campainId, int status) {
		campainDao.update(campainId, status);
	}

	@Override
	public void update(Long campainId, int status, int statusp) {
		campainDao.update(campainId, status, statusp);
	}

	@Override
	public void updateStatusPostBack(Long campainId, int statusp) {
		campainDao.updateStatusPostBack(campainId, statusp);
	}

	@Override
	public Campaign getCampainByProductIdAndTraking(Long productId, String traking) {
		return cacheBo.getCampainByProductIdAndTraking(productId, traking);
	}

	@Override
	public List<Campaign> getAllCampainByProductId(Long productId) {
		return cacheBo.getAllCampainByProductId(productId);
	}

	@Override
	public Campaign getCampainByProductIdAndTrakingSin(Long productId, String traking) {
		return campainDao.getCampainByProductIdAndTrakingMinimal(productId, traking);
	}

	@Override
	public Campaign getCampainByTraking(String traking) {
		return campainDao.getCampainByTraking(traking);
	}

	@Override
	public Campaign getCampainByXafraTracking(String tracking) {
		return  campainDao.getCampainByXafraTracking(tracking);
	}
}
