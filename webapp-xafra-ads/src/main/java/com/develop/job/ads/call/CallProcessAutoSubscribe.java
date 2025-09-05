package com.develop.job.ads.call;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.develop.job.db.dao.bi.IBlackList;
import com.develop.job.tools.LogManager;
import com.develop.job.tools.RestClient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallProcessAutoSubscribe implements Callable<Boolean> {

	protected static final Logger log = LoggerFactory.getLogger(CallProcessAutoSubscribe.class);

	private List<String> msisdns;
	private String auth;
	private String uuid;
	private Long productId;
	private String source;
	private Integer timeSleep;
	private RestClient rest;
	private IBlackList blk;

	@Override
	public Boolean call() throws Exception {
		ExecutorService threadPool = Executors.newFixedThreadPool(15);
		try {
			LogManager.setLogName("xafra-ads-auto");
			msisdns.forEach(l -> {
				if (l != null && !"".equals(l) && !"null".equals(l)) {

					CallAutoSubscribe c = new CallAutoSubscribe();
					c.setAuthentication(auth);
					c.setMsisdn(l);
					c.setUuid(uuid);
					c.setProductId(productId);
					c.setRest(rest);
					c.setBlk(blk);
					c.setSource(source);
					threadPool.submit(c);
					try {
						Thread.sleep(timeSleep);
					} catch (InterruptedException e) {
						log.error("[{}]| sleep error msisdn={}", uuid, l);
					}
				}
			});
		} catch (Exception e) {
			threadPool.shutdown();
		} finally {
			threadPool.shutdown();
		}

		return Boolean.TRUE;
	}

}
