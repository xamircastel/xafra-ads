package com.develop.job.ads.impls;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.develop.job.ads.call.CallConfirmTraking;
import com.develop.job.ads.enumr.StatusCampain;
import com.develop.job.ads.to.AdsRequest;
import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.db.dao.bi.IProductAndCustomer;
import com.develop.job.db.entity.Campaign;
import com.develop.job.db.entity.Product;
import com.develop.job.tools.AbstractProcess;
import com.develop.job.tools.RestClient;

@Component
public class ProcessAdsConfirm extends AbstractProcess<AdsRequest> {

	@Autowired
	private ICampaign campainBo;

	@Autowired
	private IProductAndCustomer prodAndCusBo;

	@Autowired
	private RestClient rest;
	
	private String idSession;
	@Override
	public void process(AdsRequest event) {
		try {
			idSession = event.getHttpRequest().getSession().getId();
			logStart("{} trakingId={}",idSession, event.getTrakingId());
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			boolean status = false;
			String uuidDB = "";
			Campaign camp = campainBo.getCampainByTraking(event.getTrakingId());
			if (camp != null && camp.getStatusPostBack() == 0) {
				uuidDB = camp.getUuid();
				Product product = prodAndCusBo.getProductById(camp.getProductId());
				if (product != null) {
					campainBo.update(camp.getId(), StatusCampain.PROCESSED.getStatus());
					status = true;
					executePostBack(threadPool, product, event.getTrakingId(), camp.getId());
				}
			}
			logEnd("traking={}, status={}, uuidDB={}", event.getTrakingId(), status, uuidDB);
		} catch (Exception e) {
			logError("process", e);
		}
	}

	private void executePostBack(ExecutorService threadPool, Product product, String traking, Long campaignId) {
		CallConfirmTraking call = new CallConfirmTraking();
		call.setProduct(product);
		call.setTraking(traking);
		call.setCampaignId(campaignId);
		call.setUuid(getUUID());
		call.setCampainBo(campainBo);
		call.setRest(rest);
		call.setStopwatch(stopwatch);
		threadPool.submit(call);
	}

	@Override
	protected Class<?> getLogName() {
		return ProcessAdsConfirm.class;
	}

	@Override
	protected String getLogNameFile() {
		return "xafra-ads-confirm";
	}

}
