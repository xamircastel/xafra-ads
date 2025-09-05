package com.develop.job.jdbi.bi;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.develop.job.db.entity.Campaign;

public interface ICampaignBI {

	/*
	 * 1 = activo 2 = inactivo
	 */

	@SqlQuery("Select * from campaign where id_product=:product_id and traking=:traking and status=2")
	@RegisterFieldMapper(Campaign.class)
	public Campaign getCampainByProductIdAndTraking(@Bind("product_id") Long id, @Bind("traking") String traking);

	@SqlQuery("Select * from campaign where traking=:traking and status=2")
	@RegisterFieldMapper(Campaign.class)
	public Campaign getCampainByTraking(@Bind("traking") String traking);

	@SqlQuery("Select * from campaign where id_product=:product_id")
	@RegisterFieldMapper(Campaign.class)
	public List<Campaign> getAllCampainByProductId(@Bind("product_id") Long id);

	@SqlUpdate("insert into campaign(id_product,traking,status,uuid,xafra_tracking_id) "
			+ "values (:productId, :traking, :status, :uuid, :xafraTrackingId)")
	public void insert(@BindBean Campaign campain);

	@SqlUpdate("update campaign set modification_date= CURRENT_TIMESTAMP, status =:status where id=:id")
	public void update(@Bind("id") Long campainId, @Bind("status") int status);

	@SqlUpdate("update campaign set modification_date= CURRENT_TIMESTAMP, status =:status, status_post_back=:statusP where id=:id")
	public void update(@Bind("id") Long campainId, @Bind("status") int status, @Bind("statusP") int statisPostBack);

	@SqlUpdate("update campaign set status_post_back=:statusP, date_post_back=CURRENT_TIMESTAMP  where id=:id")
	public void updateStatusPostBack(@Bind("id") Long campainId, @Bind("statusP") int statisPostBack);

	@SqlQuery("Select id, id_product, traking from campaign where id_product=:product_id and traking=:traking and status=2")
	@RegisterFieldMapper(Campaign.class)
	public Campaign getCampainByProductIdAndTrakingMinimal(@Bind("product_id") Long id,
			@Bind("traking") String traking);
	
	@SqlQuery("Select * from campaign where xafra_tracking_id=:tracking and status=2")
	@RegisterFieldMapper(Campaign.class)
	public Campaign getCampainByXafraTracking(@Bind("tracking") String tracking);
}
