package com.develop.job.ads.call;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.db.entity.Product;
import com.develop.job.tools.LogManager;
import com.develop.job.tools.RestClient;
import com.google.common.base.Stopwatch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallConfirmTraking implements Callable<Boolean> {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	protected Logger log = LoggerFactory.getLogger(CallProcessTraking.class);
	protected Stopwatch stopwatch;
	protected ICampaign campainBo;
	protected RestClient rest;
	private Product product;
	private String traking;
	private Long campaignId;
	private String uuid;

	@Override
	public Boolean call() throws Exception {
		try {
			LogManager.setLogName("xafra-ads-confirm");
			boolean processPostBack = postBackAds(product, traking);
			int statusPost = processPostBack ? 1 : 0;
			campainBo.updateStatusPostBack(campaignId, statusPost);
			log.info("[{}]|END postBack call {}", uuid, String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
			return processPostBack;
		} catch (Exception e) {
			log.error(uuid, e);
		}
		return Boolean.FALSE;
	}

	protected boolean postBackAds(Product product, String traking) {
		boolean r = false;
		try {
			if (product.getUrlRedirectPostBack() != null && !"".equals(product.getUrlRedirectPostBack())) {
				String url = urlPostBack(product.getUrlRedirectPostBack(), traking);
				log.info("[{}]| postBackAds {}", uuid, url);
				String resp = rest.executeApi(method(product.getMethodPostBack()), url, null, String.class, null);
				LogManager.setLogName("xafra-ads-confirm");
				log.info("[{}]| postBackAds response {}", uuid, resp);
				r = true;
			}
		} catch (Exception e) {
			log.error(uuid, e);
		}
		return r;
	}

	private String urlPostBack(String url, String traking) {
		url = url.replaceAll("<TRAKING>", traking);
		return url;
	}

	protected HttpMethod method(String method) {
		HttpMethod back = HttpMethod.POST;

		if (method != null) {
			if ("GET".equals(method.trim().toUpperCase()))
				back = HttpMethod.GET;
		}

		return back;
	}
}
