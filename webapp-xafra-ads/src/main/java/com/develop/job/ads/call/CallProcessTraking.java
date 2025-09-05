package com.develop.job.ads.call;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.db.entity.Campaign;
import com.develop.job.tools.LogManager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallProcessTraking implements Callable<Boolean> {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	protected Logger log = LoggerFactory.getLogger(CallProcessTraking.class);

	protected ICampaign campainBo;
	private Campaign campaing;

	@Override
	public Boolean call() throws Exception {
		try {
			LogManager.setLogName("xafra-ads-process");
			campainBo.insert(getCampaing());
			log.info("[{}] insert traking", getCampaing().getUuid());
			return Boolean.TRUE;
		} catch (Exception e) {
			log.error(getCampaing().getUuid(), e);
		}
		return Boolean.FALSE;
	}

}
