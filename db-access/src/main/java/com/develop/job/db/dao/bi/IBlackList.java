package com.develop.job.db.dao.bi;

import com.develop.job.db.entity.BlackList;
import com.develop.job.db.entity.XafraCampaign;

public interface IBlackList {

	public BlackList getBlackListByMsisdn(String msisdn);

	public void insert(BlackList black);
	
	public Long insertGetId(BlackList black);

	public BlackList insertGetEntity(BlackList black);
	
	public Boolean insertBlackListAndXafraCampaign(BlackList blk, XafraCampaign cXafra);
}
