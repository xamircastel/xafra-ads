package com.develop.job.db.dao;

import java.util.List;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;

import com.develop.job.db.entity.Campaign;
import com.develop.job.jdbi.BaseJbdiDao;
import com.develop.job.jdbi.bi.ICampaignBI;

@Service
public class DAOCampaign extends BaseJbdiDao<ICampaignBI> {

	public DAOCampaign() {
		super(ICampaignBI.class);
	}

	public Campaign getCampainByProductIdAndTraking(Long id, String traking) {
		return handler((Handle handle) -> binder(handle).getCampainByProductIdAndTraking(id, traking));
	}

	public Campaign getCampainByTraking(String traking) {
		return handler((Handle handle) -> binder(handle).getCampainByTraking(traking));
	}

	public List<Campaign> getAllCampainByProductId(Long productId) {
		return handler((Handle handle) -> binder(handle).getAllCampainByProductId(productId));
	}

	public void insert(Campaign campain) {
		useHandle((Handle handle) -> binder(handle).insert(campain));
	}

	public void update(Long id, int status) {
		useHandle((Handle handle) -> binder(handle).update(id, status));
	}
	
	public void update(Long id, int status, int statusp) {
		useHandle((Handle handle) -> binder(handle).update(id, status, statusp));
	}
	
	public void updateStatusPostBack(Long id, int statusp) {
		useHandle((Handle handle) -> binder(handle).updateStatusPostBack(id, statusp));
	}
	
	public Campaign getCampainByProductIdAndTrakingMinimal(Long id, String traking) {
		return handler((Handle handle) -> binder(handle).getCampainByProductIdAndTrakingMinimal(id, traking));
	}
	
	public Campaign getCampainByXafraTracking(String tracking) {
		return handler((Handle handle) -> binder(handle).getCampainByXafraTracking(tracking));
	}
}
