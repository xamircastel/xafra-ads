package com.develop.job.ads.call;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.develop.job.ads.to.Subscribe;
import com.develop.job.db.dao.bi.IBlackList;
import com.develop.job.db.entity.BlackList;
import com.develop.job.tools.LogManager;
import com.develop.job.tools.RestClient;
import com.google.common.base.Stopwatch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallAutoSubscribe implements Callable<Boolean> {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	protected Logger log = LoggerFactory.getLogger(CallAutoSubscribe.class);

	protected static String url = "http://entel.timwe.com/pe/ma/api/external/v1/subscription/ext/ui/activate/2417";

	protected RestClient rest;
	protected IBlackList blk;

	private String authentication;
	private String msisdn;
	private Long productId;
	private String uuid;
	private String source;

	@Override
	public Boolean call() throws Exception {
		LogManager.setLogName("xafra-ads-auto");
		Stopwatch stopwatch = Stopwatch.createStarted();
		Map<String, String> header = new HashMap<String, String>();
		header.put("apikey", "0bd760fe2cfb4326a03f1c87961ec7c8");
		header.put("authentication", authentication);
		Subscribe respo = rest.executeApi(HttpMethod.POST, url, request(), Subscribe.class, header);
		String result = (respo != null && respo.getResponseData() != null && !respo.getInError())
				? respo.getResponseData().getSubscriptionResult()
				: "NO_RESULT";
		String resultBlk = "NO_INSERT_BLK";
		if (result.equals("OPTIN_ACTIVE_WAIT_CHARGING")) {
			BlackList ib = new BlackList();
			ib.setMsisdn(msisdn);
			ib.setProductId(productId);
			ib.setCreationDate(new Date());
			ib.setType(2);
			blk.insert(ib);
			resultBlk = "INSERT_BLK";
		}

		log.info("[{}]| response msisdn={}, resultSub={}, resultBlk={}| TimeTaken={}", uuid, msisdn, result, resultBlk,
				String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));

		return Boolean.TRUE;
	}

	private String request() {
		StringBuilder b = new StringBuilder();
		b.append("{");
		b.append("\"userIdentifier\": ").append("\"").append(msisdn).append("\",");
		b.append("\"productId\": ").append(productId).append(",");
		b.append(
				"\"deviceUserAgent\": \"Mozilla/5.0 (Linux; Android 13.1;AppleWebKit/537.36 Chrome/73.0.3683.90 Mobile Safari/537.36\",");
		b.append("\"clientIp\": \"0.0.0.0\",");
		b.append("\"subSource\"").append(":\"").append(source).append("\"");
		b.append("}");
		return b.toString();
	}
}
