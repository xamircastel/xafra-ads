package com.develop.job.ads.impls;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.commons.help.job.utils.Encryption;
import com.develop.job.ads.call.CallProcessTraking;
import com.develop.job.ads.enumr.StatusCampain;
import com.develop.job.ads.to.AdsRequest;
import com.develop.job.db.dao.bi.ICampaign;
import com.develop.job.db.dao.bi.IProductAndCustomer;
import com.develop.job.db.entity.Campaign;
import com.develop.job.db.entity.Product;
import com.develop.job.tools.AbstractProcess;

@Component
public class ProcessAds extends AbstractProcess<AdsRequest> {

	@Value("${password.encription}")
	private String passEncription;

//	@Autowired
	private HttpServletRequest request;
//	@Autowired
	private HttpServletResponse response;
	@Autowired
	private IProductAndCustomer prodAndCusBo;

	@Autowired
	private ICampaign campainBo;

	@Override
	public void process(AdsRequest event) {
		response = event.getHttpResponse();
		request = event.getHttpRequest();
		if (event.getTrakingId() != null && !"".equals(event.getTrakingId())) {
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			logStart("process {}, trakingId={}", event.getParams(), event.getTrakingId());
			logDebug("Adrr={}, host={}", request.getRemoteAddr(), request.getRemoteHost());
			boolean redirect = false;
			try {
				String desc = "NOT_EXIST";
				Long productId = parameterDecryption(event.getParams());
				Product product = prodAndCusBo.getProductById(productId);
				if (product != null && product.getUrlRedirectSuccess() != null
						&& !product.getUrlRedirectSuccess().isEmpty()) {
					desc = "EXIST";
					if (!validateTrakingProduc(productId, event.getTrakingId())) {
						String urlRedirect = urlCustomizationRedirect(product.getUrlRedirectSuccess(),
								event.getTrakingId());
						desc = "EXIST_PROCESSED";
						redirect = true;
						insertTraking(threadPool, event, product);
						redirect(urlRedirect);
					}
				}
				logEnd("product_id={}, traking={}, status={}, {}", productId, event.getTrakingId(), redirect, desc);
			} catch (Exception e) {
				logError("process {}", e.getMessage());
			}
		} else
			logEnd("process not traking recover");

	}

	private void insertTraking(ExecutorService threadPool, AdsRequest event, Product product) {
		CallProcessTraking call = new CallProcessTraking();
		call.setCampaing(buildCampaign(product.getIdProduct(), event.getTrakingId(),
				StatusCampain.PROCESSING.getStatus(), getUUID(), product));
		call.setCampainBo(campainBo);
		threadPool.submit(call);
	}

	private void redirect(String urlRedirect) throws IOException {
		logInfo("redirect process {}", urlRedirect);
		response.sendRedirect(urlRedirect);
	}

	private boolean validateTrakingProduc(Long productId, String traking) {
		return campainBo.getCampainByProductIdAndTrakingSin(productId, traking) != null;
	}

	private Long parameterDecryption(String enc) {
		try {
			enc = enc.replaceAll("\\$", "/");
			String decr = Encryption.decrypt(enc, passEncription);
			return castLong(decr);
		} catch (Exception e) {
			logError("parameterDecryption", e);
		}
		return 0L;
	}

	private Long castLong(String o) {
		try {
			return Long.valueOf(o);
		} catch (Exception e) {
			logError("castLong", e);
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> getHeadersInfo() {

		Map<String, String> map = new HashMap<String, String>();

		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		logInfo("headers info {}", map.toString());

		return map;
	}

	private String urlCustomizationRedirect(String url, String traking) {
		// Usar replace() en lugar de replaceAll() para evitar problemas con caracteres especiales como $
		url = url.replace("<TRAKING>", traking);
		return url;
	}

	private Campaign buildCampaign(Long pId, String traking, int status, String uuid, Product product) {
		Campaign campaign = new Campaign();
		campaign.setProductId(pId);
		campaign.setTraking(traking);
		campaign.setStatus(status);
		campaign.setUuid(uuid);
		
		// ===== GENERAR XAFRA TRACKING ID INTERNO =====
		// Este es el tracking interno de XAFRA, diferente al tracking externo
		String xafraInternalTracking = generateXafraInternalTracking();
		campaign.setXafraTrackingId(xafraInternalTracking);
		
		// ===== CONFIGURAR PAÍS Y OPERADOR DESDE PRODUCT =====
		if (product != null) {
			campaign.setCountry(product.getCountry());
			campaign.setOperator(product.getOperator());
		}
		
		return campaign;
	}
	
	/**
	 * Genera un tracking ID interno único para XAFRA
	 * Formato: XFR_YYYYMMDD_HHMMSS_UUID_SHORT
	 */
	private String generateXafraInternalTracking() {
		java.time.LocalDateTime now = java.time.LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = now.format(formatter);
		String shortUuid = java.util.UUID.randomUUID().toString().substring(0, 8);
		return "XFR_" + timestamp + "_" + shortUuid.toUpperCase();
	}

	@Override
	protected Class<?> getLogName() {
		return ProcessAds.class;
	}

	@Override
	protected String getLogNameFile() {
		return "xafra-ads-process";
	}
}