package com.develop.job.db.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.develop.job.db.dao.DAOBlackList;
import com.develop.job.db.dao.bi.IBlackList;
import com.develop.job.db.entity.BlackList;
import com.develop.job.db.entity.XafraCampaign;

@Service
public class BlackListBo implements IBlackList {

	@Autowired
	private DAOBlackList blk;

	@Override
	public BlackList getBlackListByMsisdn(String msisdn) {
		return blk.getBlackListByMsisdn(msisdn);
	}

	@Override
	public void insert(BlackList black) {
		blk.insert(black);
	}

	@Override
	public Long insertGetId(BlackList black) {
		return blk.insertGetId(black);
	}

	@Override
	public BlackList insertGetEntity(BlackList black) {
		return blk.insertGetEntity(black);
	}

	@Override
	public Boolean insertBlackListAndXafraCampaign(BlackList blak, XafraCampaign cXafra) {
		return blk.insertBlackListAndXafraCampaing(blak, cXafra);
	}

}
