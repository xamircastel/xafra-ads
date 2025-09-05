package com.develop.job.db.dao;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;

import com.develop.job.db.entity.BlackList;
import com.develop.job.db.entity.XafraCampaign;
import com.develop.job.jdbi.BaseJbdiDao;
import com.develop.job.jdbi.bi.IBlackListBI;

@Service
public class DAOBlackList extends BaseJbdiDao<IBlackListBI> {

	public DAOBlackList() {
		super(IBlackListBI.class);
	}

	public BlackList getBlackListByMsisdn(String msisdn) {
		return handler((Handle handle) -> binder(handle).getBlackListByMsisdn(msisdn));
	}

	public void insert(BlackList black) {
		useHandle((Handle handle) -> binder(handle).insert(black));
	}

	public Long insertGetId(BlackList black) {
		return handler((Handle handle) -> binder(handle).insertGetId(black));
	}

	public BlackList insertGetEntity(BlackList black) {
		return handler((Handle handle) -> binder(handle).insertGetEntity(black));
	}

	public Boolean insertBlackListAndXafraCampaing(BlackList blk, XafraCampaign cXafra) {
		return handler((Handle handle) -> binder(handle).insertBlackListAndXafraCampaign(blk, cXafra));
	}
}
