package com.develop.job.ads.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscribe {

	private String message;
	private Boolean inError;
	private String requestId;
	private String code;
	private Data responseData;

	@Getter
	@Setter
	public static class Data {
		private String transactionId;
		private String subscriptionResult;
		private String subscriptionError;
	}
}
