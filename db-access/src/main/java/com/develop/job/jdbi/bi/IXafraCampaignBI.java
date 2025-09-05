package com.develop.job.jdbi.bi;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.develop.job.db.entity.XafraCampaign;

public interface IXafraCampaignBI {

	@SqlQuery("select * from xafra_campaign where traking = :traking and status != 1")
	@RegisterFieldMapper(XafraCampaign.class)
	public XafraCampaign getXafraCampaignByTraking(@Bind("traking") String traking);

	@SqlQuery("select * from xafra_campaign where product_id = :productId and traking = :traking and status != 1")
	@RegisterFieldMapper(XafraCampaign.class)
	public XafraCampaign getXafraCampaignByProductAndTraking(@Bind("productId") Long productId,
			@Bind("traking") String traking);

	@SqlQuery("select * from xafra_campaign where traking = :traking and status = :statusId")
	@RegisterFieldMapper(XafraCampaign.class)
	public XafraCampaign getXafraCampaignByTrakingAndStatus(@Bind("traking") String traking,
			@Bind("statusId") Integer status);

	@SqlQuery("select * from xafra_campaign where product_id = :productId and traking = :traking and status = :statusId")
	@RegisterFieldMapper(XafraCampaign.class)
	public XafraCampaign getXafraCampaignByProductAndTrakingAndStatus(@Bind("productId") Long productId,
			@Bind("traking") String traking, @Bind("statusId") Integer status);

	@SqlUpdate("insert into xafra_campaign (xafra_id, product_id, traking, uuid) values (:xafraId, :productId, :traking, :uuid )")
	@GetGeneratedKeys("id")
	public Long insertGetId(@BindBean XafraCampaign cXafra);

	@SqlUpdate("insert into xafra_campaign (xafra_id, product_id, traking, uuid) values (:xafraId, :productId, :traking, :uuid )")
	@GetGeneratedKeys
	@RegisterFieldMapper(XafraCampaign.class)
	public XafraCampaign insertGetEntity(@BindBean XafraCampaign cXafra);

	@SqlUpdate("update xafra_campaign set mdate= CURRENT_TIMESTAMP, status =:status where id=:id")
	public Boolean updateStatus(@Bind("id") Long id, @Bind("status") int status);
}
