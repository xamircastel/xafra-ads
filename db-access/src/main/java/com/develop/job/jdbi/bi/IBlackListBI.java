package com.develop.job.jdbi.bi;

import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.transaction.annotation.Transactional;

import com.develop.job.db.entity.BlackList;
import com.develop.job.db.entity.XafraCampaign;

public interface IBlackListBI {

	@CreateSqlObject
	IXafraCampaignBI xafraCampaign();

	@SqlQuery("select * from blacklist where msisdn=:msisdn")
	@RegisterFieldMapper(BlackList.class)
	public BlackList getBlackListByMsisdn(@Bind("msisdn") String msisdn);

	@SqlUpdate("insert into blacklist (msisdn, creation_date, product_id, \"type\") values (:msisdn, :creationDate, :productId, :type)")
	public void insert(@BindBean BlackList black);

	@SqlUpdate("insert into blacklist (msisdn, creation_date, product_id, \"type\") values (:msisdn, :creationDate, :productId, :type)")
	@GetGeneratedKeys("id")
	public Long insertGetId(@BindBean BlackList black);

	@SqlUpdate("insert into blacklist (msisdn, creation_date, product_id, \"type\") values (:msisdn, :creationDate, :productId, :type)")
	@GetGeneratedKeys
	@RegisterFieldMapper(BlackList.class)
	public BlackList insertGetEntity(@BindBean BlackList black);

	@Transactional
	public default Boolean insertBlackListAndXafraCampaign(BlackList blk, XafraCampaign cXafra) {
		Boolean isP = Boolean.FALSE;
		try {
			Long blacklistId = insertGetId(blk);
			cXafra.setXafraId(blacklistId);
			xafraCampaign().insertGetId(cXafra);
			isP = Boolean.TRUE;
		} catch (Exception ee) {
			throw ee;
		}

		return isP;
	}

}
