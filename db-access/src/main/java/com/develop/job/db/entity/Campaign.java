package com.develop.job.db.entity;

import java.util.Date;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campaign {

	private Long id;
	private Date creationDate;
	private Date modificationDate;
	@ColumnName("id_product")
	private Long productId;
	private String traking;
	private Integer status;
	private String uuid;
	private Integer statusPostBack;
	private String xafraTrackingId;
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Integer.toHexString(hashCode()));
		b.append("@Campain[id=").append(id);
		b.append(", creationDate=").append(creationDate);
		b.append(", modificationDate=").append(modificationDate);
		b.append(", productId=").append(productId);
		b.append(", traking=").append(traking);
		b.append(", status=").append(status);
		b.append(", uuid=").append(uuid);
		b.append(", xafraTrackingId=").append(xafraTrackingId);
		b.append("]");
		return b.toString();
	}
}
