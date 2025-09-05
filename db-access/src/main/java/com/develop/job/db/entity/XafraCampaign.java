package com.develop.job.db.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XafraCampaign {

	private Long id;
	private Long xafraId;
	private Long productId;
	private String traking;
	private Integer status;
	private String uuid;
	private Date cdate;
	private Date mdate;

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Integer.toHexString(hashCode()));
		b.append("@XafraCampaign[id=").append(id);
		b.append(", xafraId=").append(xafraId);
		b.append(", productId=").append(productId);
		b.append(", traking=").append(traking);
		b.append(", status=").append(status);
		b.append(", uuid=").append(uuid);
		b.append(", cdate=").append(cdate);
		b.append(", mdate=").append(mdate);
		b.append("]");
		return b.toString();
	}
}
