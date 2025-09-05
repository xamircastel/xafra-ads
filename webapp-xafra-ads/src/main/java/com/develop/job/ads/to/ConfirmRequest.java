package com.develop.job.ads.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmRequest {

	private Long productId;
	private String reference;
	private String traking;
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ConfirmRequest[productId=").append(productId);
		b.append(", reference=").append(reference);
		b.append(", traking=").append(traking);
		b.append("]");
		return b.toString();
	}
}
