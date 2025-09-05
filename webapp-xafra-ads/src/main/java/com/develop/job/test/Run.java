package com.develop.job.test;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;

import com.commons.help.job.utils.Encryption;
import com.develop.job.Configurations;
import com.develop.job.ads.impls.AutoSubscribe;
import com.develop.job.db.dao.bi.IBlackList;
import com.develop.job.db.dao.bi.IProductAndCustomer;
import com.develop.job.db.dao.impl.BlackListBo;
import com.develop.job.db.dao.impl.ProductAndCustomerBo;
import com.develop.job.db.entity.BlackList;
import com.develop.job.db.entity.Product;
import com.develop.job.exception.RequestException;
import com.develop.job.tools.LogManager;
import com.develop.job.tools.RestClient;
import com.develop.job.tools.Utils;

public class Run {

	public static void main(String... strings) {
		try {
			// encrypt();
			try (ConfigurableApplicationContext app = new AnnotationConfigApplicationContext(Configurations.class)) {
				// testApiSend(app);

				//runDb(app);
				
				testAuto(app);
				
				//insertBlackList(app);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertBlackList(ConfigurableApplicationContext app) {
		IBlackList blk = app.getBean(BlackListBo.class);
		
		BlackList b = blk.getBlackListByMsisdn("51937203591");
		
		BlackList ib = new BlackList();
		ib.setMsisdn("51986620525");
		ib.setProductId(11455L);
		ib.setCreationDate(new Date());
		ib.setType(2);
		blk.insert(ib);
		
		System.out.println("termina busqueda db " + b);
	}

	public static void testAuto(ConfigurableApplicationContext app) {
		AutoSubscribe auto = app.getBean(AutoSubscribe.class);
		LogManager.setLogName("xafra-ads-auto");
		String uuid = Utils.getUUID();
		auto.process(11455L, "23", 10, "AA30",10, uuid);
	}

	public static void runDb(ConfigurableApplicationContext app) {
		IProductAndCustomer prodAndCusBo = app.getBean(ProductAndCustomerBo.class);

		Product product = prodAndCusBo.getProductById(2L);

		System.out.println("termina busqueda db " + product);
	}

	public static void testApiSend(ConfigurableApplicationContext app) throws Exception {

		RestClient rest = app.getBean(RestClient.class);

		Long timemillis = Instant.now().toEpochMilli();

		String sharedKey = "HYClzOUxUOLzSapD"; // xafra-ads
		String encrypt = Encryption.encrypt(String.valueOf(timemillis), sharedKey);

		System.out.println(encrypt);

		String url = "http://xafra-ads.com/xafra/ads/confirm/020202020202";

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", "W9M178xiTRCg8VtBgcdyzULpbeOPGFyV11");
		headers.put("auth", "FHW3BwhWxSmW4LZXU2kAmQ==");
		try {
			String response = rest.executeApi(HttpMethod.GET, url, null, String.class, headers);

			System.out.println(response);
		} catch (RequestException ee) {
			System.out.println(ee);
		}

	}

	public static void encrypt() throws Exception {

		Long timemillis = Instant.now().toEpochMilli();

		String sharedKey = "HYClzOUxUOLzSapD"; // xafra-ads
		String encrypt = Encryption.encrypt(String.valueOf(timemillis), sharedKey);

		System.out.println(encrypt);

	}

}
