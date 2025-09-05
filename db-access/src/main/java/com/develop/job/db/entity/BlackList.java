package com.develop.job.db.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlackList {

	private Long id;
	private String msisdn;
	private Date creationDate;
	private Long productId;
	private Integer type;
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Integer.toHexString(hashCode()));
		b.append("@BlackList[id=").append(id);
		b.append(", msisdn=").append(msisdn);
		b.append(", creationDate=").append(creationDate);
		b.append(", productId=").append(productId);
		b.append(", type=").append(type);
		b.append("]");
		return b.toString();
	}
}
