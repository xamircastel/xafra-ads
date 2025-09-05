package com.develop.job.db.dao;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;

import com.develop.job.db.entity.XafraCampaign;
import com.develop.job.jdbi.BaseJbdiDao;
import com.develop.job.jdbi.bi.IXafraCampaignBI;

@Service
public class DAOXafraCampaign extends BaseJbdiDao<IXafraCampaignBI> {

	public DAOXafraCampaign() {
		super(IXafraCampaignBI.class);
	}

	public XafraCampaign getXafraCampaignByTraking(String traking) {
		return handler((Handle handle) -> binder(handle).getXafraCampaignByTraking(traking));
	}

	public XafraCampaign getXafraCampaignByProductAndTraking(Long productId, String traking) {
		return handler((Handle handle) -> binder(handle).getXafraCampaignByProductAndTraking(productId, traking));
	}

	public XafraCampaign getXafraCampaignByTrakingAndStatus(String traking, Integer status) {

		return handler((Handle handle) -> binder(handle).getXafraCampaignByTrakingAndStatus(traking, status));
	}

	public XafraCampaign getXafraCampaignByProductAndTrakingAndStatus(Long productId, String traking, Integer status) {
		return handler((Handle handle) -> binder(handle).getXafraCampaignByProductAndTrakingAndStatus(productId, traking, status));
	}

	public Long insertGetId(XafraCampaign cXafra) {
		return handler((Handle handle) -> binder(handle).insertGetId(cXafra));
	}

	public XafraCampaign insertGetEntity(XafraCampaign cXafra) {
		return handler((Handle handle) -> binder(handle).insertGetEntity(cXafra));
	}

	public Boolean updateStatus(Long id, int status) {
		return handler((Handle handle) -> binder(handle).updateStatus(id, status));
	}
}
