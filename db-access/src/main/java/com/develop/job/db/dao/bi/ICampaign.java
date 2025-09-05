package com.develop.job.db.dao.bi;

import java.util.List;

import com.develop.job.db.entity.Campaign;

public interface ICampaign {

	public void insert(Campaign campain);

	public void update(Long campainId, int status);

	public void update(Long campainId, int status, int statusP);

	public void updateStatusPostBack(Long campainId, int statusP);

	public Campaign getCampainByProductIdAndTraking(Long productId, String traking);

	public List<Campaign> getAllCampainByProductId(Long productId);

	public Campaign getCampainByProductIdAndTrakingSin(Long productId, String traking);

	public Campaign getCampainByTraking(String traking);
	
	public Campaign getCampainByXafraTracking(String tracking);
}
